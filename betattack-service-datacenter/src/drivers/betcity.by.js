const e = require("express");
const { Page, ElementHandle } = require("puppeteer");
const logger = require("../utils/logger");
const { GameStatus } = require("./gameStatus");
const dictionary = require("./dictionary");
const Predicate = require("./predicate");
const Logger = require("../utils/logger");
const common = require("./common");
const { ExceptionHandler } = require("winston");

const betSelector = ".//*[@class='dops-item']/*[contains(., 'Фора')]/parent::*//*[contains(text(), 'Ф1')]/parent::*";
const timerInfoRowSelector = ".scoreboard-content__info";
const timerSelector = ".//*[contains(@class, 'scoreboard-content__info')]//*[contains(text(), ':')]";
const matchBreakSelector = ".//*[contains(@class, 'scoreboard-content__info')]//*[contains(text(), 'перерыв')]";

const mainScoreSelector = '.scoreboard-content__main-score';
const liveStatHeaderSelector = ".livestat-table-info__header .livestat-table-info__value span";
const statValueRowsSelector = ".livestat-table-info__item";
const statValueSelector = ".livestat-table-info__value";

const ownerSelector = ".team-home";
const guestSelector = ".team-away";
const betsContainerSelector = ".dops";
const resultsSelector = ".event-result-dops"

const isOpenedSelector = ownerSelector;

// 
const Driver = {
    
    /**
     * 
     * @param {Page} page 
     */
     parseStat: async function(page){
        const statHeaders = await parseStatHeaders(page);
        
        const statValueRows = await page.$$(statValueRowsSelector);

        let ownerStat = [];
        let guestStat = [];
        if (statValueRows.length === 2) {
            const ownerValueElements = await statValueRows[0].$$(statValueSelector);
            const guestValueElements = await statValueRows[1].$$(statValueSelector);

            ownerStat = await joinHeadersAndValues(statHeaders, ownerValueElements );
            guestStat = await joinHeadersAndValues(statHeaders, guestValueElements );         
        }

        const score = await parseTotalScore(page);
        if(score){
            ownerStat['goals'] = score.owner;
            guestStat['goals'] = score.guest;
        }

        return {
            owner: ownerStat,
            guest: guestStat,
        }
    },

    parseBets: async function(page, stat) {
        const score = stat.owner.goals + stat.guest.goals;
        return {
            football: {
                total: {
                    penalty: {
                        less: await betTotal(page, "Тотал пенальти", Predicate.LESS),
                    },
                    redCards: {
                        less: await betTotal(page, "Тотал удалений", Predicate.LESS),
                    },

                    score: {
                        less_diff25: await betTotal(page, "Тотал", Predicate.LESS, score, 2.5),
                    }
                }
            }      
        }
    },
    /**
     * 
     * Определяет теущий статус игры
     * 
     * @param {Page} page - страница игры
     * @returns {Promise} state- состояние текущей игры
     */
    gameStatus: async function(page) {
        let isOpenedElement = await common.getUnique(page, isOpenedSelector);
        
        if(!isOpenedElement)
            return GameStatus.LIVE_FINISHED;

        let timerRowElement = await common.getUnique(page, timerInfoRowSelector);
        if(!timerRowElement)
            return GameStatus.LIVE_TO_START;
        
        const matchTimer = await common.getUnique(page, timerSelector);
        if(matchTimer){
            const betsBox = await common.getUnique(page, betsContainerSelector);
            if(betsBox)
                return GameStatus.LIVE;
            else 
                return GameStatus.LIVE_PAUSE;
        } 
 
        const matchBreak = await common.getUnique(page, matchBreakSelector);
        if(matchBreak)
            return GameStatus.LIVE_BREAK;  
        
        const matchResult = await common.getUnique(page, resultsSelector);
        if(matchResult)
            return GameStatus.RESULTS;
        logger.addPrint('info', 'Статус игры не определён');

        return undefined;
    },

    /**
     * 
     * @param {Page} page 
     * @returns {Promise} Значение таймера
     */
    parseTimer: async function(page){
        const timer = await common.getUnique(page, timerSelector)
        if(timer)
            return timer.evaluate(t => t.innerText);

        return undefined;
    },

    /**
     * 
     * @param {Page} page 
     */
    waitForOpened: async function(page){
        let opened = this.scoreFirstRow;
        for(let i = 0; i < 4; i++ ){
            try{
                await page.waitForSelector(isOpenedSelector, {timeout: 0});  
                return;
            } catch {
                await page.reload();
            }        
        }
        logger.addPrint('error', 'Cannot open url. Check connection.');
    },

    /**
     * 
     * @param {Page} page 
     * @returns {Promise} название команды-хозяина поля
     */
    parseTeamOwner: async function(page) {
        return await common.getInnerText(page, ownerSelector);            
    },

    /**
     * 
     * @param {Page} page 
     * @returns {Promise} название команды-гостя
     */
    parseTeamGuest: async function (page) {
        return await common.getInnerText(page, guestSelector);        
    },
}

