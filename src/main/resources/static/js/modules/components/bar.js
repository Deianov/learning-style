import factory from "../factory_loader.js";
import dom from "../utils/dom.js";
import {Component} from "./components.js";


// constants
const BAR = {}
BAR.className = "bar";
BAR.tagName = "div";
BAR.elements = {};
BAR.initElements = () => {
    BAR.elements.start = document.getElementById("start");
    BAR.elements.onPlay = {};
    BAR.elements.onPlay.back = document.getElementById("back");
    BAR.elements.onPlay.row = document.getElementById("row");
    BAR.elements.onPlay.forward = document.getElementById("forward");
    BAR.elements.onPlay.shuffle = document.getElementById("shuffle");
    BAR.elements.tabs = document.getElementById("tabs");
}

class Bar extends Component {
    constructor(parent = "control") {
        super(parent, null, BAR.tagName, BAR.className)
        this.tabs = [];
    }
    render(jsonFile) {
        super.reset();
        renderDom(this._element);
        BAR.initElements();

        this.local = jsonFile;
        this.tabs.length = 0;
        // reference to localData
        this.activeTabs = this.local.save.tabs;
        this.onPlay(false);
        BAR.elements.onPlay.row.disabled = true;

        for (let i = 0; i < this.activeTabs.length; i++) {
            this.tabs.push(dom.element("button", BAR.elements.tabs, {"key":i, "textContent":this.local.json.labels[i]}));
            this.setActiveTab(i, true);
        }
    }
    start() {
        this.local.save.status = "start";
        this.setLabel(BAR.elements.start, "Stop");
        this.setActive(BAR.elements.start, true);
        this.onPlay(true);
        this.resetTabs(true);
    }
    stop() {
        this.local.save.status = "stop";
        this.setLabel(BAR.elements.start, "Start");
        this.setActive(BAR.elements.start, false);
        this.onPlay(false);
        this.resetTabs();
    }
    resetTabs(onPlay) {
        for (let i = 0; i < this.activeTabs.length; i++) {
            const isActive = onPlay ? (i !== this.local.save.card) : true;
            this.setActiveTab(i, isActive)
        }
    }
    toggle(index) {
        this.setActiveTab(index, !this.activeTabs[index])
    }
    setActiveTab(index, flag) {
        this.setActive(this.tabs[index], flag);
        this.activeTabs[index] = flag
    }   
    shuffle(flag) {
        this.setActive(BAR.elements.onPlay.shuffle, flag);
    }
    onPlay(flag) {
        Object.values(BAR.elements.onPlay).forEach(e => e.style.display = flag ? "" : "none");
    }
    get row() {
        return BAR.elements.onPlay.row.children[0].textContent
    }
    set row(str) {
        BAR.elements.onPlay.row.children[0].textContent = str || "0"
    }
    setActive(e, flag = true) {
        e.classList.toggle("active", flag)
    }   
    setLabel(e, str) {
        e.textContent = str
    }
    isActive(index) {
        return this.activeTabs[index];
    }
    getTabIndex(e) {
        return parseInt(e.getAttribute("key"))
    }
}
factory.addClass(Bar)


function renderDom(parent) {
    parent.innerHTML = 
    `<div class="buttons">
        <button id="start">Start</button>
        <button id="back">
            <svg viewBox="0 0 32 32" width="15" height="15" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="4">
                <path d="M20 30 L8 16 20 2"/>
            </svg>
        </button>
        <button id="row" disabled><small></small></button>
        <button id="forward">
            <svg viewBox="0 0 32 32" width="15" height="15" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="4">
                <path d="M12 30 L24 16 12 2"/>
            </svg>
        </button>
    </div>
    <div class="toggles">
        <button id="shuffle">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="14" height="14" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" fill="none" shape-rendering="geometricPrecision">
                <path d="M16 3h5v5"/><path d="M4 20L21 3"/><path d="M21 16v5h-5"/><path d="M15 15l6 6"/><path d="M4 4l5 5"/>
            </svg>
        </button>
    </div>
    <div class="toggles">
        <div class="toggles" id="tabs">
        </div>
        <button class="bar-menu" disabled>
            <svg width="14" height="14" viewBox="0 0 24 24" width="15" height="15" alt="menu">
                <path d="M24 6h-24v-4h24v4zm0 4h-24v4h24v-4zm0 8h-24v4h24v-4z" fill="currentColor"/>
            </svg>
        </button>
    </div>`;
}

export default Bar;