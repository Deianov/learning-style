import Counter from "../utils/counter.js";
import {page, exercise} from "../factory.js";


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

    async render(localData) {
        this.jsonFile = localData;
        counter = new Counter(0, false, false);

        this.bar = await page.component("bar");
        this.list = await page.component("list");
        this.cards = await page.component("cards");
        this.input = await page.component("input");
        this.tags = await page.component("tags");

        this.bar.render(this.jsonFile);
        this.list.render(this.jsonFile);
        this.input.setEvent("input", this.onTextareaChange, 0);
        this.tags.render(
            {"text":"Topics", "tags":[
                {"href":"#", "textContent":"this"},
                {"href":"#", "textContent":"is"},
                {"href":"#", "textContent":"under"},
                {"href":"#", "textContent":"construction"}
            ]}
        )
    }
    start() {
        counter.reset(this.jsonFile.save.rows);
        this.bar.start();
        this.list.remove();
        this.cards.render(this.jsonFile);

        this.input.render(this.jsonFile, counter);
        this.next();
        page.play(true)
    }
    stop() {
        if (!page.active) {return}
        counter.reset(0);
        this.bar.stop();
        this.cards.remove();
        this.input.remove();
        this.list.render(this.jsonFile);
        page.play(false)
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
            this.input.show.stats.done = counter.hasPrevious();
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
        counter.random = !counter.random;
        this.bar.shuffle(counter.random);
        counter.reset();
        this.input.reset();
        this.next()
    }
    onTextareaChange() {
        exercise.current.input.readInput();
        if (exercise.current.input.done) {exercise.current.playWord(exercise.current.input.success ? 1 : 0)}
    }
    resume() {
        if (page.active) {
            this.input.visible(true)
        }
    }
}

export default Flashcards;