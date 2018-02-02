export default {
    install: (Vue) => {
        Vue.prototype.$getAreaData = function () {
            return new Promise((resolve, reject) => {
                Vue.prototype.$StorageUtils.getDataByGlobay(Vue.prototype.$StorageUtils.keys.AREA_DATA)
                    .then((result) => {
                       
                        resolve(result.data);
                    })
                    .catch((e) => {
                        //获取地区下拉列表的信息
                        Vue.prototype.$post(Vue.prototype.$api.areaInfoList).then((result) => {
                            // 缓存7天。
                            Vue.prototype.$StorageUtils.saveDataByGlobay(
                                Vue.prototype.$StorageUtils.keys.AREA_DATA,
                                result.data,
                                new Date().getTime() + (1000 * 60 * 60 * 24 * 7)
                            )
                            resolve(result.data)
                        }).catch((e) => {
                            e.message = e.message || '获取列表数据失败！';
                        });
                    })
            })

        }
    }
}
