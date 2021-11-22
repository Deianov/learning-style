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
        this.jsonFile = jsonFile;

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

        this.bar.render(this.jsonFile);
        this.list.render(this.jsonFile);
        this.input.setEvent("input", this.onTextareaChange, 0);
        this.tags.render()
    }
    start() {
        this.counter.reset(this.jsonFile.save.rows);
        this.bar.start();
        this.list.remove();
        this.cards.render(this.jsonFile);

        this.input.render(this.jsonFile, this.counter);
        this.next();
        page.play(true)
    }
    stop() {
        if (!page.active) {return}
        this.counter.reset(0);
        this.bar.stop();
        this.cards.remove();
        this.input.remove();
        this.list.render(this.jsonFile);
        page.play(false)
    }
    playWord(step) {
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
        // skip and next            
        } else if (step === 2) {
            if (this.input.repeat) {this.counter.skipLastWaiting()}
            this.input.clear();
            row = this.counter.next
        // previous           
        } else if (step === -1) {
            row = this.counter.previous
        // repeat current
        } else if (step === 0) {
            row = this.counter.repeatLast(FLASHCARDS.turnsWaitingToRepeat)
        }

        // console.debug(JSON.stringify(counter));
 
        if (!this.list.isValidRow(row)) {
            console.error("Bad row number!");
        }
        else if (this.input.error) {
            this.cards.setStatus("error", true);
            this.cards.update(this.jsonFile.save.card);
            this.input.repeatWord()
        } 
        else {
            this.cards.setStatus("error", false);
            this.input.show.stats.done = this.counter.hasPrevious();
            this.cards.populate(row);
            this.input.nextWord();
            this.bar.row = row + 1
        }
    }
    next() {
        this.playWord(1)
    }
    skip() {
        this.playWord(2)
    }
    previous() {
        this.playWord(-1)
    }
    toggleShuffle() {
        this.counter.random = !this.counter.random;
        this.bar.shuffle(this.counter.random);
        this.counter.reset();
        this.input.reset();
        this.next()
    }
    onTextareaChange() {
        Flashcards.instance.input.readInput();
        if (Flashcards.instance.input.done) {Flashcards.instance.playWord(Flashcards.instance.input.success ? 1 : 0)}
    }
    resume() {
        if (page.active) {
            this.input.visible(true)
        }
    }
}
factory.addClass(Flashcards)

export {Flashcards}