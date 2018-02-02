
<style lang="scss" scoped>
@import '~assets/css/base.scss';
.contractInfo {
    background-color: #f1f1f1;
}

.header {
    position: relative;
    padding: 20px 20px 0 20px;
    background-color: #ffffff;
    $headerContentHeight: 90px; // 顶部标题
    .header_title {
        font-size: 18px;
        color: rgba(153, 153, 153, 1);
    } // 顶部名称内容
    .header_content {
        position: relative;
        height: $headerContentHeight;
    } // 顶部LOGO
    .heaer_logo {
        float: left;
        height: 55px;
        width: 55px;
        position: absolute;
        top: 50%;
        transform: translate(0, -50%);
        img {
            border-radius: 50%;
            height: 100%;
            width: 100%;
        }
    } // 顶部名称
    .header_leftBox {
        float: left;
        margin-left: 75px;
        .header_name {
            line-height: 55px;
            font-size: 18px;
            color: #333333;
        }
        .header_baseInfo {
            color: #999999;
            font-size: 14px;
            line-height: 25px;

            &>span:not(:first-child) {
                margin-left: 20px;
            }
        }
        .sigleLine {
            line-height: 85px;
        }
    }
    .header_btnBox {
        position: absolute;
        right: 0;
        bottom: -25px;
    } // 顶部按钮
    .header_tool {
        padding-bottom: 20px;
        display: flex;
        align-items: center;
        line-height: $headerContentHeight;
        justify-content: center;
        .header_tool_item {
            flex: 1;
            min-width: 80px;
        }
        .btnItem {}
    }
    .iconTextButton {
        margin-right: 20px;
    }
    .contractStatus {
        margin: 0 auto;
        border-radius: 4px;
        background-color: #4cabe0;
        color: #ffffff;
        width: 120px;
        font-size: 16px;
        height: 34px;
        line-height: 34px;
        text-align: center;
    }
    .waitRunStatus {
        background-color: #fcb322;
    }
    .overStatus {
        background-color: #f0857d;
    }
    .runFinishStatus {
        background-color: #7edd9c;
    }
}



.mainContent {
    background-color: #ffffff; // 标签tab主面板
    .tabItemPane {
        box-sizing: border-box;
        background-color: #f1f1f1; // 标签tab主内容区
        .tabItemContent {
            margin-top: 20px;
            padding: 20px;
            background-color: #ffffff;
            .tabItemContentTitle {
                font-size: 18px;
                color: #999;
            }
        }
    }
}

// 合同信息  基本详情模块
.contractBaseInfo {
    height: 276px;
    .clientBaseInfo_item {
        float: left;
    }
}

// 合同信息 广告配置与费用模块
.contractSetting {
    height: 279px;
    padding-top: 20px;
    .contractAgreementContent {
        font-size: 14px;
        color: #333333;
        max-height: 90px;
        margin-top: 20px;
    }
}

// 合同信息 协议期限
.contractAgreement {
    height: 70px;
    .contractAgreementContent {
        font-size: 14px;
        color: #333333;
        max-height: 90px;
    }
}

