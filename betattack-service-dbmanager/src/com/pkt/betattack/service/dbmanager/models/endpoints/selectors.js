const SelectorType = {
    type: String,
    enum: ['CSS', 'XPATH'],
    default: 'CSS'
};

const Element = {
    selector: String,
    type: SelectorType
};

const Section = {
    selector: String,
    type: SelectorType,
    elements: {
        type: Map,
        of: Element,
    },
    excluded: {
        type: Map,
        of: Element
    }
};

const Selector = {
    type: Map,
    of: Section        
};

module.exports = Selector;