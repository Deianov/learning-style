import dom from '../../utils/dom.js';
import Component from "../component.js";

/*
    <ul class="breadcrumb">
        <li><a href="./">Home</a></li>
        <li><a value="1" href="javascript:void(0)">Cards</a></li>
        <li><a href="#">German</a></li>
    </ul> 
*/

const className = "breadcrumb";
const tagName = "ul"

class Breadcrumb extends Component {
    constructor(parent) {
        super(parent, null, tagName, className)
    }
    render(obj) {
        super.reset();

        if (obj) {
            for (const li of obj) {
                if (!li["href"]) {
                    li.href = "javascript:void(0)";
                }
                dom.element("a", dom.element("li", this._element), li)
            }
        }
        return this
    }
    addTopic(topic) {
        if (topic) {
            dom.element("a", dom.element("li", this._element), {"href":"#", "textContent":topic})
        }
    }
}

export default Breadcrumb;