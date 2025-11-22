const validator = require('validator');

const Link = {
    type: String,
    required: true,
    validate(value) {
        if (!validator.isURL(value)) {
            throw new Error('Invalid base URL');
        }
    }
}

const FilterItem = {
    type: Map,
    of: String
}

const FilterSet = {
    type: Map,
    of: {
        name: String,
        items: FilterItem
    }
}

const URL = {
    links: {   
        type: Map,
        of: Link
    },
    filter: {
        initSymbol: String,
        separator: String,
        set: FilterSet
    }
};

module.exports = URL;