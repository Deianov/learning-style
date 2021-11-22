import factory from "../factory_loader.js";
import dom from "../utils/dom.js";
import CS from "../constants.js";
import {Component} from "./components.js";
import {SimpleCounter} from "../utils/counters.js";
import {notify} from "../factory.js";


const INPUT = {};
INPUT.classNameWrapper = "input";
INPUT.className = "form";
INPUT.tagName = "form";
INPUT.options = {
    bar: {
        stats: {class: "stats right"},
        award: {src:"./assets/images/award.svg", alt:"award", width:"18", height:"18"},
        counts: {class: "counts left"}
    },
    form: {className: INPUT.className},
    textarea: {placeholder:"Write something..", autocapitalize:"none", autocomplete:"off"}
}
INPUT.elements = {
    "bar": "",
    "form": "",
    "textarea": "",
    "default": "",
    "messages":""   // todo: ???
}
INPUT.renders = {};


class UserInput extends Component {
    constructor(parent = "content") {
        super(parent, INPUT.classNameWrapper, INPUT.tagName, INPUT.className)
        this.focus = false;
        this.done = false;
        this.error = false;
        this.success = false;
        this.repeat = false;
        UserInput.successCounter = new SimpleCounter(1);
        UserInput.errorsCounter = new SimpleCounter(1);
    }
    render(jsonFile) {
        this.local = jsonFile;
        super.reset();
        renderInput(this.wrapper);
        this.inputElement = INPUT.elements.default;
        this.show = new UserInformation(INPUT.elements, INPUT.renders);
        this.reset();
        super.updateEvent(this.element)
    }
    readInput() {
        this.input = this.inputElement.value || "";
        this.validateInput();
        this.showExamples();
        return this.input
    }
    nextWord() {
        this.clear();
        this.words = this.local.data[this.local.save.row];
        this.word = this.words[this.local.save.card];
    }
    repeatWord() {
        this.clear();
        this.repeat = true;
        // this.show.messages.error.render("", CS.msg.input.again); // skip -> title Error:

        notify.msg("error", CS.msg.input.again, {prefix: ""})   // optional edit off default options prefix
        notify.alert("error", CS.msg.input.again)   // optional edit off default options prefix
    }
    showExamples() {
        if (this.success && !this.examples) {
            // notify.title({title: "Custom information", data: ["line 1", "line 2", "line 3"]})
            this.examples = true;
            notify.with("msg").clear();
            notify.msg("info", "", {prefix: "Done."})
        }
        // if (this.success && !this.examples && Array.isArray(this.words.slice(-1)[0])) {
        //     this.examples = this.words.slice(-1)[0];
        //     INPUT.renders.custom.examples.render();
        //     this.examples.forEach(ex => {
        //         INPUT.renders.custom.examples.addSiblingByCallback(messages.renders.textMessageWithSubject, ex)
        //     });
        // }
    }
    reset() {
        this.clear();
        UserInput.successCounter.reset();
        UserInput.errorsCounter.reset();
        this.show.stats.success = 0;
        this.show.stats.error = 0;
        this.show.stats.rows = this.local.save.rows;
        this.show.stats.done = 0;
    }
    clear() {
        this.inputElement.value = "";
        this.done = false;
        this.repeat = null;
        this.status = null;
        this.examples = null;
        notify.clear();
    }
    /**
     * @param {string} className. Class name or falsy.
     */
    set status(className) {
        this.success = (className === "success");
        this.inputElement.classList.toggle("success", this.success);
        this.error = (className === "error");
        this.inputElement.classList.toggle("error", this.error);
    }
    get status() {
        return this.success ? "success" : this.error ? "error" : null
    }
    isDone() {
        const lastChar = this.input.slice(-1);
        const flag = lastChar === "\n" || lastChar === "\r";    // todo: == ?
        this.done = flag;
        if (flag) {
            this.inputElement.value = "";
            this.inputElement.focus();   
        }
        if (flag && !this.repeat) {
            if (this.success) {
                this.show.stats.success = UserInput.successCounter.next;
            } else {
                this.show.stats.error = UserInput.errorsCounter.next;
            }
        }
        if (flag && !this.success) {
            this.status = "error";
        }
        return flag; 
    }
    /**
     * Show/Hide the input
     * @param {boolean} flag 
     */
    visible(flag) {
        this.parent.style.display = flag ? null : "none";
        this.focus = flag;
        if (flag && this.inputElement) {
            this.inputElement.focus();
        }
    }
    isSuccess() {
        const res = this.compare(this.input, this.word);
        if (res) {
            this.status = "success"
        } else if (this.success) {
            this.status = null
        }
        return res
    }
    isError() {
        const res = !this.contained(this.input, this.word);
        if (res) {
            this.status = "error"
        } else if (this.error) {
            this.status = null
        }
        return res
    }
    validateInput() {
        return this.isDone() || this.isSuccess() || this.isError()
    }
    isValid() {
        return this.isValidString(this.input) && this.isValidString(this.word)
    }
    isValidString(str) {
        return typeof str === "string" && str.length > 0
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
        notify.clear();
        super.remove()
    }

}
factory.addClass(UserInput)

