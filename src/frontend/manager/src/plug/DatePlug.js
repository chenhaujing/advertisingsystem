export default {
    install: function (Vue, options) {
        Vue.prototype.$dateFormat = function (date,fmt) { //author: meizz   
            var o = {
                "M+": date.getMonth() + 1,                 //月份   
                "d+": date.getDate(),                    //日   
                "h+": date.getHours(),                   //小时   
                "m+": date.getMinutes(),                 //分   
                "s+": date.getSeconds(),                 //秒   
                "q+": Math.floor((date.getMonth() + 3) / 3), //季度   
                "S": date.getMilliseconds()             //毫秒   
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
        Vue.prototype.$dateTransform = function (date) {
            if (!date) {
                return '';
            }
            var dateList = date.toString().split('/');
            if (dateList.length < 3) {
                return date;
            }
            return dateList[0] + "年" + dateList[1] + '月' + dateList[2] + '日';
        }
    }
}