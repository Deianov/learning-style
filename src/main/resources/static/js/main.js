"use strict";

import service from "./modules/services/service.js";
import exercise from "./modules/services/exercise.js";
import "./modules/components/topics.js";
import "./modules/components/page/scroll.js";


// default loader
service.router.navigate( 0, null, service.router.urlSearchParams);

// footer
document.getElementById("cdate").innerHTML = (new Date().getFullYear());

// events
service.page.aside.addEventListener("click", exercise.callbacks.renderExercise);
service.components.getInstance("breadcrumb").setEvent("click", service.router.callbacks.navigateEvent);
service.components.getInstance("bar").setEvent("click", exercise.callbacks.controlEvent);