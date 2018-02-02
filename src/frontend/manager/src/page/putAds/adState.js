/**
 * 广告状态
 * 
 */
export default {
    Status: {
        all: 0,                     // 全部
        pendingCommit : 1,          // 待提交
        pendingAuditing: 2,         // 待审核
        pendingDelivery: 3,         // 待投放
        deliverying: 4,             // 投放中
        abruptlyTerminated: 5,      // 已下架
        finished: 6,                // 已完成
        highRisk: 65,               // 投放中的高风险
    },
    OptionStatus: {
        submit: 1, //提交审批,
        audit: 2, //审核,
        put: 3,//投放
        over : 4
    }
}