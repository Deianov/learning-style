const constants_system = {
isMobile: (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)),
isDarkMode: window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches,
isBrowser: (typeof window === 'object' && typeof document === 'object'),
isHttps: location.protocol === "https:",
}

const constants_app = {
name: "learning-style",
title: "Learning-Style",
isStatic: false,
version: "06.10.2021",
state: "development",
lang: "en",
/** keyboard - 0: default, 1: virtual, 2: virtual (only word keys) */
keyboard: constants_system.isMobile ? 1 : 0,
}

// development state
constants_system.infoString = `state: ${constants_app.state}, version: ${constants_app.version} (${constants_app.isStatic ? "static":"api"}), `;
constants_system.infoString+= (constants_system.isMobile ? "mobile OS: " : "OS: ");
constants_system.infoString+= navigator.userAgent;
constants_system.infoString+= (constants_system.isDarkMode ? ", DarkMode" : "");
document.getElementsByClassName("version")[0].textContent = constants_system.infoString;


const constants_server = {
local: "http://192.168.0.52:9999",
relative: ".",
url: ".",
api: "./api",
json: "./json",
}

const constants_dom = {
    pageheader: {tagName: "header"},
    menu: {className: "navbar"},
    aside: {tagName: "aside"},
    article: {tagName: "article"},
    header: {id: "header"},
    control: {id: "control"},
    content: {id: "content"},
    messages: {id: "messages"},
    bottom: {id: "bottom"},
    cdate: {id: "cdate"},
    breadcrumb: {className: "breadcrumb", tagName: "ul"},
    subject: {className: "subject"},
    notify: {className: "notify-box"},
}

const ms = {}
ms.en = {}
ms.en.input = {}
ms.en.bnt = {}
ms.en.quiz = {}
ms.en.keyboard = {}

ms.de = {}
ms.de.input = {}
ms.de.bnt = {}

ms.en.input.again = "Write this again.";
ms.de.input.again = "Schreiben Sie das Wort noch einmal auf.";
ms.en.input.placeholder = "Write this ..";

ms.en.bnt.validate = "Validate";
ms.de.bnt.validate = "Bestätigen";

ms.en.bnt.clear = "Clear";
ms.de.bnt.clear = "löschen";

ms.en.bnt.view = "view";
ms.de.bnt.view = "ansehen";

ms.en.keyboard.off = "keyboard: system";
ms.en.keyboard.static = "keyboard: virtual";
ms.en.keyboard.byString = "keyboard: virtual (auto)";

ms.en.quiz.server = "Server validation is not supported on static version.";
ms.en.quiz.corect = "Not found correct answers.";


const CS = {
    app: constants_app,
    system: constants_system,
    server: constants_server,
    dom: constants_dom,
    get msg() {
        return ms[constants_app.lang];
    }
}

export default CS;