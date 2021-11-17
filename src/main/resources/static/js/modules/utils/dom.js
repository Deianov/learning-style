const dom = {
    /**
     * @param {string | HTMLElement} v   -  id | element
     * @returns {HTMLElement}
     */
    get(v) {
        return (typeof v === "string") ? document.getElementById(v) : v
    },
    /**
     * @param {HTMLElement} e       - element
     * @param {string | object} o   - className or object with options (key, value).
     */
    setOptions (e, o) {
        if (typeof o === "string") {
            e.setAttribute("class", o);
            return
        }
        for (const [k, v] of Object.entries(o)) {
            if (k === "class" || k === "key") {
                e.setAttribute(k, v);
            } else {
                e[k] = v
            }
        }
    },
    /**
     * @param {string} n            - name
     * @param {HTMLElement} p       - parent
     * @param {string | object} o   - className or object with options (key, value).
     * @returns {HTMLElement}       - element
     */
    element(n, p, o) {
        const e = document.createElement(n)
        p.appendChild(e)
        if (o) {this.setOptions(e, o)}
        return e
    },
    /**
     * @param {string} t            - text
     * @param {HTMLElement} p       - parent
     * @returns {Text}
     */
    node(t, p) {
        const node = document.createTextNode(t)
        p.appendChild(node)
        return node
    },
    /**
     * @param {string} n            - name
     * @param {HTMLElement} p       - parent
     * @param {string} t            - text
     * @param {string | object} o   - className or object with options (key, value).
     * @returns {HTMLElement}
     */
    text(n, p, t, o) {
        const e = this.element(n, p)
        e.textContent = t;
        if (o) {this.setOptions(e, o)}
        return e
    },
    /**
     * @param {HTMLElement} p       - parent
     */
    removeAll(p) {
        if (p) { p.innerHTML = "" }
    },
    /**
     * @param {HTMLElement} e       - element
     */
    remove(e) {
        return e ? e.parentNode ? e.parentNode.removeChild(e) : 0 : 0
    }
}

/*
function updateText(id, textOrNode) {
    const e = document.getElementById(id);
    e.textContent = ""; // Removes all children
    e.append(textOrNode);
}
function update(id, callback, arg) {
    const e = document.getElementById(id);
    if (e && callback) {
        callback(e, arg)
    }
}
function removeAll(parent) {
    while (parent.firstChild) {
         parent.removeChild(parent.firstChild);
    }
}
*/

// const dom = {element, text, node, removeAll, remove, get};
export default dom;