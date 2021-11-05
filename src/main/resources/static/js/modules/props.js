const app = {};
app.name = "Learning-Style";
app.title = "Learning-Style";
app.isStatic = false;
app.version = "06.10.2021";
app.state = "development";
app.lang = "en";

const server = {};
// const serverUrl = "http://192.168.0.52:9999/learning-style";
server.local = "http://192.168.0.52:9999";
server.relative = ".";
server.url = server.relative;
server.api = server.url + "/api";
server.json = server.url + "/json";

const msg = {}
msg.en = {}
msg.en.input = {}
msg.en.bnt = {}

msg.de = {}
msg.de.input = {}
msg.de.bnt = {}

msg.en.input.again = "Write that word again.";
msg.de.input.again = "Schreiben Sie das Wort noch einmal auf.";

msg.en.bnt.validate = "Validate";
msg.de.bnt.validate = "Bestätigen";

msg.en.bnt.clear = "Clear";
msg.de.bnt.clear = "löschen";

msg.en.bnt.view = "view";
msg.de.bnt.view = "ansehen";

const props = {
    app,
    server,
    "msg": msg[app.lang]
}
export default props;