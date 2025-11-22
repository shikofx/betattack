const file = require('fs'); 
const path = require('path');
const DBManager = require('./dbmanager');
const logger = require('../utils/logger');
const publicIp = require('public-ip');
const accountTable = 'accounts';
const idColumn = 'id';
const nameColumn = 'name';

const resources = './src/com/pkt/betattack/service/betspy/resources';
const betserverName = process.env.BETSERVER;
const publicPort = process.env.PORT;

const BetServer = {
    filePath: String,

    getToConnect: async function(owner) {
        const result = await DBManager.get([ accountTable, 'owner', 'id', owner._id]);
        return DBManager.formatToJSON(result);
    },

    toJSON: async function(owner){
        let results = JSON.parse(await this.getToConnect(owner));
        let publicAddress = {
            ip: await publicIp.v4(),
            port: publicPort
        };
        this.filePath = path.join(resources, 'ACC_' + owner.email + '_.json');
        for(let result of results){
            let adresses = result.spyAddress;
            if(adresses.length > 0 && adresses.filter( address => (address.ip === publicAddress.ip && address.port.toString() === publicAddress.port) ).length === 0 ){
                adresses = [];
                adresses.push(publicAddress);
                await DBManager.patch([ accountTable, 'id', result._id ], { spyAddress: adresses });
            } else if(!adresses.length){
                await DBManager.patch([ accountTable, 'id', result._id ], { spyAddress: [publicAddress] });
            }
            
        }
        
        results = await this.getToConnect(owner);
        // this.filePath = this.filePath + '/ACC_' + owner.phone.replace('+', '') + '.json'
        try{
            file.openSync(this.filePath, 'w');
            file.writeFileSync(this.filePath, results);
            logger.addPrint('info', `File for account is generated`);
            return JSON.parse(results);
        } catch(error){
            logger.addPrint('error', `Error: ${error}`);
        }        
    }
}

module.exports = BetServer;


