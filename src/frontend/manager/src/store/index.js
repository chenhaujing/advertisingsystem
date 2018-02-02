import Vue from 'vue'
import Vuex from 'vuex'
import Permission from 'common/permission'
import createLogger from 'vuex/dist/logger'
Vue.use(Vuex);
Vue.prototype.$m = Permission.module;
Vue.prototype.$mutations = {
    'BREADCRUMD_UPDATE': 'updateBreadcrumd',
    'BREADCRUMD_CLEAR': 'clearBreadcrumd'
}
const BREADCRUMD_KEY = 'BREADCRUMD_KEY'
const BREADCRUMD_MAP_KEY = 'BREADCRUMD_MAP_KEY'
Vue.prototype.$p = Permission.p;
Vue.prototype.$roleType = Permission.roleType;
var tempList = [];
const debug = process.env.NODE_ENV  === 'development';
export default new Vuex.Store({
    state: {
        map: {},
        leveList: [],
        roleType: null,
        permission: {},
        getRolePermission(m) {
            return this.permission[m];
        },
        check(m, p) {
            if (!m || !p) {
                return false;
            }
            if (!this.permission[m]) {
                return false;
            }
            return this.permission[m][p];
        }
    },
    mutations: {
        clearBreadcrumd: (state) => {
            state.map = {};
            state.leveList = [];
            sessionStorage.removeItem(BREADCRUMD_KEY);
            sessionStorage.removeItem(BREADCRUMD_MAP_KEY);
        },
        resetBreadcrumd: (state, palyload) => {
            state.leveList = palyload.data;
            state.map = palyload.map;
        },
        updateBreadcrumd: (state, palyload) => {
            var to = palyload.data;
            if (!to.meta || !to.meta.title) {
                return;
            }
            if (to && to.path == '/login') {
                return;
            }
            if (!state.map[to.name]) {

                var params = '?';
                var saveObj = {
                    path: '',
                    to: null
                }
                if (to.queryStr) {
                    to.query = JSON.parse(to.queryStr);
                }
                for (let k in to.query) {
                    params += k + '=' + to.query[k] + '&';
                }
                if (params != '?' && to.path) {
                    saveObj.path = to.path + params;
                } else {
                    saveObj.path = to.path
                }
                var temp = {};

                temp.meta = to.meta;
                temp.params = to.params;
                temp.path = to.path;
                temp.query = to.query;
                temp.name = to.name;
                temp.queryStr = JSON.stringify(to.query);
                saveObj.to = temp;
                state.map[to.name] = true;
                state.leveList.push(saveObj);
                sessionStorage.setItem(BREADCRUMD_KEY, JSON.stringify(state.leveList));
                sessionStorage.setItem(BREADCRUMD_MAP_KEY, JSON.stringify(state.map));
            } else {
                let index = -1;
                for (let i = 0; i < state.leveList.length; i++) {
                    if (state.leveList[i].to.name == to.name) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    for (let i = index + 1; i < state.leveList.length; i++) {
                        state.map[state.leveList[i].to.name] = false;
                    }
                    state.leveList = state.leveList.slice(0, index + 1);
                }
                sessionStorage.setItem(BREADCRUMD_MAP_KEY, JSON.stringify(state.map));
                sessionStorage.setItem(BREADCRUMD_KEY, JSON.stringify(state.leveList));
            }
        },
        setRoleType: (state, payload) => {
            state.permission = Permission.role[payload.roleType];
            return state.roleType = payload.roleType
        }
    },
    plugins: debug ? [createLogger()] : []
})