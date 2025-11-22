'use strict'

const browser = require('../browser/browser');
const { GameStatus } = require('./gameStatus');

module.exports = class GameParser {
    page;
    driver;
    url;
    status;
    owner;
    guest;
    timer;
    stat;
    events;
    filter;

    constructor(betserverName, url, filter){
        //**Нужно будет убрать импорт модуля betcity.by в начале, чтобы обобщить */
        // this.locators = require(`./${betserver}`).Locators;
        this.driver = require('./betcity.by').Driver;        
        this.url = url;   
        this.filter = filter;
                     
    }

    async open(filter){
        this.page = await browser.page.new({ url: this.url });  
        await this.driver.waitForOpened(this.page); 
        return this;
    }

    async reload(){
        await browser.page.reload({ url: this.url })
    }
    
    async stop(){
        await browser.page.close({ url: this.url });            
    }

    /**
     * 
     * @returns 
     */
    async parsePage(){
       
        if(!this.owner)
            this.owner = await this.driver.parseTeamOwner(this.page);
        
        if(!this.guest)
            this.guest = await this.driver.parseTeamGuest(this.page);

        if(this.url !== this.page.url()){
            await this.stop();   
            this.status = GameStatus.LIVE_FINISHED;            
        } else {
            this.status = await this.driver.gameStatus(this.page);
        }
            
        if(this.status === GameStatus.LIVE_FINISHED || this.status === GameStatus.RESULTS){
            await this.stop();            
        }

        if(!this.events){
            this.events = {appear: {}, start: {}, finish: {}, break: {}, owner: [], guest: []};
        }

        let timer = "";
        if(this.status === GameStatus.LIVE_TO_START){
            this.bets = await this.driver.parseBets(this.page, this.stat);  
            this.updateEvents(timer);

        } else if(this.status === GameStatus.LIVE || this.status === GameStatus.LIVE_BREAK){
            timer = await this.driver.parseTimer(this.page);
            const stat = await this.driver.parseStat(this.page);
            this.updateEvents(timer, stat); 
            this.stat = stat;
            this.bets = await this.driver.parseBets(this.page, this.stat);                   

        } else if(this.status === GameStatus.LIVE_PAUSE){
            timer = await this.driver.parseTimer(this.page);
            const stat = await this.driver.parseStat(this.page);  
            this.updateEvents(timer, stat);     
            this.stat = stat;

        } else if(this.status === GameStatus.LIVE_FINISHED){
            this.updateEvents(timer);
        }

        if(timer)
            this.timer = timer;

        return {
            url: this.url,
            status: this.status,
            timer: this.timer,
            teamOwner: this.owner,
            teamGuest: this.guest,
            events: this.events,
            stat: this.stat,
            bets: this.bets
        }        
    }    

    updateEvents(timer, stat){
        const time = new Date().toLocaleTimeString("ru-RU", {timeZone: "Europe/Minsk"});

        if(this.status === GameStatus.LIVE_TO_START && !this.events.appear.time)
            this.events.appear = { time };
        
        if(this.status === GameStatus.LIVE && !this.events.start.time){
            this.events.start = { timer, time }
        }

        if(this.status === GameStatus.LIVE_FINISHED && !this.events.finish.time){
            this.events.finish = { timer: this.timer, time }
        }

        if(this.status === GameStatus.LIVE_BREAK && !this.events.break.timeStart){
            this.events.break = { timer: this.timer, timeStart: time }
        } else if(this.status === GameStatus.LIVE && this.events.break.timeStart && !this.events.break.timeFinish){
            this.events.timeFinish = time;
        }
        
        if(stat){
            const currentEvents = statToEvents(this.stat, stat);
            if(Object.entries(currentEvents.owner).length > 0)
                this.events.owner.push({timer, events: currentEvents.owner});     
            
            if(Object.entries(currentEvents.guest).length > 0)
                this.events.guest.push({timer, events: currentEvents.guest});
        } 
    }
}

function statToEvents(currentStat, newStat){
    let events = {owner: {}, guest: {}};
    if(currentStat && newStat){
        for(let key in currentStat.owner){
            if(currentStat.owner[key] != newStat.owner[key])
                events['owner'][key] = newStat.owner[key] - currentStat.owner[key];                                                                                       
        }        

        for(let key in currentStat.guest){
            if(currentStat.guest[key] != newStat.guest[key])
                events['guest'][key] = newStat.guest[key] - currentStat.guest[key];                                                                                   
        }        
    }
    
     return events;    
}