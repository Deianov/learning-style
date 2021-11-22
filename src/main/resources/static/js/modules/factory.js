import factory from "./factory_loader.js";
import {Data} from "./data.js";
import {Page} from "./routes/page.js";
import {Router} from "./routes/router.js";
import {Topics} from "./components/topics.js";
import {Breadcrumb, Tags, GoTop} from "./components/components.js";
import {Menu} from "./components/navigation.js";
import {Notify} from "./components/notify.js";
import {Exercise} from "./controllers/exercise.js"


factory.init = async () => {

    // startup instances
    const data = new Data();
    const router = new Router();
    const page = new Page();
    const topics = new Topics();
    const notify = new Notify(true);
    if(!page.elements.control) {
        notify.alert("error", "This part of the static version is under construction.")
        return {}
    }
    const navigation = {
        top: new Menu(document.getElementsByClassName("navbar")[0])
    }
    new GoTop();
    new Exercise();

    // todo: if needed getInstance
    // Object.assign(factory.instances, { data, router, page, topics, navigation, exercise })

    // init dynamic imports
    factory.addClass(Breadcrumb)
    factory.addEvent("Breadcrumb", "click", Router.instance.navigateEvent)
    factory.addClass("Bar", undefined, "./components/bar.js")
    factory.addEvent("Bar", "click", Exercise.instance.controlEvent)
    factory.addClass("List", undefined, "./components/list.js")
    factory.addClass("Cards", undefined, "./components/cards.js")
    factory.addClass("UserInput", undefined, "./components/input.js")
    factory.addClass("Quizzes", undefined, "./controllers/quizzes.js")
    factory.addClass("Maps", undefined, "./controllers/maps.js")
    factory.addClass(Tags)
    factory.addClass("Flashcards", undefined, "./controllers/flashcards.js")

    // instances
    const breadcrumb = await factory.getInstance("Breadcrumb");

    // events
    page.elements.aside.addEventListener("click", Exercise.instance.renderEvent);

    return {data, page, topics, breadcrumb, router, navigation, notify}
}

export const {data, page, topics, breadcrumb, router, navigation, notify} = await factory.init();