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
     * @param {string | HTMLElement} parent Static HTMLElement (ID or Element)
     * @param {string} wrapper (Optional) className
     * @param {string} tagName (Element)
     * @param {string} className (Element)
     */
    constructor(parent, wrapper, tagName = "div", className) {
        super();
        this._parent = dom.get(parent);
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


/**
 *  breadcrumb
 */
/*
    <ul class="breadcrumb">
        <li><a href="./">Home</a></li>
        <li><a key="1" href="javascript:void(0)">Cards</a></li>
        <li><a href="#">German</a></li>
    </ul>
*/


const breadcrumb = {
    "className":"breadcrumb",
    "tagName":"ul"
}

class Breadcrumb extends Component {
    constructor(parent) {
        super(parent, null, breadcrumb.tagName, breadcrumb.className)
    }
    render(obj) {
        super.reset();

        if (obj) {
            for (const li of obj) {
                if (!li["href"]) {
                    li.href = "javascript:void(0)";
                }
                dom.element("a", dom.element("li", this._element), li)
            }
        }
    }
    addTopic(topic) {
        if (topic) {
            dom.element("a", dom.element("li", this._element), {"href":"#", "textContent":topic})
        }
    }
}


/**
 * scroll
 */
/*
   <button class="go-top">
        <svg viewBox="0 0 16 16" width="16" height="16">
            <title>Go to top</title>
            <g stroke-width="1" stroke="currentColor">
                <polyline fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10" points="15.5,11.5 8,4 0.5,11.5 "></polyline>
            </g>
        </svg>
    </button>
*/

class GoTop {
    constructor() {
        this.view;
        this.disabled;
    }
    render() {
        // todo: render
        this.view = document.getElementsByClassName("go-top")[0];
        this.visible(false);
        this.addEvent();
        return this
    }
    visible(flag) {
        if (this.valid) {
            this.view.style.display = flag ? "" : "none";
        }
    }
    addEvent() {
        this.view.addEventListener('click', scrollTop)
        window.onscroll = scrollEvent
    }
    get valid() {
        return !this.disabled && this.view
    }
}
function scrollTop() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}
function scrollEvent() {
    goTop.visible(document.body.scrollTop > 20 || document.documentElement.scrollTop > 20)
}


/**
 *  tags
 */
/*
    <div class="tags">
        <span class="heading">Topics</span>
        <div class="tag">
            <a href="#">German</a>
        </div>
        <div class="tag">
            <a href="#">Words</a>
        </div>
        ...
    </div>
*/

const tags = {
    "className":"tags",
    "tagName":"div",
    "classNameItem":"tag",
    "classNameHeader":"heading"
}

class Tags extends Component {
    constructor(parent) {
        super(parent, null, tags.tagName, tags.className)
    }
    /**
     * {"text":"Topics", "tags":[
                {"href":"#", "textContent":"German"},
                {"href":"#", "textContent":"Programing"},
                {"href":"#", "textContent":"Words"},
                {"href":"#", "textContent":"Test"}
            ]
        }
     */
    render(obj) {
        super.reset();

        dom.text("span", this.element, obj.text, tags.classNameHeader);
        for (const tag of obj.tags) {
            dom.element("a", dom.element("div", this.element, tags.classNameItem), tag)
        }
    }
}

const goTop = new GoTop().render();
export {Component, Breadcrumb, goTop, Tags};