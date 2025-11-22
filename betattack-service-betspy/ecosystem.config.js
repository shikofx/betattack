module.exports = {
  apps : [{
    name:                 'betcity-live',
    script:               'src/com/pkt/betattack/service/betspy/index.js',
    max_memory_restart:   '2G', //Выделить 2G под работу сервиса. При превышении перезагрузить
    args:                 "--max_old_space_size=8000", //Увеличить hash для NodeJS
    watch:                false,
    watch_delay:          5000,
    ignore_watch :        ["node_modules", "logs", "*.json"],
    cron_restart:         "01 */30 * * * *",
    env: {
      NODE_ENV:           'betcity:live',
      PORT:               3100,
      BROWSER:            'chromium',
      BETSERVER:          'betcity.by',
      URL_DBMANAGER_HOST: 'http://185.92.150.218:3000',
      SERVICE:            'live',
      SHOW_BROWSER:       false,
      ECONOMY_MODE:       true,
    }               
  }]
}