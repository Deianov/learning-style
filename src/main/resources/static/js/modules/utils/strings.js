// const charsRemove = ["\t", "\n", "\r"];
const charsIgnore = [".", ",", ":", ";", "_", "?", "!", "`"];
// const charsSplit = [" "];

const strings = {
    isEmpty(str) {
        return (!str || !str.length);
    },
    isBlank(str) {
        return (!str || !str.trim().length);
    },
    isValid(str) {
        return (typeof str === "string" && str.trim().length);
    },
    clear(text) {
        return (text || "").trim().replace("  ", " ");
    },
    mask(text, char) {
        let len;
        let str = text || "";
        return this.split(str.toLowerCase()).map(w => {
            len = w.length;
            return len > 3 ? (w.substr(0,1) + char.repeat(len - 2) + w.slice(-1)) : len > 2 ? (w.substr(0,1) + char.repeat(len - 1)) : w;
        }).join("");
    },
    split(text) {
        return this.clear(text).split(/([ .,:;_\-?!'`]+)/);
    },
    removeHTML(s) {
        return s.replace(/&/g, "").replace(/</g, "").replace(/"/g, "").replace(/'/g, "").replace(/`/g, "");
    },
    utf8_to_b64( str ) {
        return window.btoa(unescape(encodeURIComponent(str)));
    },
    b64_to_utf8( str ) {
        return decodeURIComponent(escape(window.atob( str )));
    },
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