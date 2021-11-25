import factory from "../factory_loader.js";
import {data, page, router} from "../factory.js";


// constants
const EXERCISE = {};
/**
 * @type {boolean} save resources in memory
 */
EXERCISE.cashable = true;
EXERCISE.classNameFocus = "focus";
EXERCISE.indexes = [
    "-",
    {"Clazz":"Flashcards", "adaptable":true},
    {"Clazz":"Quizzes"},
    {"Clazz":"Maps"}
];

class Exercise {
    // static current;

    constructor() {
        Exercise.instance = this;
        Exercise.current = undefined;
    }
    async render(fileName) {
        this.reset();
        this.id = parseInt(fileName);

        // skip index 0
        await router.navigate(router.index || 1, this.id);
        const CURRENT = EXERCISE.indexes[router.index];

        // get current exercise instance
        Exercise.current = await factory.getInstance(CURRENT.Clazz);

        const resource = router.route.path + "/" + this.id;
        const jsonFile = await data.getJson(resource, EXERCISE.cashable, CURRENT.adaptable);
        const opt = CURRENT.adaptable ? jsonFile["json"] : jsonFile;
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
        await Exercise.current.render(jsonFile)
        this.focusLink();
    }
    clickButtonStart() {
        if (page.active) {
            Exercise.current.stop()
        } else {
            Exercise.current.start()
        }
    }
    next(){
        Exercise.current.next()
    }
    skip(){
        Exercise.current.skip()
    }
    previous(){
        Exercise.current.previous()
    }
    shuffle() {
        Exercise.current.toggleShuffle()
    }
    reset() {
        if (Exercise.current) {
            Exercise.current.stop();
            // Exercise.current.visible(false);
            Exercise.current = undefined;
        }
    }
    focusLink() {
        Array.from(document.querySelectorAll("aside a")).forEach(a => {
            a.classList.toggle(EXERCISE.classNameFocus, parseInt(a.value) === this.id)
        })
    }
    // events
    static controlEvent(e) {
        clickButton(getButtonElement(e.target))
    }
    static async renderEvent(e) {
        if (e.target && e.target.tagName === "A") {
            await Exercise.instance.render(e.target.value)
        }
    }
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
            Exercise.instance.clickButtonStart()
        } else if (id === "back") {
            Exercise.instance.previous()
        } else if (id === "forward") {
            Exercise.instance.skip()
        } else if (id === "shuffle") {
            Exercise.instance.shuffle()
        }
    } else if (key) {
        const index = parseInt(key);
        Exercise.current.bar.toggle(index);
        Exercise.current.list.update(index);
        Exercise.current.cards.update(index);
    }
    Exercise.current.resume();
}

export {Exercise};