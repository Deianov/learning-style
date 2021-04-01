
// page
import topics from "../components/topics.js";
import Breadcrumb from "../components/page/breadcrumb.js";
import Tags from "../components/page/tags.js";

// exercise components
import ControlBar from "../components/bar.js";
import Lists from "../components/lists.js";
import Cards from "../components/cards.js";
import UserInput from "../components/input.js";

// exercise
import Quiz from "../exercises/quiz.js";
import Flashcards from "../exercises/flashcards.js";

// constants
const ids = {
    parent:{
        "topics":"topics",
        "breadcrumb":"header",
        "bar":"control",
        "lists":"content",
        "cards":"content",
        "input":"content",
        "tags":"bottom",
        "quiz":"content",
    }
}

const singleton = {
    topics
};
const components = {
    "tags": () => new Tags(ids.parent.tags),
    "breadcrumb": () => new Breadcrumb(ids.parent.breadcrumb),
    "bar": () => new ControlBar(ids.parent.bar),
    "lists": () => new Lists(ids.parent.lists),
    "cards": () => new Cards(ids.parent.cards),
    "input": () => new UserInput(ids.parent.input),
    "quiz": () => new Quiz(ids.parent.quiz),
    "flashcards": () => new Flashcards()
};

function getInstance(name) {
    if (!singleton.hasOwnProperty(name)) {
        const component = components[name];
        if (component) {
            singleton[name] = component()
            // console.debug("create instance: " + name);
        }
    }
    // console.debug("get: " + name);
    return singleton[name]
}

const service = {
    getInstance,
    ids
}
export default service;