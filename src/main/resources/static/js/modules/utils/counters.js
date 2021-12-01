/*
 <->[numbers]
    [stack]<->
 <--[waitingNumbers]<--     (--> skip, previous)
 <--[waitingCounts ]<--     (--> skip, previous)
 */
class ScopeCounter {
    /**
     * @param {number} min - inclusive
     * @param {number|undefined} max - exclusive
     * @param {boolean} shuffle
     */
    constructor(min = 0, max, shuffle) {
        this.min = min;
        this.max = max;
        this.stack = [];
        this.numbers = [];
        this.waitings = [];
        this.counts = [];
        this.DEFAULT_WAITING = 5;
        this.shuffle = shuffle;
        this.state = {};
    }
    getValue() {
        return this.isNumber(this.state.value) ? this.state.value : this.stack.slice(-1)[0];
    }
    next() {
        if (this.isEmpty()) {
            this.reset();
        }
        if (this.hasNext()) {
            if (this.isWaiting()){
                this.state.value = this.waitings.shift();
                return this;
            } else {
                this.stack.push(this.numbers.splice(this.shuffle ? this.randomIndex() : 0, 1)[0])
            }
        }
        this.resetState();
        return this;
    }
    previous() {
        if (this.hasPrevious()) {
            // save the current value if is last in stack !
            const n = this.getValue();
            // shift (if is not last)
            if (this.stack.length > 1 && !this.isNumber(this.state.value)) {
                this.stack.pop()
                this.numbers.unshift(n);
            }
            // remove current value from waiting list
            this.removeFromWaiting(n);
            // remove previous value from waiting list
            this.removeFromWaiting(this.stack.slice(-1)[0])
        }
        this.resetState();
        return this;
    }
    /** Remove value from waiting list. */
    removeFromWaiting(value) {
        if (this.waitings.length) {
            const i = this.waitings.indexOf(value);
            if (i > -1) {
                this.waitings.splice(i, 1);
                this.counts.splice(i, 1);
            }
        }
    }
    /** Repeats and adds the value to the waiting list. */
    repeat(waiting) {
        if (this.hasPrevious()) {
            const n = this.getValue();

            if (this.waitings.includes(n)) {
                this.counts[this.waitings.indexOf(n)] = waiting || this.DEFAULT_WAITING;
            } else {
                this.waitings.push(n);
                this.counts.push(waiting || this.DEFAULT_WAITING);
            }
        }
        this.state.repeat = true;
        return this;
    }
    getPreviousCount() {
        return this.stack.length - this.waitings.length;
    }
    isWaiting() {
        if (this.waitings.length) {
            this.counts[0]--;
        } else {
            return;
        }
        if (this.counts[0] < 1 || !this.numbers.length) {
            this.counts.shift();
            return true;
        }
    }
    skip() {
        // remove last value from waiting list
        if (this.state.repeat) {
            this.removeFromWaiting(this.waitings.slice(-1)[0]);
        }
        return this.next();
    }
    /** @returns {number} Between 0 and numbers.length (exclusive) */
    randomIndex() {
        return Math.floor(Math.random() * this.numbers.length);
    }
    hasNext() {
        return this.numbers.length || this.waitings.length;
    }
    hasPrevious() {
        return this.stack.length;
    }
    isNumber(v) {
        return (typeof v === 'number');
    }
    isValid() {
        return (this.isNumber(this.max) && (this.min > -1) && (this.max > this.min));
    }
    isEmpty() {
        return !this.numbers.length && !this.waitings.length && !this.stack.length;
    }
    isDone() {
        return !this.numbers.length && !this.waitings.length && this.stack.length;
    }
    resetState() {
        for(const key of Object.keys(this.state)) {
            this.state[key] = null;
        }
    }
    reset(maxExclusive) {
        this.max = this.isNumber(maxExclusive) ? maxExclusive : this.max;
        this.stack.length = 0;
        this.numbers.length = 0;
        this.waitings.length = 0;
        this.counts.length = 0;
        this.resetState();
        if (this.isValid()) {
            let i = this.min;
            for (let c = this.min; c < this.max; c++) {
               this.numbers[i++] = c;
            }
        }
    }
}

function SimpleCounter(start = 0) {
    const min = start;
    let n = start;
    return {
        value() {
            return n;
        },
        next() {
            return ++n;
        },
        back() {
            n = n > min ? (n - 1) : min;
            return n;
        },
        reset() {
            n = min;
        }
    }
}

/**  ScopeCounter TESTS ***************************************************
  *
  *
    function asserts (value, fun) {
        console.log(value ? true : null)
        if(fun) {
            console.log(JSON.stringify(fun))
        }
    }

    const counter = new ScopeCounter(0, 3);

    // next, previous
    asserts(counter.isEmpty())
    counter.previous() // miss
    counter.next()
    asserts(!counter.isEmpty())
    counter.next()
    counter.next()
    counter.previous()
    counter.next()
    counter.next() // miss
    asserts(counter.isDone())
    asserts(!counter.isEmpty())
    asserts(true, counter)

    // waitings
    counter.reset(6);
    counter.next() // 0
    counter.next() // 1
    counter.repeat()
    counter.repeat() // 1, 1
    asserts(counter.getValue() === 1)
    asserts(counter.state.repeat)
    counter.next()
    counter.next()
    counter.next()
    counter.next() // 5
    counter.next() // 1
    asserts(counter.getValue() === 1)
    counter.next()
    asserts(counter.getValue() === 5)
    asserts(counter.isDone)
    asserts(true, counter)

    // repeat + previous - remove waiting
    counter.reset(3);
    counter.repeat() // miss
    asserts(counter.getValue() === undefined)
    counter.next()
    counter.repeat()
    asserts(counter.getValue() === 0)
    asserts(counter.stack[0] === 0)
    asserts(counter.waitings[0] === 0)
    counter.next()
    asserts(counter.getValue() === 1)
    counter.previous()
    asserts(counter.getValue() === 0)
    asserts(counter.waitings.length === 0)
    asserts(true, counter)
*/

export {SimpleCounter, ScopeCounter}