class UserInformation {
    constructor(elementsObject, renders) {
        this.obj = elementsObject;
        this.renders = renders;
        this.elements;
        this.isText;
    }
    get stats() {
        this.elements = this.obj["stats"] || {};
        this.isText = true;
        return this
    }
    // get messages() {
    //     this.elements = this.renders["messages"];
    //     this.isText = false;
    //     return this
    // }
    getElement(name) {
        const e = this.elements[name];
        return this.isText && e ? e.textContent : e
    }
    setElement(name, value) {
        const e = this.elements[name];
        if (e && this.isText) {
            e.textContent = value
        }
    }
    // todo return {"info":...}
    get info() {
        return this.getElement("info")
    }
    set info(text) {
        this.setElement("info", text)
    }
    get success() {
        return this.getElement("success")
    }
    set success(text) {
        this.setElement("success", text)
    }
    get error() {
        return this.getElement("error")
    }
    set error(text) {
        this.setElement("error", text)
    }
    get done() {
        return this.getElement("done")
    }
    set done(text) {
        this.setElement("done", text)
    }
    get rows() {
        return this.getElement("rows")
    }
    set rows(text) {
        this.setElement("rows", text)
    }
}

/*
<div id="input" class="input">
<div>
    <div id="stats" class="right">
        <small>1</small>
        <small>2</small>
        <img src="./assets/img/award.svg" alt="award" width="18" height="18">
    </div>
    <div id="counts" class="left">
        <small>3</small>
        <span>|</span>
        <small>4</small>
    </div>
</div>
<form id="form">
</form>
</div>
*/

function createStatsBar(parent) {
    const div = dom.element("div", parent);
    const parentStats = dom.element("div", div, INPUT.options.bar.stats);
    dom.element("small", parentStats);
    dom.element("small", parentStats);
    dom.element("img", parentStats, INPUT.options.bar.award);

    const parentCounts = dom.element("div", div, INPUT.options.bar.counts);
    dom.element("small", parentCounts);
    dom.text("span", parentCounts, "|");
    dom.element("small", parentCounts);
    return div
}

function renderInput (wrapper) {
    // messages.removeMessages();
    notify.clear()
    dom.removeAll(wrapper);
    INPUT.elements.bar = createStatsBar(wrapper);
    INPUT.elements.form = dom.element("form", wrapper, INPUT.options.form);
    INPUT.elements.textarea = dom.element("textarea", INPUT.elements.form, INPUT.options.textarea);
    INPUT.elements.default = INPUT.elements.textarea;
    INPUT.elements.stats = {
        "success": INPUT.elements.bar.children[0].children[1],
        "error": INPUT.elements.bar.children[0].children[0],
        "done": INPUT.elements.bar.children[1].children[0],
        "rows": INPUT.elements.bar.children[1].children[2]
    };
    // INPUT.elements.messages = document.getElementById("messages");
    // INPUT.renders.messages = messages.create.renders(INPUT.elements.form, messages.renders.textMessageWithSymbol);
    // INPUT.renders.custom = {};
    // INPUT.renders.custom.examples = messages.create.customRender(INPUT.elements.messages, "info", messages.renders.textMessageWithSymbol, "")
}

export {UserInput} ;