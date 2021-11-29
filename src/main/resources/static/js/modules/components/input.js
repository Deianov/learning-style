import factory from "../factory_loader.js";
import dom from "../utils/dom.js";
import CS from "../constants.js";
import {Component} from "./components.js";
import {SimpleCounter} from "../utils/counters.js";
import {notify} from "../factory.js";
import {Stats} from "./stats.js";
import {numbers} from "../utils/numbers.js";


class UserInput extends Component {
    constructor(parent = CS.dom.content.id) {
        super(parent, undefined, "div", "input")
        this.successCounter = new SimpleCounter(1);
        this.errorsCounter = new SimpleCounter(1);
        this.stats = new Stats();
        this.input = new TextInput(this)
        this.state = {}
    }
    render(jsonFile, controller) {
        super.reset(); // create this.element
        this.json = jsonFile;
        dom.removeAll(this.element);

        this.stats.render(this.element)
        this.input.render(this.element)
        this.reset()
        // Flashcards.onTextareaChange
        this.controller = controller || this.controller;
    }
    read() {
        this.content = this.input.value
        this.state.done = this.input.isDone();

        // save previous state
        if (!this.state.done) {
            this.state.error = !this.contained(this.content, this.word);
            this.state.success = this.compare(this.content, this.word);
        }

        this.update();
        if (this.state.done) { this.controller(this) }
    }
    next() {
        this.clear();
        this.words = this.json.data[this.json.state.row];
        this.word = this.words[this.json.state.card];
        this.input.setAssert(this.word);
        if (this.input.keyboard) {
            this.input.keyboard.renderKeys(this.word);
        }
    }
    repeat() {
        this.clear();
        this.state.repeat = true; // skip stats
        notify.msg("error", CS.msg.input.again, {prefix: ""})
    }
    reset() {
        this.clear();
        this.successCounter.reset();
        this.errorsCounter.reset();
        this.stats.setStats(0, this.json.state.rows, 0, 0);
    }
    clear() {
        for(const key of Object.keys(this.state)) {
            this.state[key] = null;
        }
        this.input.clear();
        notify.clear();
    }
    update() {
        if (this.state.done) {
            this.input.clear();
            this.input.focus();
            this.state.error = !this.state.success

            // stats
            if (!this.state.repeat) {
                if (this.state.success) {
                    this.stats.change("success", this.successCounter.next)
                } else {
                    this.stats.change("error", this.errorsCounter.next);
                }
            }

            // error
            if (this.state.error) {
                this.input.setStatus("error")
            }

            // examples  todo:
            if (!this.state.examples) {
                // notify.title({title: "Custom information", data: ["line 1", "line 2", "line 3"]})
                if (Array.isArray(this.words.slice(-1)[0])) {
                    this.state.examples = this.words.slice(-1)[0];
                    notify.title({title: "Examples", data: this.state.examples})
                }
            }
        } else {
            // update current state
            this.input.setStatus(this.state.success ? "success" : this.state.error ? "error" : "");
            if (this.state.success) {
                notify.clear();
                notify.msg("info", "", {prefix: "Done.", timer: 2000})
            }
        }
    }
    /**
     * Show/Hide the input
     * @param {boolean} flag 
     */
    visible(flag) {
        this.element.style.display = flag ? "" : "none";
        if (flag) { this.input.focus() }
    }
    /**
     * @param {string} str1  "abc"
     * @param {string} str2  "abc"
     */
    compare(str1, str2) {
        return str1 === str2
    }
    /**
     * str2 start with str1
     * @param {string} str1  "abc"
     * @param {string} str2  "abcdef"
     */
    contained(str1, str2) {
        return str1 === str2.substr(0, str1.length)
    }
    remove() {
        super.remove()
    }
}
factory.addClass(UserInput)


class TextInput {
    constructor(controller) {
        TextInput.default = {
            form: {className: "form", spellcheck: "false", autocapitalize: "none", autocomplete: "off"},
            textarea: {placeholder: CS.msg.input.placeholder} // disabled: true
        }
        // UserInput.read()
        this.controller = controller;
    }
    render(parent, options) {
        const opt = options || TextInput.default;
        this.form = dom.element("form", parent, opt.form);
        this.textarea = dom.element("textarea", this.form, opt.textarea);
        this.textarea.addEventListener("input", this);
        this.keyboard = new MyKeyboard(this).render(parent, this.textarea, null);
        this.clear();
    }
    handleEvent(e) {
        /** read speed optimisation - skip unnecessary calls */
        if (this.assert()) {
            return
        }
        this.setStatus("")
        this.controller.read();
    }
    get value() {
        return this.textarea.value || "";
    }
    setStatus(className) {
        if (this.status !== className) {
            this.status = className || "";
            this.textarea.className = this.status;
        }
    }
    clear() {
        this.status = "";
        this.textarea.value = "";
    }
    focus() {
        if (this.keyboard.opt.mode.off) {
            this.textarea.focus();
        }
    }
    isDone() {
        return ["\n", "\r"].includes(this.value.slice(-1));
    }
    setAssert(text) {
        this.text = text;
    }
    assert() {
        if (this.text) {
            this.text = this.value.length < this.text.length ? this.text : null;
            return this.text && !this.status && this.text.startsWith(this.value);
        }
        return null;
    }
}

