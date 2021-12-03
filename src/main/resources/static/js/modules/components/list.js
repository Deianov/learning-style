import factory from "../factory_loader.js";
import dom from "../utils/dom.js";
import {Component} from "./components.js";
import strings from "../utils/strings.js";


class List extends Component {
    constructor(parent = "content") {
        List.default = {
            className: "lists",
            tagName: "table"
        }
        super(parent, null, List.default.tagName, List.default.className)
    }
    render(jsonFile, options) {
        super.reset();
        this.json = jsonFile;
        populateTable(this.element, this.json.data, this.json.state.tabs.length, options)
        /** editable */
        if (options && options.contenteditable) {
            dom.setOptions(this.element, {spellcheck: "false", autocapitalize: "none", autocomplete: "off"})
        }
    }
    visibleContent(col, flag) {
        tableVisibility(this.element, col, flag)
    }
    update(index) {
        if (!this.element || !this.json) {
            return
        }
        if (typeof index === "number") {
            this.visibleContent(index, this.json.state.tabs[index])
        } else {
            for (let col = 0; col < this.json.state.tabs.length; col++) {
                this.visibleContent(col, this.json.state.tabs[col])
            }
        }
    }
    isValidRow(row) {
        return Number.isInteger(row) && row > -1 && row < this.json.data.length
    }
    read() {
        if (this.element) {
            const rows = this.element.rows.length;
            /** static length ! */
            const cols = this.element.rows[0].cells.length;
            const arr = Array(rows);
            for (let r = 0; r < rows; r++) {
                const row = Array(cols);
                for (let c = 0; c < cols; c++) {
                    row[c] = strings.removeHTML(this.element.rows[r].cells[c].textContent || "");
                }
                arr[r] = row;
            }
            return arr;
        }
    }
    /** debug only
     * @param colors  - {{name: "colorName", colors: ["color1", "colorN"]}}
     */
    coloredTable(colors) {
        super.reset();
        for (const obj of colors) {
            const tr = dom.element("tr", this.element);
            if (obj.name) {
                dom.text("th", tr, obj.name)
                obj.colors.forEach(c => {
                    const e = dom.text("th", tr, c)
                    e.style.backgroundColor = c;
                })
            }
        }
    }
}
factory.addClass(List)

function populateTable(parent, data, cols, opt) {
    let tr;
    for (const row of data) {
        tr = dom.element("tr", parent);
        for (let col = 0; col < cols; col++) {
            dom.text("th", tr, row[col], opt)
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