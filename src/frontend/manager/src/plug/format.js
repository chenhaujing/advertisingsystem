export default {
  install: (Vue) => {
    Vue.prototype.$format = {
      setPriceNumber(obj, key) {
        //第一个字符是小数点的情况.  
        if (obj[key] != '' && obj[key].substr(0, 1) == '.') {
          obj[key] = "0.";
        }
        obj[key] = obj[key].replace(/^0*(0\.|[1-9])/, '$1');
        obj[key] = obj[key].replace(/[^\d.]/g, "");
        obj[key] = obj[key].replace(/\.{2,}/g, ".");
        obj[key] = obj[key].replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        obj[key] = obj[key].replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        if (obj[key].indexOf(".") < 0 && obj[key] != "") {
          if (obj[key].substr(0, 1) == '0' && obj[key].length == 2) {
            obj[key] = obj[key].substr(1, obj[key].length);
          }
        }
      },
      setNumber(obj,key){
        obj[key] =  obj[key].replace(/^[0]{1,}/g, "");
        obj[key] =  obj[key].toString().trim();
        obj[key] =  obj[key].replace(/^\./g, "");
        // 清除非数字和.字符
        obj[key] =  obj[key].replace(/[^\d.]/g, "");
        // 当多个0开头，去掉多个0
        obj[key]=obj[key].replace(/[^0-9]/g,'')
    },
      setString(obj, key) {
        obj[key] = obj[key].replace(/^\s+|\s+$/g, '')
      },
      toKeepPoint(str){
          if(str){
            return str.toFixed(2);
          }else{
           return "0"
          }
         
      }
    }
  }
}
