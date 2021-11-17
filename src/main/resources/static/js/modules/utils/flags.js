class Flags {
    static set(flags, bitmask) {
        return (flags | bitmask)
    }
    static clear(flags, bitmask) {
        return (flags & ~bitmask)
    }
    static toggle(flags, bitmask) {
        return (flags ^ bitmask)
    }
    static isTrue(flags, bitmask) {
        return ((flags & bitmask) > 0)
    }
    static isFalse(flags, bitmask) {
        return ((flags & bitmask) === 0)
    }
    /**
     * Index to bitmask: 0, 1, 2, 3 ... 30  =>  1, 2, 4, 8, ... 1073741824
     * 
     * @param {number} index (max: ~30)
     * @returns {number} bits
     */
    static toBits(index) {
        return (1 << index)
    }
    /**
     * Array of booleans to flags: [true, false, true] => 5
     * 
     * @param {boolean[]} arr 
     * @returns {number} flags
     */
    static toNumber(arr) {
        return arr.reduce((acc, c, i) => {
            return acc + (c ? (1 << i) : 0)
        }, 0)
    }
    /**
     * Bits to index
     * 
     * base 2: Math.log2(bits) 
     * @returns: 0 for bits=0
     */
    static toIndex(bits) {
        let log = 0;
        if( ( bits & 0xffff0000 ) !== 0 ) { bits >>>= 16; log = 16 }
        if( bits >= 256 ) { bits >>>= 8; log += 8 }
        if( bits >= 16  ) { bits >>>= 4; log += 4 }
        if( bits >= 4   ) { bits >>>= 2; log += 2 }
        return (log + ( bits >>> 1 ))
    }
    /**
     * Flags to array of booleans: (5, 3) => [true, false, true]
     * 
     * @param {number} flags 
     * @param {number} length 
     * @returns {boolean[]} 
     */
    static toArray(flags, length) {
        return Array.from({length}, (v, i) => Flags.isTrue(flags, Flags.toBits(i)))
    }
    /**
     * Set flags by length 
     * 
     * @param {number} length 
     * @returns {number} flags
     */
    static byLength(length) {
        let flags = 0;
        for (let i = 0; i < length; i++) {
            flags = (flags | (1 << i))
        }
        return flags
    }
}

class ArrayOfFlags {
    constructor() {
        this.number = 0;
        this.length = 0;
    }
    get length() {
        return this._length
    }
    set length(length) {
        this._length = length;
        this.number = Flags.byLength(length);
    }
    getIndex(index) {
        return Flags.isTrue(this.number, Flags.toBits(index))
    }
    setIndex(index, flag) {
        this.number = flag ? Flags.set(this.number, Flags.toBits(index)) : Flags.clear(this.number, Flags.toBits(index))
    }
    toggle(index) {
        this.number = Flags.toggle(this.number, Flags.toBits(index))
    }
    get array() {
        return Flags.toArray(this.number, this.length)
    }
    set array(arr) {
        this._length = arr.length;
        this.number = Flags.toNumber(arr)
    }
}

export {Flags, ArrayOfFlags}