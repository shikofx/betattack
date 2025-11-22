const logger        = require('../../utils/logger');
const filterRequest = require('../../resources/filter.json');
const selector      = require('../Selector');
const { game } = require('../Selector');
let gameLogMessage;

const timerToNumber = function(timer) {
    const array = timer.split(":");
    if(array.length > 1){
        return Number.parseInt(array[array.length-2])*60 + Number.parseInt(array[array.length-1]);
    }
    return timer;
}

const Betcity = {
    dbmbetcity: Object,
    browser: Object,
    filterRequest: Object,
    server: String,
    page: Object,
    
    init: function( _betserver, _browser ) {
        logger.addPrint('info', `DRIVER:\t>> init << ${_betserver.name}`);
        dbmbetcity = _betserver;
        server = dbmbetcity.name,
        browser = _browser;
        selector.set(dbmbetcity);
    },

    url: function( sports ) {
        const filter = selector.urls.filter;
        let kinds;
        let amount = 0;
        for( let sport of sports) {
            if(amount == 0){
                kinds = filter.initSymbol + filter.sports[sport.kind];
            } else { 
                kinds = filter.initSymbol + filter.separator + filter.sports[sport.kind];
            }
            amount++;
        }

        return (`${selector.urls.live}${kinds}`);
    },

    open: async function () {
        let liveURL = this.url(filterRequest.sports);
        this.page = await browser.page.new({ url: liveURL });
    }, 

    champ: {
        line: Object,
        url: async function() {
            return await this.line.$eval(selector.champ.url, node => node.attributes.href.value);
        },
        name: async function() {
            return await this.line.$eval(selector.champ.name, node => node.innerText);
        }    
    },
    
    getGames: async function( dbGames ) {
        let livePage = this.page;
        let gamesArray = [];
        await livePage.waitFor(selector.champ.name);
        
        const sports = filterRequest.sports;
        for (let sport of sports) {
            const sportSelector = await selector.champ.sports[sport.kind];
            let betsFilter = Object.entries(sport.bets);
            await livePage.waitFor(sportSelector, { timeout: 2 * 60 * 1000 }); //ждем 2 минуты после запуска браузера
            let champLines = await livePage.$x(sportSelector);
            for (let champLine of champLines) {
                this.champ.line = champLine;
                let gameBoxes = await this.champ.line.$$(selector.game.box);
                for (let gameBox of gameBoxes) {
                    this.game.box = gameBox;
                    const timer = await this.game.timer();
                    const urlRef = await this.game.url();
                    const url = selector.urls.base + urlRef;
                    //Определим является ли игра активной
                    let dbGameIndex = dbGames.findIndex(g => g.url.includes(urlRef));
                    const date = `${new Date().toLocaleString("ru-RU", {timeZone: "Europe/Minsk"})}`;
                    const bets = await this.game.betsByFilter(timerToNumber(timer), 
                                                                betsFilter, 
                                                                dbGameIndex >= 0 ? dbGames[dbGameIndex] : undefined);
                                                                
                    if (Object.getOwnPropertyNames(bets).length > 0) {
                        gamesArray.push({
                            serverName: dbmbetcity.name,
                            url: url,
                            sport: sport.kind,
                            championship: {
                                name: await this.champ.name(),
                                url: selector.urls.base + await this.champ.url()
                            },
                            teamFirst: {
                                name: await this.game.team.name(1),
                                score: await this.game.team.score(1)
                            },
                            teamSecond: {
                                name: await this.game.team.name(2),
                                score: await this.game.team.score(2)
                            },
                            isFinished: false,
                            date: date,
                            startTime: await this.game.start(),
                            timer: timer,
                            bets                        
                        });
                    }
                }
            }
        }
        let message;

        if(gamesArray.length > 0)
            message = `DRIVER:\t\tНашел ${gamesArray.length}`
        else
            message = `DRIVER:\t\tИгр не нашел`;
        if(message && message != gameLogMessage){
            gameLogMessage = message;
            logger.addPrint('info', gameLogMessage);
            
            //ОТПРАВИТЬ СООБЩЕНИЕ В ВАЙБЕР
        }

        return gamesArray;
    }, 

    game: {
        box: Object,
        timer: async function() {
            return await this.box.$eval(selector.game.timer, node => node.innerText);
        },
    
        start: async function() {
            return await this.box.$eval(selector.game.start, node => node.innerText);
        },
        team: {
            name: async function(teamNumber) {
                let team = "";
                try{
                    let teams = await Betcity.game.box.$$(selector.game.teams);  
                    team = await teams[teamNumber - 1].$eval('span', node => node.innerText);
                } catch {
                    let teams = await Betcity.game.box.$$('.line-event__name-logo');  
                    team = await teams[teamNumber - 1].$eval('.line-event__name-text', node => node.innerText);
                }
                return team;
            },
            score: async function(teamNumber) {
                if(await Betcity.game.box.$(selector.game.score))
                    return Number.parseInt((await Betcity.game.box.$eval(selector.game.score, node => node.innerText)).split(':')[teamNumber-1]);
                return null;
            },        
        },
        
        url: async function() {
            return await this.box.$eval(selector.game.url, node => node.attributes.href.value);
        },
        
        getBetsBox: async function() {
            let betsBox = null;
            let collapseCount = 0;
            try{
                let collapseButton = await this.box.$(selector.bets.collapseButton);
                if (collapseButton) {
                    let collapse = await this.box.$eval(selector.bets.collapseButton, node => node.innerText);
                    if (collapse.length > 0){
                        collapseCount = Number.parseInt(collapse);
                        if(collapseCount > 0){
                                await collapseButton.tap();                        
                        } 
                            
                        betsBox = await this.box.$(selector.bets.box);
                    }
                }
            } catch (e) {
            }
            return betsBox;
        },
        
        betButton: async function( betSectionSelector, lineSelector){
            let betsBox = await this.getBetsBox();
            let bet = '0';
            if (betsBox){
                let betSection = await this.box.$x(betSectionSelector);
                let betLine = null;
                if(betSection && betSection.length > 0){ 
                    betLine = await betSection[0].$x(lineSelector);
                    if(betLine && betLine.length > 0){
                        bet = await betLine[0].$eval('button', node => node.innerText);
                    }
                }
            }
            return Number.parseFloat(bet);
        },

        betValue: async function( betSectionSelector, lineSelector){
            let betsBox = await this.getBetsBox();
            let bet = '0';
            if (betsBox){
                let betSection = await this.box.$x(betSectionSelector);
                let betLine = null;
                if(betSection && betSection.length > 0){ 
                    betLine = await betSection[0].$x(lineSelector);
                    if(betLine && betLine.length > 0){
                        bet = await betLine[0].$eval('button', node => node.innerText);
                    }
                }
            }
            return Number.parseFloat(bet);
        },
        value: async function( betSectionSelector, betSelector){
            let betsBox = await this.getBetsBox();
            let bet = '0';
            if (betsBox){
                let betSection = await this.box.$x(betSectionSelector);
                let betLine = null;
                if(betSection && betSection.length > 0){ 
                    betLine = await betSection[0].$x(betSelector);
                    if(betLine && betLine.length > 0){
                        bet = await (await betLine[0].getProperty('innerText')).jsonValue();//$eval('./*[1]', node => node.innerText);
                    }
                }
            }
            let number = Number.parseFloat(bet)
            return number;
        },
        
        betsByFilter: async function(timer, filters, dbGame) {
            let objects = {};
            for (let filter of filters) {
                const header = filter[0];
                let filterCases = filter[1];
                let betCases = {};
                let amount = 0;
                //пробегаем по всем направлениям (less, more, p125 ...)
                for (let filterCase of filterCases) {
                    let betValue;
                    if(filterCase === 'value'){
                        betValue = await this.value(selector.bets[header], selector.bets.values[filterCase]);
                    } else {
                        betValue = await this.betValue(selector.bets[header], selector.bets.values[filterCase]);
                    }
                    
                    let currentValue;                        
                    if(dbGame && !dbGame.isFinished){
                        let currentValues = dbGame.bets[header][filterCase];
                        if(currentValues && currentValues.length > 0){
                            currentValue = Object.values(currentValues[currentValues.length - 1])[0]
                        }                        
                    }
                    if(betValue != 0 || (currentValue && currentValue > 0) ){
                        let betCase = {};                    
                        Object.defineProperty(betCase, timer, {
                            value: betValue,
                            enumerable: true
                        });
                        Object.defineProperty(betCases, filterCase, {
                            value: [betCase],
                            enumerable: true
                        });
                        amount++;
                    };
                }
                if(amount > 0){
                    Object.defineProperty(objects, header, {
                        value: betCases,
                        enumerable: true
                    });
                }
            }
            return objects;
        }        
    }    
}

module.exports = Betcity;


