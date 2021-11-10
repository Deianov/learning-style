import dom from "../utils/dom.js";
// import {GERMANY} from "../components/maps/maps-de.js";

// constants
const MAPS_PATH = "../components/maps/";

class Maps {
    constructor(parent) {
        this.parent = dom.get(parent)
    }

    async render(jsonFile) {
        const maps = Object.entries(jsonFile.options).sort(((a, b) => a[1] - b[1])).map(v => v[0]);
        const [name, file1, file2] = maps;

        let imp;
        const map = new Map(this.parent);

        // dynamic import - map
        imp = await import(MAPS_PATH + encodeURIComponent(file1));  // "../components/maps/maps-de.js"
        const res1 = imp.default;

        await map.render(res1[name]);
        map.renderInfo(res1.meta);

        // dynamic import - full data
        imp = await import(MAPS_PATH + encodeURIComponent(file2)); // "../components/maps/maps-de-full.js"
        const res2 = imp.default;
        map.meta = res2[name].meta;
    }

    stop() {
        // required from exercise.js
    }
    reset() {
        this.parent.innerHTML = "";
    }
}

class Map {
    constructor(parent) {
        this.wrapper = dom.element("div", parent, "row maps");
        this.element = dom.element("div", this.wrapper);
        this.info = dom.element("div", this.wrapper, "column");

        this.element.addEventListener("click", this);
    }
    async render(obj) {
        this.reset();
        this.element.innerHTML = obj.svg;
        this.meta = obj.meta;
    }
    renderInfo(meta) {
        this.info.innerHTML = "";
        dom.element("div", this.info).innerHTML = meta.flag || meta.coat_of_arms;
        dom.text("h3", this.info, `${meta.state} (${meta.ISO3166_2})`);
        dom.element("div", this.info).innerHTML = `<strong>Fläche</strong> : ${meta.area} km<sup>2</sup>`;
        dom.element("div", this.info).innerHTML = `<strong>Einwohner (2018)</strong> : ${numberWithCommas(meta.population)} Mio.`;

        this.textElement = this.textElement || document.getElementById("map-text");
        this.textElement.textContent = meta.state;
    }
    reset() {
        this.textElement = null
        this.element.innerHTML = "";
        this.info.innerHTML = "";
    }
    setActive(e) {
        if (e && this.active) {
            this.active.classList.toggle("active", null)
        }
        if (e) {
            e.classList.toggle("active", true);
        }
        this.active = e
    }
    navigateByIndex(index) {
        this.setActive(this.element.getElementById(index))
    }
    handleEvent(e) {
        const t = e ? e.type : null;
        if (t === "click") {
            this.onclick(e.target)
        }
    }
    onclick(e) {
        if (e.tagName === "path") {
            this.setActive(e);
            this.renderInfo(this.meta[Number.parseInt(e.id)])
        }
    }
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

export default Maps;