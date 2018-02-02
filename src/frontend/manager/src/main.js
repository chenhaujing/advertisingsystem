// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import routerInfo from './router'
import 'iview/dist/styles/iview.css';
import 'assets/css/common.css'
import Router from 'vue-router'
import Http from 'common/http';
import Toast from 'components/toast';
import 'assets/fonts/iconfont.css';
import CommPlug from 'plug/commPlug';
import VerifyPlug from 'plug/verifyPlug';
import format from 'plug/format';
import DatePlug from 'plug/DatePlug';
import Directive from 'plug/directive';
import Message from 'iview/src/components/message';
import iLoadingBar from 'iview/src/components/loading-bar';
import md5 from 'js-md5';
import StorageUtils from 'common/storage';
import iModal from 'iview/src/components/modal';
import iNotice from 'iview/src/components/notice';
import store from './store';
require('es6-promise').polyfill();
Vue.config.productionTip = false;
Vue.prototype.$Md5 = md5;
Vue.prototype.$Message = Message;
Vue.prototype.$Modal = iModal;
Vue.prototype.$StorageUtils = StorageUtils;
var pathname = null;

Vue.prototype.$Notice = iNotice;
var router = new Router({
  routes: routerInfo.routes,
  scrollBehavior(to, from, savedPosition) {
    // return 期望滚动到哪个的位置
    return { x: 0, y: 0 };
  }
});
Vue.use(Toast);
// Vue.use(iView);

Vue.use(VerifyPlug);
Vue.use(DatePlug);
Vue.use(Directive);
Vue.use(format);

Vue.use(Router);
Vue.use(Http, {
  router: router
});
var lev = JSON.parse(sessionStorage.getItem('BREADCRUMD_KEY'));
var map = JSON.parse(sessionStorage.getItem('BREADCRUMD_MAP_KEY'));
if (!lev) {
  lev = new Array();
}
if (!map) {
  map = {};
}
store.commit('resetBreadcrumd', {
  data: lev,
  map: map
});
Vue.use(CommPlug);
var tempToName = '';
router.beforeEach((to, from, next) => {
  store.commit(Vue.prototype.$mutations.BREADCRUMD_UPDATE, {
    data: tempToName != to.name ? to : from
  })
  tempToName = to.name;
  iLoadingBar.start();
  pathname = to.name;
  next();
});

router.afterEach((to, from, next) => {
  iLoadingBar.finish();
});
/* eslint-disable no-new */
if (process.env.NODE_ENV != 'development') {
  var _mtac = {};
  (function () {
    var mta = document.createElement("script");
    mta.src = "http://pingjs.qq.com/h5/stats.js?v2.0.2";
    mta.setAttribute("name", "MTAH5");
    mta.setAttribute("sid", "500497016");
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(mta, s);
  })();
}


new Vue({
  el: '#app',
  router,
  store,
  data() {
    return {
      pathname: pathname
    };
  },
  template: '<App/>',
  components: { App }
})
