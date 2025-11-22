const axios = require('axios');
const logger = require('../utils/logger');

const viberSenderUser = '24517286';
const viberSenderPass = 'TwlQpK3Gqi4dGhUh';
const viberRecieverUser = '375297222876'
const sandboxUrl = 'https://messages-sandbox.nexmo.com/v0.1/messages';
const messageType = "viber_service_msg";

const Viber = {
    send: async function(message){
        axios.post(sandboxUrl, {
            "from": {"type": messageType, "id": '16273'},
            "to": {"type": messageType, "number": viberRecieverUser},
            "message": {
                "content": {
                    "type": "text",
                    "text": "Привет милый!!!"
                }
            }
        }, {
            auth: {
                username: viberSenderUser,
                password: viberSenderPass
            }
        }).then(function (response) {
            logger.addPrint('info', 'Status: ' + response.status);
            logger.addPrint('info', response.data);
        }).catch(function (error) {
            logger.addPrint('error', error);
        });
    }
}

module.exports = Viber;
