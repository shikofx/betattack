const express = require('express');
const router = new express.Router();
const Betserver = require('../models/betserver');
const auth = require('../../../../../../../middlware/auth');

router.post('/betserver', async (req, res) => {
    const betserver = new Betserver(req.body);
    try{
        await betserver.save();
        res.status(201).send(betserver);
    } catch (e) {
        res.status(400).send(e);
    }
});

router.get('/betserver/name/:name', async (req, res) => {
    try{
        const server = await Betserver.findOne( { name: req.params.name } );
        if(!server){
            return res.status(404).send('No server with such name');
        }
        res.status(200).send(server);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/betserver/id/:id', async (req, res) => {
    try{
        const server = await Betserver.findOne( { _id: req.params.id } );
        if(!server){
            return res.status(404).send('No server with such ID');
        }
        res.status(200).send(server);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/betserver/all', async (req, res) => {
    try{
        const servers = await Betserver.find( { } );
        if(!servers){
            return res.status(404).send('No servers in database');
        }
        res.status(200).send(servers);
    } catch (error){
        res.status(500).send(error);
    }
});

router.patch('/betserver/name/:name', async (req, res) => {
    try{
        const server = await Betserver.findOne({ name: req.params.name })
        if(!server){
            return res.status(404).send('No server with such name');
        }
        const updates = Object.keys(req.body);

        updates.forEach((update) => server[update] = req.body[update]);
        await server.save();
        res.status(200).send(server);
    } catch(error){
        res.status(400).send(error);
    }
});

router.patch('/betserver/id/:id', async (req, res) => {
    try{
        const server = await Betserver.findOne({ _id: req.params.id })
        if(!server){
            return res.status(404).send('No server with such ID');
        }
        const updates = Object.keys(req.body);
        updates.forEach((update) => server[update] = req.body[update]);
        await server.save();
        res.status(200).send(server);
    } catch(error){
        res.status(400).send(error);
    }
});

router.delete('/betserver/id/:id', async (req, res) => {
    try{
        const server = await Betserver.findById(req.params.id);
        if(!server){
            return res.status(404).send('No server with such ID');
        }
        await server.remove();
        res.status(200).send(server);
    } catch(error){
        res.status(500).send(error);
    }
})

router.delete('/betserver/name/:name', async (req, res) => {
    try{
        const server = await Betserver.findOne( { name: req.params.name } );
        if(!server){
            return res.status(404).send('No server with such name');
        }
        await server.remove();
        res.status(200).send(server);
    } catch(error){
        res.status(500).send(error);
    }
})

module.exports = router;