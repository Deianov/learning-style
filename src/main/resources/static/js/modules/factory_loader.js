import getInstance from "./factory_suffix.js";

const factory = (function() {

    const classes = {};
    const instances = {};
    const isClass = (obj) => (typeof obj === "function");
    const getName = (obj) => (typeof obj === "function") ? obj.name || obj.constructor.name : obj;
    const instance = (name) => instances[getName(name).toLowerCase()];
    const props = (name) => classes[getName(name)];

    /**
     * @param {Function || string} Clazz
     * @param {string} parent
     * @param {string} path
     */
    const addClass = (Clazz, parent, path) => {
        const className = getName(Clazz)
        classes[className] = {Clazz: (isClass(Clazz) ? Clazz : undefined), parent, path}
    }

    /**
     * @param {Function || string} Clazz
     * @param {object} obj
     */
    const addInstance = (Clazz, obj) => {
        const className = getName(Clazz)
        instances[className.toLowerCase()] = obj
        // delete classes.className
    }

    /**
     * @param {Function || string} Clazz
     * @param {string} eventType
     * @param {Function} event
     */
    const addEvent = (Clazz, eventType, event) => {
        const className = getName(Clazz)
        classes[className].eventType = eventType;
        classes[className].event = event;
    }

    return {
        addClass,
        addInstance,
        addEvent,
        getInstance,
        instance,
        props
    }
}());

export default factory;