const betserverManager = require('./betserver');
const accountManager = require('./account');

betserverManager.toJsonFile()
.then(async () => {
    await accountManager.toJsonFile();
});
