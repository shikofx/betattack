const BetserverManager = require('../dbmanager/betserver')

const GlobalVariables = {
    victims: [],
    userTokens: new Map(),
    connections: new Map(),
    
    init: async function(){
        this.victims = await BetserverManager.getAll();        
    }
}

module.exports = GlobalVariables;