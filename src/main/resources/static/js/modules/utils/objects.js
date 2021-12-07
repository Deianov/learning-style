
const objects = {
    isObject(obj) {
        return (obj && typeof obj === 'object' && !Array.isArray(obj));
    },
    isArray(obj) {
        return (obj && Array.isArray(obj));
    },
    assign: object_assign,
}

// refactoring of https://www.npmjs.com/package/nested-object-assign
function object_assign (target, ...sources) {
    let s, t;
    for (const source of sources) {
        for (const key of Object.keys(source || {})) {
            s = source[key];
            t = target[key];

            if (objects.isObject(s)) {
                if (!t && objects.isObject(s)) {
                    Object.assign(target, {[key]: {}});
                }
                object_assign(target[key], s);

            } else if (objects.isArray(s)) {
                /** replace with source array (save the references!) */
                target[key] = s.slice();

            } else {
                Object.assign(target, {[key]: s});
            }
        }
    }
    return target;
}

export default objects;