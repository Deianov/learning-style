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
        /** editable */
        if (this.opt && this.opt.contenteditable) {
            this.numbering = true;
            this.cols++;
            dom.setOptions(this.element, {spellcheck: "false", autocapitalize: "none", autocomplete: "off"});
            this.element.classList.toggle("numbering", true);
        } else {
            this.element.classList.toggle("numbering", false);
            this.numbering = undefined;
        }
        List.renderRows(this.element, this.cols ,this.json.data, this.numbering, this.opt);
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
        if (!this.element || !this.element.rows.length) {
            return [];
        }
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
                if (!arr.some(ar => List.equals(ar, a, this.numbering))) {
                    arr.push(a);
                }
            } else {
                empty.push(this.element.rows[r]);
            }
        }
        empty.forEach(tr => dom.remove(tr));
        return arr;
    }
    add() {
        List.renderRow(this.element, this.cols, this.numbering ? [this.element.rows.length + 1] : [], this.opt)
    }
    static equals(arr1, arr2, numbering) {
        if (arr1 && arr2) {
            const i = numbering ? 1 : 0;
            const tmp = arr2.slice(i);
            return arr1.slice(i).every((v, i) => v === tmp[i]);
        }
    }
    static renderRow(table, cols, data, opt) {
        const tr = dom.element("tr", table);
        for (let c = 0; c < cols; c++) {
            dom.text("th", tr, data[c] || "", opt);
        }
    }
    static renderRows(table, cols, data, numbering, opt) {
        let i = 1;
        for (const row of data || []) {

            const cells = numbering ? [i++, row].flat() : row;
            List.renderRow(table, cols, cells, opt);
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