//合同信息 备注
.contractRemark {
    height: 60px;
    .contractRemarkContent {
        font-size: 14px;
        color: #333333;
        max-height: 90px;
        margin-top: 10px;
    }
}
</style>
<style  lang="scss">
.contractInfo {
    .field_title {
        width: 130px;
        text-align: right;
    }
    .contractAgreement .field_title {
        width: 120px;
    }
    .taskType .field_title {
        width: 90px;
    }
    .iconTextButton {
        display: block;
        text-align: center;
    }
    .ivu-tabs-nav .ivu-tabs-tab {
        font-size: 16px;
    }


    .ivu-tabs-bar {
        margin-bottom: 0;
    }

    .ivu-tabs {
        width: 100%;
    }

    .ivu-tabs-nav-container {
        padding-left: 20px;
    }
}
</style>
<template>
    <div class="contractInfo">
        <div class="main">

            <div class="header">
                <div class="header_title">合同信息</div>
                <div class="header_content">
                    <div class="heaer_logo">
                        <img v-imgError="errorImg" :src="contractInfo.headPortrait" />
                    </div>
                    <div class="header_leftBox">
                        <div class="header_name" v-text="contractInfo.contractName"></div>
                        <div class="header_baseInfo">
                            <span v-if="contractInfo.contractCode">[合同编号：{{contractInfo.contractCode}}]</span>
                            <span v-if="contractInfo.signTime">签约时间：{{contractInfo.signTime}}</span>
                        </div>
                    </div>
                    <div class="header_btnBox">
                        <div class="header_tool" v-if="contractInfo.contractStatus == constractStatus.pendingCommit">
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-chakan" text="合同预览" @click.native="preView(contractInfo.id)"></tyIconTextButton>
                            <tyIconTextButton v-if="$store.state.check($m.contract,$p.u)" class="btnItem header_tool_item" iconClass="icon-bianji" text="编辑" @click.native="gotoEdit(contractInfo.id)"></tyIconTextButton>
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-tijiaoshenhe" text="提交审核" @click.native="submitAudit(contractInfo.id)"></tyIconTextButton>
                        </div>
                        <div class="header_tool" v-if="contractInfo.contractStatus == constractStatus.pendingAuditing">
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-chakan" text="合同预览" @click.native="preView(contractInfo.id)"></tyIconTextButton>
                            <tyIconTextButton v-if="$store.state.check($m.contract,$p.u)" class="btnItem header_tool_item" iconClass="icon-bianji" text="编辑" @click.native="gotoEdit(contractInfo.id)"></tyIconTextButton>
                            <tyIconTextButton v-if="$store.state.check($m.contract,$p.audit)" class="btnItem header_tool_item" iconClass="icon-audit" text="审核" @click.native="openOptionModal(1)"></tyIconTextButton>
                        </div>
                        <div class="header_tool signTool" v-if="contractInfo.contractStatus == constractStatus.pendingSign">
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-chakan" text="合同预览" @click.native="preView(contractInfo.id)"></tyIconTextButton>
                            <tyIconTextButton v-if="$store.state.check($m.contract,$p.sign)" class="btnItem header_tool_item" iconClass="icon-qianyue" text="签约" @click.native="openOptionModal(2)"></tyIconTextButton>
                        </div>
                        <div class="header_tool" v-if="contractInfo.contractStatus == constractStatus.pendingExecutiom||contractInfo.contractStatus == constractStatus.executing">
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-chakan" text="合同预览" @click.native="preView(contractInfo.id)"></tyIconTextButton>
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-zhongzhizhihang" text="终止执行" @click.native="openOptionModal(3)"></tyIconTextButton>
                        </div>
                        <div class="header_tool preViewTool" v-if="contractInfo.contractStatus == constractStatus.abruptlyTerminated">
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-chakan" text="合同预览" @click.native="preView(contractInfo.id)"></tyIconTextButton>
                        </div>
                        <div class="header_tool preViewTool" v-if="contractInfo.contractStatus == constractStatus.finished">
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-chakan" text="合同预览" @click.native="preView(contractInfo.id)"></tyIconTextButton>
                        </div>
                        <div v-text="contractInfo.contractStatusName" :class="{
                                        'waitRunStatus': contractInfo.contractStatus == constractStatus.pendingExecutiom,
                                        'runFinishStatus': contractInfo.contractStatus == constractStatus.finished,
                                        'overStatus': contractInfo.contractStatus === constractStatus.abruptlyTerminated}" class="contractStatus"></div>
                    </div>
                    <!-- , -->
                </div>

            </div>
            <div class="mainContent">
                <div class="content">
                    <iTabs v-model="tabValue">
                        <iPane label="基本详情" name="base" class="tabItemPane">
                            <div class="tabItemContent">
                                <iRow class="contractBaseInfo">
                                    <iCol span="12" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="甲方（广告客户）：" :value="contractInfo.firstPartyName"></tyFields>
                                        <tyFields title="甲方联系人：" :value="contractInfo.firstPartyResponsibilityPerson"></tyFields>
                                        <tyFields title="联系电话：" :value="contractInfo.firstPartyPhone"></tyFields>
                                        <tyFields title="甲方送达地址：" :value="contractInfo.firstPartyContractReceiveAddress"></tyFields>
                                        <tyFields title="联系邮箱：" :value="contractInfo.firstPartyEmail"></tyFields>
                                        <tyFields title="创建人：" :value="contractInfo.creator"></tyFields>
                                        <tyFields title="创建时间：" :value="contractInfo.createdTime"></tyFields>
                                    </iCol>
                                    <iCol span="12" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="乙方：" :value="contractInfo.secondPartyName"></tyFields>
                                        <tyFields title="乙方联系人：" :value="contractInfo.secondPartyResponsibilityPerson"></tyFields>
                                        <tyFields title="联系电话：" :value="contractInfo.secondPartyPhone"></tyFields>
                                        <tyFields title="乙方送达地址：" :value="contractInfo.secondPartyContractReceiveAddress"></tyFields>
                                        <tyFields title="联系邮箱：" :value="contractInfo.secondPartyEmail"></tyFields>
                                        <tyFields title="签约人：" :value="contractInfo.signer"></tyFields>
                                        <tyFields title="维护人：" :value="contractInfo.owner"></tyFields>
                                    </iCol>
                                </iRow>
                            </div>
                            <div class="tabItemContent">
                                <div class="tabItemContentTitle">广告配置与费用</div>
                                <iRow class="contractSetting">
                                    <iCol span="12" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="广告位总数：" :value="contractInfo.totalStore"></tyFields>
                                        <tyFields title="A类门店总数：" :value="contractInfo.storeACount"></tyFields>
                                        <tyFields title="B类门店总数：" :value="contractInfo.storeBCount"></tyFields>
                                        <tyFields title="C类门店总数：" :value="contractInfo.storeCCount"></tyFields>
                                        <tyFields title="前述费用缴纳日期：" :value="contractInfo.signAfterDay+'日【签约合同后】'"></tyFields>
                                        <tyFields title="是否产生滞纳金：" :value="contractInfo.adressDetail"></tyFields>
                                    </iCol>
                                    <iCol span="12" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="广告总额：" :value="$format.toKeepPoint(contractInfo.totalCost)"></tyFields>
                                        <tyFields title="媒体费用：" :value="$format.toKeepPoint(contractInfo.mediumCost)"></tyFields>
                                        <tyFields title="制作费用：" :value="$format.toKeepPoint(contractInfo.productCost)"></tyFields>
                                        <tyFields title="广告折扣：" :value="$format.toKeepPoint(contractInfo.discount)"></tyFields>
                                        <tyFields title="折扣金额：" :value="$format.toKeepPoint(contractInfo.discountMoney)"></tyFields>
                                        <tyFields title="滞纳金总额：" :value="$format.toKeepPoint(contractInfo.adressDetail)"></tyFields>
                                    </iCol>
                                </iRow>
                            </div>
                            <div class="tabItemContent">
                                <div class="contractAgreement taskType">
                                    <div class="tabItemContentTitle">投放配置</div>
                                    <iRow class="contractSetting">
                                        <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                            <tyFields title="广告位置：" :value="contractInfo.sizeName"></tyFields>
                                        </iCol>
                                        <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                            <tyFields title="广告时长：" :value="contractInfo.duration+contractInfo.durationUnitName+'/次'"></tyFields>
                                        </iCol>
                                            <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                                <tyFields title="展示次数：" :value="contractInfo.displayTimes+'次/'+contractInfo.timeUnitName"></tyFields>
                                            </iCol>
                                    </iRow>
                                </div>
                            </div>
                            <div class="tabItemContent">
                                <div class="contractAgreement taskType">
                                    <div class="tabItemContentTitle">收款方式</div>
                                    <iRow class="contractSetting">
                                        <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                            <tyFields title="乙方户名：" :value="contractInfo.bankAccountName"></tyFields>
                                        </iCol>
                                        <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                            <tyFields title="乙方账号：" :value="contractInfo.bankAccountNumber"></tyFields>
                                        </iCol>
                                        <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                            <tyFields title="乙方开户行：" :value="contractInfo.bankName"></tyFields>
                                        </iCol>
                                    </iRow>
                                </div>
                            </div>
                            <div class="tabItemContent">
                                <div class="contractAgreement">
                                    <div class="tabItemContentTitle">协议期限</div>
                                    <iRow class="contractSetting">
                                        <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                            <tyFields title="合作周期（月）：" :value="contractInfo.totalMonths+'个月'"></tyFields>
                                        </iCol>
                                        <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                            <tyFields title="开始时间：" :value="contractInfo.startTime"></tyFields>
                                        </iCol>
                                        <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                            <tyFields title="结束时间：" :value="contractInfo.endTime"></tyFields>
                                        </iCol>
                                    </iRow>
                                </div>
                            </div>
                            <div class="tabItemContent">
                                <div class="contractRemark">
                                    <div class="tabItemContentTitle">备注</div>
                                    <div class="contractRemarkContent" v-text="contractInfo.remark">
                                    </div>
                                </div>
                            </div>
                        </iPane>
                    </iTabs>
                </div>
            </div>
        </div>
        <AuditModal ref="auditModal" @optionSuccess="refresh"></AuditModal>
    </div>
