const DBManager = require('./dbmanager');
const logger = require('../utils/logger');
const gameTable = 'game';
const idColumn = 'id';

const GameManager = {
    timerIsUpdated: Boolean,
    getGameById : async function(gameId){
        return await DBManager.get([ gameTable, idColumn, gameId ]);
    }, 

    getAll : async function(/*betcity*/){
        return await DBManager.get([ gameTable, 'all' ]);;
    },

    getActive : async function(/*betcity*/){
        return await DBManager.get([ gameTable, 'active' ]);;
    },

    getAllHoursAgo : async function( hours ) {
        const games = await DBManager.get([ gameTable, 'all', hours ]);
        // logger.addPrint('info', `DB:\t\t>> ${games.length} games for ${hours} hours`);
        return games;
    }, 

    post : async function(body) {
        return await DBManager.post([ gameTable ], body);
    }, 
    
    patch : async function(game, body) {
        return await DBManager.patch([ gameTable, idColumn, game._id ], body)
    },

    delete : async function(gameId){
        return await DBManager.delete([ gameTable, idColumn, gameId ])
    },

    findFinished: async function(betcityGames, games){
        if(games.length > 0){
            for(let game of games){
                let arr = betcityGames.filter( g => g.url === game.url )
                if(arr.length === 0 && !game.isFinished){
                    let body = {
                        isFinished: true
                    }
                    await this.patch(game, body);
                    logger.addPrint('info', `>><< xx Game is finished:   ${game.teamFirst.name} <-> ${game.teamSecond.name}`);
                } else if(arr.length > 0 &&  game.isFinished){
                    let body = {
                        isFinished: false
                    }
                    await this.patch(game, body);
                    logger.addPrint('info', `>>>> ** Game is resumed:    ${game.teamFirst.name} <-> ${game.teamSecond.name} `);
                }
            }   
        }
            
    },

    updateDbGames : async function(games, dbGames) {
        for (let game of games) {
            const url = game.url; 
            dbGame = dbGames.find(g => g.url.includes(url));
            let body = { };
            if (dbGame && !dbGame.isFinished) {
                let newFirstTeam = getNewScore(game, dbGame, 'teamFirst');
                let newSecondTeam = getNewScore(game, dbGame, 'teamSecond');
                let newBets = getNewBets(game, dbGame);
                let newTimer;
                if(game.timer !== dbGame.timer){
                    newTimer = game.timer;
                }
                if(newBets || newFirstTeam || newSecondTeam || newTimer){
                    if(newTimer) addToObject(body, 'timer', game.timer);
                    if(newFirstTeam) addToObject(body, 'teamFirst', newFirstTeam);   
                    if(newSecondTeam) addToObject(body, 'teamSecond', newSecondTeam);                    
                    if(newBets) addToObject(body, 'bets', newBets);                                        
                }                   
            } else if(!dbGame){
                logger.addPrint( 'info', `>>>> ++ Game was added:    ${game.teamFirst.name} <-> ${game.teamSecond.name}`);
                const responce = await this.post(game);                
            }
            if (Object.entries(body).length > 0) {
                logger.addPrint( 'info', `>>>> @@ Game was updated:    ${game.teamFirst.name} <-> ${game.teamSecond.name} with body: ${JSON.stringify(body)}`);
                const responce = await this.patch(dbGame, body);               
            }
        }
    }
}

module.exports = GameManager;

function addToObject(body, name, object) {
    if(object)
        Object.defineProperty(body, name, {
            value: object,
            enumerable: true
        });
}

function getNewBets(game, dbGame) {
    let newBets = null;            
    let betsFromServer = game.bets;
    let betsFromDB = dbGame.bets;
    let betHeaders = Object.getOwnPropertyNames(betsFromServer);
    for (let headerName of betHeaders) {
        let betFromServer = betsFromServer[headerName];
        let betFromDb = betsFromDB[headerName];
        if (betFromDb) {
            betDirections = Object.getOwnPropertyNames(betFromServer);
            for (let diractionName of betDirections) {
                let betValueFromServer = betFromServer[diractionName][0];
                let betValuesFromDb = betFromDb[diractionName];
                if (betValuesFromDb) {
                    // берем последнюю ставку и сравниваем со значением с сервера
                    let lastValueFromDb = betValuesFromDb[betValuesFromDb.length - 1];
                    let valueFromServer = Object.values(betValueFromServer)[0];
                    let valueFromDB = Object.values(lastValueFromDb)[0];
                    if (valueFromDB !== valueFromServer) {
                        //добавляем ставку в список
                        betValuesFromDb.push(betValueFromServer);
                        newBets = betsFromDB;
                    }
                }
                else {
                    //добавляем новое направление
                    Object.defineProperty(betFromDb, diractionName, {
                        value: [betValueFromServer],
                        configurable: true,
                        enumerable: true
                    });
                    newBets = betsFromDB;
                }
            }
        }
        else {
            // Добавляем новый вид ставок
            Object.defineProperty(betsFromDB, headerName, {
                value: betFromServer,
                configurable: true,
                enumerable: true
            });
            newBets = betsFromDB;
        }
        return newBets;
    }
}

function getNewScore(game, dbGame, team){
    if(game[team].score !== dbGame[team].score){
        dbGame[team].score = game[team].score;
        return dbGame[team];
    }
    return null;
}