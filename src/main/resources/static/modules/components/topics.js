import data from "../data/data.js";
import dom from "../utils/dom.js";
import constants from "../services/constants.js";


class Topics {
    constructor() {
        this.element = document.getElementsByTagName("aside")[0]
    }
    async render(page = "cards") {

        this.element.innerHTML = "";
        try {
            const index = await data.getJson(`${page}?lang=${constants.lang}`, true, false);

            for (const topic of index) {
                renderTopic(this.element, topic)
            }
        } catch {
            this.element.innerHTML = `<ul><img src="./assets/img/loaders/puff.svg" alt="loader" width="60" height="60"></img></ul>`;
        }
    }
}

function renderTopic(parent, topic) {
    const ul = dom.element("ul", parent);
    const api = topic.api || "";
    dom.text("h3", ul, topic.category);
    topic.links.forEach(link => renderLink(ul, `${api}${link.id}`, link.text))
}

function renderLink(parent, value, text) {
    dom.text("a", dom.element("li", parent), text, {"href":"#", value})
}

export default new Topics();