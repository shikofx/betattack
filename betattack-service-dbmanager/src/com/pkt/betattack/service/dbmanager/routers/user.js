const express = require('express');
const User = require('../models/user');
const router = new express.Router();
const auth = require('../../../../../../../middlware/auth');

router.post('/user', auth, async (req, res) => {
    const user = new User(req.body);
    try{
        await user.save();
        const token = await user.createNewToken();
        res.status(201).send({ user, token });
    } catch (error) {
        res.status(400);
        res.send(error);
    }
});

router.post('/user/login', async (req, res) => {
    try {
        const user = await User.findByCredantials(req.body.email, req.body.password );
        const token = await user.createNewToken();
        res.send({ user: user, token });

    } catch (error) {
        console.log(error);
        res.status(400).send(error);
    }
});

router.post('/user/logout', auth, async (req, res) => {
    try {
        req.user.tokens = req.user.tokens.filter((token) => {
            return token.token !== req.token;
        });
        await req.user.save();
        res.send({ "message": "You have logged out!" } )
    } catch (error) {
        res.status(500).send(error)
    } 
})

router.post('/user/logoutAll', auth, async (req, res) => {
    try {
        req.user.tokens = [];
        await req.user.save();
        res.status(200).send({ "message": "You have logged out of all devices" });
    } catch (error) {
        res.status(500).send(error);
    }
})

router.get('/user', auth, async (req, res) => {
    res.send(req.user);
});

router.get('/users', auth, async (req, res) => {
    try{
        const users = await User.find( { } );
        if(!users){
            return res.status(404).send();
        }
        res.status(200).send(users);
    } catch (error) {
        res.status(500).send(error);
    }
});

router.patch('/user', auth, async (req, res) => {
    const updates = Object.keys(req.body);
    const allowedUpdates = ['name', 'password', 'email', 'age'];
    const isValidOperation = updates.every(updateItem => allowedUpdates.includes(updateItem));
    
    if(!isValidOperation){
        return res.status(400).send({ error: 'Invalid parameters for update!' })
    }

    try {
        updates.forEach((update) => req.user[update] = req.body[update]);
        await req.user.save();
        res.send(req.user);

    } catch (error) {
        res.status(400).send(error);
    }
});

router.patch('/user/:id', auth, async (req, res) => {
    const updates = Object.keys(req.body);
    const allowedUpdates = ['name', 'password', 'age'];
    const isValidOperation = updates.every(updateItem => allowedUpdates.includes(updateItem));
    
    if(!isValidOperation){
        return res.status(400).send({ error: 'Invalid parameters for update!' })
    }

    try {
        const user = await User.findById(req.params.id);
        
        if( !user ){
            return res.status(404).send();            
        }
    
        updates.forEach((update) => user[update] = req.body[update]);
        await user.save();
        res.send(user);

    } catch (error) {
        res.status(400).send(error);
    }
});

router.delete('/user', auth, async (req, res) => {
    try{
        await req.user.remove();
        res.send(req.user);
    } catch (error) {
        res.status(500).send(error);
    }
});

router.delete('/user/:id', auth, async (req, res) => {
    try{
        const user = await User.findById(req.params.id);
        if( !user ){
            return res.status(404).send();            
        }
        await user.remove();
        res.status(200).send(user);
    } catch (error) {
        res.status(500).send(error);
    }
});

module.exports = router;