const { Page, ElementHandle } = require("puppeteer");
const logger = require('../utils/logger');

module.exports = {

    /**
     * Определяет, присутствует ли в DOM страницы элемент по локатору и является ли он уникальным
     * @param {Page, ElementHandle} documentPart 
     * @param {String} selector 
     * @returns Найденный элемент или undefined
     */
    async getUnique(documentPart, selector) {
        
        let elements = await elementByXpath(documentPart, selector);
        if(!elements)
            elements = await elementByCSS(documentPart, selector);
        
        if(!elements || elements.length === 0){
            return undefined;
        }

        if(elements.length === 1)
            return elements[0];

        logger.addPrint('error', `Элемент НЕ уникален: ${selector}`);
        return undefined;

    },

   /**
     * 
     * @param {Page, ElementHandle} documentPart 
     * @param {String} selector 
     * @returns Текст, который содержится для элемента по селектору
     */
    async getInnerText(documentPart, selector){
        const element = await this.getUnique(documentPart, selector);
            
        if(element)
            return await element.evaluate(e => e.innerText);
            
        return undefined;
    },  


}

/**
 * Ищет элементы на странице по CSS
 * @param {Page, ElementHandle} documentPart 
 * @param {String} selector 
 * @return Найденные элементы или {undefined}
*/
const elementByCSS = async (documentPart, selector) => {
    try{
        return await documentPart.$$(selector);
    } catch {
        return undefined;
    }
}

/**
 * Ищет элементы на странице по Xpath
 * @param {Page, ElementHandle} documentPart 
 * @param {String} selector 
 * @return Найденные элементы или {undefined}
*/
const elementByXpath = async (documentPart, selector) => {
    try{
        return await documentPart.$x(selector);
    } catch {
        return undefined;
    }
}
