const file = require('fs'); 
const path = require('path');
const DBManager = require('./dbmanager');
const logger = require('../utils/logger');
const publicIp = require('public-ip');
const accountTable = 'accounts';

const publicPort = process.env.PORT;

const Account = {
    findAllBy: async function(owner) {
        let result = await DBManager.get([ accountTable, 'owner', 'id', owner._id]);
        const accs = JSON.parse(DBManager.formatToJSON(result));
        if(accs.length && accs.length > 0){
            let publicAddress = {
                ip: await publicIp.v4(),
                port: publicPort
            };
            for(let account of accs){
                let adresses = account.spyAddress;
                if(adresses.length > 0 && adresses.filter( address => (address.ip === publicAddress.ip && address.port.toString() === publicAddress.port) ).length === 0 ){
                    adresses = [];
                    adresses.push(publicAddress);
                    await this.modify(account, { spyAddress: adresses });
                } else if(!adresses.length){
                    await this.modify(account, { spyAddress: [publicAddress] });
                }
            }
            result = await DBManager.get([ accountTable, 'owner', 'id', owner._id]);
        }
        return result;
    },

    find: async function(owner, server){
        let result = await DBManager.get([ accountTable, 'owner', 'id', owner._id ]);
        let account = result.find(a => a.server === server._id);
        let publicAddress = {
            ip: await publicIp.v4(),
            port: publicPort
        };
        let adresses = account.spyAddress;
        const address = adresses.filter( address => (address.ip === publicAddress.ip && address.port.toString() === publicAddress.port) )
        if(adresses.length > 0 && address.length === 0 ){
            adresses = [];
            adresses.push(publicAddress);
            account = (await this.modify(account, { spyAddress: adresses })).data;
        } else if(!adresses.length){
            account = (await this.modify(account, { spyAddress: [publicAddress] })).data;
        }
        return account;
    },

    modify: async function( account, body ) {
        return await DBManager.patch( [accountTable, 'id', account._id],  body);
    }
}

module.exports = Account;


