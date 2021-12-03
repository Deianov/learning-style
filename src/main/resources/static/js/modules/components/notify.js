import dom from "../utils/dom.js";
import factory from "../factory_loader.js";
import {Page} from "../routes/page.js";


/**  examples:

 parent
    notify-box
        msg-box
            item
        alert-box
            item

 <div class="notify-box">
     <div class="msg-box">
         <svg class="item" width="22" height="22" role="img">
            <use href="#error"></use>
         </svg>
         <small class="item">Error: </small>
         <small class="item">This is an error!</small>
     </div>
 </div>

 class Msg {
        render(status, text, options)
 }

 * Notify.defaultInit()
     * msg
         notify.msg("error", "Tish is an error!")
         notify.msg("error", "Tish is an error!", {prefix: ""})
         notify.with("msg").render("error", "This is an error!");
         notify.with("msg").options.prefix = "";                    // change default options
     * alert
        notify.alert("error", "Error message!")
     * bnt
        notify.bnt("info", "Click Me!", clickMe, {callback: {svg: false}});

 * custom
     * myMsg
         const myMsg = notify.create("myMsg", parent, null, "msg", notify.template.msg.text)
         myMsg("error", "Error message!")
     * MyBnt
         const myBnt = notify.create("MyBnt", parent, "info", "msg", notify.template.msg.callback, {
               callback: {label: "Click Me!", svg: true, func: clickMe}
         });
         myBnt()
         myBnt(newLabel)
         notify.myBnt()
         notify.with("myBnt").render("error", "errorLabel", {svg: false})
 */


