const file = require('fs');

const DBManager = require('./dbmanager');
const logger = require('../utils/logger');

const betserverTable = 'betserver';
const idColumn = 'id';
const nameColumn = 'name';

const BetServer = {
    getBetserverById: async function(serverID) {
        const result = await DBManager.get([ betserverTable, idColumn, serverID]);
        return DBManager.formatToJSON(result);
    },
    getBetserverByName: async function(betserverName) {
        const result = await DBManager.get([ betserverTable, nameColumn, betserverName ]);
        return result;
    }
}

module.exports = BetServer;


