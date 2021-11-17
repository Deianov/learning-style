import factory from "../factory_loader.js";
import dom from "../utils/dom.js";
import {Component} from "./components.js";


const LIST = {
    className: "lists",
    tagName: "table"
}

class List extends Component {
    constructor(parent = "content") {
        super(parent, null, LIST.tagName, LIST.className)
    }
    render(jsonFile) {
        this.local = jsonFile;
        super.reset();
        populateTable(this.element, this.local.data, this.local.save.tabs.length)
    }
    visibleContent(col, flag) {
        tableVisibility(this.element, col, flag)
    }
    update(index) {
        if (!this.element || ! this.local) {
            return
        }
        if (typeof index === "number") {
            this.visibleContent(index, this.local.save.tabs[index])
        } else {
            for (let col = 0; col < this.local.save.tabs.length; col++) {
                this.visibleContent(col, this.local.save.tabs[col])
            }
        }
    }
    isValidRow(row) {
        return Number.isInteger(row) && row > -1 && row < this.local.data.length
    }
}
factory.addClass(List)

function populateTable(parent, data, cols) {
    let tr;
    for (const row of data) {
        tr = dom.element("tr", parent);
        for (let col = 0; col < cols; col++) {
            dom.text("th", tr, row[col])
        }
    }
}

function tableVisibility (table, col, flag) {
    const rows = table.getElementsByTagName("tr");
    for (let row = 0; row < rows.length; row++) {
        rows[row].children[col].style.display = flag ? null : "none";
    }
}

export default List;