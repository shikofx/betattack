const Selector = {
    urls:      {
        base: String,
        live: String,
        line: String, 
        filter: {
            initSymbol: String, 
            separator:  String,
            sports: {
                football: String,
                iceHockey: String,
                volleyball: String
            }
        }
    },

    login: {
        initButton    : String,
        usernameInput : String,
        passwordInput : String,
        saveCheckbox  : String,
        submitButton  : String
    },

    account: {
        userInfoBox: String
    },
    cart: {
        container     : String,
        box           : String,
        title         : String,
        clearButton   : String,
        clearAfter    : String,
        betSum        : String,
        kfChangeTitle : String,
        kfAlwaysAgree : String,
        submitAttack  : String,
        successAttack : String
    },
    champ: {
        name   : String,
        url    : String,
        sports : {
            football    :  String,
            iceHockey   :  String
        }
    },
    game: {
        box     : String,
        url     : String, 
        teams   : String,
        start   : String,
        timer   : String,
        score   : String 
    },

    bets: {
        box            : String,
        collapseButton : String,
        asianFora      : String,
        totalPenalty   : String,
        values: {
            p125   : String,
            p025   : String,
            value  : String,
            less   : String,
            more   : String
        }
    },

    set: function( dbmserver ) {
        const loginElements   = dbmserver.selectors.login.elements;
        const accountElements = dbmserver.selectors.account.elements;
        const cartContainer   = dbmserver.selectors.cart.container;
        const cartElements    = dbmserver.selectors.cart.elements;
        const urls            = dbmserver.urls;
        const champElements   = dbmserver.selectors.liveChamp.elements;
        const gameContainer   = dbmserver.selectors.liveGame.container;
        const gameElements    = dbmserver.selectors.liveGame.elements;
        const bets            = dbmserver.selectors.bets;
        const betsElements    = dbmserver.selectors.bets.elements;

        if(Object.entries(dbmserver).length > 0){
            this.urls = {
                base: urls.base,
                live: urls.live,
                line: urls.line, 
                filter: {
                    initSymbol: urls.filter.initSymbol, 
                    separator:  urls.filter.separator,
                    sports: {
                        football: urls.filter.sports.football,
                        iceHockey: urls.filter.sports.volleyball,
                        volleyball: urls.filter.sports.volleyball
                    }
                }
            }; 
            
            this.login = {
                initButton    : loginElements.initButton, 
                usernameInput : loginElements.usernameInput,
                passwordInput : loginElements.passwordInput,
                saveCheckbox  : loginElements.saveCheckbox,
                submitButton  : loginElements.submitButton
            };
            
            this.account = {
                userInfoBox : accountElements.userInfoBox
            };
            
            this.cart = {
                container     : cartContainer,
                box           : cartElements.box,
                title         : cartElements.title,
                clearButton   : cartElements.clearButton,
                clearAfter    : cartElements.clearAfter,
                betSum        : cartElements.betSum,
                kfChangeTitle : cartElements.kfChangeTitle,
                kfAlwaysAgree : cartElements.kfAlwaysAgree,
                submitAttack  : cartElements.submitAttack,
                successAttack : cartElements.successStatus
            };

            this.champ = {
                name   : champElements.name,
                url    : champElements.url,
                sports : {
                    football    :  champElements.football,
                    iceHockey   :  champElements.iceHockey
                }                
            };

            this.game = {
                box:    gameContainer,
                url:    gameElements.url,
                teams:  gameElements.teams, 
                start:  gameElements.start,
                timer:  gameElements.timer,
                score:  gameElements.score,        
            }
            
            this.bets = {
                box            : bets.container,
                collapseButton : betsElements.collapseButton,
                asianFora      : bets.asianFora,
                totalPenalty   : bets.totalPenalty,
                values: {
                    p025   : betsElements.p025,
                    p125   : betsElements.p125,
                    value  : betsElements.value,
                    less   : betsElements.less,
                    more   : betsElements.more   
                }
            }   
        }        
    }
}

module.exports = Selector;
    
      