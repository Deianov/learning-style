import CS from "../constants.js"
import dom from "../utils/dom.js"
import {data} from "../factory.js";


class Topics {
    constructor() {
        this.element = document.getElementsByTagName("aside")[0]
    }
    async render(page = "cards") {

        this.element.innerHTML = "";
        const path = page;
        const query = CS.app.isStatic ? "" : `?lang=${CS.app.lang}`;

        try {
            const index = await data.getJson(`${path}${query}`, true, false);

            for (const topic of index) {
                this.renderTopic(this.element, topic)
            }
        } catch {
            this.element.innerHTML = `<ul><img src="./assets/images/loaders/puff.svg" alt="loader" width="60" height="60"></img></ul>`;
        }
    }
    renderTopic(parent, topic) {
        const ul = dom.element("ul", parent);
        const api = topic.api || "";
        dom.text("h3", ul, topic.category);
        topic.links.forEach(link => this.renderLink(ul, `${api}${link.id}`, link.text))
    }
    renderLink(parent, value, text) {
        dom.text("a", dom.element("li", parent), text, {href: "javascript:void(0)", value})
    }
}

export {Topics};