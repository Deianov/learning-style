import dom from "../utils/dom.js";
import {factory, router, breadcrumb} from "../factory.js";


// constants
const classNameSubject = "subject";
function getElements() {
    return {
        "aside": document.getElementsByTagName("aside")[0],
        "article": document.getElementsByTagName("article")[0],
        "header": document.getElementById("header"),
        "control": document.getElementById("control"),
        "content": document.getElementById("content"),
        "messages": document.getElementById("messages"),
        "bottom": document.getElementById("bottom"),
        "subject": () => document.getElementsByClassName(classNameSubject)[0]
    }
}

class Page {
    constructor() {
        this.elements = getElements();
        this.active = undefined;
        this.router = router;
    }
    reset() {
        this.elements.control.innerHTML = "";
        this.elements.content.innerHTML = "";
        this.elements.messages.innerHTML = "";
        this.elements.bottom.innerHTML = "";
        this.elements.aside.style.display = "";
        this.elements.article.style.display = "";
    }
    blank(subject, category, obj) {
        this.reset();
        this.renderBreadcrumb(category);
        renderSubject(this.elements.header, subject || router.route.subject, obj)
    }
    async component(name) {
        return await factory.getInstance(name)
    }
    async renderContent(callback, args) {
        await callback(this.elements.content, args)
    }
    renderBreadcrumb(topic) {
        const links = [];
        if (router.index > 0) {
            links.push({"href": "./", "textContent": router.routes[0].subject})
            links.push({"key": router.index, "textContent": router.route.subject})
        }
        breadcrumb.render(links)
        breadcrumb.addTopic(topic)
    }
    renderSubject(text) {
        const e = this.elements.subject() || renderSubject(this.elements.header, text);
        e.firstChild.textContent = text
    }
    play(flag) {
        document.getElementsByTagName("header")[0].style.display = flag ? "none" : "";
        this.elements.aside.style.display = flag ? "none" : "";
        this.active = flag
    }
}

/**
 * @param {HTMLElement} parent
 * @param {string} subject  - name
 * @param {object} obj      - {source, sourceUrl, author, authorUrl}
 */
function renderSubject(parent, subject, obj) {
    let div = document.getElementsByClassName(classNameSubject)[0];
    div = div || dom.element("div", parent, classNameSubject);
    div.innerHTML = "";
    dom.text("h3", div, subject)

    const data = [];
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

        const small = dom.element("small", div);
        if (source) {
            dom.node( (i > 0 ? "; " : "") + "source: ", small)
            dom.text("a", small, source, formatLink(sourceUrl))
        }
        if (author) {
            dom.node(source ? ", author: " : "author: ", small)
            dom.text("a", small, author, formatLink(authorUrl))
        }
    }

    dom.element("hr", div)
}

function formatLink(url) {
    const options = {};
    options.href = url || "#";
    if (!["#", "/", "\\"].includes(options.href)) {
        options.target = "_blank";
    }
    return options
}

export default Page;