import props from "./props.js";
import data from "./data.js";

import Page from "./routes/page.js";
import Topics from "./components/topics.js";
import {Breadcrumb, Tags, goTop} from "./components/components.js";
import router from "./routes/router.js";
import routes from "./routes/routes.js";
import {callbacks, exercise} from "./controllers/exercise.js"
import navigation from "./controllers/navigation.js";

// constants
const OPT = {};
OPT.breadcrumb = {};
OPT.breadcrumb.parentId = "header";
OPT.breadcrumb.event = router.callbacks.navigateEvent;
OPT.breadcrumb.eventType = "click";
OPT.bar = {};
OPT.bar.parentId = "control";
OPT.bar.path = "./components/bar.js";
OPT.bar.event = callbacks.controlEvent;
OPT.bar.eventType = "click";
OPT.list = {};
OPT.list.parentId = "content";
OPT.list.path = "./components/list.js";
OPT.cards = {};
OPT.cards.parentId = "content";
OPT.cards.path = "./components/cards.js";
OPT.input = {};
OPT.input.parentId = "content";
OPT.input.path = "./components/input.js";
OPT.quizzes = {};
OPT.quizzes.parentId = "content";
OPT.quizzes.path = "./controllers/quizzes.js";
OPT.maps = {};
OPT.maps.parentId = "content";
OPT.maps.path = "./controllers/maps.js";
OPT.tags = {};
OPT.tags.parentId = "bottom";
OPT.flashcards = {}
OPT.flashcards.path = "./controllers/flashcards.js";

// create instances
const page = new Page();
const topics = new Topics();
const breadcrumb = new Breadcrumb(OPT.breadcrumb.parentId);

// events
page.elements.aside.addEventListener("click", callbacks.renderExercise);

const singleton = {
    page,
    topics,
    breadcrumb
};

const components = {
    "tags": () => new Tags(OPT.tags.parentId)
};

async function getInstance(name) {
    if (!singleton.hasOwnProperty(name)) {

        let clazz;
        const component = components[name];
        const myOpt = OPT[name];

        if (component) {
            /**   create instance     */
            clazz = component();
        } else {
            /**   dynamic import, then create instance    */
            // await sleep(1000)
            const obj = await import(myOpt["path"])
            const Clazz = obj["default"] || obj;
            clazz = new Clazz(myOpt["parentId"])
        }

        if (clazz) {
            singleton[name] = clazz;

            if (myOpt.hasOwnProperty("event")) {
                clazz.setEvent(myOpt.eventType, myOpt.event);
            }
        } else {
            console.error("Unable to create instance: " + name)
        }
    }
    return singleton[name]
}

/*
async function sleep(milliseconds) {
    const date = Date.now();
    let currentDate = null;
    do {
        currentDate = Date.now();
    } while (currentDate - date < milliseconds);
    console.debug(`sleep: ${milliseconds}`);
}
*/
const factory = {
    getInstance
}
export {factory, props, data, page, topics, breadcrumb, router, routes, exercise, navigation}
export default factory;