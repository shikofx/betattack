const express = require('express');
const betserverManager = require('./dbmanager/betserver');    
const browser = require('./browser/browser');
const logger = require('./utils/logger');
const logsRouter = require('./routers/logs')
const filterRequest = require('./resources/filter.json');
const liveRouter = require('./routers/live');
const live = require('./spyes/betcity/live');
const driver = require('./spyes/betcity/driver');

const port = process.env.PORT;
const betserverName = process.env.BETSERVER;
const showBrowser = process.env.SHOW_BROWSER == 'true';
const economy = process.env.ECONOMY_MODE == 'true';
const browserType = process.env.BROWSER;
        
const app = express();

logger.addConsole();

app.use(express.json());
app.use(logsRouter);
app.use(liveRouter);
            
app.listen(port, async () => {    
	
	while(true){
		const betserver = await betserverManager.getBetserverByName(betserverName);
	
		logger.addPrint('info', `SERVER:\t>> is up on port ${port}<<`);
		
	    try{
			await browser.start({ browserType, showBrowser, economy });

			driver.init(betserver, browser);

			await browser.sleep(filterRequest.startSleepHour, filterRequest.finishSleepHour);

			await live.run(driver);  

		} catch(e){

			await browser.stop();			

		}
	}             
})

