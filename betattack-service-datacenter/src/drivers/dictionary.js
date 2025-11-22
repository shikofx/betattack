module.exports = {
    
    /**
     * Переводит название параметра статистики, считанного с сайта в универсальный ключ
     * 
     * @param {String} keyPhrase 
     *                  Содержит название параметра, считанное с сайта
     * @returns 
     * 
     */
    translateStatKey: function(keyPhrase){
        return translate(dictionary.statistic, keyPhrase); 
    },

    /**
     * Переводит название Исхода, считанного с сайта в универсальный ключ
     * 
     * @param {String} keyPhrase 
     *                  Содержит название параметра, считанное с сайта
     * @returns 
     * 
     */
     translateBetKey: function(keyPhrase){
        return translate(dictionary.bets, keyPhrase);        
    },

    /**
     * Переводит название направления, считанного с сайта в универсальный ключ
     * 
     * @param {String} keyPhrase 
     *                  Содержит название параметра, считанное с сайта
     * @returns 
     * 
     */
     translateDirectionKey: function(keyPhrase){
        return translate(dictionary.direction, keyPhrase);       
    }

}

const translate = (dic, keyPhrase) => {
    for(let key in dic){
        const index = dic[key].findIndex(phrase => phrase == keyPhrase.toLowerCase());
        if(index >= 0)
            return key;
    }
    return undefined;    
}

//Нужно чтобы не было повторений по элементам. Как проверять???
const dictionary = {
    statistic: {
        /**
         * Статистика. Футбол
         */
        goals: ["gls", "goals", "голы"],
        corners: ["corners", "угловые"],
        ycards: ["yellow cards", "желтые карточки"],
        rcards: ["red cards", "красные карточки"],
        penalty: ["penalty", "пенальти"],
        fouls: ["fouls", "фолы"],
        offsides: ["offsides", "офсайды"],
        shots_on_target: ["shots on target", "удары в створ ворот"],
        shots_goal: ["удары по воротам"],
        shots_out: ["удары от ворот"],
        outs: ["ауты"],
    },
    
    bets: {
        total_score: ["тотал"], 
        total_penalty: ["тотал пенальти"],
        total_red_cards: ["тотал удалений"]
    }, 
    
    direction: {
        less: ["Мен", "мен"],
        greater: ["Бол", "бол"]
    }
} 