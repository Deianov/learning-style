import page from "./page.js";
import topics from "../components/topics.js";

/*  if (location.protocol !== "https:"){
        location.replace(window.location.href.replace("http:", "https:"));
    }
*/

/*  constants
    searchParams    -> /?page={routes.name}&id={exercise.id}
    navigate(index) -> routes[index]
*/
const routes = [
    { path:"/", name:"home", title: "Learning Geist", subject: "Home"},
    { path:"/cards", name:"cards", title: "Cards", subject: "Cards" },
    { path:"/quiz", name:"quiz", title: "Quiz", subject: "Quiz" },
    { path:"/games", name:"games", title: "Games", subject: "Games" },
    { path:"/login", title: "Login" },
    { path:"/register", title: "Register" }
];

class Router {
    constructor() {
        this._lastIndex = -1;
        this._index = 0;
    }
    get index() {
        return this._index
    }
    set index(value) {
        const newIndex = getRouteIndex(value);
        this._index = Router.isValid(newIndex) ? newIndex : 0
    }
    get route() {
        return routes[this.index]
    }
    get title() {
        return this.route.title
    }
    get subject() {
        return this.route.subject
    }
    static isValid(index) {
        return Number.isInteger(index) && index > -1 && index < routes.length
    }
    async navigate(value, params, redirect) {
        if (redirect) {
            await this.navigate(redirect.index, redirect.id);
            return;
        }
        this.index = value;
        if (this.index === this._lastIndex) {
            return
        }
        const path = this.route.path + (params || "");
        window.history.pushState({}, null, path); // const url = window.location.origin + path;
        await updateRoute(this.index)
    }
    get urlSearchParams() {
        const params = new URLSearchParams(window.location.search);
        if (params.has("page")) {
            const page = params.get("page");
            const id = params.get("id");
            const index = getRouteIndex(page);

            if (page === routes[index].name) {
                const result = {index}
                if (id) {
                    result.id = "?id=" + id;
                }
                return result;
            }
        }
        return null;
    }
    getBreadcrumb() {
        if (this._index === 0) {
            return
        }
        const result = [];
        result.push({"href":"./", "textContent":routes[0].subject})
        result.push({"key":this.index, "textContent":this.subject})
        return result
    }
    get callbacks() {
        return callbacks
    }
}

/**
 * @param {number|string} value -> Page index, name or window.location.pathname ?
 * @returns {number} page index
 */
function getRouteIndex(value) {
    if (typeof value === "number") {
        return value
    }
    if (typeof value === "string") {
        if (isNaN(value)) {
            const str = value.trim().toLowerCase();
            return routes.findIndex(r => r.path === ((str.startsWith("/") ? "" : "/") + str));
        }
        return (value ^ 0) // parseInt
    }
    return 0
}

async function updateRoute(routerIndex) {
    router.index  = routerIndex || window.location.pathname;
    const index = router.index;
    const route = router.route;
    console.log("update route: " + window.location.pathname);
    
    if (typeof route.init === "function") {
        route.init()
    }

    page.render(index, route.subject);

    // mix home page with cards
    if (index === 0) {
        page.navigation.top.navigateByIndex(1);
        await topics.render(routes[1].name)
    } else {
        page.navigation.top.navigateByIndex(index);
        await topics.render(route.name)
    }

    router._lastIndex = index;
    document.title = route.title;
}


const callbacks = {
    navigateEvent
}

// events
function navigateEvent (e) {
    if (e.target && e.target.hasAttribute("key")) {
        router.navigate(parseInt(e.target.getAttribute("key")));
    }
}

// navigating between two history entries
window.onpopstate = () => setTimeout(updateRoute, 0);

const router = new Router();
export default router