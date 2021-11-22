import factory from "../factory_loader.js";
import dom from "../utils/dom.js";


class MyEvent {
    constructor() {
        this.eventChildIndex = -1;
    }
    setEventElement(e) {
        this.eventElement = e ? (this.eventChildIndex === -1) ? e : e.children[this.eventChildIndex] : undefined
    }
    setEvent(e, type, callback, index) {
        this.eventChildIndex = (typeof index === "number") ? index : -1;
        this.setEventElement(e);
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
    updateEvent(e) {
        if (this.eventWaiting && this.eventCallback) {
            this.setEventElement(e);
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
    get wrapper() {
        this._wrapper = this._parent.getElementsByClassName(this.classNameWrapper)[0];
        return this._wrapper
    }
    get element() {
        this._element = this._parent.getElementsByClassName(this.className)[0];
        return this._element
    }
    /**
     * re-create wrapper if not exists.
     */
    updateWrapper() {
        if (!this.classNameWrapper) {
            return
        }
        this._wrapper = this.wrapper || dom.element("div", this._parent, this.classNameWrapper);
    }
    /**
     * re-create element and/or wrapper if not exists.
     * update event
     */
    updateElement() {
        this._element = this.element;
        this.updateWrapper()
        if (!this._element) {
            this._element = dom.element(this.tagName, (this._wrapper || this._parent), this.className);
            super.eventWaiting = true;
        }
        this._element = this._element || dom.element(this.tagName, (this._wrapper || this._parent), this.className);
    }
    reset() {
        this.updateElement();
        this._element.innerHTML = "";
        this._element.classList.toggle(this.className, true);
        this.updateEvent(this._element);
        // this.visible(true)
    }
    remove() {
        dom.remove(this._wrapper || this._element); // this.element ?
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
 *
    <ul class="breadcrumb">
        <li><a href="./">Home</a></li>
        <li><a key="1" href="javascript:void(0)">Cards</a></li>
        <li><a href="#">German</a></li>
    </ul>
*/
class Breadcrumb extends Component {
    constructor(parent = "header") {
        super(parent, "row", Breadcrumb.PROPS.tagName, Breadcrumb.PROPS.className)
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
Breadcrumb.PROPS = {
    className: "breadcrumb",
    tagName: "ul"
}
factory.addClass(Breadcrumb)


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
        GoTop.instance = this;
        this.render();
    }
    render() {
        this.е = dom.element("button", document.getElementsByTagName("main")[0], "go-top")
        dom.svgUse(this.е, "#go-top", "", "30", "30", "img");
        this.visible(false);
        this.е.addEventListener('click', scrollTop)
        window.onscroll = scrollEvent
    }
    visible(flag) {
        if (this.е && !this.disabled) {
            this.е.style.display = flag ? "" : "none";
        }
    }
}
function scrollTop() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}
function scrollEvent() {
    GoTop.instance.visible(document.body.scrollTop > 20 || document.documentElement.scrollTop > 20)
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
class Tags extends Component {
    constructor(parent = "bottom") {
        super(parent, null, Tags.PROPS.tagName, Tags.PROPS.className)
    }
    render(obj) {
        const tmp = {
            text:"Topics", tags:[
                {"href":"#", "textContent":"this"},
                {"href":"#", "textContent":"is"},
                {"href":"#", "textContent":"under"},
                {"href":"#", "textContent":"construction"}
            ]
        }
        super.reset();
        dom.text("span", this.element, tmp.text, Tags.PROPS.header.className);
        for (const tag of tmp.tags) {
            dom.element("a", dom.element("div", this.element, Tags.PROPS.item.className), tag)
        }
    }
}
Tags.PROPS = {
    className: "tags",
    tagName: "div",
    item: {className: "tag"},
    header: {className: "heading"}
}
factory.addClass(Tags)

export {Component, Breadcrumb, GoTop, Tags};