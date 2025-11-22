const mongoose = require('mongoose');
const validator = require('validator');

const gameSchema = new mongoose.Schema({
    serverName: {
        type: String,
        required: true
    },
    url: {
        type: String,
        required: true,
        unique: true,
        sparse: true,
        validate(value) {
            if (!validator.isURL(value)) {
                throw new Error('Invalid base URL');
            }
        }
    }, 
    // attack: {
    //     type: Map,
    //     of: [Number]
    // },
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
    date: {
        type: Date,
        default: Date.now()
    },
    startTime: String,
    timer: String,
    isFinished: Boolean,
    bets: Object        
});

gameSchema.virtual('accounts', {
    ref: 'Account',
    localField: '_id',
    foreignField: 'game'
});

const Game = mongoose.model('Game', gameSchema);

module.exports = Game;