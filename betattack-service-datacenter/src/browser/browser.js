const puppeteerCore = require('puppeteer-core');
const puppeteer     = require('puppeteer');
const { Page }      = require('puppeteer')
const os            = require('os');
const logger        = require('../utils/logger');
const sleeper = require('../utils/sleeper');

const Browser = {
    instance: Object, 
    pages: new Map(Page),
    economyMode: Boolean,
    
    start: async function( { type, show, economy } ) {
        economyMode = economy;
        logger.addPrint('info', 'BROWSER:\t++ start ++');
        if(type === 'chrome'){
            this.instance = await puppeteerCore.launch({ 
                executablePath: `C:/Program Files (x86)/Google/Chrome/Application/chrome.exe`,
                userDataDir: `${os.homedir}/AppData/Local/Google/Chrome/User Data/`,
                defaultViewport: {
                    width: 1400,
                    height: 768
                },
                headless: !show,
                args: ['--no-sandbox'], 
                timeout: 20000
            });
        } else if(type === 'chromium'){
            this.instance = await puppeteer.launch({ 
                defaultViewport: {
                    width: 1400,
                    height: 768
                },
                headless: !show,
                args: ['--no-sandbox'], 
                timeout: 20000,  

            });
        } else if(type === 'chromium-userdata'){
            this.instance = await puppeteer.launch({ 
                userDataDir: `./.chrome/UserData/`,
                defaultViewport: {
                    width: 1400,
                    height: 768
                },
                headless: !show,
                args: ['--no-sandbox'], 
                timeout: 20000,  
            });
        }
         
    }, 

    isConnected: function(){
        if(this.instance.isConnected && this.instance.isConnected())
            return true;
        else 
            return false
    },

    stop: async function(){
        logger.addPrint('info', "BROWSER:\tXXX stop XXX")
        await sleeper.sleep(1);
        if(this.isConnected()){
            await this.instance.disconnect();
            await this.instance.close();
        }
    },

    page: {
        new: async function( { url } ) {
            let page;
            if(Browser.isConnected()){
                logger.addPrint('info', `BROWSER PAGE:\t>> new << ${url}`);
        
                page = await Browser.instance.newPage();
                if(economyMode){
                    await page.setRequestInterception(true);
                    page.on('request', (request) => {
                        if (['image', 'font', 'media', 'stylesheet'].indexOf(request.resourceType()) !== -1 ||
                            request.url().endsWith('.png')) {
                            request.abort();
                        } else {
                            request.continue();
                        }
                    });                
                }        
                if(url){
                    try{
                        await page.goto( url, {timeout: 60000} );                        
                    } catch(e){
                        logger.addPrint(e.stack);
                        return;
                    }
                }
                    
                Browser.pages.set( url, page );           
                logger.addPrint('info', `BROWSER PAGE:\t>> opened << ${url}`);
                return page;
            } else {
                throw "BROWSER PAGE:\tERR> Browser закрыт <ERR";
            }
        },

        reload: async function( { url } ){
            logger.addPrint('info', `BROWSER PAGE:\t>> reload << ${ url }`);
            let page = Browser.pages.get(url);
            if(page instanceof Page)
                await page.reload();
        },
    
        close: async function( { url } ){
            logger.addPrint('info', `BROWSER PAGE:\t<< close >> ${ url }`)
            let page = Browser.pages.get(url);
            if(page) {
                if(page.close)
                    await page.close();
                Browser.pages.delete(url);
            }
        }
    },
     
    element: {
        validate: async (page, selector) => {
            return page.$(selector) ? page.$(selector) : false;
        }
    },

    sleep: async function (startHour, finishHour) {
        let currentDate = new Date();
        if (currentDate.getHours() >= startHour && currentDate.getHours() < finishHour) {
            logger.addPrint('info', `BROWSER:\t<> sleep <> from ${startHour}h - to ${finishHour}h`)
            await this.stop();
            let delayMinutes = finishHour * 60 - currentDate.getHours() * 60 - currentDate.getMinutes();
            await sleeper.sleepSeconds({ seconds: delayMinutes * 60 });            
        }
    }
}

module.exports = Browser