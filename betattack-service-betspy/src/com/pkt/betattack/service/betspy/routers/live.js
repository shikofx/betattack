const express = require('express');

const router = express.Router();

router.post('/live', async (req, res) => {
    try{
        // let result = await attack.run( req.body );
        // if(result === true)
        //     res.send(req.body);
        // else
        //     res.send({ error: result });
    } catch(error){
        res.status(400).send(error);
    }
});

router.get('/live/betserver/:betserver', async (req, res) => {
    try{
        if(req.params.betserver === process.env.BETSERVER)
            res.status(200).send(true);
    } catch(error){
        res.status(400).send(error);
    }
})

module.exports = router;