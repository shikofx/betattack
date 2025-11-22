const mongoose = require('mongoose');
const Url = require('./endpoints/url');
const Selector = require('./endpoints/selectors');
const Account = require('./account');
const validator = require('validator').default;

const betserverSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        unique: true,
        sparse: true,
        minlength: 4
    },
    urls: {
        type: Url,
        validate(value){
            if(value.length <= 0){
                throw new Error("Add URLs values")
            }
        }
    },
    selectors: Selector,
    delay: {
        account: {
            repeater: Number
        },
        attack: {
            equalIP: Number,
            differentIP: Number
        }
    }
}, {
    timestamps: true
});

betserverSchema.virtual('accounts', {
    ref: 'Account',
    localField: '_id',
    foreignField: 'betserver'
});

betserverSchema.pre('remove', async function (next) {
    const betserver = this;
    await Account.deleteMany({betserver: betserver._id});
    next();
});

//delete accounts on server when server is removed
betserverSchema.pre('remove', async function (next) {
    const user = this;

    await Account.deleteMany({ owner: user._id })
    
    next();
})

const Betserver = mongoose.model('Betserver', betserverSchema);

module.exports = Betserver;

