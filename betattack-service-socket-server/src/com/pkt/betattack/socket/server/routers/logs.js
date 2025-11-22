const express = require('express');
const router = express.Router();

const logger = require('../utils/logger')

router.get('/logs/info', async (req, res) => {
    try{
        res.send(logger.toJSON('info'));
    } catch(error){
        res.status(400).send(error);
    }
});

router.get('/logs/error', async (req, res) => {
    try{
        res.send(logger.toJSON('error'));
    } catch(error){
        res.status(400).send(error);
    }
});

module.exports = router;