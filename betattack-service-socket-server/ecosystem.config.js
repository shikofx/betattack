module.exports = {
  apps : [{
    name:                   'socket-server',
    script:                 'src/com/pkt/betattack/socket/server/index.js',
    max_memory_restart:     '2G', //Выделить 2G под работу сервиса. При превышении перезагрузить
    args:                   "--max_old_space_size=8000", //Увеличить hash для NodeJS
    watch:                  false,
    watch_delay:            1000,
    ignore_watch :          ["node_modules", "logs"],
    // cron_restart:           "01 01 * * * *",
    env: {
      SERVICE:              "socket-server",
      PORT:                 "3001",
      URL_DBMANAGER_HOST:   "http://185.213.209.34:3000"    
    }               
  }]
};
