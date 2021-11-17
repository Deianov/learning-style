import factory from "./factory_loader.js";

/**
 *  Extracted from 'factory_loader.js' for easy gradle.build
 *
 * @param className
 * @returns {Promise<object>}
 */
async function getInstance(className) {

    let clazz = factory.instance(className);

    if (!clazz) {
        let {Clazz, parent, path, eventType, event} = factory.props(className)

        if (Clazz) {
            /**   create instance from class               (default parent) */
            clazz = new Clazz()
            // console.debug("create instance from Class: " + className)

        } else if (path) {
            /**   dynamic import, then create instance     (default parent) */
                // await sleep(1000)
            const obj = await import(path)
            Clazz = obj[className] || obj["default"];
            clazz = new Clazz()
            // console.debug("import class and create instance: " + path)
        }
        /**   update event and factory storage              */
        if (clazz) {
            if (event) {
                clazz.setEvent(eventType, event)
            }
            factory.addInstance(className, clazz)
        } else {
            console.error("unable to create instance: " + className)
        }
    }
    // console.debug("get instance: " + name)
    return clazz;
}

/*
async function sleep(milliseconds) {
    const date = Date.now();
    let currentDate = null;
    do {
        currentDate = Date.now();
    } while (currentDate - date < milliseconds);
    console.debug(`sleep: ${milliseconds}`);
}
*/

export default getInstance;