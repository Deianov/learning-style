// const charsRemove = ["\t", "\n", "\r"];
const charsIgnore = [".", ",", ":", ";", "_", "?", "!", "`"];
// const charsSplit = [" "];

const strings = {
    clear(text) {
        return (text || '').trim().replace('  ', ' ')
    },
    mask(text, char) {
        let len;
        let str = text || '';
        return this.split(str.toLowerCase()).map(w => {
            len = w.length;
            return len > 3 ? w.substr(0,1) + char.repeat(len - 2) + w.slice(-1) :
                len > 2 ? w.substr(0,1) + char.repeat(len - 1) :
                    len > 1 ? char.repeat(len) : w;
        }).join('')
    },
    split(text) {
        const arr = Array.from(this.clear(text));
        const result = [];
        let temp = '';
        let c;
        for (let index = 0; index < arr.length; index++) {
            c = arr[index];
            if (charsIgnore.includes(c)) {
                result.push(c)
            } else if (c === ' ' || c === "-") {
                if (temp) {
                    result.push(temp);
                    temp = ''
                }
                result.push(c)
            } else {
                temp += c
            }
        }
        if (temp) {result.push(temp)}
        return result;
    }
}

/*
function textForCompare(text) {
    return (text || '').trim().replace('  ', ' ').replace(/[.,!?]/g,"")
}

function textClearLines(str) {
    str = str.replace(/\s{2,}/g, ' ');
    str = str.replace(/\t/g, ' ');
    str = str.toString().trim().replace(/(\r\n|\n|\r)/g,"");
    return str
}

todo: nextChar, hasNext, toMask
class Stringer {
    constructor(string, length) {
        this.innerString = string;
        this.length = 0;
        this.increase(length);
    }

    increase(length) {
        this.innerLength += length;
    }

    decrease(length) {
        this.innerLength -= length;
    }

    get innerLength() {
        return this.length
    }

    set innerLength(value) {
        this.length = value < 0 ? 0 : value;
    }

    toString() {
        return this.length < this.innerString.length ? this.innerString.substring(0, this.length) + '...' : this.innerString;
    }
}
*/
export default strings;