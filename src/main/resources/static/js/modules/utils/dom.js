const dom = {
    /**
     * @param {string | HTMLElement} v   -  id | element
     * @returns {HTMLElement}
     */
    get(v) {
        return (typeof v === "string") ? document.getElementById(v) : document.body.contains(v) ? v : null;
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
            if (k === "textContent" || k === "className" || k === "value") {
                e[k] = v
            } else {
                e.setAttribute(k, v);
            }
        }
    },
    /**
     * @param {string} tag             - nameName
     * @param {HTMLElement} parent     - parent
     * @param {string | object} opt    - className or object with options (key, value).
     * @returns {HTMLElement}          - element
     */
    element(tag, parent, opt) {
        const e = document.createElement(tag)
        parent.appendChild(e)
        if (opt) {this.setOptions(e, opt)}
        return e
    },
    /**
     * @param {string} text         - text
     * @param {HTMLElement} parent  - parent
     * @returns {Text}
     */
    node(text, parent) {
        const node = document.createTextNode(text)
        parent.appendChild(node)
        return node
    },
    /**
     * @param {string} tag          - tag name
     * @param {HTMLElement} parent  - parent
     * @param {string} text         - text
     * @param {string | object} opt - className or object with options (key, value).
     * @returns {HTMLElement}
     */
    text(tag, parent, text, opt) {
        const e = this.element(tag, parent, 0)
        e.textContent = text;
        if (opt) {this.setOptions(e, opt)}
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
    },
    /** Use svg from template in html
     *
     * @param {HTMLElement} parent  - parent
     * @param {string} href         - id of used svg
     * @param {string} className
     * @param {string} w            - width
     * @param {string} h            - height
     * @param {string} role         - img, button
     * @returns {SVGSVGElement}
     *
     *  or insert from file with:
     *  const options = {class: "info, src:"./assets/images/info.svg", alt: "info", width: 20, height: 20}
     *  dom.element("img", parent, options)
     *
     */
    svgUse(parent, href, className, w, h, role = 'img') {
        const NS = 'http://www.w3.org/2000/svg';
        const s = document.createElementNS(NS, 'svg');
        const u = document.createElementNS(NS, 'use');
        if(className) {
            s.setAttributeNS(null, 'class', className);
        }
        s.setAttributeNS(null, 'width', w);
        s.setAttributeNS(null, 'height', (h || w));
        s.setAttributeNS(null, 'role', role);
        u.setAttributeNS(null, 'href', href);
        s.appendChild(u);
        parent.appendChild(s);
        return s;
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