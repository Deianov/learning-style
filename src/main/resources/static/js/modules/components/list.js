import factory from "../factory_loader.js";
import dom from "../utils/dom.js";
import {Component} from "./components.js";
import strings from "../utils/strings.js";


class List extends Component {
    constructor(parent = "content") {
        List.default = {
            className: "lists",
            tagName: "table",
        }
        super(parent, null, List.default.tagName, List.default.className);
        this.i = this;
    }
    async render(jsonFile, options) {
        super.reset();
        this.json = jsonFile;
        this.cols = this.json.state.tabs.length;
        this.opt = options;
        List.renderRows(this.element, this.cols ,this.json.data, this.opt);
        /** editable */
        if (this.opt && this.opt.contenteditable) {
            dom.setOptions(this.element, {spellcheck: "false", autocapitalize: "none", autocomplete: "off"});
        }
    }
    /** @param {number|null} column - update column|columns visibility from state */
    update(column) {
        if (!this.element || !this.json) {
            return;
        }
        if (typeof column === "number") {
            List.visibleColumn(this.element, column, this.json.state.tabs[column]);
        } else {
            for (let col = 0; col < this.cols; col++) {
                List.visibleColumn(this.element, col, this.json.state.tabs[col]);
            }
        }
    }
    isValidRow(row) {
        return Number.isInteger(row) && row > -1 && row < this.json.data.length;
    }
    isFilled(row) {
        for (let c = 0; c < row.length; c++) {
            if (strings.isBlank(row[c].textContent)) {
                return false;
            }
        }
        return true;
    }
    read() {
        if (this.element) {
            const rows = this.element.rows.length;
            /** constant length - matrix ! */
            const cols = this.element.rows[0].cells.length;
            const arr = [];
            const empty = [];
            for (let r = 0; r < rows; r++) {
                const a = Array(cols);
                const cells = this.element.rows[r].cells;
                /** skip rows with empty cell */
                if (this.isFilled(cells)) {
                    for (let c = 0; c < cols; c++) {
                        a[c] = cells[c].textContent || "";
                    }
                    /** removing duplicates */
                    if (!arr.some(ar => ar.every((v, i) => v === a[i]))) {
                        arr.push(a);
                    }
                } else {
                    empty.push(this.element.rows[r]);
                }
            }
            empty.forEach(tr => dom.remove(tr));
            return arr;
        }
    }
    add() {
        List.renderRow(this.element, this.cols, [], this.opt);
    }
    static renderRow(table, cols, data, opt) {
        const tr = dom.element("tr", table);
        for (let c = 0; c < cols; c++) {
            dom.text("th", tr, data[c] || "", opt);
        }
    }
    static renderRows(table, cols, data, opt) {
        for (const row of data || []) {
            List.renderRow(table, cols, row, opt);
        }
    }
    static visibleColumn(table, col, flag) {
        const rows = table.getElementsByTagName("tr");
        for (let row = 0; row < rows.length; row++) {
            rows[row].children[col].style.display = flag ? null : "none";
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
                dom.text("th", tr, obj.name);
                obj.colors.forEach(c => {
                    const e = dom.text("th", tr, c);
                    e.style.backgroundColor = c;
                });
            }
        }
    }
}
factory.addClass(List);


export default List;