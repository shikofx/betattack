const sleeper = require('../../utils/sleeper')
const logger = require('../../utils/logger')
const gameManager = require('../../dbmanager/game');
const filterRequest = require('../../resources/filter.json'); 

const Live = {
    run: async function ( driver ) {
        logger.addPrint('info', `LIVE:\t\t>> run <<`);
        let momentNoGames;  
        let reloadCount = 1;
        let startTime;
        let finishTime;

        await driver.open( );      
        while(true){
            let initTime = new Date();   
            finishTime = new Date();
            if(initTime.getSeconds() === 0 || initTime.getSeconds() === 30 || (finishTime-startTime)/1000 > 30 || (!startTime || !finishTime)){
                startTime = new Date();
                // logger.add('info', `----------------------------------- Start:  ${startTime}`)
                let gamesLive = [];
                try{        
                    let gamesDB = await gameManager.getAllHoursAgo(filterRequest.hoursAgo);
                    gamesLive = await driver.getGames(gamesDB);
                    await gameManager.findFinished(gamesLive, gamesDB);
                    await gameManager.updateDbGames(gamesLive, gamesDB);
                } catch (e) {
                    logger.addPrint('error', `${e}`)
                    continue;
                }
                if(gamesLive &&gamesLive.length === 0){
                    if(!momentNoGames)
                        momentNoGames = new Date();
                    if((new Date() - momentNoGames) / 1000 >= 8 * 60 && sleeper.isMinuteMultiple(filterRequest.awakeMinutes ) ){
                        logger.addPrint('info', '<<<<----------------- No games in live');
                        logger.addPrint('info', `<<<<__________________Go to sleep for ${filterRequest.awakeMinutes - .8} minutes`);
                        await sleeper.sleepSeconds({ seconds: (filterRequest.awakeMinutes - .8) * 60 })
                        continue;
                    } 
    
                    // //Каждые 30 минут простоя перезапускаем браузер
                    // if((new Date() - momentNoGames) / 1000 >= reloadCount * 30 * 60){
                    //     logger.add('info', '<><><><><><> Reload page');
                    //     await betcity.live.page.reload();
                    //     reloadCount++;
                    //     continue;
                    // }
                        //с момента пропадния всех игр прошло 10 минут                    
                } else if(momentNoGames) {
                    momentNoGames = undefined;
                    reloadCount = 1;
                }
            }
            
        }                 
    }
}

module.exports = Live;