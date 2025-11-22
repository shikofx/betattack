const Sleeper = {
    sleep: async function (seconds) {
        const date = Date.now();
        let currentDate = null;
        do {
          currentDate = Date.now();
        } while (currentDate - date < seconds * 1000);
    },

    sleepSeconds: async ({ seconds }) => {
        return new Promise((resolve) => setTimeout(resolve, seconds*1000));
    },
    
    //Минуты кратные 5 - 0, 5, 10, 15 ...
    isMinuteMultiple: (minute) => {
        let currentTime = new Date();
        if(currentTime.getMinutes() % minute === 0){
            return true;
        } 
        return false;
    },

    randomSleep: (min, max) => {
        const timeout = Math.random() * (max - min) + min;
        return new Promise((resolve) => setTimeout(resolve, timeout*1000));
    }
}
    
module.exports = Sleeper;