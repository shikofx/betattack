const Game = {
    url: String,
    championship: {
        name: String,
        url: String
    },
    teamFirst: {
        name: String,
        score: Number
    },
    teamSecond: {
        name: String,
        score: Number
    },
    startTime: Date,
    timer: Number,
    bets: {
    }
}

module.exports = Game;