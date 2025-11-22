const file = require('fs');

const DBManager = require('./dbmanager');
const logger = require('../utils/logger');

const betserverTable = 'betserver';
const idColumn = 'id';
const nameColumn = 'name';

const resourceFolder = './src/com/pkt/betattack/service/betspy/resources';

const BetServer = {
    filePath: String, 

    getAll: async function(serverID) {
        const result = await DBManager.get([ betserverTable, 'all']);
        return JSON.parse(DBManager.formatToJSON(result));
    },

    getById: async function(serverID) {
        const result = await DBManager.get([ betserverTable, idColumn, serverID]);
        return JSON.parse(DBManager.formatToJSON(result));
    },
    getByName: async function(betserverName) {
        const result = await DBManager.get([ betserverTable, nameColumn, betserverName ]);
        return JSON.parse(DBManager.formatToJSON(result));
    },

    get: async function(betserverName){
        const result = await this.getByName(betserverName);
        try{
            return JSON.parse(result);
        } catch(error) {
            logger.addPrint('error', error)
        }    
    },

    toJsonFile: async function(betserverName){
        this.filePath = resourceFolder + '/betserver/' + betserverName + '.json';
        const server = await this.getByName(betserverName);
        try{
            file.openSync(this.filePath, 'w');
            file.writeFileSync(this.filePath, JSON.stringify(server, null, '\t'));
            logger.addPrint('info', `GENERATED: File for "${betserverName}"`);
            return server;
        } catch(error) {
            logger.addPrint('error', error)
        }
    },

    allToJsonFiles: async function( ){
        const servers = await this.getAll();
        servers.forEach( server => {
            this.filePath = resourceFolder + '/betserver/' + server.name + '.json';
            try{
                file.openSync(this.filePath, 'w');
                file.writeFileSync(this.filePath, JSON.stringify(server, null, '\t'));
                logger.addPrint('info', `GENERATED: File for "${server.name}"`);
            } catch(error) {
                logger.addPrint('error', error);
            }
        });
        return servers;
    }        
}

module.exports = BetServer;


