const express = require('express');
const router = new express.Router();
const Account = require('../models/account');

router.post('/accounts', async (req, res) => {
    const account = new Account(req.body);
    try{
        await account.save();
        res.status(201).send(account);
    } catch(error){
        res.status(400).send(error);
    }
});

router.get('/accounts/all', async (req, res) => {
    try{
        const accounts = await Account.find( { } );
        if(!accounts){
            return res.status(404).send('No accounts in database');
        }
        res.status(200).send(accounts);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/accounts/id/:id', async (req, res) => {
    try{
        const account = await Account.findOne( { _id: req.params.id } );
        if(!account){
            return res.status(404).send('No client with such ID on this server');
        }
        res.status(200).send(account);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/accounts/server/id/:serverID', async (req, res) => {
    try{
        const accounts = await Account.find( { server: req.params.serverID } );
        if(!accounts){
            return res.status(404).send('No clients in database');
        }
        res.status(200).send(accounts);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/accounts/owner/id/:ownerID', async (req, res) => {
    try{
        const accounts = await Account.find( { owner: req.params.ownerID } );
        if(!accounts){
            return res.status(404).send('No clients in database');
        }
        res.status(200).send(accounts);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/accounts/toconnect/:serverID', async (req, res) => {
    try{
        const accounts = await Account.find( { "$or": [{ spyAddress: undefined }, { spyAddress: null }, { spyAddress: "" }], server: req.params.serverID });
        if(!accounts){
            return res.status(404).send('No clients in database');
        }
        res.status(200).send(accounts);
    } catch (error){
        res.status(500).send(error);
    }
});

router.get('/accounts/connected/:serverID', async (req, res) => {
    try{
        const accounts = await Account.find( { "$nor": [{ spyAddress: undefined }, { spyAddress: null }, { spyAddress: "" }], server: req.params.serverID });
        if(!accounts){
            return res.status(404).send('No clients in database');
        }
        res.status(200).send(accounts);
    } catch (error){
        res.status(500).send(error);
    }
});

router.patch('/accounts/id/:id', async (req, res) => {
    
    try{
        const account = await Account.findOne({ _id: req.params.id })
        if(!account){
            return res.status(404).send('No client with such ID on this server');
        }
        const updates = Object.keys(req.body);
        updates.forEach((update) => account[update] = req.body[update]);
        await account.save();
        res.status(200).send(account);
    } catch(error){
        res.status(400).send(error);
    }
});

router.delete('/accounts/id/:id', async (req, res) => {
    try{
        const account = await Account.findById(req.params.id);
        if(!account){
            return res.status(404).send('No client with such ID on this server');
        }
        await account.remove();
        res.status(200).send('account is successfully removed').send(account);
    } catch(error){
        res.status(500).send(error);
    }
})

module.exports = router;