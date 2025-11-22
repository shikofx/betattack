//Эмулированный запрос с админ-панели
    const filterRequest = {
        server: "betcity",
        repeater: 10,
        sports: [{
            kind: "football",
            bets: {
                totalPenalty: [ "less", "more" ],
                asianFora: [ "p025", "p125" ]                
            }
        },         
        {
            kind: "iceHockey",
            bets: {
                asianFora: [ "p025", "p125" ]
                                        
            }
        }]
    }