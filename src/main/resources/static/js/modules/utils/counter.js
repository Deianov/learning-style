/*
 <->[numbers] (<-- 0 || random )
    [stack]<->
 <--[waitingNumbers]<-- (--> skip)
 <--[waitingCounts ]<-- (--> skip)
 */
class Counter {
    constructor(minValue = 0, maxValue = false, isRandom = false) {
        this.min = minValue;
        this.max = maxValue;
        this.stack = [];
        this.numbers = [];
        this.waitingNumbers = [];
        this.waitingCounts = [];
        this.waitingDefault = 5;
        this.random = isRandom;
    }
    get next() {
        if (this.isEmpty()) {
            this.reset()
        }
        if (this.hasNext()) {
            if (this.isNextWaiting()){
                return this.push(this.waitingNumbers.shift())
            }
            return this.push(this.numbers.splice(this.random ? this.randomIndex() : 0, 1)[0])
        }
        return false
    }
    get previous() {
        if (this.hasPrevious()) {
            this.numbers.unshift(this.stack.pop());
            return this.stack.slice(-1)[0]
        }
        return false
    }
    /** 
     * @param {integer} waiting (turns)
     * Move to the waitings list, and set waitings turns == waiting || waitingDefault.
     * Only after next in error condition. 
     * @returns {number}
     */
    repeatLast(waiting) {
        if (this.stack.length > 0) {
            this.waitingNumbers.push(this.stack.pop());
            this.waitingCounts.push(waiting ? waiting : this.waitingDefault);
            return this.waitingNumbers.slice(-1)[0]
        }
        return false
    }
    isNextWaiting() {
        if (this.waitingNumbers.length === 0) {
            return false
        } else {
            this.waitingCounts[0]--;
        }
        if (this.waitingCounts[0] < 1 || this.numbers.length === 0) {
            this.waitingCounts.shift();
            return true
        }
        return false
    }
    skipLastWaiting() {
        if (this.waitingNumbers.length > 0) {
            this.stack.push(this.waitingNumbers.pop());
            this.waitingCounts.pop();
        }
        return this
    }
    /** @returns {index} Between 0 and numbers.length (exclusive)
     */
    randomIndex() {
        return Math.floor(Math.random() * this.numbers.length)
    }
    hasNext() {
        return this.isValid() && this.numbers.length > 0 || this.waitingNumbers.length > 0
    }
    hasPrevious() {
        return this.stack.length
    }
    isValid() {
        return typeof this.max === 'number' && this.min > -1 && this.max > this.min
    }
    isEmpty() {
        return this.numbers.length === 0 && this.waitingNumbers.length === 0 && this.stack.length === 0
    }
    isDone() {
        return this.numbers.length === 0 && this.waitingNumbers.length === 0 && this.stack.length > 0
    }
    push(number) {
        if (typeof number === 'number' && !this.stack.includes(number)) {
            this.stack.push(number);
            return number
        }
        return false
    }
    reset(maxExclusive) {
        if (typeof maxExclusive === 'number') {
            this.max = maxExclusive
        }
        this.stack.length = 0;
        this.numbers.length = 0;
        this.waitingNumbers.length = 0;
        this.waitingCounts.length = 0;
        if (this.isValid()) {
            let i = this.min;
            for (let c = this.min; c < this.max; c++) {
               this.numbers[i++] = c
            }
        }
    }
}

class SimpleCounter {
    constructor(num) {
        this.start = num;
        this.number = num;
    }
    get next() {
        return this.number++
    }
    reset() {
        this.number = this.start;
    }
}

export {SimpleCounter};
export default Counter;