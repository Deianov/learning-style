import constants from "../services/constants.js";


// const serverUrl = "http://192.168.0.52:9999/geist";
const serverUrl = "./api";

function requestString(api) {
    return serverUrl + (api.startsWith("/") ? "" : "/") + api
}


async function sendRequest(api, method, body) {
    try {
        const response = await fetch(requestString(api), {
            method: method || "GET",
            headers: body ? { "Content-Type" : "application/json" } : undefined,
            body
        });
        return await response.json();
    } catch (error) {
        return { error: error.message || "Unknown error" };
    }
}


async function postData(api, data) {
    return fetch(requestString(api), {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, *cors, same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "include", // include, *same-origin, omit
        headers: {
            "Content-Type": "application/json",
            "Accept-Language": constants.lang
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: "follow", // manual, *follow, error
        referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
        body: JSON.stringify(data) // body data type must match "Content-Type" header
    }).then(response => {
        if (!response.ok) {
            throw new Error("HTTP error, status = " + response.status);
        }
        return response.json();
    }).catch(err => {
        return err.message
    });
}


export default {sendRequest, postData, requestString}


