export default {
  install: (Vue) => {
    Vue.prototype.$formVerify = {
      //校验手机号
      verifyPhone(val) {
        if (!val) {
          return false;
        }
        var reg = /^(16[0-9]|19[0-9]|14[0-9]|17[0-9]|13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/;
        return reg.test(val);
      },
      //校验字符串不能为空
      verifyString(val) {

        if (!val || val.toString().length == 0) {
          return true;
        } else {
          return false;
        }
      },
      //校验邮箱格式
      verifyMail(val) {
        var reg = /^\w+([-+.]\w+)*@\w+([-.]\\w+)*\.\w+([-.]\w+)*$/;
        return reg.test(val);
      },
      //校验对象是否相等
      isEqual(x, y) {
        // If both x and y are null or undefined and exactly the same 
        if (x === y) {
          return true;
        }

        if (!(x instanceof Object) || !(y instanceof Object)) {
          return false;
        } 
        if (x.constructor !== y.constructor) {
          return false;
        }
        for (var p in x) {
          if (x.hasOwnProperty(p)) {
            if (!y.hasOwnProperty(p)) {
              return false;
            }
            if (x[p] === y[p]) {
              continue;
            }
            if (typeof (x[p]) !== "object") {
              return false;
            }
            if (!Object.equals(x[p], y[p])) {
              return false;
            }
          }
        }
        for (p in y) {
          if (y.hasOwnProperty(p) && !x.hasOwnProperty(p)) {
            return false;
          }
        }
        return true;
      }
    }
  }
}
