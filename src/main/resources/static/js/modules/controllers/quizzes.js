import factory from "../factory_loader.js";
import CS from "../constants.js"
import dom from "../utils/dom.js"
import {data, notify} from "../factory.js";
import {Flags} from "../utils/flags.js";


const QUIZZES = {
    "questions": {"className": "questions"},
    "results": {"className": "results"},
    "card": {"className": "card"},
    "validate": {"className": "validate"}
}

class Quizzes {
    constructor(parent = "content") {
        this.parent = dom.get(parent)
        this.active = false;
        Quizzes.instance = this;
    }
    async render(jsonFile) {
        this.jsonFile = jsonFile;
        this.questions = this.jsonFile.questions;
        this.validated = false;

        // list of correct answers, if not found -> server validation
        this.correct = this.jsonFile.correct;
        // this.correct = this.correct || this.questions.map(q => Flags.toNumber(q.answers.map(a => a.correct)))

        if (!this.correct && CS.app.isStatic) {
            notify.alert("error", CS.msg.quiz.server)
        }

        // top
        this.resultsElement = Quizzes.renderResults(this.parent, QUIZZES.results.className, QUIZZES.card.className);
        this.resultsElement.firstChild.lastChild.addEventListener("click", function () {Quizzes.instance.validate()});
        this.resultsElement.firstChild.lastChild.textContent = CS.msg.bnt.view;

        // table
        this.element = dom.element("ol", this.parent, QUIZZES.questions.className);
        for (let i = 0; i < this.questions.length; i++) {
            renderQuestion(this.element, this.questions[i], i)
        }

        // bottom
        this.bnt = dom.text("button", this.parent, CS.msg.bnt.validate, QUIZZES.validate.className);
        this.bnt.addEventListener("click", function () {Quizzes.instance.validate()});
    }
    static renderResults(parent, className, classNameCard) {
        const element = dom.element("div", parent, className);
        const card = dom.element("div", element, classNameCard);
        dom.element("span", card);
        dom.element("a", card, {href: "#"});
        return element
    }
    stop() {
        // required from exercise.js
    }
    reset() {
        this.parent.innerHTML = "";
        this.render(this.jsonFile)
    }
    async validate() {

        if (Quizzes.instance.validated) {
            Quizzes.instance.reset();
            return
        }

        Quizzes.instance.validated = true;
        Quizzes.instance.bnt.textContent = CS.msg.bnt.clear;
        Quizzes.instance.bnt.classList.toggle("clear", true);

        /* server validation */
        if (!this.correct) {
            const res = await data.getJsonWithPayload(`/quizzes/${this.jsonFile.id}/certification`, {"answers": getUserAnswers()});
            if (res) {
                Quizzes.instance.correct = res.correct;
            }
        }

        if (!Quizzes.instance.correct) {
            notify.alert("error", CS.msg.quiz.corect)
            return;
        }

        const results = validateQuestions(Quizzes.instance.correct);
        const all = results.length;
        const correct = results.filter(Boolean).length;

        if(correct > 0 && (correct / all) > 0.5) {
            notify.alert("info", `Bravo! ${parseInt(correct / all * 100)}%`)
        }

        // remove correct
        const questions = Array.from(document.getElementsByClassName(QUIZZES.questions.className)[0].children)
        questions.map((question, index) => {
            if(results[index]) {
                question.innerHTML = `<li><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="10" height="10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" fill="none" shape-rendering="geometricPrecision"><path d="M20 6L9 17l-5-5"/></svg></li>`;
            }
        })

        document.getElementsByClassName(QUIZZES.results.className)[0].firstChild.firstChild.textContent = `${correct}/${all}`;
        document.getElementsByClassName(QUIZZES.results.className)[0].firstChild.lastChild.textContent = CS.msg.bnt.clear;
    }
}
factory.addClass(Quizzes)


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
    const parent = document.getElementsByClassName(QUIZZES.questions.className)[0];
    const questions = Array.from(parent.children)
    let i = 0;
    return questions.map(question => validateQuestion(question, correct[i++]))
}

function validateQuestion(question, correctFlags) {
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
    const parent = document.getElementsByClassName(QUIZZES.questions.className)[0];
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

export {Quizzes}