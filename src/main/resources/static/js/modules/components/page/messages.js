import dom from "../../utils/dom.js";

/* 
<div class="text-message">
    <img class="msg-item" src="./assets/images/error.svg" alt="error" width="24" height="24">
    <small class="msg-item">
        <strong>Error: </strong>
    </small>
    <small class="msg-item">This is an error.</small>
</div>
*/

const messages = [];
const status = {
    info:"info",
    error:"error",
    success:"success"
};
const size = {
    normal:20
};
const classNameParent = "text-message";
const classNameParentSubjects = "subject-text";
const classNameChild = "msg-item";
const path = "./assets/images";
const options = {
    info: {
        "subject":"Info: ",
        "img":{"class":classNameChild, src:`${path}/info.svg`, "alt":status.info, "width":size.normal, "height":size.normal}
    },
    error: {
        "subject":"Error: ",
        "img":{"class":classNameChild,src:`${path}/error.svg`, "alt":status.error, "width":size.normal, "height":size.normal}
    },
    success: {
        "subject":"Success: ",
        "img":{"class":classNameChild,src:`${path}/check-in-circle.svg.svg`, "alt":status.success, "width":size.normal, "height":size.normal}
    }
};
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
        this.element;
        this.siblings;
        messages.push(this)
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

function clear() {
    messages.forEach(e => e.remove())
}
function remove() {
    clear();
    messages.fill();
    messages.length = 0
}

// renders
function createRender(parent, status, render, args) {
    return new CustomMessage(parent, status, render, args);
}

function createRenders(parent, render) {
    const info = new CustomMessage(parent, status.info, render);
    const error = new CustomMessage(parent, status.error, render);
    const success = new CustomMessage(parent, status.success, render);
    return {
        info,
        error,
        success
    }
}

function textMessageWithSymbol (parent, status, args) {
    const [subject, text] = args || [];
    const div = dom.element("div", parent, classNameParent);
    const img = dom.element("img", div, options[status].img);
    const small = dom.element("small", div, classNameChild);
    dom.text("strong", small, subject ? subject : subject === "" ? "" : options[status].subject);
    dom.text("small", div, text ? text : "" , classNameChild);
    return div
}
function textMessageWithSubject (parent, dataObject) {
    const {subject, data} = dataObject || {};
    const div = dom.element("div", parent, classNameParentSubjects);
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

export default {remove, clear, create, renders};