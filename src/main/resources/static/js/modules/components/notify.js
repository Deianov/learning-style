import dom from "../utils/dom.js";
import factory from "../factory_loader.js";
import {Page} from "../routes/page.js";
import objects from "../utils/objects.js";


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
     * btn
        notify.btn("info", "Click Me!", clickMe, {hideSvg: true});

 * custom
     * myMsg
         const myMsg = notify.create("myMsg", parent, null, "msg", notify.template.msg.text)
         myMsg("error", "Error message!")
     * MyBtn
         const myBtn = notify.create("MyBtn", parent, "info", "msg", notify.template.msg.button, {
               button: {label: "Click Me!", func: clickMe, svg: {id: "plus", width: 22, height: 22, color: "green", className: "append-me"}}
         });
        const myBtn = notify.create("MyBtn", parent, "info", "msg", notify.template.msg.button, {
               button: {label: "Click Me!", func: clickMe}  // default svg byStatus
         });
         myBtn()
         myBtn(newLabel)
         notify.myBtn()
         notify.with("myBtn").render("error", "errorLabel", {hideSvg: true})
 */


const NOTIFY = {
    box: { className: "notify-box" },
    type: { msg: "msg", alert: "alert" },
    status: { info: "info", error: "error", success: "success" },
    prefix: { info: "Info: ", error: "Error: ", success: "Success: " },
    size: { small: 20, normal: 22, large: 24 },
    msg: { className: "msg-box", classNameItem: "item", classNameTitle: "msg-title", capacity: 0, timer: 0},
    alert: { className: "alert-box", classNameItem: "item", capacity: 2, timer: 10000, ticker: 1, close: 1},
    button: { capacity: 1, button: {label: "clickMe", svg: "svg_msg"}},
}
NOTIFY.svg_msg = {width: NOTIFY.size.normal, height: NOTIFY.size.normal, byStatus: {info: {id: "info"}, success: {id: "success"}, error: {id: "error"}}};
NOTIFY.svg_alert = {width: NOTIFY.size.normal, height: NOTIFY.size.normal, byStatus: {info: {id: "info-fill"}, success: {id: "success-fill"}, error: {id: "error-fill"}}};
NOTIFY.button.button.svg = NOTIFY.svg_msg;
const NOTIFY_TEMPLATES = {
    msg: {
        symbol_prefix_text: {
            callback: msg_symbol_prefix_text,
            options: {svg: NOTIFY.svg_msg},
        },
        title_text: {
            callback: msg_title_text,
        },
        text: {
            callback: msg_text,
        },
        button: {
            callback: msg_button,
            options: NOTIFY.button,
        }
    },
    alert: {
        text: {
            callback: alert_text,
            options: {svg: NOTIFY.svg_alert},
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
     * @param {object} options                  - options (Optional)
     * @returns {function}                      - f(text, options) | f(status, text, options)
     */
    create(name, parent, status, type, template, options) {
        // merge options
        const myOptions = objects.assign({}, template.options, options);
        // parent element
        Notify.storage[name] = {};
        Notify.storage[name].parent = parent;
        // notify-box element
        const box = Notify.getBox(name);
        // render instance
        const msg = new Msg(name, box, status, type, template.callback, myOptions);
        Notify.storage[name].obj = msg;
        // notify method with dynamic name 'name'
        Notify.prototype[name] = (status, text, options) => msg.render(status, text, options);
        return Notify.prototype[name];
    }
    with(name) {
        return Notify.storage[name]["obj"];
    }
    clear() {
        Object.values(Notify.storage).forEach(msg => msg.obj.clear());
    }
    msg(status, text, options) {
        Notify.storage.msg.obj.render(status, text, options);
    }
    alert(status, text, options) {
        Notify.storage.alert.obj.render(status, text, options);
    }
    btn(status, label, func, options) {
        const myOptions = objects.assign({},
            NOTIFY.button,
            {button: {svg: {byStatus: null}}},
            {button: {svg: NOTIFY.button.button.svg.byStatus[status]}},
            options,
            {button: {func}},
        );
        Notify.storage.btn.obj.render(status, label, myOptions);
    }
    static remove(name) {
        if (typeof Notify.storage[name] === "object") {
            Notify.storage[name].obj.clear();
            delete Notify.storage[name];
        }
    }
    static getBox(name) {
        const parent = dom.get(Notify.storage[name].parent);
        return parent.getElementsByClassName(NOTIFY.box.className)[0] || dom.element("div", parent, NOTIFY.box.className);
    }
    /** notify.msg();  notify.alert();  notify.title();  notify.btn()  */
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
        template = NOTIFY_TEMPLATES.msg.button;

        const btn = new Msg("btn", box, null, "msg", template.callback, template.options);
        Notify.storage.btn = {obj: btn, parent};
    }
}
factory.addClass(Notify);


class Msg {
    constructor(name, parent, status, type, callback, options) {
        this.storage = [];
        this.name = name;
        this.parent = dom.get(parent);
        this.status = status;
        this.type = type;
        this.callback = callback;
        this.options = objects.assign({}, NOTIFY[type], options);
    }
    render(status, text, options) {
        /** options: {merge} <- 1.NOTIFY <- 2.TEMPLATES <- 3.RENDER(args) */
        const myOptions = objects.assign({}, this.options, options);
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
            const btn = dom.svgUse(dom.element("span", box, "btn-close"), "#close-button", "", "24", "24", "button")
            btn.addEventListener("click", this.clear.bind(this))
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
function msg_button (parent, status, text, options) {
    const {svg, tag, label, func} = options.button;
    const box = notify_box(parent, null, options);
    dom.setOptions(box, {role: "button"});
    if (svg) {
        notify_svg(box, status, options);
    }
    const span = dom.element("span", box, options.classNameItem);
    const btn = dom.text(tag || "strong", span,text || label);
    box.addEventListener("click", func);
    return box;
}
function alert_text (parent, status, text, options) {
    const box = notify_box(parent, status, options);
    notify_svg(box, status, options);
    dom.text(options.tag || "span", box, text, options.classNameItem);
    return box;
}
// TEMPLATES (helpers)
function notify_box(parent, status, options) {
    return dom.element("div", parent, options.className + (status ? (" " + status) : ""));
}
function notify_svg(parent, status, options) {
    const {hideSvg, classNameItem} = options;
    if (hideSvg) {
        return;
    }
    const opt = objects.assign({}, options.svg || options.button.svg);
    if (opt.byStatus) {
        const tmp = objects.assign({}, opt);
        objects.assign(opt, opt.byStatus[status], tmp);
    }
    const {id, width, height, color, className} = opt;
    if (!id) {
        return;
    }
    const svg = dom.svgUse(parent, "#" + id, classNameItem, width, height);
    if (className) {
        svg.classList.toggle(className, true);
    }
    if (color) {
        svg.style.color = color;
    }
    return svg;
}
// box.innerHTML = `<svg class="${classNameItem}" width="24" height="24" role="img"><use href="#${svg}"/></svg>`;


export {Notify}