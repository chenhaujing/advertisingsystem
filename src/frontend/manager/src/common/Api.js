const proBaseUrl = "http://" + window.location.host;
const devBaseUrl = 'http://139.219.224.171:8090';
const testBaseUrl = "http://" + window.location.host;
const developBaseUrl = "http://139.219.224.171:8090";
// const devBaseUrl = 'http://localhost:8080'
var baseUrl = '';
switch (process.env.NODE_ENV) {
  case "production":
    baseUrl = proBaseUrl;
    break;
  case "develop":
    baseUrl = developBaseUrl;
    break;
  case "test":
    baseUrl = testBaseUrl;
    break;
  case "development":
    baseUrl = devBaseUrl;
    break;
}
// baseUrl = process.env.NODE_ENV == 'production' ? proBaseUrl : devBaseUrl;
export default {
  baseUrl: baseUrl,
  clientListUrl: '/api/customer',
  loginUrl: '/login',
  logoutUrl: '/logout',
  //客户管理
  addClient: '/api/customer/create',
  clientInfo: '/api/customer/{id}',
  updateClientInfo: '/api/customer/update',
  deleteClientInfo: '/api/customer/{id}/delete',
  getUseruserInfosStatistics: '/api/user/userInfosStatistics/{organizationId}',
  customerADList: '/api/customer/advertisementList',
  customerMaterialList: '/api/customer/advertisementMaterialList',
  //为客户分配业务员
  distributedCustomerUrl: '/api/customer/distribute/{customerId}/{userId}',


  areaInfoList: '/api/area',
  imageUploadUrl: '/api/file/upload',
  //修改密码   
  modifyPasswordUrl: '/api/user/modifyPassword',
  crSettingReceiptInfoUrl: '/api/contractReceiptInfo',
  crSettingUpdate: '/api/contractReceiptInfo/update',
  //角色管理
  getRoleListUrl: '/api/role',
  getRoleAllListUrl: '/api/role/all',
  addRoleUrl: '/api/role/create',
  deleteRoleUrl: '/api/role/{id}/delete',
  updateRoleUrl: '/api/role/update',
  banRoleUrl: '/api/role/{id}/toggledEnabled',
  //分配角色
  distributeRole: '/api/role/distribute',
  //根据角色查询菜单权限
  getMenuPower: '/api/role/menus/{roleId}',
  //获取权限菜单
  getUserMenus: '/api/user/userInfoAndMenus',
  //人员管理
  getMemberList: '/api/user',
  deleteMemberUrl: '/api/user/{id}/delete',
  getOrganizationUser: '/api/user/organization/{id}',
  addMemberUrl: '/api/user/create',
  updateMemberUrl: '/api/user/update',
  toggledEnabledMemberUrl: '/api/user/{id}/toggledEnabled',

  //所有组织机构的树状结构
  getOrganizationUrl: '/api/organization/all',
  //所有组织机构的列表
  getOrganizationListUrl: '/api/organization',
  //删除组织机构列表
  deleteOrganizationUrl: '/api/organization/{id}/delete',
  //单个删除组织内人员
  deleteOrganizationPersonUrl: '/api/user/{userId}/organization/{organizationId}/delete',
  //批量伤处组织内人员
  deleteOrganizationPersonListUrl: '/api/user/userList/organization/{organizationId}/delete',
  //修改组织信息
  updateOrganizationPersonUrl: '/api/organization/update',

  //分页获取广告尺寸配置
  getSizeConfigUrl: '/api/advertisement/sizeConfig',
  //新增广告尺寸配置
  addSizeConfigUrl: '/api/advertisement/sizeConfig/create',
  //修改广告尺寸配置
  updateSizeConfigUrl: '/api/advertisement/sizeConfig/update',
  //删除广告尺寸配置
  deleteSizeConfigUrl: '/api/advertisement/sizeConfig/{id}/delete',
  //分页获取广告时长配置
  getDurationConfigUrl: '/api/advertisement/durationConfig',
  //新增广告时长配置
  addDurationConfigUrl: '/api/advertisement/durationConfig/create',
  //修改广告时长配置
  updateDurationConfigUrl: '/api/advertisement/durationConfig/update',
  //删除广告时长配置
  deleteDurationConfigUrl: '/api/advertisement/durationConfig/{id}/delete',
  //分页广告展示次数配置
  getDisplayTimesConfigUrl: '/api/advertisement/displayTimesConfig',
  //新增广告展示次数配置
  addDisplayTimesConfigUrl: '/api/advertisement/displayTimesConfig/create',
  //修改广告展示次数配置
  updateDisplayTimesConfigUrl: '/api/advertisement/displayTimesConfig/update',
  //删除广告展示次数配置
  deleteDisplayTimesConfigUrl: '/api/advertisement/displayTimesConfig/delete/{id}',
  // 新增广告价格配置
  addAdsPriceConfigUrl: '/api/advertisement/priceConfig/create',
  // 查询广告价格配置列表
  getAdsPriceConfigListUrl: '/api/advertisement/priceConfig',
  // 更新广告价格配置
  updateAdsPriceConfigUrl: '/api/advertisement/priceConfig/update',
  // 删除广告价格配置
  deleteAdsPriceConfigUrl: '/api/advertisement/priceConfig/delete/{id}',

  //广告套餐列表
  getPackageConfig: '/api/advertisement/packageConfig',
  addPackageConfig: '/api/advertisement/packageConfig/create',
  updatePackageConfig: '/api/advertisement/packageConfig/update',
  deletePackageConfig: '/api/advertisement/packageConfig/delete/{id}',

  //投放广告
  //获取客户待执行合同
  getCustomerContractStatistic: '/api/customer/customerContractStatistic',
  //获取客户待执行合同
  getPendingExecutionContracts: '/api/contract/customer/pendingExecutionContracts',
  //新增投放合同
  addPutAds:"/api/advertisement/create",
  //编辑投放合同
  updatePutAds:"/api/advertisement/update",
  //获取客户详情
  getPutAdsContractInfo:'/api/advertisement/contractAdvertisementConfig/{contractId}',
  //上传素材
  updateMaterial:"/api/advertisement/material/upload",
  //获取素材
  getMaterial:"/api/advertisement/material",
  //获取编辑回显信息
  getAdsEditInfo:"/api/advertisement/{id}/editInfo",
  //获取素材统计数量信息
  getAdsMaterialNum:"/api/advertisement/material/{customerId}",
  //投放广告的操作
  adsOperate:"/api/advertisement/operate",
  //删除广告信息
  deletePutAds:"/api/advertisement/{id}/delete",
  //广告合同历史信息
  advertisingHistory:"/api/advertisement/advertisingHistory",

  addOrganizationUrl: '/api/organization/create',
  searchMemberUrl: '/api/user/OrganizationOwners',
  //门店类别配置
  getStoreTypeListUrl: '/api/storeCategoryStandard',
  updateStoreTypeUrl: '/api/storeCategoryStandard/update',
  deleteStoreTypeListUrl: '/api/storeCategoryStandard/{id}/delete',
  addStoreTypeListUrl: '/api/storeCategoryStandard/create',
  //类别广告位配置
  getAdTypeListUrl: '/api/adPosition',
  deleteAdTypeListUrl: '/api/adPosition/{id}/delete',
  addAdTypeListUrl: '/api/adPosition/create',
  updateAdTypeListUrl: '/api/adPosition/update',
  //合同收款信息配置
  getContractReceivablesInfoUrl: '/api/contractReceiptInfo',
  updateContractReceivablesInfoUrl: '/api/contractReceiptInfo/update',
  // 合同模块
  getContractListUrl: '/api/contract',
  deleteContract: '/api/contract/{id}/delete',
  getContractTotal: '/api/contract/statusStatistics',
  // 

  // 查询客户的合同信息
  getContractDetailList: '/api/contract/{customerId}/list',
  // 查询有客户的地区列表
  getAreaListByValid: '/api/area/existsCustomer',
  // 查询合同状态信息
  getContractTotalData: '/api/contract/statusStatistics',
  // 获取包含门店的地区
  getAreaListByValidStore: '/api/area/containStoreInfo',
  // 获取合同详情
  getContractInfo: '/api/contract/{id}',
  // 获取所有组织机构
  getAllOrganizationUrl: '/api/organization',
  // 分页获取菜单
  getMenuByPageUrl: '/api/menu',
  // 获取当前系统所有菜单
  getAllSysMenu: '/api/menu/all',
  // 添加菜单
  addSysMenu: '/api/menu/create',
  //更新菜单
  updateSysMenu: '/api/menu/update',
  //删除菜单
  deleteSysMenu: '/api/menu/{id}/delete',
  // 查询所有角色，附带菜单信息
  getAllRoleAndMenuUrl: '/api/role/all',
  // 新增类别广告位
  addAdsTypePosUrl: '/api/adPosition/create',
  // 合同门店列表
  getStoreByTypeUrl: '/api/store',
  // 生成合同编码
  getContractNoUrl: '/api/code/generate',
  // 创建合同
  createdContractUrl: '/api/contract/create',
  // 更新合同
  updateContractUrl: '/api/contract/update',
  // 往合同内添加门店
  addStoreContractUrl: '/api/contract/store/add',
  // 清空已选择的门店
  clearSelectStoreUrl: '/api/contract/store/empty',
  // 门店一键选取
  onkeySelectStoreUrl: '/api/store/autoChoose',
  // 编辑合同获取已保存的数据
  getSaveDataContactUrl: '/api/contract/{id}/editInfo',
  // 预览代码
  previewContractUrl: '/api/contract/preview/{contractId}',
  // 删除
  deleteStoreByContractUrl: '/api/contract/store/delete',
  // 合同操作
  optionContranctUrl: '/api/contract/operate',
  // 判断是否完成门店价格配置
  hasCompletedPriceConfigUrl: '/api/advertisement/hasCompletedPriceConfig',
  // 查询地区
  getAreaByIdUrl: '/api/area/{areaId}/sub',
  //查看合同是否存在未执行或正在执行的广告
  hasUnDeliveryAdvertisement:'/api/contract/hasUnDeliveryAdvertisement/{contractId}',
  //广告模块
  // 获取广告列表
 
  getAdListUrl: '/api/advertisement',
  getAdListTotal: '/api/advertisement/statusStatistics',
  // 广告详情
  getAadvierInfo: '/api/advertisement/{id}',
  getAdAreaCountList: '/api/advertisement/areaCountList',
  // 下架统计 高风险广告数据量
  getDeliverStadtistics: '/api/advertisement/deliveringStatistics',
  generateUrl(subSub) {
    return baseUrl + subSub;
  }
}
