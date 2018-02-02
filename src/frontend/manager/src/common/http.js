/**
 * Created by caizepeng on 17/4/5.
 */
import axios from 'axios';
import Qs from 'qs';
import api from './Api';

var instance = axios.create({
    baseURL: api.baseUrl,
    headers: { 'Content-Type': 'application/json' }
});
const API_CODE = {
    SUCCESS: 1,
    NOTLOGIN: 401 // 当前未登录
}
const GET = 1;
const POST = 2;

var prevParams={};
var prevUrl='';
// 非正常错误提示
var errorReject = {
    message: '请求失败，请稍后再试'
}
class Http { }
Http.install = function (Vue, options) {
    function generateResultObj(msg) {
        return {
            message: msg
        }
    }
    function handlerError(error, reject) {
        prevParams={}
        prevUrl=''
        if (error) {
            if (error.response && error.response.status == 404) {
                reject(generateResultObj('无效的URL连接'))
                return;
            }
            if (error.response && error.response.status == 500) {
                options.router.push({ name: 'error500' });
                reject(generateResultObj('请求失败，请稍后再试'))
                return;
            }
            if (error.message.indexOf("timeout") != -1) {

                reject(generateResultObj('连接超时，请稍后再试'))
                return;
            }
            if (error.message.indexOf("Network Error") != -1) {
                reject(generateResultObj('请求失败，请稍后再试'))
                return;
            }
        }
        if (error.message.indexOf("Network Error") != -1) {
            reject(generateResultObj('操作失败，请稍后再试'))
            return;
        }
    }
    function handlerSuccess(result, resolve, reject, notNorm) {
        prevParams={}
        prevUrl=''
        if (!result) {
            reject(errorReject)
            return;
        }
        // 如果不是标准接口，直接将数据返回
        if (notNorm) {
            resolve(result)
            return;
        }
        // 请求成功
        if (result.code === API_CODE.SUCCESS) {
            resolve(result);
            return;
        } else {
            switch (result.code) {
                case API_CODE.NOTLOGIN:
                    options.router.push({ name: 'login' })
                    reject(result);
                    return;
            }
        }
        if (result.message && result.message.length > 40) {
            options.router.push({ name: 'error500' })
            return;
        }
        reject(result);
        return;
    }
    function closeLoading() {
        Vue.prototype.$loading.close()
    }
    Vue.prototype.$api = api;

    function request(methods, url, params, options = {}) {
        if(Vue.prototype.$formVerify.isEqual(params,prevParams)&&Vue.prototype.$formVerify.isEqual(url,prevUrl)){
            return
        }
        prevParams=params
        prevUrl=url

        if (process.env.NODE_ENV === 'development') {
            if(url.indexOf("?")==-1){
                url = url + '?mockUser=15013429775';
            }else{
                url = url + '&mockUser=15013429775';
            }
        }
        if (options.urlParams) {
            var arr = Object.keys(options.urlParams);
            for (let i = 0; i < arr.length; i++) {
                url = url.replace('{' + arr[i] + '}', options.urlParams[arr[i]]);
            }
        }
        switch (methods) {
            case GET:
                return get(url, params, options);
            case POST:
                return post(url, params, options);
        }
    }
    function get(url, params, options) {
        return new Promise((resolve, reject) => {
            options.params = params;
            instance.get(
                url,
                options.params,
                options.config
            ).then((result) => {
                handlerSuccess(result.data, resolve, reject, options.notNorm)
            }).catch((e) => {
                handlerError(e, reject);
            })
        })
    }
    function submit(url, params, options = {}) {
        options.config = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: [function (data) {
                // 这里可以在发送请求之前对请求数据做处理，比如form-data格式化等，这里可以使用开头引入的Qs（这个模块在安装axios的时候就已经安装了，不需要另外安装）
                data = Qs.stringify(data);
                return data;
            }]
        }
        return post(url, params, options)
    }
    function upload(url, params, options = {}) {
        options.config = {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        return request(POST, url, params, options)
    }
    function post(url, params, options = {}) {
        options.loading && Vue.prototype.$loading("加载中...")
        return new Promise((resolve, reject) => {
            instance.post(
                url,
                params,
                options.config).then((result) => {
                    closeLoading();
                    handlerSuccess(result.data, resolve, reject, options.notNorm);
                }).catch((e) => {
                    closeLoading();
                    handlerError(e, reject);
                });
        })

    }

    Vue.prototype.$get = function (url, urlParams, params, options = {}) {
        options.urlParams = urlParams;
        return request(GET, url, params, options);
    }
    /**
     * options: 
     *      loading ： 是否显示loading
     *      notNorm : true，不是标准接口
     *      config  : http请求的配置信息
     */
    Vue.prototype.$post = function (url, params = {}, options = {}, urlParams = {}) {
        options.urlParams = urlParams;
        return request(POST, url, params, options);
    }
    Vue.prototype.$postForm = function (url, params = {}, options = {}, urlParams = {}) {
        options.urlParams = urlParams;
        options.config = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: [function (data) {
                // 这里可以在发送请求之前对请求数据做处理，比如form-data格式化等，这里可以使用开头引入的Qs（这个模块在安装axios的时候就已经安装了，不需要另外安装）
                data = Qs.stringify(data);
                return data;
            }]
        }
        return request(POST, url, params, options);
    }
    // 目前只用于登录
    Vue.prototype.$submit = function (url, params, options = {}) {
        return submit(url, params, options);
    }
    //  文件上传
    Vue.prototype.$upload = function (url, params, options = {}, urlParams = {}) {
        options.urlParams = urlParams;
        return upload(url, params, options)
    }
}
export default Http;