"use strict";
import {router} from "./modules/factory.js";


// default loader
await router.navigate(0, null, router.urlSearchParams);

// footer
document.getElementById("cdate").innerHTML = (new Date().getFullYear());