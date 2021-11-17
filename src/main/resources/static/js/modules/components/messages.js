import dom from "../utils/dom.js";

/* 
<div class="text-message">
    <img class="msg-item" src="./assets/images/error.svg" alt="error" width="24" height="24">
    <small class="msg-item">
        <strong>Error: </strong>
    </small>
    <small class="msg-item">This is an error.</small>
</div>
*/
const MSG = {}
MSG.status = { info: "info", error: "error", success: "success" }
MSG.size = { normal: 20 }
MSG.parent = { className: "text-message" }
MSG.subject = { className: "subject-text" }
MSG.child = { className: "msg-item" }
MSG.svg = { path: "./assets/images" }
MSG.options = {
    info: {
        subject: "Info: ",
        img : { class: MSG.child.className, src:`${ MSG.svg.path }/info.svg`, alt: MSG.status.info, width: MSG.size.normal, height: MSG.size.normal}
    },
    error: {
        subject: "Error: ",
        img: { class: MSG.child.className, src:`${ MSG.svg.path }/error.svg`, alt: MSG.status.error, width: MSG.size.normal, height: MSG.size.normal}
    },
    success: {
        subject: "Success: ",
        img: { class: MSG.child.className, src:`${ MSG.svg.path }/check-in-circle.svg.svg`, alt: MSG.status.success, width: MSG.size.normal, height: MSG.size.normal}
    }
}

class CustomMessage {
    /**
     * @param {HTMLElement} parent 
     * @param {string} status 
     * @param {Function} callback 
     * @param {Array} args 
     */
    constructor(parent, status, callback, args) {
        this.parent = parent;
        this.status = status;
        this.callback = callback;
        this.defaultArgs = args;
        CustomMessage.store.push(this)
        // console.log(JSON.stringify(messages));
    }
    get text() {
        return this.element.children[2].textContent
    }
    set text(str) {
        this.element.children[2].textContent = str
    }
    render(...args) {
        this.element = this.callback(this.parent, this.status, (args && args.length > 0) ? args : this.defaultArgs)
    }
    visible(flag) {
        this.element.style.display = flag ? "" : "none"
    }
    addSibling(name, text, options) {
        if (!Array.isArray(this.siblings)) {
            this.siblings = [];
        }
        this.siblings.push(dom.text(name, this.parent, text, options))
    }
    addSiblingByCallback(callback, args) {
        if (!Array.isArray(this.siblings)) {
            this.siblings = [];
        }
        this.siblings.push(callback(this.parent, args))
    }
    remove() {
        dom.remove(this.element) // todo: last?
        this.element = null;
        if(this.siblings) {
            this.siblings.forEach(e => dom.remove(e));
            this.siblings.length = 0;
        }
    }
}
CustomMessage.store = []

function clear() {
    CustomMessage.store.forEach(e => e.remove())
}
function removeMessages() {
    clear()
    CustomMessage.store.fill()
    CustomMessage.store.length = 0
}

// renders
function createRender(parent, status, render, args) {
    return new CustomMessage(parent, status, render, args)
}

function createRenders(parent, render) {
    const info = new CustomMessage(parent, MSG.status.info, render)
    const error = new CustomMessage(parent, MSG.status.error, render)
    const success = new CustomMessage(parent, MSG.status.success, render)
    return {
        info,
        error,
        success
    }
}

function textMessageWithSymbol (parent, status, args) {
    const [subject, text] = args || [];
    const div = dom.element("div", parent, MSG.parent.className)
    const img = dom.element("img", div, MSG.options[status].img)
    const small = dom.element("small", div, MSG.child.className)
    dom.text("strong", small, subject ? subject : subject === "" ? "" : MSG.options[status].subject)
    dom.text("small", div, text ? text : "" , MSG.child.className)
    return div
}
function textMessageWithSubject (parent, dataObject) {
    const {subject, data} = dataObject || {};
    const div = dom.element("div", parent, MSG.subject.className)
    dom.node(subject, dom.element("p", div))
    const ul = dom.element("ul", div)
    for (const line of data) {
        dom.node(line, dom.element("li", ul))
    }
    return div
}

const renders = {
    textMessageWithSymbol,
    textMessageWithSubject
}
// <= render functions

const create = {
    renders: (parent, render) => createRenders(parent, render),
    customRender: (parent, status, render, ...args) => createRender(parent, status, render, args)
}

/*
    renders = messages.create.renders(parent, messages.renders.textMessageWithSymbol)
    render = messages.create.customRender(parent, "info", messages.renders.textMessageWithSymbol)
*/

const messages = {removeMessages, clear, create, renders};
export default messages;