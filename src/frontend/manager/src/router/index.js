export var sessionRouters = (menu = []) => {

}
export default {
  routes: [
    //	登录页
    {
      path: '/login',
      name: 'login',
      component: (resolve) => require(['page/login/login.vue'], resolve),
    },
    { path: '/', redirect: '/index'},
    {
      path: '/',
      name: 'clientIndex',
      meta: { title: '客户管理',expandName:'clientManager'},
      component: (resolve) => require(['page/index.vue'], resolve),
      children: [
        { meta: { title: '客户管理' }, name: 'clientManagerIndex', path: 'index', component: (resolve) => require(['page/client/clientManager'], resolve) },
        { meta: { title: '添加客户' }, name: 'addClient', path: 'addClient', component: (resolve) => require(['page/client/addClient'], resolve) },
        { meta: { title: '编辑客户' }, name: 'editClient', path: 'editClient', component: (resolve) => require(['page/client/addClient'], resolve) },
        { meta: { title: '客户详情' }, name: 'clientInfo', path: 'clientInfo', component: (resolve) => require(['page/client/clientInfo'], resolve) },
      ]
    },
    {
      path: '/contract',
      meta: { title: '合同管理',expandName:'contractIndex' },
      component: (resolve) => require(['page/index.vue'], resolve),
      children: [
        { meta: { title: '合同管理' }, name: 'contractManager', path: '/', component: (resolve) => require(['page/contract/contractIndex.vue'], resolve) },
        // { meta: { title: '提交审核' }, name: 'submitAudit', path: 'submitAudit', component: (resolve) => require(['page/contract/submitAudit.vue'], resolve) },
        { meta: { title: '新建合同' }, name: 'addContract', path: 'addContract', component: (resolve) => require(['page/contract/addContract.vue'], resolve) },
        { meta: { title: '编辑合同' }, name: 'editContract', path: 'editContract', component: (resolve) => require(['page/contract/addContract.vue'], resolve) },
        { meta: { title: '合同详情' }, name: 'contractInfo', path: 'contractInfo', component: (resolve) => require(['page/contract/contractInfo.vue'], resolve) },
        { meta: { title: '状态提示' }, name: 'contracatState', path: 'contracatState', component: (resolve) => require(['page/contract/contractStatus.vue'], resolve) }
      ]
    },
    {
      path: '/sys',
      meta: { title: '系统设置',expandName:'sysIndex'},
      component: (resolve) => require(['page/index.vue'], resolve),
      children: [
        { meta: { title: '人员管理' }, name: 'peopleManager', path: 'peopleManager', component: (resolve) => require(['page/peopleManager/peopleManagerIndex.vue'], resolve) },
        { meta: { title: '组织机构管理' }, name: 'sysManager', path: 'sysManager', component: (resolve) => require(['page/sys/sysIndex.vue'], resolve) },
        { meta: { title: '修改密码' }, name: 'updatePw', path: 'updatePw', component: (resolve) => require(['page/sys/updatePw.vue'], resolve) },
        { meta: { title: '广告配置' }, name: 'adSetting', path: 'adSetting', component: (resolve) => require(['page/ads/adSetting.vue'], resolve) },
        { meta: { title: '合同收款信息设置' }, name: 'contractReceivablesSetting', path: 'contractReceivablesSetting', component: (resolve) => require(['page/contract/contractReceivablesSetting.vue'], resolve) },
        { meta: { title: '角色管理' }, name: 'roleIndex', path: 'roleIndex', component: (resolve) => require(['page/roleManager/roleIndex.vue'], resolve) },
        { meta: { title: '菜单配置' }, name: 'menuSettingIndex', path: 'menuSettingIndex', component: (resolve) => require(['page/menuSettingManager/menuSettingIndex.vue'], resolve) },
        { meta: { title: '门店类别配置' }, name: 'storeTypeSettingIndex', path: 'storeTypeSettingIndex', component: (resolve) => require(['page/storeTypeSettingManager/storeTypeSettingIndex.vue'], resolve) },
        { meta: { title: '广告套餐管理' }, name: 'adsPackageIndex', path: 'adsPackageIndex', component: (resolve) => require(['page/ads/adsPackage.vue'], resolve) },
      ]
    },
    {
      path: '/putAds',
      meta: { title: '广告投放',expandName:'putAdsIndex'},
      component: (resolve) => require(['page/index.vue'], resolve),
      children: [
       // 广告配置
      { meta: { title: '选择合同' }, name: 'selContract', path: 'selContract', component: (resolve) => require(['page/putAds/selContract.vue'], resolve) },
      { meta: { title: '新建广告' }, name: 'addPutAds', path: 'addPutAds', component: (resolve) => require(['page/putAds/addPutAds.vue'], resolve) },
      { meta: { title: '编辑广告' }, name: 'editPutAds', path: 'editPutAds', component: (resolve) => require(['page/putAds/addPutAds.vue'], resolve) },
      { meta: { title: '广告上架' }, name: 'upperAds', path: 'upperAds', component: (resolve) => require(['page/putAds/upperAds.vue'], resolve) },
      { meta: { title: '广告下架' }, name: 'lowerAds', path: 'lowerAds', component: (resolve) => require(['page/putAds/lowerAds.vue'], resolve) },
      { meta: { title: '广告详情' }, name: 'advierInfo', path: 'advierInfo', component: (resolve) => require(['page/putAds/advierInfo.vue'], resolve) },
      { meta: { title: '广告投放管理' }, name: 'adServing', path: 'adServing', component: (resolve) => require(['page/putAds/adServing.vue'], resolve) },
        { meta: { title: '状态提示' }, name: 'adsStatus', path: 'adsStatus', component: (resolve) => require(['page/putAds/adsStatus.vue'], resolve) },
      ]
    },
    // 报错404或者500
    {
      path: '/global',
      meta: { title: '错误' },
      component: (resolve) => require(['page/index.vue'], resolve),
      children: [
        { path: 'error404', name: 'error404', component: (resolve) => require(['page/error/error404.vue'], resolve) },
        { path: 'error500', name: 'error500', component: (resolve) => require(['page/error/error500.vue'], resolve) }
      ]
    },
    {
      path: "*",
      // name:"*",
      redirect: { name: 'error404' }
    }
  ]
}
