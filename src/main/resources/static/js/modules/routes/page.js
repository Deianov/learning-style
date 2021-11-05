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
    blank(subject, category, source) {
        this.reset();
        this.renderBreadcrumb(category);
        renderSubject(this.elements.header, subject || router.route.subject, source)
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

function renderSubject(parent, subject, source) {
    let div = document.getElementsByClassName(classNameSubject)[0];
    div = div || dom.element("div", parent, classNameSubject);
    div.innerHTML = "";
    dom.text("h3", div, subject)
    if (source) {
        let small;
        if (source["source"] || source["author"]) {
            small = dom.element("small", div);
        }
        if (source["source"]) {
            dom.node("source: ", small)
            dom.text("a", small, source.source, formatLink(source.sourceUrl))
        }
        if (source["author"]) {
            dom.node((source["source"] ? ", author: " : "author: "), small)
            dom.text("a", small, source.author, formatLink(source.authorUrl))
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