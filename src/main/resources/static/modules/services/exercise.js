import service from "./service.js";

// constants
const cashable = true;


class Exercise {
    constructor() {
        this.current
    }
    async render(fileName) {
        this.reset();
        this.current = this.getCurrentExercise();
        const route = service.router.route;
        const resource = route.path + "/" + fileName;
        const adaptable = service.router.index < 2;
        const json = await service.data.getJson(resource, cashable, adaptable);
        const opt = adaptable ? json.json : json;
        const source = {
            "source":opt.source,
            "sourceUrl":opt.sourceUrl,
            "author":opt.author,
            "authorUrl":opt.authorUrl
        }
        service.page.blank(opt.name, opt.category, source);
        this.current.render(json)
    }
    clickButtonStart() {
        if (service.page.active) {
            this.current.stop()
        } else {
            this.current.start()
        }
    }
    next(){
        this.current.next()
    }
    skip(){
        this.current.skip()
    }
    previous(){
        this.current.previous()
    }
    shuffle() {
        this.current.toggleShuffle()
    }
    reset() {
        if (this.current) {
            this.current.stop();
            // this.current.visible(false);
            this.current = null;
        }
    }
    getCurrentExercise() {
        const routerIndex = service.router.index;
        if (routerIndex === 0) {
            service.router.navigate(1);
            return service.components.getInstance("flashcards")
        } else if (routerIndex === 1) {
            return service.components.getInstance("flashcards")
        } else if (routerIndex === 2) {
            return service.components.getInstance("quiz");
        } else {
            throw new Error("Bad exercise route: " + service.router.route)
        }
    }
    // todo: not used
    use(functionName, args) {
        if (this.current) { // && that.current.hasOwnProperty(functionName)
            this.current[functionName](args)
        }
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
        exercise.current.controlBar.toggle(index);
        exercise.current.lists.update(index);
        exercise.current.cards.update(index);
    }
    exercise.current.resume();
}

function renderExercise (e) {
    if (e.target && e.target.tagName === "A") {
        exercise.render(e.target.value)
    }
}

const exercise = new Exercise();
const callbacks = {
    renderExercise,
    controlEvent
}
export default exercise;