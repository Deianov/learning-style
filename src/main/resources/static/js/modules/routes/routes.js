import CS from "../constants.js";
import dom from "../utils/dom.js";
import {page, router, topics, navigation} from "../factory.js";


const routes = [
    { path:"/", name:"home", title: CS.app.title, subject: "Home" , render: home},
    { path:"/cards", name:"cards", title: "Cards", subject: "Cards", init: () => router.index = 1, render: defaultRender},
    { path:"/quizzes", name:"quizzes", title: "Quiz", subject: "Quiz", init: () => router.index = 2, render: defaultRender},
    { path:"/maps", name:"maps", title: "Maps", subject: "Maps", init: () => router.index = 3, render: defaultRender},
    { path:"/login", title: "Login" },
    { path:"/register", title: "Register" }
];

async function home() {
    router.index = 0;
    document.title = routes[router.index].title;
    navigation.top.navigateByIndex(1);
    page.blank();
    await page.renderContent(debugContent)
    await topics.render(routes[1].name)
}

async function defaultRender() {
    document.title = routes[router.index].title;
    navigation.top.navigateByIndex(router.index);
    page.blank();
    await page.renderContent(debugContent)
    await topics.render(routes[router.index].name)
}

function debugContent(parent) {
    // dom.text("small", parent,
    //     `Lorem ipsum, dolor sit amet consectetur adipisicing elit. Voluptatem, quibusdam dolores magni eveniet reiciendis fuga illum ad.
    // Harum molestias dolorum deserunt repudiandae molestiae in aut dolorem vitae ducimus sint, illum eum tenetur minima quo dignissimos?
    // Dolorum ad rerum adipisci dolore dignissimos deleniti facere, natus maxime voluptatum veniam soluta ab placeat? Incidunt laborum tempore
    // necessitatibus esse, officia est nemo rerum placeat consectetur atque suscipit modi animi dignissimos eos obcaecati, iste et illo saepe numquam
    // deleniti omnis iusto perferendis distinctio! Soluta maiores adipisci ipsum sint beatae earum ea. Minus dolore molestiae, cupiditate fuga quod,
    // voluptates, nulla consequuntur autem ex facere animi ducimus.`
    // )
    parent.innerHTML = `<svg id="kiwi" xmlns="http://www.w3.org/2000/svg" x="0px" y="0px" width="100" viewBox="0 65.326 612 502.174" enable-background="new 0 65.326 612 502.174" xml:space="preserve"><ellipse class="ground" cx="283.5" cy="487.5" rx="259" ry="80"/><path class="kiwi" d="M210.333,65.331C104.367,66.105-12.349,150.637,1.056,276.449c4.303,40.393,18.533,63.704,52.171,79.03 c36.307,16.544,57.022,54.556,50.406,112.954c-9.935,4.88-17.405,11.031-19.132,20.015c7.531-0.17,14.943-0.312,22.59,4.341 c20.333,12.375,31.296,27.363,42.979,51.72c1.714,3.572,8.192,2.849,8.312-3.078c0.17-8.467-1.856-17.454-5.226-26.933 c-2.955-8.313,3.059-7.985,6.917-6.106c6.399,3.115,16.334,9.43,30.39,13.098c5.392,1.407,5.995-3.877,5.224-6.991 c-1.864-7.522-11.009-10.862-24.519-19.229c-4.82-2.984-0.927-9.736,5.168-8.351l20.234,2.415c3.359,0.763,4.555-6.114,0.882-7.875 c-14.198-6.804-28.897-10.098-53.864-7.799c-11.617-29.265-29.811-61.617-15.674-81.681c12.639-17.938,31.216-20.74,39.147,43.489 c-5.002,3.107-11.215,5.031-11.332,13.024c7.201-2.845,11.207-1.399,14.791,0c17.912,6.998,35.462,21.826,52.982,37.309 c3.739,3.303,8.413-1.718,6.991-6.034c-2.138-6.494-8.053-10.659-14.791-20.016c-3.239-4.495,5.03-7.045,10.886-6.876 c13.849,0.396,22.886,8.268,35.177,11.218c4.483,1.076,9.741-1.964,6.917-6.917c-3.472-6.085-13.015-9.124-19.18-13.413 c-4.357-3.029-3.025-7.132,2.697-6.602c3.905,0.361,8.478,2.271,13.908,1.767c9.946-0.925,7.717-7.169-0.883-9.566 c-19.036-5.304-39.891-6.311-61.665-5.225c-43.837-8.358-31.554-84.887,0-90.363c29.571-5.132,62.966-13.339,99.928-32.156 c32.668-5.429,64.835-12.446,92.939-33.85c48.106-14.469,111.903,16.113,204.241,149.695c3.926,5.681,15.819,9.94,9.524-6.351 c-15.893-41.125-68.176-93.328-92.13-132.085c-24.581-39.774-14.34-61.243-39.957-91.247 c-21.326-24.978-47.502-25.803-77.339-17.365c-23.461,6.634-39.234-7.117-52.98-31.273C318.42,87.525,265.838,64.927,210.333,65.331 z M445.731,203.01c6.12,0,11.112,4.919,11.112,11.038c0,6.119-4.994,11.111-11.112,11.111s-11.038-4.994-11.038-11.111 C434.693,207.929,439.613,203.01,445.731,203.01z"/></svg>`;
}

/** export unreferenced copy of the routes */
export default routes.slice(0);