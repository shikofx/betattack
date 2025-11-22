const mongoose = require('mongoose');

const betSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    sport: {
        type: String,
        required: [true, 'Sport kind is required']
    },
    
    conditions: {
        //-1 means "break" "перерыв"
        startTimer: Number,
        isAlwaysOpened: Boolean,
        minKef: Number
    },

    UI: {
        name: String,
    },

    result: {
        type: Map,
        of: {
            UI: {
                // Больше, меньше, равно
                name: {
                    type: String,
                    required: true
                },

                sign:{
                    type: String,
                    required: true
                }
            }
        }   
    },

    values: {
        type: Map,
        of: String
    }

}, {
    timestamps: true
});

const Bet = mongoose.model('bet', betSchema);

module.exports = Bet;