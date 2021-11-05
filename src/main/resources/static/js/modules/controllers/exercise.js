import {data, page} from "../factory.js";

// constants
/**
 * @type {boolean} save resources in memory
 */
const CASHABLE = true;
const INDEX_EXERCISE = [
    "-",
    {"name":"flashcards", "adaptable":true},
    {"name":"quizzes"},
    {"name":"maps"}
];
let currentExercise = undefined;

class Exercise {

    async render(fileName) {
        this.reset();

        // skip index 0
        await page.router.navigate(page.router.index || 1);
        const PROPS = INDEX_EXERCISE[page.router.index];

        // get current exercise instance
        currentExercise = await page.component(PROPS.name);

        const resource = page.router.route.path + "/" + fileName;
        const jsonFile = await data.getJson(resource, CASHABLE, PROPS.adaptable);
        const opt = PROPS.adaptable ? jsonFile["json"] : jsonFile;
        const source = {
            "source":opt.source,
            "sourceUrl":opt.sourceUrl,
            "author":opt.author,
            "authorUrl":opt.authorUrl
        }
        /**
         * name - exercise name
         * category - exercise category
         * source - type/author
         */
        await page.blank(opt.name, opt.category, source);
        await currentExercise.render(jsonFile)
    }
    clickButtonStart() {
        if (page.active) {
            currentExercise.stop()
        } else {
            currentExercise.start()
        }
    }
    next(){
        currentExercise.next()
    }
    skip(){
        currentExercise.skip()
    }
    previous(){
        currentExercise.previous()
    }
    shuffle() {
        currentExercise.toggleShuffle()
    }
    reset() {
        if (currentExercise) {
            currentExercise.stop();
            // currentExercise.visible(false);
            currentExercise = null;
        }
    }
    // todo: not used
    use(functionName, args) {
        if (currentExercise) { // && that.current.hasOwnProperty(functionName)
            currentExercise[functionName](args)
        }
    }
    get current() {
        return currentExercise
    }
    get callbacks() {
        return callbacks
    }
}

function controlEvent(e) {
    clickButton(getButtonElement(e.target))
}

function getButtonElement(target) {
    if (!target || target.tagName === "DIV") {
        return
    }
    if (target.tagName === "BUTTON") {
        return target
    } else {
        return getButtonElement(target.parentNode)
    }
}

function clickButton(bnt) {
    if (!bnt) {
        return
    }

    const id = bnt.getAttribute("id");
    const key = bnt.getAttribute("key");
    
    if (id) {
        if (id === "start") {
            exercise.clickButtonStart()
        } else if (id === "back") {
            exercise.previous()
        } else if (id === "forward") {
            exercise.skip()
        } else if (id === "shuffle") {
            exercise.shuffle()
        }
    } else if (key) {
        const index = parseInt(key);
        currentExercise.bar.toggle(index);
        currentExercise.list.update(index);
        currentExercise.cards.update(index);
    }
    currentExercise.resume();
}

async function renderExercise (e) {
    if (e.target && e.target.tagName === "A") {
       await exercise.render(e.target.value)
    }
}

export const exercise = new Exercise();
export const callbacks = {
    renderExercise,
    controlEvent
}