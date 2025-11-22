const mongoose = require('mongoose');

const accountSchema = new mongoose.Schema({
    balance: Number,
    currentBets: Number,
    login:      { 
        type: String, 
        required: [ true, 'Login is required' ],
        sparse: true
    },
    id_number:     { 
        type: String, 
        required: [ true, 'Account number is required' ],
        sparse: true
    },
    password:   { 
        type: String, 
        required: [ true, 'Account password is required' ]
    },
    spyAddress: [{
        ip: String,
        port: Number
    }],
    owner:      { 
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Owner',
        required: [ true, 'Account ownener is required' ]
    },
    server:     { 
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Server',
        required: [ true, 'Bet server is required' ]
    },
    limit: {
        attack: {
            maxCount: Number,
            maxSum: Number
        }
    },
    attacks: [{
        request: Object,
        available: Boolean,
        values: [{
            timer: String,
            sum: Number,
            koefficient: Number
        }]
    }]
}, {
    timestamps: true
});

accountSchema.index({ login: 1, server: 1 }, { unique: [ true, 'Login on server is already available' ]});
accountSchema.index({ owner: 1, server: 1 }, { unique: [ true, 'Owner on server is already registered' ] });
accountSchema.index({ id_number: 1, server: 1 }, { unique: [ true, 'Number on server is already used' ] });

const Account = mongoose.model('Account', accountSchema);

module.exports = Account;