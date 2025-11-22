const browser = require('./browser/browser');
const Sleeper = require('./utils/sleeper');
const server = require('express')();
const { Locators } = require('./drivers/betcity.by');
const Parser = require('./drivers/parser');
const logger = require('./utils/logger')
const cron = require('node-cron');

const parsers = [];


const urls = [
    'https://betcity.by/ru/live/soccer/145322/9098289',
    // 'https://betcity.by/ru/live/soccer/145294/8553643',
    // 'https://betcity.by/ru/live/soccer/113874/8814790',
    // 'https://betcity.by/ru/live/soccer/78068/8814811', 

]

const filter = {
    bets: {
        totalPenalty: ['less'],
        rcards: ['less'],        
    }
}
server.listen(3100, async () => {
    await browser.start({type: 'chromium', show: true, economy: false});

    for(let url of urls){
        parsers.push(await new Parser('betcity.by', url, filter).open());
    }    

    cron.schedule('*/15 * * * * *', async function () {
        for (let parser of parsers) {
            logger.addPrint('info', await parser.parsePage())
                // JSON.stringify(await parser.parsePage(), null, '\t'));
        }
    
        console.log('-----------------------------');
    });       
});

/**
 * Обновлять страницы каждые 5 минут
 */
/**
 * Нужно разделить сбор данных для атаки и сбор данных для статистики и вилок.
 * Если распределением данных будет заниматься каждый сервис в отдельности, то 
 * логика сервера будет предельно простой
 * 
 * Анализ состояния игры:
 * НЕ НАЧАЛАСЬ 
 * ** нету таймера, 
 * ** в хидере не написано - "перерыв"
 
 * - -> ИСХОДОВ НЕТ -> закрываем страницу
 * - -> ИСХОДЫ ЕСТЬ -> Начинаем сбор данных с интервалом 20 секунд
 * 
 * ИДЕТ ИГРА
 * ** появился таймер
 * ** таймер -> "перерыв"
 * - -> ИСХОДЫ ЕСТЬ - собираем данные
 * - -> ИСХОДЫ ИСЧЕЗЛИ - ждем появления данных
 * - -> ИСХОДЫ ВЕРНУЛИСЬ - 
 
* ОКОНЧИЛАСЬ -> закрываем страницу и высвобождаем ячейку:
 * - URL сменился -> ушел из игры в Live
 * - Вместо ставок - страница со статистикой
 * 
 *  - Ставки есть, игра не началась -> Анал
 * - Ставки есть, игра началась, исхода нет
 * - Ставки есть, игра началась, исход есть
 * - Ставки есть, игра началась, исход был
 * - Ставок нет, игра началась, исход был (ставки не принимаются)
   
 */

