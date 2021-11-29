const getRandomUUID = (a) => (a ? (a ^ ((Math.random() * 16) >> (a / 4))).toString(16) : ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, generateRandomUUID));
const numbers = {
    /** The maximum is exclusive and the minimum is inclusive
     * @param min  (inclusive)
     * @param max  (exclusive)
     * @returns {number}
     */
    getRandomInt(min, max) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min) + min);
    },
    /** Generate a Random UUID: 128-bit value; */
    getRandomUUID
}


export {numbers};