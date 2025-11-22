const fs = require('fs');
const winston = require('winston');

const service = process.env.SERVICE;

const Logger = {
    log: winston.createLogger({
        level: 'info',
        format: winston.format.json(),
        transports: [
            new winston.transports.File({filename: `logs/${service}-error.log`, level: 'error'}),
            new winston.transports.File({filename: `logs/${service}-info.log`})
        ]
    }),
    addConsole: function() {
        if(process.env.NODE_ENV != 'production'){
            this.log.add(new winston.transports.Console({
                format: winston.format.simple()
            }));
        }        
    },
    toJSON: function (level) {
        const logs = fs.readFileSync(`./logs/${service}-${level}.log`).toLocaleString().split('\n');
        const logsNew = [];
        for (let log of logs) {
            if (log.length > 0) {
                let json = JSON.parse(log);
                logsNew.push(json);
            }
        }
        return logsNew;
    },
    add: function(level, message){
        this.log.log(level, `${new Date().toLocaleString("ru-RU", {timeZone: "Europe/Minsk"})}   | ${message}`);
    },
    
    addPrint: function(level, message) {
        this.log.log(level, `${new Date().toLocaleString("ru-RU", {timeZone: "Europe/Minsk"})}   | ${message}`);
        if(process.env.NODE_ENV === "debug")
            console.log(`${new Date().toLocaleString("ru-RU", {timeZone: "Europe/Minsk"})}   | ${message}`)
    }
}

module.exports = Logger;