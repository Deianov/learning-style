import dom from "../utils/dom.js";


class MyEvent {
    constructor() {
        this.eventType;
        this.eventCallback;
        this.eventChildIndex = -1;
        this.eventWaiting;
    }
    get eventElement() {
        return this._eventElement
    }
    set eventElement(e) {
        this._eventElement = e ? (this.eventChildIndex === -1) ? e : e.children[this.eventChildIndex] : undefined
    }
    setEvent(element, type, callback, index) {
        this.eventChildIndex = (typeof index === "number") ? index : -1;
        this.eventElement = element;
        this.eventType = type;
        this.eventCallback = callback;
        this.eventWaiting = true

        if (this.eventElement) {
            this.eventElement.addEventListener(this.eventType, this.eventCallback)
            this.eventWaiting = undefined
            // console.log("event:" + this.className);
        }
    }
    resetEvent() {
        if (this.eventElement && this.eventCallback) {
            this.eventElement.removeEventListener(this.eventType, this.eventCallback)
        }
        this.eventElement = undefined
        this.eventWaiting = true
    }
    updateEvent(element) {
        if (this.eventWaiting && this.eventCallback) {
            this.eventElement = element;
            if (this.eventElement) {
                this.eventElement.addEventListener(this.eventType, this.eventCallback)
                this.eventWaiting = undefined;
                // console.log("Update event:" + this.className);
            }
        }
    }
}

class Component extends MyEvent {
    /**
     * @param {string | HTMLElement} parent Static HTMLElement
     * @param {string} wrapper (Optional) className
     * @param {string} tagName (Element)
     * @param {string} className (Element)
     */
    constructor(parent, wrapper, tagName = "div", className) {
        super();
        this._parent = dom.getLazy(parent);
        // wrapper
        this.classNameWrapper = wrapper;
        // element
        this.tagName = tagName;
        this.className = className;
    }
    get parent() {
        return this._parent
    }
    set parent(e) {
        this._parent = e
    }
    get wrapper() {
        this._wrapper = this.parent.getElementsByClassName(this.classNameWrapper)[0];
        return this._wrapper
    }
    set wrapper(e) {
        this._wrapper = e
    }
    get element() {
        this._element = this.parent.getElementsByClassName(this.className)[0];
        return this._element
    }
    set element(e) {
        this._element = e
    }
    get creatableWrapper() {
        if (!this.classNameWrapper) {
            return
        }
        this._wrapper = this.wrapper || dom.element("div", this._parent, this.classNameWrapper);
        return this._wrapper
    }
    /**
     * re-create wrapper and/or element if not exists.
     * update event
     */
    get creatableElement() {
        this._element = this.element;
        this._wrapper = this.creatableWrapper
        if (!this._element) {
            this._element = dom.element(this.tagName, (this._wrapper || this._parent), this.className);
            super.eventWaiting = true;
        }
        this._element = this._element || dom.element(this.tagName, (this._wrapper || this._parent), this.className);
        return this._element
    }
    reset() {
        dom.removeAll(this._element);
        this._element = this.creatableElement;
        this._element.classList.toggle(this.className, true);
        this.updateEvent(this._element);
        // this.visible(true)
    }
    remove() {
        if (this._element || this._wrapper) {
            dom.remove(this._wrapper || this.element);
        }
        this.resetEvent();
        this._element = undefined;
        this._wrapper = undefined;
    }
    visible(flag) {
        if (this.disabled || (!this._element && !this._wrapper)) {
            return
        }
        (this._wrapper || this._element).style.display = flag ? "" : "none";
    }
    /**
     * @param {string} type 
     * @param {Function} callback 
     * @param {number} index (Optional) Set to child element by Index
     */
    setEvent(type, callback, index) {
        super.setEvent(this.element, type, callback, index)
    }
}

export default Component;