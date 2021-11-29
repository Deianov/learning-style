import factory from "../factory_loader.js";
import dom from "../utils/dom.js";


/**
 <div>
    <div class="stats right">
        <small>error</small>
        <small>success</small>
        <img src="./assets/images/award.svg" alt="award" width="18" height="18">
    </div>
    <div class="counts left">
        <small>done</small>
        <span>|</span>
        <small>rows</small>
    </div>
 </div>
*/
class Stats {
    constructor() {
        Stats.default = {
            stats: {className: "stats right"},
            award: {src:"./assets/images/award.svg", alt: "award", width: "18", height: "18"},
            counts: {className: "counts left"}
        }
    }
    render(parent, options) {
        const opt = options || Stats.default;
        this.elements = {};
        this.element = dom.element("div", parent);

        const stats = dom.element("div", this.element, opt.stats);
        this.elements.error = dom.element("small", stats);
        this.elements.success = dom.element("small", stats);
        dom.element("img", stats, opt.award);

        const counts = dom.element("div", this.element, opt.counts);
        this.elements.done = dom.element("small", counts);
        dom.text("span", counts, "|");
        this.elements.rows = dom.element("small", counts);

        this.setStats(0, 0, 0, 0);
    }
    getStats() {
        return {
            done: this.elements.done.textContent,
            rows: this.elements.rows.textContent,
            error: this.elements.error.textContent,
            success: this.elements.success.textContent,
        }
    }
    setStats(done, rows, error, success) {
        this.elements.done.textContent = done;
        this.elements.rows.textContent = rows;
        this.elements.error.textContent = error;
        this.elements.success.textContent = success;
    }
    change(name, value) {
        this.elements[name].textContent = value;
    }
    plus(name) {
        this.elements[name].textContent = this.elements[name].textContent + 1;
    }
}

factory.addClass(Stats);
export {Stats}