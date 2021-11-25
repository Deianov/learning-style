const constants_app = {
name: "learning-style",
title: "Learning-Style",
isStatic: false,
version: "06.10.2021",
state: "development",
lang: "en",
}

const constants_server = {
local: "http://192.168.0.52:9999",
relative: ".",
url: ".",
api: "./api",
json: "./json",
}

const constants_dom = {
    pageheader: {tagName: "header"},
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

ms.de = {}
ms.de.input = {}
ms.de.bnt = {}

ms.en.input.again = "Write this word again.";
ms.de.input.again = "Schreiben Sie das Wort noch einmal auf.";

ms.en.bnt.validate = "Validate";
ms.de.bnt.validate = "Bestätigen";

ms.en.bnt.clear = "Clear";
ms.de.bnt.clear = "löschen";

ms.en.bnt.view = "view";
ms.de.bnt.view = "ansehen";

ms.en.quiz.server = "Server validation is not supported on static version.";
ms.en.quiz.corect = "Not found correct answers.";


const CS = {
    app: constants_app,
    server: constants_server,
    dom: constants_dom,
    get msg() {
        return ms[constants_app.lang];
    }
}

export default CS;