import {page, topics, navigation, props, exercise} from "../factory.js";
import dom from "../utils/dom.js";
let route;
let index;

const routes = [
    { path:"/", name:"home", title: props.app.title, subject: "Home" , render: home},
    { path:"/cards", name:"cards", title: "Cards", subject: "Cards", init: () => index = 1, render: defaultRender},
    { path:"/quizzes", name:"quizzes", title: "Quiz", subject: "Quiz", init: () => index = 2, render: defaultRender},
    { path:"/maps", name:"maps", title: "Maps", subject: "Maps", init: () => index = 3, render: defaultRender},
    { path:"/login", title: "Login" },
    { path:"/register", title: "Register" }
];

async function home() {
    index = 0;
    route = routes[index];

    document.title = route.title;
    navigation.top.navigateByIndex(1);
    page.blank();
    await page.renderContent(debugContent)
    await topics.render(routes[1].name)
}

async function defaultRender() {
    route = routes[index];

    document.title = route.title;
    navigation.top.navigateByIndex(index);
    page.blank();
    await page.renderContent(debugContent)
    await topics.render(route.name)
}

function debugContent(parent) {
    dom.text("small", parent,
        `Lorem ipsum, dolor sit amet consectetur adipisicing elit. Voluptatem, quibusdam dolores magni eveniet reiciendis fuga illum ad. 
    Harum molestias dolorum deserunt repudiandae molestiae in aut dolorem vitae ducimus sint, illum eum tenetur minima quo dignissimos? 
    Dolorum ad rerum adipisci dolore dignissimos deleniti facere, natus maxime voluptatum veniam soluta ab placeat? Incidunt laborum tempore 
    necessitatibus esse, officia est nemo rerum placeat consectetur atque suscipit modi animi dignissimos eos obcaecati, iste et illo saepe numquam 
    deleniti omnis iusto perferendis distinctio! Soluta maiores adipisci ipsum sint beatae earum ea. Minus dolore molestiae, cupiditate fuga quod, 
    voluptates, nulla consequuntur autem ex facere animi ducimus.`
    )
}

/** export unreferenced copy of the routes */
export default routes.slice(0);