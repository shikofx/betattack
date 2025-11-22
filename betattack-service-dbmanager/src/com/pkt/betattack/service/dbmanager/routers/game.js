const express = require('express');
const router = express.Router();
const Game = require('../models/game');


router.post('/game', async (req, res) => {
    const game = new Game(req.body);
    try{
        await game.save();
        res.status(201).send(game);
    } catch(error){
        res.status(400).send(error);
    }
});

router.get('/game/id/:id', async (req, res) => {
    const id = req.params.id;
    try{
        const game = await Game.findOne( { _id: id } );
        if(!game){
            return res.status(404).send('No game with such ID');
        }
        res.status(200).send(game);
    } catch (error){
        res.status(500).send(error);
    }
});

// GET: /tasks?url=https...
router.get('/game/url', async (req, res) => {
    const url = req.query.url;
    if(url){
        try{
            const game = await Game.findOne( { url: url } );
            if(!game){
                return res.status(404).send('No game with such URL');
            }
            res.status(200).send(game);
        } catch (error){
            res.status(500).send(error);
        }
    } else {
        res.status(404).send('You should define URL');
    }
});

router.get('/game/all', async (req, res) => {
    try{
        const games = await Game.find( { } );
        if(!games){
            return res.status(404).send('No game in database');
        }
        res.status(200).send(games);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/game/all/:hours', async (req, res) => {
    const hours = req.params.hours;
    const date = new Date();
    const yesterday = new Date(date - hours*3.6e6);
    try{
        const games = await Game.find( { date: { '$gte': yesterday } } );
        if(!games){
            return res.status(404).send('No game in database');
        }
        res.status(200).send(games);
    } catch (error){
        res.status(500).send(error);
    }
});
    
router.get('/game/active', async (req, res) => {
    try{
        const games = await Game.find( { isFinished: false } );
        if(!games){
            return res.status(404).send('No game in database');
        }
        res.status(200).send(games);
    } catch (error){
        res.status(500).send(error);
    }
});


router.patch('/game/id/:id', async (req, res) => {
    const id = req.params.id;
    try{
        const game = await Game.findOne({ _id: id })
        if(!game){
            return res.status(404).send('No game with such URL');
        }
        const updates = Object.keys(req.body);
        updates.forEach((update) => game[update] = req.body[update]);
        await game.save();
        res.status(200).send(game);
    } catch(error){
        res.status(400).send(error);
    }
});

router.delete('/game/id/:id', async (req, res) => {
    const id = req.params.id;
    try{
        const game = await Game.findOne({ _id: id });
        if(!game){
            return res.status(404).send('No game with such URL on this server');
        }
        await game.remove();
        res.status(200).send('game is successfully removed').send(game);
    } catch(error){
        res.status(500).send(error);
    }
});

module.exports = router;