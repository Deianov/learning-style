import dom from "../utils/dom.js";
import service from "../services/service.js";

// static  ids/elements
const classNameSubject = "subject";
const ids = {
    "header":"header",
    "control":"control",
    "content":"content",
    "messages":"messages",
    "bottom":"bottom"
}


class Page {
    constructor() {
        this.parent = document.main;
        this.aside = document.getElementsByTagName("aside")[0];
        this.article = document.getElementsByTagName("article")[0];
        this.header = document.getElementById(ids.header);
        this.control = document.getElementById(ids.control);
        this.content = document.getElementById(ids.content);
        this.messages = document.getElementById(ids.messages);
        this.bottom = document.getElementById(ids.bottom);
        this.active;
    }
    blank(subject, topic, source) {
        this.reset();
        this.breadcrumb.render(service.router.getBreadcrumb()).addTopic(topic);
        renderSubject(this.header, subject, source)
    }
    render(index, subject) {
        this.blank(subject)
        this.renderContentByIndex(index)
    }
    renderContent(callback, args) {
        if (callback) {
            callback(this.content, args)
        }
    }
    renderContentByIndex(index) {
        this.renderContent(renders[index]["content"])
    }
    reset() {
        this.control.innerHTML = "";
        this.content.innerHTML = "";
        this.messages.innerHTML = "";
        this.bottom.innerHTML = "";
        this.aside.style.display = "";
        this.article.style.display = "";
    }
    play(flag) {
        document.getElementsByTagName("header")[0].style.display = flag ? "none" : "";
        this.aside.style.display = flag ? "none" : "";
        this.active = flag
    }
    component(name) {
        return service.components.getInstance(name)
    }
    get navigation() {
        return service.navigation;
    }
    get breadcrumb() {
        return this.component("breadcrumb")
    }
    get tags() {
        return this.component("tags")
    }
    get subject() {
        return document.getElementsByClassName(classNameSubject)[0]
    }
    set subject(text) {
        if (!this.subject) {
            renderSubject(this.header, text)
        }
        this.subject.firstChild.textContent = text
    }
    goTop() {
        document.body.scrollTop = 0; // For Safari
        document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
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

const renders = [
    {"name":"home", "content":debugContent},
    {"name":"cards", "content":debugContent},
    {"name":"quiz", "content":debugContent},
    {"name":"games", "content":debugContent}
]

function debugContent(parent) {
    dom.text("small", parent, 
    `Lorem ipsum, dolor sit amet consectetur adipisicing elit. Voluptatem, quibusdam dolores magni eveniet reiciendis fuga illum ad. 
    Harum molestias dolorum deserunt repudiandae molestiae in aut dolorem vitae ducimus sint, illum eum tenetur minima quo dignissimos? 
    Dolorum ad rerum adipisci dolore dignissimos deleniti facere, natus maxime voluptatum veniam soluta ab placeat? Incidunt laborum tempore 
    necessitatibus esse, officia est nemo rerum placeat consectetur atque suscipit modi animi dignissimos eos obcaecati, iste et illo saepe numquam 
    deleniti omnis iusto perferendis distinctio! Soluta maiores adipisci ipsum sint beatae earum ea. Minus dolore molestiae, cupiditate fuga quod, 
    voluptates, nulla consequuntur autem ex facere animi ducimus.`
    )
}

const page = new Page();
export default page;