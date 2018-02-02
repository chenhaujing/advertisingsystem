
const permission = {
    c: "created",
    u: "update",
    s: "search",
    d: "delete",
    allocation: 'allocation',
    login: "login",
    updatePw: 'updatePw',
    resetPw: 'resetPw',
    over: 'over',
    audit: 'audit',
    sign: 'sign',
    delByMultiple: 'delByMultiple',
    addRootOrgin: 'addRootOrgin',
    addChildOrgin: 'addOrgin',
    disable: 'disable',
    menuPerConfig: 'menuConfig'
}
const module = {
    clientMng: 'clientManager',
    organiztion: 'organizationeModule',
    contract: 'contract',
    adManage: 'adManage',                    
    roleManager: 'roleManager',
    peopleManager: 'peopleManager',
    contractTaskInfo: 'contractTaskInfo',
    adsTypePriceConfig: 'adsTypePriceConfig',
    storeTypeConfig: 'storeTypeConfig',
    storeAdsPosition: 'storeAdsPosition',
    menuConfig: 'menuConfig',
    adsSetting:'adsSetting',
    adsPackage:'adsPackage',
}

const roleType={
    "manager":2,
    "salesman":3,
    "admin":99,
}

const role = {
    // 管理人员
    [roleType.manager]: {
        [module.clientMng]: { [permission.c]: true, [permission.u]: true, [permission.s]: true, [permission.d]: true, [permission.allocation]: true },
        globalModule: { [permission.login]: true, [permission.updatePw]: true, [permission.resetPw]: true },
        [module.organiztion]: {
            [permission.c]: true,
            [permission.u]: true,
            [permission.d]: true,
            [permission.s]: true,
            [permission.addChildOrgin]: true,
            [permission.addRootOrgin]: false,
            [permission.delByMultiple]: true,
        },
        [module.contract]: {
            [permission.c]: true,
            [permission.u]: true,
            [permission.s]: true,
            [permission.d]: true,
            [permission.over]: true,
            [permission.audit]: true,
            [permission.sign]: true
        },
        [module.peopleManager]: {
            [permission.c]: true,
            [permission.d]: true,
            [permission.u]: true,
            [permission.s]: true,
            [permission.disable]: true
        },
        [module.adsSetting]:{
            [permission.c]: false,
            [permission.u]: false,
            [permission.d]: false,
            [permission.s]: false,
        },
        [module.adsPackage]:{
            [permission.c]: false,
            [permission.u]: false,
            [permission.d]: false,
            [permission.s]: false,
        },
        [module.adManage]: {
            [permission.u]: true,
            [permission.audit]: true,
        },
    },
    // 业务员
    [roleType.salesman]: {
        [module.clientMng]: { [permission.c]: true, [permission.u]: true, [permission.s]: true, [permission.d]: true, [permission.allocation]: false },
        globalModule: { [permission.login]: true, [permission.updatePw]: true, [permission.resetPw]: true },
        [module.organiztion]: {
            [permission.c]: false,
            [permission.u]: false,
            [permission.d]: false,
            [permission.s]: false,
            [permission.addChildOrgin]: false,
            [permission.addRootOrgin]: false,
            [permission.delByMultiple]: false,
        },
        [module.contract]: {
            [permission.c]: true,
            [permission.u]: true,
            [permission.s]: true,
            [permission.d]: true,
            [permission.over]: true,
            [permission.audit]: false,
            [permission.sign]: true
        },
        [module.adsSetting]:{
            [permission.c]: false,
            [permission.u]: false,
            [permission.d]: false,
            [permission.s]: false,
        },
        [module.adsPackage]:{
            [permission.c]: false,
            [permission.u]: false,
            [permission.d]: false,
            [permission.s]: false,
        },
        [module.adManage]: {
            [permission.u]: false,
            [permission.audit]: false,
        },
        
    },
    // 系统管理员
    [roleType.admin]: {
        [module.clientMng]: { [permission.c]: true, [permission.u]: true, [permission.s]: true, [permission.d]: true, [permission.allocation]: true },
        globalModule: { [permission.login]: true, [permission.updatePw]: true, [permission.resetPw]: true },
        [module.organiztion]: {
            [permission.c]: true,
            [permission.u]: true,
            [permission.d]: true,
            [permission.s]: true,
            [permission.addChildOrgin]: true,
            [permission.addRootOrgin]: true,
            [permission.delByMultiple]: true
        },
        [module.contract]: {
            [permission.c]: true,
            [permission.u]: true,
            [permission.s]: true,
            [permission.d]: true,
            [permission.over]: true,
            [permission.audit]: true,
            [permission.sign]: true
        },
        [module.roleManager]: {
            [permission.c]: true,
            [permission.u]: true,
            [permission.s]: true,
            [permission.d]: true,
            [permission.disable]: true
        },
        [module.peopleManager]: {
            [permission.c]: true,
            [permission.d]: true,
            [permission.u]: true,
            [permission.s]: true,
            [permission.disable]: true
        },
        [module.contractTaskInfo]: {
            [permission.u]: true
        },

        [module.adsTypePriceConfig]: {
            [permission.c]: true,
            [permission.d]: true,
            [permission.u]: true
        },
        [module.storeTypeConfig]: {
            [permission.c]: true,
            [permission.d]: true,
            [permission.u]: true
        },
        [module.storeAdsPosition]: {
            [permission.c]: true,
            [permission.d]: true,
            [permission.u]: true
        },
        [module.menuConfig]: {
            [permission.c]: true,
            [permission.d]: true,
            [permission.u]: true,
            [permission.menuPerConfig]: true
        },
        [module.adsSetting]:{
            [permission.c]: true,
            [permission.u]: true,
            [permission.d]: true,
            [permission.s]: true,
        },
        [module.adsPackage]:{
            [permission.c]: true,
            [permission.u]: true,
            [permission.d]: true,
            [permission.s]: true,
        },
        [module.adManage]: {
            [permission.u]: true,
            [permission.audit]: true,
        },
    }
}

export default {
    module: module,
    p: permission,
    role: role,
    roleType:roleType
}