const NOTIFY = {
    box: { className: "notify-box" },
    type: { msg: "msg", alert: "alert" },
    status: { info: "info", error: "error", success: "success" },
    prefix: { info: "Info: ", error: "Error: ", success: "Success: " },
    size: { small: 20, normal: 22, large: 24 },
    msg: { className: "msg-box", classNameItem: "item", classNameTitle: "msg-title", capacity: 0, timer: 0},
    alert: { className: "alert-box", classNameItem: "item", capacity: 2, timer: 10000, ticker: 1, close: 1},
    callback: { capacity: 1, callback: {label: "clickMe", svg: true}},
}
NOTIFY.msg_default_svg = {width: NOTIFY.size.normal, height: NOTIFY.size.normal, info: {svg: "info"}, success: {svg: "success"}, error: {svg: "error"}};
NOTIFY.alert_default_svg = {width: NOTIFY.size.normal, height: NOTIFY.size.normal, info: {svg: "info-fill"}, success: {svg: "success-fill"}, error: {svg: "error-fill"}};
const NOTIFY_TEMPLATES = {
    msg: {
        symbol_prefix_text: {
            callback: msg_symbol_prefix_text,
            options: NOTIFY.msg_default_svg,
        },
        title_text: {
            callback: msg_title_text,
        },
        text: {
            callback: msg_text,
        },
        callback: {
            callback: notify_callback,
            options: NOTIFY.msg_default_svg,
        }
    },
    alert: {
        text: {
            callback: alert_text,
            options: NOTIFY.alert_default_svg,
        }
    },
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
     * @param {object} options                  - options
     * @returns {function}                      - f(text, options) | f(status, text, options)
     */
    create(name, parent, status, type, template, options) {
        // merge options
        const myOptions = Object.assign({}, template.options);
        Object.assign(myOptions, options);
        // parent element
        Notify.storage[name] = {};
        Notify.storage[name].parent = parent;
        // notify-box element
        const box = Notify.getBox(name);
        // render instance
        const msg = new Msg(name, box, status, type, template.callback, myOptions);
        Notify.storage[name].obj = msg;
        // create render function with name 'name'
        if (status) {
            Notify.prototype[name] = function(text, options) { msg.render(null, text, options) };
        } else {
            Notify.prototype[name] = function(status, text, options) { msg.render(status, text, options) };
        }
        return Notify.prototype[name];
    }
    with(name) {
        return Notify.storage[name]["obj"];
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
    bnt(status, label, func, options) {
        const myOptions = Object.assign({}, options);
        // todo: ?
        Object.assign(myOptions, NOTIFY.callback);
        Object.assign(myOptions.callback, options ? options.callback : {})
        Object.assign(myOptions.callback, {func})
        Notify.storage.bnt.obj.render(status, label, myOptions)
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
    /** notify.msg();  notify.alert();  notify.title();  notify.bnt()  */
    static defaultInit(flag) {
        if (!flag) { return }
        let parent = Page.elements.article;
        let box = Page.elements.messages;
        let template = NOTIFY_TEMPLATES.msg.symbol_prefix_text;
        box.classList.toggle(NOTIFY.box.className, true);

        const msg = new Msg("msg", box, null, "msg", template.callback, template.options);
        Notify.storage.msg = {obj: msg, parent};

        parent = Page.elements.header.firstChild;
        box = Page.elements.notify1;
        template = NOTIFY_TEMPLATES.alert.text;

        const alert = new Msg("alert", box, null, "alert", template.callback, template.options);
        Notify.storage.alert = {obj: alert, parent};

        parent = parent.nextSibling;
        box = Page.elements.notify2;
        template = NOTIFY_TEMPLATES.msg.callback;

        const bnt = new Msg("bnt", box, null, "msg", template.callback, template.options);
        Notify.storage.bnt = {obj: bnt, parent};
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
        this.options = Object.assign({}, NOTIFY[type]);
        Object.assign(this.options, options);
    }
    render(status, text, options) {
        // options
        const myOptions = Object.assign({}, this.options);
        Object.assign(myOptions, options);
        // parent
        if (!document.body.contains(this.parent)) {
            this.parent = Notify.getBox(this.name)
        }
        // storage
        if (myOptions.capacity > 0 && this.storage.length >= myOptions.capacity) {
            dom.remove(this.storage.shift())
        }
        // set ticker
        if (myOptions.timer > 0) {
            this.timeout = myOptions.timer / 1000;
            this.ticker = this.ticker || setInterval(this.tick.bind(this), 1000);
        }
        // render
        const box = this.callback(this.parent, status || this.status, text, myOptions);
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
        if (this.options.ticker) {
            this.update();
        }
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


// TEMPLATES (renders)
function msg_symbol_prefix_text (parent, status, text, options) {
    let {prefix, classNameItem} = options;
    prefix = prefix ? prefix : prefix === "" ? "" : NOTIFY.prefix[status];
    const box = notify_box(parent, null, options);
    notify_svg(box, status, options);
    const small = dom.element("small", box, classNameItem);
    const _prefix = prefix ? dom.text("strong", small, prefix) : null;
    const _text = text ? dom.text("small", box, text, classNameItem) : null;
    return box;
}
function msg_title_text (parent, status, text, options) {
    const {title, data} = text || {};
    const box = notify_box(parent, null, options);
    const div = dom.element("div", box, options.classNameTitle);
    dom.node(title, dom.element("p", div));
    const ul = dom.element("ul", div);
    for (const line of data) {
        dom.node(line, dom.element("li", ul));
    }
    return box;
}
function msg_text (parent, status, text, options) {
    const box = notify_box(parent, null, options);
    dom.text(options.tag || "strong", box, text, options.classNameItem);
    return box;
}
function alert_text (parent, status, text, options) {
    const box = notify_box(parent, status, options);
    notify_svg(box, status, options);
    dom.text(options.tag || "span", box, text, options.classNameItem);
    return box;
}
function notify_callback (parent, status, text, options) {
    const {svg, tag, label, func} = options.callback;
    const box = notify_box(parent, null, options);
    if (svg) {
        notify_svg(box, status, options);
    }
    const span = dom.element("span", box, options.classNameItem);
    const bnt = dom.text(tag || "strong", span,text || label, {role: "button"});
    bnt.addEventListener("click", func);
    return box;
}
// TEMPLATES (helpers)
function notify_box(parent, status, options) {
    return dom.element("div", parent, options.className + (status ? (" " + status) : ""));
}
function notify_svg(parent, status, options) {
    const {classNameItem, width, height} = options;
    const id = "#" + options[status || options.status].svg;
    return dom.svgUse(parent, id, classNameItem, width, height);
}
// box.innerHTML = `<svg class="${classNameItem}" width="24" height="24" role="img"><use href="#${svg}"/></svg>`;


export {Notify}