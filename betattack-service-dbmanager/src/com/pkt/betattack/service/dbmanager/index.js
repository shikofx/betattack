require('./db/mongooseApp');
const express = require('express');
const { ObjectID } = require('mongodb');
const userRouter = require('./routers/user');
const betserverRouter = require('./routers/betserver');
const betclientRouter = require('./routers/betclient');
const accountRouter = require('./routers/account')
const gameRouter = require('./routers/game');

const app = express();

const port = process.env.PORT;

app.use(express.json());
app.use(userRouter);
app.use(betserverRouter);
app.use(betclientRouter);
app.use(accountRouter);
app.use(gameRouter);

app.listen(port, () => {
    console.log('Server is up on port ' + port);
});