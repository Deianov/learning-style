import CS from "./constants.js";
import strings from "./utils/strings.js";


const inMemoryRepository = (function (){
    const files = {};
    return {
        existsByName(name) {
            return files.hasOwnProperty(name)
        },
        getByName(name) {
            return files[name]
        },
        save(name, obj) {
            files[name] = obj;
            return this.getByName(name)
        },
        remove(name) {
            if (this.existsByName(name)) {
                delete files[name]
            }
        }
    }
}());

const cookie = {
    encode(str) { /*! js-cookie v3.0.1 | MIT */
        return encodeURIComponent(str).replace(/%(2[346B]|5E|60|7C)/g, decodeURIComponent).replace(/[()]/g, escape);
    },
    props(days) {
        const defaultDays = 365; // 365 * 24 * 60 * 60 = 31536000 (1 year)
        const expiresDate = new Date(Date.now() + (days || defaultDays) * 864e5).toUTCString();
        return "; expires=" + expiresDate + "; path=/; SameSite=Lax";
    },
    getItem(key) {
        const arr = (document.cookie || "").split('; ');
        const f = this.encode(key) + "=";
        const str = arr.find(s => s.indexOf(f) === 0);
        return str ? strings.b64_to_utf8(str.substring(f.length)) : null;
    },
    setItem(key, data) {
        const f = this.encode(key) + "=";
        return (document.cookie = ((f + strings.utf8_to_b64(data)) + this.props()));
    }
}

const localRepository = (function (){
    const storage = CS.system.isMobile || !localStorage ? cookie : localStorage;
    return {
        getItem(key) {
            return storage.getItem(key);
        },
        setItem(key, data) {
            storage.setItem(key, data);
        }
    }
}());

const repository = (function () {
    const isStatic = CS.app.isStatic;
    const server = CS.server.json;
    return {
        async getByName(name) {
            // switch between static/api version
            const fun = isStatic ? this.fetchStatic : this.fetchApi;
            return await fun(name);
        },
        // static
        async fetchStatic(fileName) {
            const req = server + (fileName.startsWith("/") ? "" : "/") + fileName + ".json";
            const res = await fetch(req);
            return await res.json();
        },
        // api
        async fetchApi(apiPath) {
            return await sendRequest(apiPath);
        },
    }
}());


class Data {
    /**
     * @param {string} name Relative resource path. ("fileName" || path/filename)
     * @param {boolean} cashable
     * @param {boolean | Function} adaptable  // todo: function is not supported ?
     */
    async getJson(name, cashable, adaptable) {
        let res;
        if (cashable) {
            res = this.getCashable(name)
            if (res) {
                return res;
            }
        }
        res = await repository.getByName(name);
        if(!res) {
            return null;
        }
        if (adaptable) {
            res = this.adapt(res)
        }
        if (cashable) {
            res = inMemoryRepository.save(name, res)
        }
        return res
    }
    async getJsonWithPayload(api, data) {
        return await postData(api, data);
    }
    getCashable(name) {
        if (inMemoryRepository.existsByName(name)) {
            console.debug("cashed: " + name + ".json")
            return inMemoryRepository.getByName(name)
        }
    }
    adapt(json) {
        return dataAdapter(json)
    }
}

/**
 * @param {{}} jsonFile base format from server
 * @returns {{}} result {..., "data":[adapted data], state: {}}
 */
function dataAdapter(jsonFile) {
    const json = jsonFile;
    const adapterId = json.props["adapter"];
    const labels = Object.entries(json.props)
        .filter((en) => en[0].startsWith("label"))
        .sort((a, b) => a[0].localeCompare(b[0]))
        .map((en) => en[1]);
    json.labels = labels;
    json.props.card = parseInt(json.props.card);
    if(!isNaN(adapterId)) {
        if (adapterId === "0") {
            json.data = arrayToMatrix(json.data, labels.length);
        }
        else{
            const adapter = getAdapter(json, (parseInt(adapterId)));
            json.data = json.data.map(row => adapter(row));
        }
    }
    // state
    json.state = {
        counts: Array(json.data.length).fill(0),
        status: false,
        rows: json.data.length,
        row: 0,
        tabs: Array(labels.length).fill(true),
        card: json.props.card,
    }
    json.state.tabs[json.state.card] = false;
    return json
}

/**
 * @param {object} json Server file
 * @param {number} adapterId  json["adapter"]
 * @returns {Function} adapter function
 */
function getAdapter(json, adapterId) {
    const labels = json.labels.map(o => o.toLowerCase());

    /**
     * Json.data adapter
     * @param {Array} data     "[a, b, c ... n]"
     * @returns {Array}        "[[a, b], [c, d] ...]"
     */
    // arrayToMatrix(arr, cols)

    /**
     * Row adapter (not used)
     * @param {Array} arr   current row
     * @returns {Array}     ["a", "b", "c - d", [{"examples":["example1"]}, ..{}]]
     */
    function adapter1 (arr) {
        return [arr[0][labels[0]], arr[1][labels[1]], arr[0][labels[2]] + " - " + arr[1][labels[2]], arr.slice(-1)[0]]
    }

    const adapters = [
        arrayToMatrix,
        adapter1
    ]
    return adapters[adapterId]
}

function arrayToMatrix(arr, cols) {
    const res = [];
    let i = 0;
    for (let index = 0; index < arr.length; index += cols) {
        res[i++] = arr.slice(index, index + cols)
    }
    return res
}

/**
 *
 * server requests
 *
 */

function requestString(path) {
    return CS.server.api + (path.startsWith("/") ? "" : "/") + path
}

async function sendRequest(path, method, body) {
    try {
        const response = await fetch(requestString(path), {
            method: method || "GET",
            headers: body ? { "Content-Type" : "application/json" } : undefined,
            body
        }).then(response => {
            /* todo: redirect/error
            if (response.redirected) {
                window.location.assign(response.url);
            } else {
                return response;
            }
            */
            return response;
        });
        return await response.json();
    } catch (error) {
        return { error: error.message || "Unknown error" };
    }
}

async function postData(path, data) {
    return fetch(requestString(path), {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, *cors, same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "include", // include, *same-origin, omit
        headers: {
            "Content-Type": "application/json",
            "Accept-Language": CS.app.lang
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: "follow", // manual, *follow, error
        referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
        body: JSON.stringify(data) // body data type must match "Content-Type" header
    })
        .then(res => {
            const size = res.headers.get("content-length");
            const type = res.headers.get("content-type");

            // todo: (res.status == 401) -> 401 Unauthorized / 403 Forbidden -> ?
            if (res.redirected) {
                window.location.assign(res.url);
                return;
            }
            return res.text();
        })
        .then((text) => {
            try {
                return JSON.parse(text);
            } catch (e) {
                return text;
            }
        });
}


export {Data, localRepository};