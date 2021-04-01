import data from "../data/data.js";
import components from "./components.js";
import constants from "./constants.js";
import navigation from "./navigation.js";
import page from "../pages/page.js";
import router from "../pages/router.js";

const service = {
    data,
    components,
    "constants": constants[constants.lang],
    navigation,
    page,
    router
};

export default service;