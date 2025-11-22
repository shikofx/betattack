//Образцы игр для разработки
    const serverGames = [{
        championship: {
            name: "Футбол. Россия. Премьер-лига.",
            url: "http://betcity.by/ru/live/soccer/74979"
        },
        teamFirst: {
            name: "Крылья Советов",
            score: 3
        },
        teamSecond: {
            name: "Ахмат",
            score: 3
        },
        serverName: "betcity",
        url: "http://betcity.by/ru/live/soccer/74979/6885113",
        startTime: "18:00",
        timer: "45:40",
        isFinished: false,
        bets: {
            totalPenalty: {
                less: [{2: 1.1 }],
                more: [{2: 4.2}]
            },
            asianFora: {
                p025: [{1: 3.23}],
                p125: [{3: 1.23}]
            },
            loser: {
                yes: [{4: 1.44}]
            }           
        }       
    },
    {
        championship: {
            name: "Футбол. Россия. Премьер-лига.",
            url: "http://betcity.by/ru/live/soccer/74979"
        },
        teamFirst: {
            name: "Спартак",
            score: 3
        },
        teamSecond: {
            name: "Динамо",
            score: 3
        },
        serverName: "betcity",
        url: "http://betcity.by/ru/live/soccer/74979/6885122",
        startTime: "19:00",
        timer: "45:30",
        isFinished: true,
        bets: {
            totalPenalty: {
                less: [{1: 1.555}],
                more: [{2: 3.555}]
            },
            loser: {
                yes: [{4: 1.555}]
            }  
        }
    },
    // {
    //     championship: {
    //         name: "Футбол. Англия. Премьер-лига.",
    //         url: "http://betcity.by/ru/live/soccer/74980"
    //     },
    //     teamFirst: {
    //         name: "Барселона",
    //         score: 0
    //     },
    //     teamSecond: {
    //         name: "Монреаль",
    //         score: 0
    //     },
    //     serverName: "betcity",
    //     url: "http://betcity.by/ru/live/soccer/74979/6885112",
    //     startTime: "21:00",
    //     timer: "45:00",
    //     isFinished: false,
    //     bets: {
    //         asianFora: {
    //             p025: [{1: 2.11}],
    //             p125: [{2: 2.22}],
    //             p225: [{3: 3.33}],
    //             m025: [{3: 1.33}]
    //         },
    //         loser: {
    //             yes: [{4: 1.44}]
    //         }  
    //     }}
    ]; 

    module.exports = serverGames;