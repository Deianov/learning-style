import routes from "./routes.js";

/*
if (location.protocol !== "https:"){
    location.replace(window.location.href.replace("http:", "https:"));
}
*/
/**
 *  query           -> /?page={routes.name | routes index}&id={exercise.id}
 *  navigate(index) -> routes[index]
 *
 ** constants
 *
 *  {string} path   = '/' + name (if name exists)
 *  {number} index  = valid routes index
 *  {number} id     = exercise id
 *
 *  {string|number|null} page  -> .getIndex -> index
 *  params          = {page: value, id: number}
 *
 */

class Router {
    constructor() {
        Router.instance = this;
        this.index = 0;
        this.routes = routes;
        this.state = {page: -1};
    }
    setIndex(value) {
        this.index = Router.getIndex(value);
        this.route = this.getRoute();
        // console.debug("setIndex: " + this.index)
    }
    getRoute() {
        return this.routes[this.index];
    }
    static isValid(i) {
        return Number.isInteger(i) && i > -1 && i < routes.length;
    }
    /**
     * @param {string|number|null} page     - page index or name || null (from exercise.render -> skip repeat)
     * @param id            - exercise id
     * @param {{}} params   - {page, id}
     * @returns {Promise<void>}
     */
    async navigate(page, id, params) {

        if (params && params.page) {
            await this.navigate(params.page, params.id, null);
            return;
        }

        const index = (page === null) ? this.index : Router.getIndex(page);
        const isNewPage = index !== this.state.page;
        const isNewId = id !== this.state.id;

        if (isNewPage || isNewId) {
            this.setIndex(index);
            this.state = {page: this.index, id};
            const query = "?page=" + this.index + (id ? "&id=" + id : "");
            window.history.pushState(Object.assign({}, this.state), this.route.title, query);

            const flag = (isNewPage ? 1 : 0) + (isNewId ? 2 : 0) + (page === null ? 4 : 0);
            await Router.update(null, this.state, flag)
        }
    }
    /** object from params
     *
     * @returns {object}  -  {p:1, id:10}
     */
    urlSearchParams() {
        // https://stackoverflow.com/questions/901115/how-can-i-get-query-string-values-in-javascript
        const params = new URLSearchParams(window.location.search);
        const res = {};
        if (params.has("page")) {
            res.page = params.get("page");
        }
        if (params.has("id")) {
            res.id = params.get("id");
        }
        return res;
    }
    /**
     * @param {number|string} value -> index, path or name (path without '/')?
     * @returns {number} page index
     */
    static getIndex(value) {
        let index = 0;
        if (typeof value === "number") {
            index = value;
        }
        else if (typeof value === "string") {
            if (isNaN(value)) {
                const str = value.trim().toLowerCase();
                index = routes.findIndex(r => r.path === ((str.startsWith("/") ? "" : "/") + str));
            } else {
                index = (value ^ 0) // parseInt
            }
        }
        return Router.isValid(index) ? index : 0;
    }
    // -> callbacks
    /**
     * @param event - window.onpopstate
     * @param state - router new state of
     * @param flag
     */
    static async update(event, state, flag) {
        // console.log(`update: event: ${event}; eventState: ${event ? JSON.stringify(event.state) : null}; w.path: ${window.location.pathname}; w.search: ${window.location.search}; routerState: ${JSON.stringify(Router.instance.state)}`)

        let isNewPage = null;
        let isNewId = null;
        const router = Router.instance;
        const _state = router.state;

        if (event) {
            if (!event.state) {
                return
            }
            const {page, id} = event.state;

            isNewPage = page !== _state.index;
            isNewId = id !== _state.id

            if (isNewPage) {
                router.setIndex(page);
            }
            // update router state
            _state.page = page;
            _state.id = id;
        }
        if (flag) {
            isNewPage = flag === 1 || flag === 3;
            isNewId = flag > 1
        }
        // reset from exercise to exercise home
        const isBlank = isNewId && !_state.id;
        // render page
        if (isNewPage || isBlank) {
            if (typeof router.route.init === "function") {
                router.route.init()
            }
            if (typeof router.route.render === "function") {
                await router.route.render()
            }
        }
        // render exercise
        if (isNewId && _state.id) {
            await Router.exercise.render(_state.id);
        }
    }
    static async navigateEvent (e) {
        if (e.target && e.target.hasAttribute("key")) {
            await Router.instance.navigate(parseInt(e.target.getAttribute("key")), null, null);
        }
    }
}


// events
// navigating between two history entries
// window.onpopstate = () => setTimeout(Router.instance.update, 0);
window.onpopstate = async function(event) {
  await Router.update(event)
}

export {Router}