</template>
<script>

import AuditModal from './auditModal';
import ContractState from './contractState';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import iTabs from 'iview/src/components/tabs';
import tyTableView from 'components/tyTableView';
import tyFields from 'components/tyFields';
import tyDivider from 'components/tyDivider';
import contractMixni from './contractMixni';
import tyIconTextButton from 'components/tyIconTextButton';
export default {
    mounted() {
        this.refresh();
    },
    mixins: [contractMixni],
    methods: {
        openOptionModal(mode) {
            this.$refs.auditModal.setParams(this.contractInfo.id, mode)
            this.$refs.auditModal.toggle();
        },
        refresh() {
            var cid = this.$route.query.cid;
            if (cid || cid === 0) {
                this.$get(this.$api.getContractInfo, {
                    id: cid
                }).then((result) => {
                    this.contractInfo = result.data;
                }).catch((e) => {
                    this.$Message.error(e.message);
                })
            }
        },
        deleteConfirm() {
            this.deleteControll(this.contractInfo).then((r) => {
                this.$Notice.success({
                    title: '提示',
                    desc: '删除成功'
                })
                this.$router.go(-1);
            });
        }
    },
    data() {
        return {
            errorImg: require('assets/img/client/client_dafault_icon.png'),
            constractStatus: ContractState.Status,
            //客户基础信息
            contractInfo: {},
            tabValue: 'base',//用于tab切换
        }
    },
    watch: {
        tabValue() {
            /*console.log(this.tabValue);*/
        }
    },
    components: {
        AuditModal,
        iTabs,
        iRow,
        iCol,
        'iPane': iTabs.Pane,
        tyIconTextButton,
        tyTableView,
        tyFields,
        tyDivider
    }
}

</script>
