const axios = require('axios').default;

const dbmanagerURL = process.env.URL_DBMANAGER_HOST;

const DBManager = {
    url : function(args) {
        let url = '';
        args.forEach(element => {
            url = url + '/' + element;
        });
        return dbmanagerURL + url;
    },
    urlWithParams : function(args, params) {
        let url = this.url(args);
        if(params.length > 0){
            url = url + '?'
            params.forEach(param => {
                url = url + param.name + '=' + param.value;
            })
        }
        return url;
    }, 
    get : async function(args) {
        const url = this.url(args);
        try{
            const betcity = await axios.get(url);
            return betcity.data;
        } catch (error) {   
            return error;
        }
    },
    getByParams : async function (args, params) { 
        try{
            return await (await axios.get(this.urlWithParams(args, params))).data;
        } catch (error) {   
            return error;
        }
    }, 

    post : async function (args, body) {
        try{
            return await axios.post(this.url(args), body);
        } catch (error) {
            return error;
        }
    },

    patch : async function (args, body) {
        try{
            return await axios.patch(this.url(args), body);
        } catch(error) {
            return error;
        }
    }, 

    delete : async function (args) {
        try{
            return await axios.delete(this.url(args));
        } catch (error) {
            return error;
        }
    },

    formatToJSON: function (result) {
        return JSON.stringify(result, null, 4);
    }
}

module.exports = DBManager;