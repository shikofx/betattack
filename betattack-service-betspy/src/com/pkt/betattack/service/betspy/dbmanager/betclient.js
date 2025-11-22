const file = require('fs'); 
const path = require('path');
const DBManager = require('./dbmanager');
const logger = require('../utils/logger');

const ownerTable = 'betclient';
const idColumn = 'id';
const nameColumn = 'name';

const resources = './src/com/pkt/betattack/service/betspy/resources';

const BetServer = {
    filePath: String,

    getByEmail: async function(email) {
        const result = await DBManager.get([ ownerTable, 'email', email]);
        return DBManager.formatToJSON(result);
    },

    toJSON: async function(email){
        const result = await this.getByEmail(email);
        this.filePath = path.join(resources, 'BC_' + email + '_.json');
        try{
            file.openSync(this.filePath, 'w');
            file.writeFileSync(this.filePath, result);
            logger.addPrint('info', `File for client with email "${email}" is generated`);
            return JSON.parse(result);
        } catch(error){
            logger.addPrint('error', `Error: ${error}`);
        }        
    }
}

module.exports = BetServer;


