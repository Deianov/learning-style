import dom from "../utils/dom.js";
import factory from "../factory_loader.js";
import {Page} from "../routes/page.js";


const NOTIFY = {
    box: { className: "notify-box" },
    type: { msg: "msg", alert: "alert" },
    status: { info: "info", error: "error", success: "success" },
    prefix: { info: "Info: ", error: "Error: ", success: "Success: " },
    size: { small: 20, normal: 22, large: 24 },
    msg: { className: "msg-box", classNameItem: "msg-item", classNameTitle: "msg-title", capacity: 0, timer: 0},
    alert: { className: "alert-box", classNameItem: "alert-item", capacity: 2, timer: 10000, close: 1}
}
const NOTIFY_TEMPLATES = {
    msg: {
        symbol_prefix_text: {
            callback: msg_symbol_prefix_text,
            options: {
                width: NOTIFY.size.normal,
                height: NOTIFY.size.normal,
                info: { svg: "info" },
                success: { svg: "success" },
                error: { svg: "error" }
            }
        },
        title_text: {
            callback: msg_title_text
        },
        text: {
            callback: msg_text
        }
    },
    alert: {
        alert_text: {
            callback: alert_text,
            options: {
                width: NOTIFY.size.normal,
                height: NOTIFY.size.normal,
                info: { svg: "info-fill" },
                success: { svg: "success-fill" },
                error: { svg: "error-fill" }
            }
        }
    }
}


class Notify {
    constructor(isDefaultInit) {
        Notify.storage = {};
        Notify.defaultInit(isDefaultInit);
        this.type = NOTIFY.type;
        this.status = NOTIFY.status;
        this.template = NOTIFY_TEMPLATES;
    }
    /**
     * @param {string} name
     * @param {string | HTMLElement} parent     - parent of "notify-box" (id or element)
     * @param {string} status                   - status or null for all statuses
     * @param {string} type                     - "msg" ot "alert"
     * @param {object} template                 - {callback, options}
     * @returns {Msg}
     */
    create(name, parent, status, type, template) {
        // parent element
        Notify.storage[name] = {};
        Notify.storage[name].parent = parent;
        // notify-box element
        const box = Notify.getBox(name);
        // render instance
        const msg = new Msg(name, box, status, type, template.callback, template.options);
        Notify.storage[name].obj = msg;
        // create render function with name 'name'
        if (status) {
            Notify.prototype[name] = function(text, options) { msg.render(null, text, options) };
        } else {
            Notify.prototype[name] = function(status, text, options) { msg.render(status, text, options) }
        }
        return msg;
    }
    with(name) {
        return Notify.storage[name]["obj"]
    }
    clear() {
        Object.values(Notify.storage).forEach(msg => msg.obj.clear())
    }
    msg(status, text, options) {
        Notify.storage.msg.obj.render(status, text, options)
    }
    alert(status, text, options) {
        Notify.storage.alert.obj.render(status, text, options)
    }
    static remove(name) {
        if (typeof Notify.storage[name] === "object") {
            Notify.storage[name].obj.clear();
            delete Notify.storage[name]
        }
    }
    static getBox(name) {
        const parent = dom.get(Notify.storage[name].parent);
        return parent.getElementsByClassName(NOTIFY.box.className)[0] || dom.element("div", parent, NOTIFY.box.className);
    }
    static defaultInit(flag) {
        if (!flag) { return }
        let parent = Page.elements.article;
        let box = Page.elements.messages;
        box.classList.toggle(NOTIFY.box.className, true);
        let template;

        template = NOTIFY_TEMPLATES.msg.symbol_prefix_text;
        const msg = new Msg("msg", box, null, "msg", template.callback, template.options);
        Notify.storage.msg = {obj: msg, parent};

        template = NOTIFY_TEMPLATES.msg.title_text;
        const title = new Msg("title", box, "info", "msg", template.callback, null);
        Notify.storage.title = {obj: title, parent};
        Notify.prototype.title = function(text, options) { title.render("info", text, options) }

        parent = Page.elements.header.firstChild;
        box = Page.elements.notify;

        template = NOTIFY_TEMPLATES.alert.alert_text;
        const alert = new Msg("alert", box, null, "alert", template.callback, template.options);
        Notify.storage.alert = {obj: alert, parent};
    }
}
factory.addClass(Notify)