module.exports = {
    Driver
}

async function joinHeadersAndValues(headers, values) {
    let index = 0;
    let joined = {};

    if(headers instanceof Array && values instanceof Array && headers.length === values.length){
        for (let element of values) {
            joined[headers[index]] = await element.evaluate(e => e.innerText);
            index++;
        }
        return joined;
    }
    return undefined;
    
}

/**
 * 
 * @param {Page} page 
 * @returns Заголовки таблицы со статистикой
 */
async function parseStatHeaders(page){
    const headerElements = await page.$$(liveStatHeaderSelector);
    let headers = [];
    for(let element of headerElements){
        const headerString = await element.evaluate(e => e.getAttribute('title'));            
        headers.push(dictionary.translateStatKey(headerString));
    }
    return headers;
}

 /**
 * Общий счет в виде объекта:
 * 0:1 ==> {owner: 0, guest: 1}
 * 
 * @param {Page} page 
 */
async function parseTotalScore(page) {
    
    const scoreString = await page.$eval(mainScoreSelector, e => e.innerText);

    return{
        owner: Number.parseInt(scoreString.split(":")[0]),
        guest: Number.parseInt(scoreString.split(":")[1])
    }
}



/**
 * @param {Page} page 
 * @param {String} name 
 * @param {Predicate} predicate 
 * @param {Number} total 
 * @param {Number} diff 
 * @returns {Array} of bets
 */
async function betTotal (page, name, predicate, total, diff) {
    let bets = await parse2DirectionBets(page, name);
    if ((total || total >= 0) && diff) {
        return bets.filter(bet => bet.direction === predicate && diff <= bet.value - total);
    } else {
        return bets.filter(bet => bet.direction === predicate);            
    }
}

/**
 * 
 * @param {Page} page 
 * @param {String} boxName 
 * @returns 
 */
async function parse2DirectionBets (page, boxName) {
    let bets = [];

    let boxContent;
    try{
        boxContent = await common.getInnerText(page, `//div[.='${boxName}']/parent::*`);
    } catch {
        Logger.addPrint("info", `Исход '${boxName}' не найден`);
    }

    if(!boxContent)
        return [];

    let fields = boxContent.split('\n');
    const title = dictionary.translateBetKey(fields.shift().toLowerCase());
    //TODO: Добавить подстановку из словаря Название и направления
    while(fields.length > 0){
        
        let value = Number.parseFloat(fields.shift().toLowerCase());
        let direction1 = dictionary.translateDirectionKey(fields.shift().toLowerCase());

        let size1 = Number.parseFloat(fields.shift().toLowerCase());
        bets.push({title, value, direction: direction1, size: size1});
        
        let dir2 = fields.shift().toLowerCase();

        //Если столбец смещен то нужно начать сначала
        if(Number.parseFloat(dir2) || Number.parseInt(dir2))
            continue;

        let direction2 = dictionary.translateDirectionKey(dir2);
        let size2 = Number.parseFloat(fields.shift().toLowerCase());

        bets.push({title, value, direction: direction2, size: size2});
    }
    return bets;
}