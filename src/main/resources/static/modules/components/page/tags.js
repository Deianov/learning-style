import dom from "../../utils/dom.js";
import Component from "../component.js";

/*
    <div class="tags">
        <span class="heading">Topics</span>
        <div class="tag">
            <a href="#">German</a>
        </div>
        <div class="tag">
            <a href="#">Words</a>
        </div>
        ...
    </div>
*/

const className = "tags";
const classNameItem = "tag";
const classNameHeader = "heading";

class Tags extends Component {
    constructor(parent) {
        super(parent, null, "div", className)
    }
    /**
     * {"text":"Topics", "tags":[
                {"href":"#", "textContent":"German"},
                {"href":"#", "textContent":"Programing"},
                {"href":"#", "textContent":"Words"},
                {"href":"#", "textContent":"Test"}
            ]
        }
     */
    render(obj) {
        super.reset();

        dom.text("span", this.element, obj.text, classNameHeader);
        for (const tag of obj.tags) {
            dom.element("a", dom.element("div", this.element, classNameItem), tag)
        }
    }
}

export default Tags;