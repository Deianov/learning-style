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
const CSS_COLORS = [
    {name: "white", colors: ["white"]},
    {name: "black", colors: ["black"]},
    {name: "dark", colors: ["#373737"]},
    {name: "green", colors: ["#28a745"]},
    {name: "red", colors: ["#dc3545"]},
    {name: "orange", colors: ["#ff6600"]},
    {name: "-", colors: [""]},
    {name: "gray-dark", colors: ["#333333"]},
    {name: "gray", colors: ["#646464"]},
    {name: "gray-light", colors: ["#959595"]},
    {name: "gray-text", colors: ["#cacaca"]},
    {name: "gray-line", colors: ["#dedede"]},
    {name: "--", colors: [""]},
    {name: "light-red", colors: ["#feaec8"]},
    {name: "light-blue", colors: ["#99d9e9"]},
    {name: "light-yellow", colors: ["#efe4b0"]},
    {name: "light-pink", colors: ["#c7bfe6"]},
    {name: "---", colors: [""]},
    {name: "links", colors: ["#0275d8"]},
    {name: "color-bg", colors: ["#f1f1f1"]},
    {name: "color-bg-hover", colors: ["#ddd"]},
    {name: "hover", colors: ["#1795ec"]},
    {name: "active", colors: ["#4299E1"]},
    {name: "border", colors: ["#ccc"]},
    {name: "submit", colors: ["#4caf50"]},
    {name: "submit-hover", colors: ["#45a049"]},
    {name: "logo", colors: ["#517cc6"]},
    {name: "----", colors: [""]},
    {name: "success", colors: ["#c1ffde", "#c1ffde", "#dbfff8"]},
    {name: "danger", colors: ["#ffcede"]},
    {name: "error", colors: ["#e00"]},
    {name: "info", colors: ["#4299E1"]},
    {name: "alert-success", colors: ["#00A4A6"]},
];


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
ms.en.server = {}

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

ms.en.server.required = "Server side required!";

const CS = {
    app: constants_app,
    system: constants_system,
    server: constants_server,
    dom: constants_dom,
    colors: CSS_COLORS,
    // get msg() {
    //     return ms[constants_app.lang];
    // }
    msg: ms.en,
}

export default CS;