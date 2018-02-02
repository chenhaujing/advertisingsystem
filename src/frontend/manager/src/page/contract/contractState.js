/**
 * 合同状态
 * 
 */
export default {
    Status: {
        all: 0,
        pendingCommit : 1,  //  待提交
        pendingAuditing: 2, //  待审核
        pendingSign: 3,     // 待签约
        pendingExecutiom: 4, // 待执行
        abruptlyTerminated: 5, // 以外终止
        finished: 6, // 执行完成
        executing: 7 // 执行中
    },
    OptionStatus: {
        submit: 1, //提交审批,
        audit: 2, //审核,
        sign: 3,
        run : 4
    }
}