const KEYBOARDS = {
    german: {
        small: ["q", "w", "e", "r", "t", "z", "u", "i", "o", "p", "ü", "ß", "<--", "a", "s", "d", "f", "g", "h", "j", "k", "l", "ö", "ä", "#", "Enter", "_^", "y", "x", "c", "v", "b", "n", "m", ",", ".", "-", "!", "?"],
        big: ["Q", "W", "E", "R", "T", "Z", "U", "I", "O", "P", "Ü", "~", "<--", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Ö", "Ä", "'", "Enter", "_^", "Y", "X", "C", "V", "B", "N", "M", ";", ":", "_", "!", "?"],
    }
}


class MyKeyboard {
    constructor(controller, textarea) {
        MyKeyboard.instance = this;
        MyKeyboard.default = {
            keys: KEYBOARDS.german,
            className: "keyboard",
            classNameItem: "item",
            classNameLong: "item-long",
            classNameOther: "other",
            buttons: {"Enter": "\n", "<--": "del", "__": " ", "_^": "shift"},
            mode: {
                off: CS.app.keyboard === 0,
                static: CS.app.keyboard === 1,
                byString: CS.app.keyboard === 2,
                size: 0, // 11
            }
        }
        // TextInput.handleEvent()
        this.controller = controller;
        this.textarea = textarea;
        this.opt = Object.assign({}, MyKeyboard.default);
    }
    render(parent, textarea, options) {
        Object.assign(this.opt, options);

        this.textarea = textarea || this.textarea;
        this.modeButton = dom.element("span", parent, {id: "keyboard", role: "button", className: "right"});
        this.modeButton.addEventListener("click", this.changeMode)
        this.keyboard = dom.element("div", parent, this.opt.className)
        this.keyboard.addEventListener("click", this);
        this.setMode("");
        return this;
    }
    renderKeyboard(isBig) {
        if (this.opt.mode.off || !this.opt.mode.static) {
            return;
        }
        this.clear();

        let div, c = 0;
        const keys = isBig ? this.opt.keys.big : this.opt.keys.small;
        keys.forEach(k => {
            if (c === 0 || c % 13 === 0) {
                div = dom.element("div", this.keyboard);
            }
            dom.element("button", div, {index: c, textContent: k, value: (k.length === 1 ? k : this.opt.buttons[k]), type: "button", role: "button", className: this.opt.classNameItem})
            c++;
        })
        div = dom.element("div", this.keyboard, this.opt.classNameOther);
        dom.element("button", div, {textContent: " ", value: " ", type: "button", role: "button", className: this.opt.classNameItem})
    }
    renderKeys(str = "") {
        if (this.opt.mode.off || this.opt.mode.static) {
            return;
        }
        this.clear();
        const div = dom.element("div", this.keyboard);
        const chars = this.opt.keys;
        const len = chars.small.length;
        const keys = {};
        for (const letter of str.split("")) {
            let index = chars.small.indexOf(letter);
            index = index > -1 ? index : chars.big.indexOf(letter);
            index = index > -1 ? index : len;
            keys[letter] = index;
        }

        // add random buttons if "keyboard size" > keys.length
        const left = this.opt.mode.size - Object.keys(keys).length;
        for (let c = 0; c < left;) {
            const i = numbers.getRandomInt(0, len);
            const k = chars.small[i];
            if (!keys[k]) {
                keys[k] = i;
                c++;
            }
        }

        keys["<--"] = len + 2;
        keys.Enter = len + 3;

        const sorted = Object.entries(keys).sort((a, b) => a[1] - b[1]).map(v => v[0]);
        sorted.forEach(k => {
            dom.element("button", div, {textContent: k, value: (k.length === 1 ? k : this.opt.buttons[k]), type: "button", role: "button", className: this.opt.classNameItem})
        })
    }
    handleEvent(e) {
        if (e.target.value) {  // e.target.tagName === "BUTTON"
            const value = e.target.value;
            if (value.length > 2) {
                if (value === "del") {
                    this.textarea.value = this.textarea.value.slice(0, -1);
                } else {
                    this.isBig = !this.isBig;
                    this.renderKeyboard(this.isBig);
                    return;
                }
            } else {
                this.textarea.value+= value;
            }
            this.controller.handleEvent();
        }
    }
    /**
     * @param {string|null} str - if null, update current;
     */
    setMode(str) {
        const mode = MyKeyboard.instance.opt.mode;
        const key = str || (mode.static ? "static" : mode.byString ? "byString" : "off");
        const bnt = MyKeyboard.instance.modeButton;
        mode.off = key === "off";
        mode.static = key === "static";
        mode.byString = key === "byString";
        CS.app.keyboard = mode.off ? 0 : mode.static ? 1 : 2;
        if (key === "off") {
            MyKeyboard.instance.clear();
            bnt.textContent = CS.msg.keyboard.off;
        } else if (key === "static") {
            MyKeyboard.instance.renderKeyboard();
            bnt.textContent = CS.msg.keyboard.static;
        } else if (key === "byString") {
            MyKeyboard.instance.renderKeys();
            bnt.textContent = CS.msg.keyboard.byString;
        }
    }
    /**
     * @param {string|null} key  - if not key, change to next
     */
    changeMode(key) {
        const d = MyKeyboard.instance;
        const m = d.opt.mode;
        const name = (typeof key === "string") ? key : m.off ? "static" : m.static ? "byString" : "off";
        d.setMode(name);
    }
    clear() {
        this.keyboard.innerHTML = "";
    }
}


export {UserInput};