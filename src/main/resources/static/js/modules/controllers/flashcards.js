import factory from "../factory_loader.js";
import {ScopeCounter} from "../utils/counters.js";
import {page, notify, data} from "../factory.js";
import CS from "../constants.js";
import strings from "../utils/strings.js";
import numbers from "../utils/numbers.js";


// constants
const FLASHCARDS = {};
FLASHCARDS.turnsWaitingToRepeat = 5;


class Flashcards {
    constructor() {
        Flashcards.instance = this;
        this.btnEdit = () => notify.btn("info", "edit", Flashcards.edit, {hideSvg: false, button: {svg: {id: "edit", width: 14}}});
        this.btnAdd = notify.create("btnAdd", page.elements.messages, "info", "msg", notify.template.msg.button, {
           capacity: 1, button: {func: () => Flashcards.instance.list.add(), svg: {id: "plus", width: 22, height: 22, color: "green"}}
        })
    }
    async render(jsonFile) {
        this.json = jsonFile;
        /**
         * @param {number} min Inclusive (default: 0)
         * @param {number} max Exclusive (optional)
         * @param {boolean} shuffle
         */
        this.counter = new ScopeCounter(0, undefined, undefined);

        this.bar = await factory.getInstance("Bar");
        this.list = await factory.getInstance("List");
        this.cards = await factory.getInstance("Cards");
        this.input = await factory.getInstance("UserInput");
        this.tags = await factory.getInstance("Tags");

        this.bar.render(this.json);
        this.list.render(this.json);
        this.input.controller = Flashcards.onTextareaChange;
        this.tags.render();
        this.btnEdit();
    }
    start() {
        this.counter.reset(this.json.state.rows);
        this.bar.start();
        this.list.remove();
        this.cards.render(this.json);
        this.input.render(this.json, null);
        this.next();
        page.play(true)
    }
    stop() {
        if (!page.active) {return}
        this.counter.reset(0);
        this.bar.stop();
        this.cards.remove();
        this.input.remove();
        this.list.render(this.json);
        page.play(false)
    }
    finish(result) {
        this.stop();
        page.elements.content.style.display="none";
        this.controller.finish(result);
    }
    play(step) {
        let row = false;

        // finish
        if (!this.counter.hasNext() && (step === 1 || step === 2)) {
            this.finish({
                    path: this.json.exercise.path,
                    count: this.counter.getPreviousCount(),
                    stack: this.counter.stack.length,
                    success: this.input.successCounter.value(),
                    errors: this.input.errorsCounter.value(),
                    timer: numbers.timer.stop(this.startTime, 0, 1, 1, 0),
                }
            );
            return;
        }

        // next
        if (step === 1) {
            row = this.counter.next().getValue();
        // skip
        } else if (step === 2) {
            this.input.clear();
            row = this.counter.skip().getValue();
        // previous           
        } else if (step === -1) {
            this.input.stats.change("success", this.input.successCounter.back())
            row = this.counter.previous().getValue() || 0;
        // repeat
        } else if (step === 0) {
            row = this.counter.repeat(FLASHCARDS.turnsWaitingToRepeat).getValue();
        }

        if (!this.list.isValidRow(row)) {
            console.error("Bad row number!");
        }
        else if (this.input.state.error) {
            this.cards.setStatus("error", true);
            this.cards.update(this.json.state.card);
            this.input.repeat()
        }
        else {
            this.cards.setStatus("error", false);
            this.input.stats.change("done", this.counter.getPreviousCount() || 1)
            this.cards.populate(row);
            this.input.next();
            this.bar.row = row + 1
        }
    }
    next() {
        this.play(1)
    }
    skip() {
        this.play(2)
    }
    previous() {
        this.play(-1)
    }
    toggleShuffle() {
        this.counter.shuffle = !this.counter.shuffle;
        this.bar.shuffle(this.counter.shuffle);
        this.counter.reset();
        this.input.reset();
        this.next()
    }
    static onTextareaChange(input) {
        Flashcards.instance.play(input.state.success ? 1 : 0)
    }
    resume() {
        if (page.active) {
            this.input.visible(true)
        }
    }
    static edit() {
        notify.btn("error", "save", Flashcards.save)
        notify.btnAdd("info", "add");
        const that = Flashcards.instance;
        that.list.render(that.json, {contenteditable: true})
    }
    static async save() {
        const that = Flashcards.instance;
        that.btnEdit();
        notify.with("btnAdd").clear();

        const tmp = [];
        for (let row of that.list.read()) {
            tmp.push(row.map(c => strings.removeHTML(c)));
        }

        // static
        if (CS.app.isStatic) {
            // sort and remove numbering
            that.json.data = tmp.sort((a, b) => a[0].localeCompare(b[0])).map(d => d.slice(1));
            that.list.render(that.json);

            notify.alert("error", CS.msg.server.required)
            return;
        }

        // server
        const res = await data.getJsonWithPayload(that.json.exercise.path, {
            username: CS.app.username || "",
            data: tmp,
        });

        if (res) {
            // update data
            if (res.status === 200) {
                that.json.data = res.data || [];
            }
            notify.alert(res.status === 200 ? "success" : "error", res.message)
        }
        that.list.render(that.json);
    }
}
factory.addClass(Flashcards)


export {Flashcards}