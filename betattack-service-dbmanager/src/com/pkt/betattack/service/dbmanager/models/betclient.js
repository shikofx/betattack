const mongoose = require('mongoose');
const validator = require('validator').default;
const Account = require('./account');

const betclientSchema = new mongoose.Schema({
    name: {
        first: {
            type: String,
            required: true,
            trim: true
        },
        middle: {
            type: String,
            trim: true
        },
        last: {
            type: String,
            required: true,
            trim: true
        }
    },

    phone: {
        type: String,
        unique: true,
        sparse: true,
        validate(value){
            if(!validator.isMobilePhone(value)){
                throw new Error("Invalid phone")
            }
        }
    },

    email: {
        type: String,
        required: true,
        unique: true,
        trim: true,
        lowercase: true,
        validate(value) {
            if (!validator.isEmail(value)) {
                throw new Error('Invalid email')
            }
        }
    }
}, {
    timestamps: true
});

betclientSchema.virtual('accounts', {
    ref: 'Account',
    localField: '_id',
    foreignField: 'owner'
});

//delete accounts when person is removed
betclientSchema.pre('remove', async function (next) {
    const betclient = this;
    await Account.deleteMany({owner: betclient._id});
    next();
})

const Betclient = mongoose.model('Betclient', betclientSchema);

module.exports = Betclient;