class Msg {
    constructor(name, parent, status, type, callback, options) {
        this.storage = [];
        this.name = name;
        this.parent = dom.get(parent);
        this.status = status;
        this.type = type;
        this.callback = callback;
        // merge <- 1.NOTIFY <- 2.TEMPLATES <- render(args)
        this.options = Object.assign(Object.assign({}, NOTIFY[type]), options);
    }
    render(status, text, options) {
        if (!document.body.contains(this.parent)) {
            this.parent = Notify.getBox(this.name)
        }
        if (this.options.capacity > 0 && this.storage.length >= this.options.capacity) {
            dom.remove(this.storage.shift())
        }
        const myOptions =  Object.assign(options || {}, this.options);
        // set ticker
        if (myOptions.timer > 0) {
            this.timeout = myOptions.timer / 1000;
            this.ticker = this.ticker || setInterval(this.tick.bind(this), 1000);
        }
        // render
        const box = this.callback(this.parent, status || myOptions.status, text, myOptions);
        // add close button
        if (myOptions.close) {
            const bnt = dom.svgUse(dom.element("span", box, "btn-close"), "#close-button", "", "24", "24", "button")
            bnt.addEventListener("click", this.clear.bind(this))
        }
        // add to storage
        this.storage.push(box)
    }
    tick() {
        if (--this.timeout < 1) {
            this.clear();
        }
        this.update();
    }
    update() {
        this.storage.forEach(e => {
            (e.getElementsByClassName("ticker")[0] || dom.element("small", e, "ticker")).textContent = this.timeout;
        })
    }
    clear() {
        const count = this.storage.length;
        for (let i = 0; i < count; i++) { dom.remove(this.storage.shift()) }
        if (this.ticker) {
            clearInterval(this.ticker);
            this.ticker = null;
        }
    }
}


/** examples

 *notify-box
     msg-box
        msg-item
     alert-box
        alert-item

 <div class="msg-box">
     <img class="msg-item" src="./assets/images/error.svg" alt="error" width="24" height="24">
     <small class="msg-item">
        <strong>Error: </strong>
     </small>
     <small class="msg-item">This is an error.</small>
 </div>

 * msg
     notify.create("msg", "messages", null, notify.type.msg, notify.template.msg.symbol_prefix_text);
     notify.msg("error", "Tish is an error!")
     notify.msg("error", "Tish is an error!", {prefix: ""})
     notify.with("msg").render("error", "Tish is an error!");
     notify.with("msg").options.prefix = "";                    // change default option

 * title
     const title = notify.create("title", "messages", "info", "msg", notify.template.msg.title_text);
     title.render({title: "Custom information"})
     title.render({title: "Custom information", data: ["line 1", "line 2", "line 3"]})
     notify.title(..)
 */


// TEMPLATES (renders)
function msg_symbol_prefix_text (parent, status, text, options) {
    let {prefix, className, classNameItem, width, height} = options;
    prefix = prefix ? prefix : prefix === "" ? "" : NOTIFY.prefix[status]
    const svgId = "#" + options[status].svg;
    const box = dom.element("div", parent, className)
    dom.svgUse(box, svgId, classNameItem, width, height);
    const small = dom.element("small", box, classNameItem)
    dom.text("strong", small, prefix)
    dom.text("small", box, text ? text : "" , classNameItem)
    return box
}
function msg_title_text (parent, status, text, options) {
    const {title, data} = text || {};
    const {className, classNameTitle} = options;
    const box = dom.element("div", parent, className)
    const div = dom.element("div", box, classNameTitle)
    dom.node(title, dom.element("p", div))
    const ul = dom.element("ul", div)
    for (const line of data) {
        dom.node(line, dom.element("li", ul))
    }
    return box
}
function msg_text (parent, status, text, options) {
    const {tag, className, classNameItem} = options;
    return dom.text(tag || "strong", dom.element("div", parent, className), text, classNameItem)
}
function alert_text (parent, status, text, options) {
    const {tag, className, classNameItem, width, height} = options;
    const svgId = "#" + options[status].svg;
    const box = dom.element("div", parent, className + " " + status);
    // box.innerHTML = `<svg class="${classNameItem}" width="24" height="24" role="img"><use href="#${svg}"/></svg>`;
    dom.svgUse(box, svgId, classNameItem, width, height);
    dom.text(tag || "span", box, text, classNameItem)
    return box;
}

export {Notify}