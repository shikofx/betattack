const { loggers } = require('winston');
const globalV = require('../global/global');
const Logger = require('../utils/logger');

const NEXT_DELAY = 5;
const INIT_DELAY = 0;
const AttackQueue = {
    io: Object,
    attacks: [],

    queue: [],

    updateConnectionList: function(){
        this.attacks.forEach(attack => {
            attack.connections = globalV.connections;
        });
    },
    
    add: function({ io, request }){
        Logger.addPrint('info', `START: ${request.game.url}`)
        this.io = io;
        let attack = this.getAttack(request);
        if(!attack){
            this.attacks.push({ request, connections: globalV.connections });
            let accounts = getAccountsForAttack(globalV.connections);        
            this.io.emit(`attacked-accounts-${this.getUISign(request)}`, accounts);        
            if(this.attacks.length > 0)
                this.run(INIT_DELAY);
        }        
    },

    stopAttack: async function(req){
        // Удаляю из очереди все соединения с этой атакой
        let index = 0;
        while( index >= 0){
            index = this.queue.findIndex(att => equalRequests(att.request, req));
            if(index >= 0){
                let socket = this.queue[index].connection.socket;
                await socket.emit(`stop-attack-${socket.id}`, req.game.url);        
                this.queue.splice(index, 1);
            }
        }        
        // Удаляю из списка атак эту атаку
        index = this.attacks.findIndex(att => equalRequests(att.request, req));
        if(index >= 0)
            this.attacks.splice(index, 1);

        if(this.attacks.length == 0)
            await this.io.emit('stop-browser');
        Logger.addPrint('info', `STOP: ${req.game.url}`);

        await this.io.emit(`attack-stopped-${this.getUISign(req)}`);
    },

    run: function(timeout, fromRequest, fromSocket){
        this.attacks.forEach(attack => {
            // проходим по всем атакам
            if(attack.connections.size > 0){
                // в каждой атаке пробегаемсяч по серверам
                attack.connections.forEach(connection => {
                    // определяем, свободно ли данное соединение (нет в списке активных)
                    if(fromSocket && fromSocket.id === connection.socket.id && attack.connections.size > 1){
                        return;
                    }
                    const reqIndex = AttackQueue.queue.findIndex(att => equalRequests(att.request, attack.request)); 
                    const connIndex = AttackQueue.queue.findIndex(attack => attack.connection.socket.id === connection.socket.id);  
                    if(reqIndex < 0 && connIndex < 0){
                        Logger.addPrint('info', `ATTACK: start -> ${connection.account.login} - ${connection.socket.id} - ${attack.request.game.url}`);
                        AttackQueue.queue.push({ request: attack.request, connection });
                        connection.socket.emit(`init-attack-${connection.socket.id}`, attack.request, timeout);
                    }   
                });
                this.mixConnections(attack);
            }
        });
        this.mixAttacks();        
    },

    next: async function(fromSocket, fromRequest){
        this.removeFromQueue(fromSocket.id);
        // let attack = this.getAttack(fromRequest);
        this.run(NEXT_DELAY, fromRequest, fromSocket);
        //если соединений несколько
    },

    mixConnections: function(attack){
        let mixer = 0;
        let shifter;
        let mixedConnections = new Map();
        attack.connections.forEach(connection => {
            if(mixer === 0){
                mixer++;
                shifter = connection;
                return;
            }
            mixedConnections.set(connection.account._id, connection);
        })
        mixedConnections.set(shifter.account._id, shifter);
        attack.connections = mixedConnections;
    },
    
    mixAttacks: function(){
        let mixer = 0;
        let shifter;
        let mixedAttacks = [];
        this.attacks.forEach(attack => {
            if(mixer === 0){
                mixer++;
                shifter = attack;
                return;
            }
            mixedAttacks.push(attack);
        })
        if(shifter)
            mixedAttacks.push(shifter);
        this.attacks = mixedAttacks;
    },

    getAttack: function(request){
        return this.attacks.find(attack => equalRequests(attack.request, request));
    },

    getAttackIndex: function(request){
        return this.attacks.findIndex(attack => equalRequests(attack.request, request));
    },

    getAccountID: function(attack, socketID){
        if(attack){
            let accountIDs = Array.from(attack.connections.keys());
            return accountIDs.find(key => attack.connections.get(key).socket.id === socketID);         
        }
        return undefined;
    },

    removeFromQueue: function(socketID){
        let attackIndex = this.queue.findIndex(attack => attack.connection.socket.id === socketID);
        if(attackIndex >= 0) 
            this.queue.splice(attackIndex, 1);
    }, 

    closeAttack: async function(socketID, request){
        Logger.addPrint('info', `CLOSE: attack ${socketID} - ${request.game.url} - ${request.game.bet.name} - ${request.game.bet.direction}`)
        let attack = this.getAttack(request);
        let attackIndex = this.getAttackIndex(request);
        let accountID = this.getAccountID(attack, socketID);
        if(accountID)
            attack.connections.delete(accountID);
        if(attack && attack.connections.size === 0){
            //Удалить атаку из очереди, если все получилось или она уже не актуальна
            this.attacks.splice(attackIndex, 1);
            if(this.attacks.length === 0)
                this.io.emit('stop-browser');
        }
        this.removeFromQueue(socketID);
    },

    getUISign: function(req) {
        return `${req.game.url}-${req.game.bet.name}-${req.game.bet.direction}`;
    }
}

module.exports = AttackQueue;

const equalRequests = function (req1, req2){
    let betName1 = req1.game.bet.name;
    let betDirection1 = req1.game.bet.direction;
    let url1 = req1.game.url;
    let betName2 = req2.game.bet.name;
    let betDirection2 = req2.game.bet.direction;
    let url2 = req2.game.url;
    if(url1 === url2 && betName1 === betName2 && betDirection1 === betDirection2)
        return true;
    return false;
}

const getAccountsForAttack = (connections) => {
    let accounts = [];
    connections.forEach( connection => {
        accounts.push(connection.account)
    })
    return accounts;
}
