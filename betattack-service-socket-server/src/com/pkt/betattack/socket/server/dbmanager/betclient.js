const file = require('fs'); 
const path = require('path');
const DBManager = require('./dbmanager');
const logger = require('../utils/logger');

const ownerTable = 'betclient';
const idColumn = 'id';
const nameColumn = 'name';

const resources = './src/com/pkt/betattack/service/betspy/resources';

let owner;

const Owner = {
    findOne: async function(email) {
        const result = await DBManager.get([ ownerTable, 'email', email]);
        return result;
    }
}

module.exports = Owner;


