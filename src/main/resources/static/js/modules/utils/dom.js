function element(name, parent, options) {
    const e = document.createElement(name)
    parent.appendChild(e)
    if (options) {setOptions(e, options)}
    return e
}

function node(text, parent) {
    const node = document.createTextNode(text)
    parent.appendChild(node)
    return node
}

function text(name, parent, text, options) {
    const e = element(name, parent)
    e.textContent = text;
    if (options) {setOptions(e, options)}
    return e
}

function removeAll(parent) {
    if (parent) {
        parent.innerHTML = "";
    }
    // while (parent.firstChild) {
    //     parent.removeChild(parent.firstChild);
    // }
}

function remove(e) {
    if (e) {
        const parent = e.parentNode;
        if (parent) {
            parent.removeChild(e)
        }
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
*/

/**
 * @param {string | HTMLElement} value id | element
 */
function get(value) {
    return (typeof value === "string") ? document.getElementById(value) : value
}

/**
 * @param {HTMLElement} e 
 * @param {string | object} options String (className) or object with options.
 */
function setOptions (e, options) {
    if (typeof options === "string") {
        e.setAttribute("class", options);
        return
    }
    for (const [key, value] of Object.entries(options)) {
        if (key === "class" || key === "key") {
            e.setAttribute(key, value);
        } else {
            e[key] = value
        }
    }
}

const dom = {element, text, node, removeAll, remove, get};
export default dom;