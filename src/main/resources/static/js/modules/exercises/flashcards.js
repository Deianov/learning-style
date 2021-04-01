import service from "../services/service.js";
import exercise from "../services/exercise.js";
import Counter from "../utils/counter.js";

// constants
const turnsWaitingToRepeat = 5;

// objects
/**
 * @param {number} min Inclusive (default: 0)
 * @param {number} max Exclusive (optional)
 * @param {boolean} random (shuffle)
 */
let counter;


class Flashcards {
    constructor() {
        this.local;
    }
    render(localData) {
        counter = new Counter(0, false, false);
        this.local = localData;
        this.controlBar = service.components.getInstance("bar");
        this.lists = service.components.getInstance("lists");
        this.cards = service.components.getInstance("cards");
        this.input = service.components.getInstance("input");

        this.controlBar.render(this.local);
        this.lists.render(this.local);
        this.input.setEvent("input", this.onTextareaChange, 0);

        service.page.tags.render(
            {"text":"Topics", "tags":[
                {"href":"#", "textContent":"this"},
                {"href":"#", "textContent":"is"},
                {"href":"#", "textContent":"under"},
                {"href":"#", "textContent":"construction"}
            ]}
        )
    }
    start() {
        counter.reset(this.local.save.rows);
        this.controlBar.start();
        this.lists.remove();
        this.cards.render(this.local);

        this.input.render(this.local, counter);
        this.next();
        service.page.play(true)
    }
    stop() {
        if (!service.page.active) {return}
        counter.reset(0);
        this.controlBar.stop();
        this.cards.remove();
        this.input.remove();
        this.lists.render(this.local);
        service.page.play(false)
    }
    playWord(step) {
        let row = false;

        // finish
        if (!counter.hasNext() && (step === 1 || step === 2)) {
            this.stop();
            console.log("finish");
            return
        }

        // next
        if (step === 1) {
            row = counter.next
        // skip and next            
        } else if (step === 2) {
            if (this.input.repeat) {counter.skipLastWaiting()}
            this.input.clear();
            row = counter.next
        // previous           
        } else if (step === -1) {
            row = counter.previous
        // repeat current
        } else if (step === 0) {
            row = counter.repeatLast(turnsWaitingToRepeat)
        }

        // console.debug(JSON.stringify(counter));
 
        if (!this.lists.isValidRow(row)) {
            console.error("Bad row number!");
        }
        else if (this.input.error) {
            this.cards.setStatus("error", true);
            this.cards.update(this.local.save.card);
            this.input.repeatWord()
        } 
        else {
            this.cards.setStatus("error", false);
            this.input.show.stats.done = counter.hasPrevious();
            this.cards.populate(row);
            this.input.nextWord();
            this.controlBar.row = row + 1
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
        counter.random = !counter.random;
        this.controlBar.shuffle(counter.random);
        counter.reset();
        this.input.reset();
        this.next()
    }
    onTextareaChange() {
        exercise.current.input.readInput();
        if (exercise.current.input.done) {exercise.current.playWord(exercise.current.input.success ? 1 : 0)}
    }
    resume() {
        if (service.page.active) {
            this.input.visible(true)
        }
    }
}

export default Flashcards;