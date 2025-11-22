const express = require('express');
const socketio = require('socket.io');
const http = require('http');
const logger = require('./utils/logger');
const logsRouter = require('./routers/logs');
const _attackQueue = require('./attack/queue');
const global = require('./global/global');
const app = express();
const server = http.createServer(app);
const io = socketio(server);

const _port = process.env.PORT || 3100;

const RUN_DELAY = 2;
const PREPARE_DELAY = 0;

io.on('connection', async (socket) => {
    //При подключении нового счета
    socket.on('connected-account', (account) => {
        global.connections.set(account._id, {socket, account});
        //добавить соединение в текущие атаки, если атаки уже в работе и он там к месту
        if(_attackQueue.attacks.length > 0){
            _attackQueue.updateConnectionList();            
        }
        //добавить счет в UI отображение
        logger.addPrint('info', `CONNECTED: account serverID: ${account.server} => Login: '${account.login}' \tconnected on IP: '${account.spyAddress[0].ip}'`);                
    });

    socket.on('connect-ui', (token) => {
        if( global.userTokens.size == 0 || !global.userTokens.get(socket.id) || global.userTokens.get(socket.id).valueOf() != token ) {
            global.userTokens.set(socket.id, token);
            logger.addPrint('info', `CONNECTED: UI - ${token}`);
            if(global.userTokens.size > 0){
                io.emit('ui-connected', Array.from(global.userTokens.values()));
            }
        }
    });

    socket.on('disconnect', () => {
        const token = global.userTokens.get(socket.id);
        if(token){
            global.userTokens.delete(socket.id);
            if(global.userTokens.size == 0){
                io.emit('stop-browser');
                _attackQueue.attacks = [];
                _attackQueue.queue = []
            }

            logger.addPrint('info', `DISCONNECTED: UI - ${token.valueOf()}`)
        } else {
            let accountID = getAccountId(socket);
            if(accountID){
                const account = global.connections.get(accountID).account;
                logger.addPrint('info', `DISCONNECTED: xxx xxx serverID: ${account.server} => Login: '${account.login}'`)
                global.connections.delete(accountID);
                if(_attackQueue.attacks.length > 0){
                    _attackQueue.updateConnectionList();            
                }
            }
        }                
    });

    socket.on('init-attack', async (req) => {
        const request = JSON.parse(req);
        const connections = new Map(global.connections);
        _attackQueue.add({ io, request, connections });        
    });

    socket.on('attack-initiated', (socketId) => {
        io.emit(`prepare-attack-${socketId}`, PREPARE_DELAY); //10 seconds
    })

    socket.on('attack-prepared', (socketId) => {
        io.emit(`run-attack-${socketId}`, RUN_DELAY); //10 seconds
    })

    
    socket.on('stop-attack', async (req) => {
        const request = JSON.parse(req);
        _attackQueue.stopAttack(request);        
    });

    socket.on('attack-result', async (socketID, request, result) => {
        if(result.repeat == false){
            await _attackQueue.closeAttack(socketID, request);
            logger.addPrint('info', `ATTACK: closed status: ${result.status} and message: ${result.message}`);
            io.emit(`attack-result-${_attackQueue.getUISign(request)}`, result );
            await _attackQueue.next(socket, request);
        } else {
            logger.addPrint('error', result.message);
            await _attackQueue.next(socket, request);
        }
    });

    socket.on('attack-stopped', (email, victim) => {
        io.emit(`attack-stopped-${_attackQueue.getUISign(request)}`, email, victim);
    });

    socket.on('account-changed', (uiSign, email , account) => {
        io.emit(`account-changed-${uiSign}`, email, account);
    });
    
});

server.listen(_port, async () => {
    await global.init();

    logger.addPrint('info', `Server is up on port ${_port}`);
});



const adressesEqual = (connected, existing) => {
    const connectedIp = connected.spyAdress.ip;
    const connectedPort = connected.spyAdress.port;
    const existingIp = existing.spyAdress.ip;
    const existingPort = existing.spyAdress.port;
    return connectedIp == existingIp && connectedPort == existingPort;
}

function getAccountId(socket) {
    let connectionsArray = Array.from(global.connections);
    let connection = connectionsArray.find(c => c[1].socket === socket);
    let accountID = undefined;
    if(connection && connection.length > 0)
        accountID = connection[0];
    return accountID;
}

function sendConnections(account) {
    const queue = [];
    global.connections.forEach(connection => {
        if (connection.account && connection.account.server == account.server)
            queue.push(connection);
    });
    if(global.connections.length > 0)
        logger.addPrint('info', "==> Connections pool: ")
    queue.forEach(connection => {
        logger.addPrint('info', `ServerID: ${connection.account.server} => Login: '${connection.account.login}' \tconnected on IP: '${connection.account.spyAddress[0].ip}'`);
    })
    
    if (queue.length > 0)
        io.emit(`connections-${account.server}`, queue);
}

