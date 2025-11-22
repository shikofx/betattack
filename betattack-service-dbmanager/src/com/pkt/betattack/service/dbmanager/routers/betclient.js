const express = require('express');
const router = new express.Router();
const Betclient = require('../models/betclient');

router.post('/betclient', async (req, res) => {
    const betclient = new Betclient(req.body);
    try{
        await betclient.save();
        res.status(201).send(betclient);
    } catch(error){
        res.status(400).send(error);
    }
});

router.get('/betclient/id/:id', async (req, res) => {
    try{
        const betclient = await Betclient.findOne( { _id: req.params.id } );
        if(!betclient){
            return res.status(404).send('No client with such ID on this server');
        }
        res.status(200).send(betclient);
    } catch (error){
        res.status(500).send(error);
    }
});


router.get('/betclient/phone/:phone', async (req, res) => {
    try{
        const betclient = await Betclient.findOne( { phone: req.params.phone } );
        if(!betclient){
            return res.status(404).send('No client with such telephone on this server');
        }
        res.status(200).send(betclient);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/betclient/email/:email', async (req, res) => {
    try{
        const betclient = await Betclient.findOne( { email: req.params.email } );
        if(!betclient){
            return res.status(404).send('No client with such email on this server');
        }
        res.status(200).send(betclient);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/betclient/all', async (req, res) => {
    try{
        const betclients = await Betclient.find( { } );
        if(!betclients){
            return res.status(404).send('No clients in database');
        }
        res.status(200).send(betclients);
    } catch (error){
        res.status(500).send(error);
    }
});

router.patch('/betclient/id/:id', async (req, res) => {
    try{
        const betclient = await Betclient.findOne({ _id: req.params.id })
        if(!betclient){
            return res.status(404).send('No client with such ID on this server');
        }
        const updates = Object.keys(req.body);
        updates.forEach((update) => betclient[update] = req.body[update]);
        await betclient.save();
        res.status(200).send(betclient);
    } catch(error){
        res.status(400).send(error);
    }
});

router.delete('/betclient/id/:id', async (req, res) => {
    try{
        const betclient = await Betclient.findById(req.params.id);
        if(!betclient){
            return res.status(404).send('No client with such ID on this server');
        }
        await betclient.remove();
        res.status(200).send('Betclient is successfully removed').send(betclient);
    } catch(error){
        res.status(500).send(error);
    }
})

module.exports = router;