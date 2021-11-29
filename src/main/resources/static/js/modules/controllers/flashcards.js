import factory from "../factory_loader.js";
import {ScopeCounter} from "../utils/counters.js";
import {page, notify} from "../factory.js";


// constants
const FLASHCARDS = {};
FLASHCARDS.turnsWaitingToRepeat = 5;


class Flashcards {
    constructor() {
        Flashcards.instance = this;
    }

    async render(jsonFile) {
        this.json = jsonFile;
        /**
         * @param {number} min Inclusive (default: 0)
         * @param {number} max Exclusive (optional)
         * @param {boolean} random (shuffle)
         */
        this.counter = new ScopeCounter(0, false, false);

        this.bar = await factory.getInstance("Bar");
        this.list = await factory.getInstance("List");
        this.cards = await factory.getInstance("Cards");
        this.input = await factory.getInstance("UserInput");
        this.tags = await factory.getInstance("Tags");

        this.bar.render(this.json);
        this.list.render(this.json);
        this.input.controller = Flashcards.onTextareaChange;
        this.tags.render()
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
    play(step) {
        let row = false;

        // finish
        if (!this.counter.hasNext() && (step === 1 || step === 2)) {
            this.stop();
            // todo: something more
            page.elements.content.style.display="none" ;
            notify.clear();
            notify.msg("success", "", {prefix: "Done!"});
            notify.alert("success", "Well done !!!");
            notify.alert("info", "Play again ?");
            return
        }

        // next
        if (step === 1) {
            row = this.counter.next
        // skip
        } else if (step === 2) {
            if (this.input.state.repeat) {this.counter.skipLastWaiting()}
            this.input.clear();
            row = this.counter.next
        // previous           
        } else if (step === -1) {
            row = this.counter.previous;
            this.input.stats.change("success", this.input.successCounter.back())
        // repeat
        } else if (step === 0) {
            row = this.counter.repeatLast(FLASHCARDS.turnsWaitingToRepeat)
        }

        console.log({row})

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
            this.input.stats.change("done", this.counter.hasPrevious())
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
        this.counter.random = !this.counter.random;
        this.bar.shuffle(this.counter.random);
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
}
factory.addClass(Flashcards)

export {Flashcards}