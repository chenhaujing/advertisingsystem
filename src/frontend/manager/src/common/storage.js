var host = window.location.hostname + "_v11";
var keys = {
    AREA_DATA: host + "_001"
}
const unavaKey = generateCallback('无效key!');
const successCallbackObj = generateCallback('', 1);
function generateCallback(message, code = -1, data) {
    return {
        code: code,
        data: data,
        message: message
    }
}
var utils = {
    keys: keys,
    getDataByGlobay(key) {
        return new Promise((resolve, reject) => {
            if (!key) {
                reject(unavaKey);
                return;
            }
            try {
                key = key.toString();
                var result = JSON.parse(localStorage.getItem(key));
                if (result && result.data && result.expireTime) {
                    var time = new Date().getTime();
                    if (time > result.data.expireTime) {
                        reject(generateCallback('数据已过期', -1, null));
                        return;
                    }
                    resolve(result);
                } else {
                    reject(generateCallback('empty', -1, null));
                }

            } catch (e) {
                console.log(e);
                reject(generateCallback(e));
            }
        })
    },
    saveDataByGlobay(key, data, expireTime = 0) {
        return new Promise((resolve, reject) => {
            if (!key) {
                reject(unavaKey);
                return;
            }
            try {
                key = key.toString();
                localStorage.setItem(key, JSON.stringify({
                    data: data,
                    expireTime: expireTime
                }))
                resolve(successCallbackObj);
            } catch (e) {
                reject(generateCallback(e));
            }
        })

    }
}
export default utils;