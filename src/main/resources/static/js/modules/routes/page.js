import dom from "../utils/dom.js";
import CS from "../constants.js";
import {router, breadcrumb, notify} from "../factory.js";


class Page {
    constructor() {
        Page.init();
        this.elements = Page.elements;
    }
    static init() {
        const e = Page.elements;
        e.pageheader = document.getElementsByTagName(e.pageheader.tagName)[0];
        e.aside = document.getElementsByTagName(e.aside.tagName)[0];
        e.article = document.getElementsByTagName(e.article.tagName)[0];
        e.header = document.getElementById(e.header.id);
        e.control = document.getElementById(e.control.id);
        e.content = document.getElementById(e.content.id);
        e.messages = document.getElementById(e.messages.id);
        e.bottom = document.getElementById(e.bottom.id);
        e.cdate = document.getElementById(e.cdate.id);
        e.cdate.textContent = `${new Date().getFullYear()}`;
        const row1 = dom.element("div", e.header, "row");
        const row2 = dom.element("div", e.header, "row");
        e.breadcrumb = dom.element(e.breadcrumb.tagName, row1, e.breadcrumb.className);
        e.notify = dom.element("div", row1, e.notify.className);
        e.subject = dom.element("div", row2, e.subject.className);
        dom.element("div", row2, CS.dom.notify.className)
    }
    static reset() {
        notify.clear();
        this.elements.control.innerHTML = "";
        this.elements.content.innerHTML = "";
        this.elements.content.removeAttribute("style");
        this.elements.messages.innerHTML = "";
        this.elements.bottom.innerHTML = "";
        this.elements.aside.removeAttribute("style");
        this.elements.article.removeAttribute("style");
        this.elements.pageheader.removeAttribute("style");
    }
    blank(subject, category, obj) {
        Page.reset();
        renderBreadcrumb(category);
        renderSubject(this.elements.subject, subject || router.route.subject, obj)
    }
    play(flag) {
        this.elements.pageheader.style.display = flag ? "none" : "";
        this.elements.aside.style.display = flag ? "none" : "";
        this.active = flag
    }
    async renderContent(callback, args) {
        await callback(this.elements.content, args)
    }
}
Page.elements = Object.assign({}, CS.dom);


function renderBreadcrumb(topic) {
    const links = [];
    if (router.index > 0) {
        links.push({"href": "./", "textContent": router.routes[0].subject})
        links.push({"key": router.index, "textContent": router.route.subject})
    }
    breadcrumb.render(links)
    breadcrumb.addTopic(topic)
}

/**
 * @param {HTMLElement} parent
 * @param {string} subject  - name
 * @param {object} obj      - {source, sourceUrl, author, authorUrl}
 */
function renderSubject(parent, subject, obj) {
    const data = [];
    parent.innerHTML = "";
    dom.text("h3", parent, subject)
    if (obj) {
        // split to array
        const arr = Object.values(obj);
        const tmp = arr.map(value => value ? value.split(";") : []);
        const sum = tmp.map(value => value.length).reduce((a, b) => a + b, 0);

        /* separate multiple authors */
        if (sum >= 8) {
            for (let i = 0; i < sum / 4; i++) {
                data.push([tmp[0][i], tmp[1][i], tmp[2][i], tmp[3][i]])
            }
        /* single author */
        } else {
            data.push(arr)
        }
    }

    for (let i = 0; i < data.length; i++) {
        const [source, sourceUrl, author, authorUrl] = data[i];

        const small = dom.element("small", parent);
        if (source) {
            dom.node( (i > 0 ? "; " : "") + "source: ", small)
            dom.text("a", small, source, formatLink(sourceUrl))
        }
        if (author) {
            dom.node(source ? ", author: " : "author: ", small)
            dom.text("a", small, author, formatLink(authorUrl))
        }
    }

    dom.element("hr", parent)
}

function formatLink(url) {
    const options = {};
    options.href = url || "#";
    if (!["#", "/", "\\"].includes(options.href)) {
        options.target = "_blank";
    }
    return options
}

export {Page};