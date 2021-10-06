import myApi from "./myapi.js";

const path = "./json";
const contentType = ".json"
const isStatic = false;
const files = {};
const version = "06.10.2021";

// development state
document.getElementsByClassName("version")[0].textContent = `state: development, JS.version: ${version} (${isStatic ? "static":"api"})`;

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
        const fun = isStatic ? this.fetchStatic : this.fetchApi;
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
        return await myApi.sendRequest(apiPath)
    }
    static requestString(value) {
        // replace(/\\/g, '/')
        return path + (value.startsWith("/") ? "" : "/") + value + contentType
    }
}


class Service {
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
        return await myApi.postData(api, data);
    }
    getCashable(name) {
        if (localRepository.existsByName(name)) {
            console.debug("use cashed: " + name)
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

/*
async saveToLocal(name, callback) {
    const req = Repository.requestString(name);
    await fetch(req)
        .then(response => {
            if (response.ok) {
                return response;
            }
            // const error = new Error(response.statusText);
            // error.response = response;
            // return Promise.reject(error);
            return Promise.reject("Repository: error(404):" + name);
        })
        .then(response => response.json())
        .then(data => {
            console.debug("Repository fetch:")
            console.debug(data);
            localRepository.save(name, callback ? callback(data) : data)
        });
}
*/

const localRepository = new LocalRepository();
const repository = new Repository();
const service = new Service();
export default service;