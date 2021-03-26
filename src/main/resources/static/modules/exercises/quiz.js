import dom from "../utils/dom.js";
import service from "../services/service.js";
import Flags from "../utils/flags.js";

// constant
const className = "questions";
const classNameResults = "results";
const classNameCard = "card";
const classNameValidate = "validate";
let that;

class Quiz {
    constructor(parent) {
        this.parent = dom.getLazy(parent)
        this.element;
        this.local;
        this.active = false;
        that = this;
    }
    async render(localData) {
        this.local = localData;
        this.questions = this.local.questions;
        this.validated = false;

        // todo: server validation
        this.correct = this.local.correct;
        // this.correct = this.correct || this.questions.map(q => Flags.toNumber(q.answers.map(a => a.correct)))


        // top
        this.resultsElement = Quiz.renderResults(this.parent, classNameResults, classNameCard);
        this.resultsElement.firstChild.lastChild.addEventListener("click", function () {that.validate()});
        this.resultsElement.firstChild.lastChild.textContent = service.constants.button.view;
    
        // table
        this.element = dom.element("ol", this.parent, className);
        for (let i = 0; i < this.questions.length; i++) {
            renderQuestion(this.element, this.questions[i], i)
        }

        // bottom
        this.bnt = dom.text("button", this.parent, service.constants.button.validate, classNameValidate);
        this.bnt.addEventListener("click", function () {that.validate()});   
    }
    static renderResults(parent, className, classNameCard) {
        const element = dom.element("div", parent, className);
        const card = dom.element("div", element, classNameCard);
        dom.element("span", card);
        dom.element("a", card, {"href":"#"});
        return element
    }
    stop() {
        // required from exercise.js
    }
    reset() {
        this.parent.innerHTML = "";
        this.render(this.local)
    }
    async validate() {
        service.page.goTop();

        if (that.validated) {
            that.reset();
            return
        }

        that.validated = true;
        that.bnt.textContent = service.constants.button.clear;
        that.bnt.classList.toggle("clear", true);

        if (!this.correct) {
            // todo: client/server validation
            const res = await service.data.getJsonWithPayload(`/quiz/${this.local.id}/validate`, {"answers": getUserAnswers()});
            if (res) {
                that.correct = res.correct;
            }
        }

        if (!that.correct) {
            throw new Error("Not found correct answers.")
        }

        const results = validateQuestions(that.correct);
        const all = results.length;
        const correct = results.filter(Boolean).length;

        // remove correct
        const questions = Array.from(document.getElementsByClassName(className)[0].children)
        questions.map((question, index) => {
            if(results[index]) {
                question.innerHTML = `<li><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="10" height="10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" fill="none" shape-rendering="geometricPrecision"><path d="M20 6L9 17l-5-5"/></svg></li>`;
            }
        })

        document.getElementsByClassName(classNameResults)[0].firstChild.firstChild.textContent = `${correct}/${all}`;
        document.getElementsByClassName(classNameResults)[0].firstChild.lastChild.textContent = service.constants.button.clear;
    }
}


function renderQuestion(parent, obj, questionNumber) {
    const div1 = dom.element("div", parent, "question")
    dom.text("span", dom.element("li", div1), obj.text)
    const div2 = dom.element("div", div1)
    for (let i = 0; i < obj.answers.length; i++) {
        renderLabel(div2, obj.answers[i], i, questionNumber)
    }
}

function renderLabel(parent, obj, value, questionNumber) {
    const label = dom.element("label", parent, "answer");
    dom.element("input", label, {"name":`question${questionNumber}`, "type":"checkbox", value});
    dom.text("span", label, obj.text)
}

function validateQuestions(correct) {
    const parent = document.getElementsByClassName(className)[0];
    const questions = Array.from(parent.children)
    let i = 0;
    return questions.map(question => validate(question, correct[i++]))
}

function validate(question, correctFlags) {
    const answers = question.getElementsByTagName("input");
    let hasChecked, hasError, input, isChecked;

    for (let index = 0; index < answers.length; index++) {
        input = answers[index];
        isChecked = input.checked;
        hasChecked = hasChecked || isChecked;

        if (Flags.isTrue(correctFlags, Flags.toBits(index))) {
            input.parentNode.classList.toggle("correct", true)

        } else if (isChecked) {
            input.parentNode.classList.toggle("wrong", true)
            hasError = true
        }
    }
    return !hasError && hasChecked
}

/**
 * Used by server validation
 *
 * @returns {number[]} Array of flags -> user answers
 */
function getUserAnswers() {
    const parent = document.getElementsByClassName(className)[0];
    const questions = Array.from(parent.children);
    const userAnswers = Array(questions.length);
    let flags;
    for (let index = 0; index < questions.length; index++) {
        const answers = questions[index].getElementsByTagName("input");
        flags = 0;
        for (let i = 0; i < answers.length; i++) {
            if (answers[i].checked) {
                flags = Flags.set(flags, Flags.toBits(i));
            }
        }
        userAnswers[index] = flags;
    }
    return userAnswers;
}

export default Quiz