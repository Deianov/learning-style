import factory from "../factory_loader.js";
import {data, notify, page, router} from "../factory.js";
import {localRepository} from "../data.js";
import numbers from "../utils/numbers.js";


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
        Exercise.current.controller = this;

        const resource = router.route.path + "/" + this.id;
        this.json = await data.getJson(resource, EXERCISE.cashable, CURRENT.adaptable);
        this.key = "learning-style/" + this.json.exercise.path;

        /** {name, category, description, source, sourceUrl, author, authorUrl, createdBy} exercise */
        await page.blank(this.json.exercise);
        await Exercise.current.render(this.json)
        this.focusLink();
    }
    start() {
        if (!page.active) {
            Exercise.current.startTime = new Date();
            Exercise.current.start();
            // show finished
            const storage = localRepository.getItem(this.key);
            if (storage) {
                const obj = JSON.parse(storage);
                if (obj && obj.games) {
                    const {stack, success, errors, timer} = obj;
                    notify.btn("", ("successful: " + obj.games), () => {notify.alert("info", `Top result - successes: ( ${success} / ${stack} ), errors: ( ${errors} ), time: ${numbers.timer.text(timer, 0, 1, 1, 0)}`)}, {hideSvg: true, button: {tag: "small"}});
                }
            }
        } else {
            Exercise.current.stop()
        }
    }
    finish(result) {
        finishExercise(result);
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
            Exercise.instance.start()
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


function finishExercise(result) {
    const {path, count, stack, success, errors, timer} = result;
    const key = Exercise.instance.key;
    const storage = localRepository.getItem(key);

    notify.msg("success", `${count} | ${stack} (${success} successes, ${errors} errors, ${timer.text} time)`, {prefix: "Done!"});

    const rate = success / stack;
    if (isNaN(rate) || rate < 0.9) {
        notify.alert("info", "Done with less than 90%.");
        notify.alert("info", "Play again ?");
        return;
    }
    notify.alert("success", "Well done !!!");
    notify.alert("info", "Play again ?");

    if (!storage) {
        localRepository.setItem(key, JSON.stringify({games: 1, count, stack, success, errors, timer: timer.diff}));
        return;
    }
    const obj = JSON.parse(storage);
    if (!obj.games) {
        return;
    }
    obj.games++;
    notify.msg("success", `${obj.games} ${obj.games === 1 ? "time" : "times"}`, {prefix: "Finished: "});

    if (success > obj.success) {
        obj.success = success;
        notify.alert("success", `Top success points: ${success} !!!`);
        notify.msg("success", `${success} !!!`, {prefix: "Top success points: "});
    }
    if (timer.diff < obj.timer) {
        obj.timer = timer.diff;
        notify.alert("success", `Top time: ${timer.text} !!!`);
        notify.msg("success", `${timer.text} !!!`, {prefix: "Top time: "});
    }
    if (errors < obj.errors) {
        obj.errors = errors;
        notify.alert("success", `Top errors points: ${success} !!!`);
        notify.msg("success", `${success} !!!`, {prefix: "Top errors points: "});
    }
    localRepository.setItem(key, JSON.stringify(obj));
}

export {Exercise};