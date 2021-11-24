import CS from "./constants.js";

/** todo: change inmemory with LocalStorage */
const files = {};

// development state
document.getElementsByClassName("version")[0].textContent = `state: ${CS.app.state}, version: ${CS.app.version} (${CS.app.isStatic ? "static":"api"})`;

class LocalRepository {
    existsByName(name) {
        return files.hasOwnProperty(name)
    }
    getByName(name) {
        return files[name]
    }
    save(name, obj) {
        files[name] = obj;
        return this.getByName(name)
    }
    remove(name) {
        if (this.existsByName(name)) {
            delete files[name]
        }
    }
}

class Repository {

    // switch between static/api version
    async getByName(name) {
        const fun = CS.app.isStatic ? this.fetchStatic : this.fetchApi;
        return await fun(name)
    }
    // static
    async fetchStatic(fileName) {
        const req = Repository.requestString(fileName);
        const res = await fetch(req);
        return await res.json()
    }
    // api
    async fetchApi(apiPath) {
        return await sendRequest(apiPath)
    }
    static requestString(value) {
        // replace(/\\/g, '/')
        return CS.server.json + (value.startsWith("/") ? "" : "/") + value + ".json"
    }
}


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
                return res
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
            res = localRepository.save(name, res)
        }
        // console.log(files);
        return res
    }
    async getJsonWithPayload(api, data) {
        return await postData(api, data);
    }
    getCashable(name) {
        if (localRepository.existsByName(name)) {
            console.debug("cashed: " + name + ".json")
            return localRepository.getByName(name)
        }
    }
    adapt(json) {
        return dataAdapter(json)
    }
}

/**
 * @param {object} json base format from server
 * @returns {object} result {"json":json, "data":[adapted data], ...meta}
 */
function dataAdapter(json) {
    const result = {};
    const adapterIndex = json.options["adapter"];
    const sourceData = json["dictionaries"];
    result.json = json;

    if (isNaN(adapterIndex)) {
        result.data = sourceData.slice();
    }
    else if (adapterIndex === 0) {
        result.data = arrayToMatrix(sourceData, json.labels.length)
    }
    else {
        const adapter = getAdapter(json, (parseInt(adapterIndex)));
        result.data = sourceData.map(row => adapter(row))
    }

    // append metadata
    result.counts = Array(result.data.length).fill(0);
    result.save = {
        "status":false,
        "rows":result.data.length,
        "row":0,
        "tabs":Array(json.labels.length).fill(true),
        "card":json.options.card,
    }
    result.save.tabs[result.save.card] = false;
    return result
}

/**
 * @param {object} json Server file
 * @param {number} adapterIndex  json["adapter"]
 * @returns {Function} adapter function
 */
function getAdapter(json, adapterIndex) {
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
    return adapters[adapterIndex]  
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
    }).then(response => {
        if (!response.ok) {
            throw new Error("HTTP error, status = " + response.status);
        }
        // todo: (response.status == 401) 401 Unauthorized / 403 Forbidden -> ?
        if (response.redirected) {
            window.location.assign(response.url);
            return;
        }
        return response.json();
    }).catch(err => {
        return err.message
    });
}

const localRepository = new LocalRepository();
const repository = new Repository();
export {Data};