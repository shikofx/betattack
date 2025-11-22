module.exports = {
  apps : [{
    name: 'db-manager',
    script: 'src/com/pkt/betattack/service/dbmanager/index.js',
    args: ["--max_old_space_size=8000"], //Увеличить hash для NodeJS
    //max_memory_restart: '2G', //Выделить 2G под работу сервиса. При превышении перезагрузить
    watch: false,
    env: {
      JWT_SECRET: "betattackapplication",
      MONGODB_URL: "mongodb+srv://betattack:rfr3t2f_RFR5@betattack-byqzl.mongodb.net/betattack-api?retryWrites=true",
      NODE_ENV: 'dbmanager',
      PORT: 3000
    }    
  }],

  deploy : {
    production : {
      user : 'SSH_USERNAME',
      host : 'SSH_HOSTMACHINE',
      ref  : 'origin/master',
      repo : 'GIT_REPOSITORY',
      path : 'DESTINATION_PATH',
      'pre-deploy-local': '',
      'post-deploy' : 'npm install && pm2 reload ecosystem.config.js --env production',
      'pre-setup': ''
    }
  }
};