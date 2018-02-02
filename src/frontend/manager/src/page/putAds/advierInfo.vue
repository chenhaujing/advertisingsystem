<template>
    <div class="advierInfo">
        <div class="main">

            <div class="header">
                <div class="header_title">广告信息</div>
                <div class="header_content">
                    <div class="heaer_logo">
                        <img v-imgError="errorImg" :src="AadvierInfo.portrait" />
                    </div>
                    <div class="header_leftBox">
                        <div class="header_name" v-text="AadvierInfo.advertisementName"></div>
                        <div class="header_baseInfo">
                            <span v-if="AadvierInfo.createdTime">创建时间：{{AadvierInfo.createdTime}}</span>
                            <span v-if="AadvierInfo.creator">创建人：{{AadvierInfo.creator}}</span>
                        </div>
                    </div>
                    <div class="header_btnBox" :class="StyleCenter">
                        <!-- 待提交  -->
                        <div class="header_tool" v-if="AadvierInfo.advertisementStatus == ADState.pendingCommit">
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-bianji" text="编辑" @click.native="gotoEdit(ids)"></tyIconTextButton>
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-tijiaoshenhe" text="提交审核" @click.native="submitAudit(ids, optionSuccess)"></tyIconTextButton>
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-laji" text="删除" @click.native="deleteConfirm(ids, goback)"></tyIconTextButton>
                        </div>
                        <!-- 待审核 -->
                        <div class="header_tool" v-if="$store.state.check($m.adManage, $p.u) && AadvierInfo.advertisementStatus == ADState.pendingAuditing">
                            <tyIconTextButton v-if="$store.state.check($m.adManage, $p.u)" class="btnItem header_tool_item" iconClass="icon-bianji" text="编辑" @click.native="gotoEdit(ids)"></tyIconTextButton>
                            <tyIconTextButton v-if="$store.state.check($m.adManage, $p.audit)" class="btnItem header_tool_item" iconClass="icon-audit" text="审核" @click.native="openOptionModal(ids, OptionStatus.audit)"></tyIconTextButton>
                        </div>
                        <!-- 待投放 -->
                        <div class="header_tool signTool" v-if="AadvierInfo.advertisementStatus == ADState.pendingDelivery">
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-bianji" text="编辑" @click.native="gotoEdit(ids)"></tyIconTextButton>
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-toufangpingtai" text="投放" @click.native="put(ids, optionSuccess)"></tyIconTextButton>
                        </div>
                        <!-- 投放中 -->
                        <div class="header_tool" v-if="AadvierInfo.advertisementStatus == ADState.deliverying">
                            <tyIconTextButton class="btnItem header_tool_item" iconClass="icon-soldout" text="下架" @click.native="openOptionModal(ids, OptionStatus.over)"></tyIconTextButton>
                        </div>
                        <!-- 已下架 -->
                        <!-- <div class="header_tool preViewTool" v-if="AadvierInfo.advertisementStatus == ADState.abruptlyTerminated">
                            
                        </div>
                        <div class="header_tool preViewTool" v-if="AadvierInfo.advertisementStatus == ADState.finished">
                            
                        </div> -->
                        <div v-text="AadvierInfo.advertisementStatusName" :style="styleColor" class="contractStatus"></div>
                    </div>
                    <!-- , -->
                </div>

            </div>
            <div class="mainContent">
                <div class="content">
                    <iTabs v-model="tabValue">
                        <iPane label="基本详情" name="base" class="tabItemPane">
                            <div class="tabItemContent">
                                <iRow class="">
                                    <iCol span="12" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="广告客户：" :value="AadvierInfo.customerName"></tyFields>
                                        <tyFields title="广告合同：" :value="AadvierInfo.contractName"></tyFields>
                                        <tyFields title="广告总价：" :value="$format.toKeepPoint(AadvierInfo.totalCost)"></tyFields>
                                        <tyFields title="是否折扣：" :value="AadvierInfo.isDiscount==1 ? '是':'否'"></tyFields>
                                        <tyFields title="折扣比例：" :value="AadvierInfo.isDiscount==1 ? $format.toKeepPoint(AadvierInfo.discount) : ''"></tyFields>
                                    </iCol>
                                    <iCol span="12" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="合同编号：" :value="AadvierInfo.contractCode"></tyFields>
                                        <tyFields title="投放门店总数：" :value="AadvierInfo.totalStoreCount"></tyFields>
                                        <tyFields title="A类门店数量：" :value="AadvierInfo.storeACount"></tyFields>
                                        <tyFields title="B类门店数量：" :value="AadvierInfo.storeBCount"></tyFields>
                                        <tyFields title="C类门店数量：" :value="AadvierInfo.storeCCount"></tyFields>
                                    </iCol>
                                </iRow>
                            </div>
                            <div class="tabItemContent">
                                <div class="tabItemContentTitle">广告配置</div>
                                <iRow class="contractSetting">
                                    <iCol span="6" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="合同周期开始时间：" :value="AadvierInfo.startTimeOfContract"></tyFields>
                                        <tyFields title="广告开始时间：" :value="AadvierInfo.startTime"></tyFields>
                                        <tyFields title="投放广告位置：" :value="AadvierInfo.advertisementPosition"></tyFields>
                                        
                                    </iCol>
                                    <iCol span="6" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="合同周期结束时间：" :value="AadvierInfo.endTimeOfContract"></tyFields>
                                        <tyFields title="广告结束时间：" :value="AadvierInfo.endTime"></tyFields>
                                        <tyFields title="广告尺寸：" :value="AadvierInfo.advertisementSize"></tyFields>
                                    </iCol>
                                    <iCol span="6" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="合同周期(月)：" :value="AadvierInfo.contractPeriod"></tyFields>
                                        <tyFields title="广告投放周期(月)：" :value="AadvierInfo.advertisementPeriod"></tyFields>
                                        <tyFields title="广告时长：" :value="AadvierInfo.advertisementDuration "></tyFields>
                                    </iCol>
                                    <iCol span="6" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="已投放(月)：" :value="AadvierInfo.usedContractPeriod"></tyFields>
                                        <tyFields title=" " :value="''"></tyFields>
                                        <tyFields title="广告展示次数：" :value="AadvierInfo.getAdvertisementDisplayTimes"></tyFields>
                                    </iCol>
                                </iRow>
                                <iRow class="">
                                    <iCol span="24" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="广告类型：" :value="AadvierInfo.advertisementTypeName"></tyFields>
                                        <div class="fields fields_video_box">
                                            <div class="field_title">广告内容：</div> 
                                            <div class="field_video">
                                                <div v-if="AadvierInfo.advertisementType ==2" class="text_box">{{AadvierInfo.data}}</div>
                                                <div class="img_box" v-else @click="showBigFodder">
                                                    <img v-if="AadvierInfo.advertisementType ==1" :src="AadvierInfo.data" width="100%" height="100%">
                                                    <video v-if="AadvierInfo.advertisementType ==3"  :src="AadvierInfo.data"  width="100%" height="100%"></video>
                                                    <!-- <div data-v-04d867f4="" class="playIcon"><i data-v-04d867f4="" class="iconfont icon-cplay1"></i></div> -->
                                                    <div class="cover">
                                                        <div class="cover-box">
                                                            <i class="iconfont iconTextButton icon-chakan"></i>
                                                            <span>查看</span>
                                                        </div>
                                                    </div>
                                                    
                                                </div>
                                                
                                            </div>
                                        </div>
                                        
                                     </iCol>   
                                </iRow>
                            </div>
                            <div class="tabItemContent">
                                <div class="tabItemContentTitle">广告投放</div>
                                <iRow class="adSetting">
                                    <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="广告开始投放时间：" :value="AadvierInfo.effectiveStartTime"></tyFields>
                                        <tyFields title="广告下架时间：" :value="AadvierInfo.takeOffTime"></tyFields>
                                    </iCol>
                                    <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="广告结束投放时间：" :value="AadvierInfo.effectiveEndTime"></tyFields>
                                        <tyFields title="下架操作人：" :value="AadvierInfo.takeOffOperator"></tyFields>
                                    </iCol>
                                    <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="投放操作人：" :value="AadvierInfo.deliveryOperator"></tyFields>
                                    </iCol>
                                </iRow>
                                <iRow>
                                    <iCol span="24" class="clientBaseInfo_left clientBaseInfo_item">
                                        <tyFields title="广告下架备注：" :value="AadvierInfo.takeOffRemark"></tyFields>
                                    </iCol>
                                </iRow>
                            </div>
                            <div class="tabItemContent">
                                <div class="contractRemark">
                                    <div class="tabItemContentTitle">广告备注</div>
                                    <div class="contractRemarkContent" v-text="AadvierInfo.remark">
                                    </div>
                                </div>
                            </div>
                            <div class="tabItemContent">
                                <div class="tabItemContentTitle">广告投放地区</div>
                                <tyTableView 
                                :pageSizeOpts="[10, 20, 30]"
                                :number="true"
                                ref="tyTable"
                                :columns="columns1"
                                :url="url" 
                                :notDataText="'暂无数据'" 
                                :params="params">
                                </tyTableView>
                            </div>

                        </iPane>
                    </iTabs>
                </div>
            </div>
        </div>
        <auditModal ref="auditModal" @optionSuccess="optionSuccess"></auditModal>
        <showFodderModal ref="showFodderModal" :url="AadvierInfo.data" :type="AadvierInfo.advertisementType"></showFodderModal>
    </div>
</template>
<script>

import tyTableView from 'components/tyTableView';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import iTabs from 'iview/src/components/tabs';
import tyFields from 'components/tyFields';
import mixADState from './adState';
import mixinFunction from './mixinFunction.js';
import auditModal from './auditModal.vue';
import showFodderModal from './showFodderModal.vue';

const NO_DATA_ARRAY = ['暂无可上架的广告数据', '暂无匹配可上架的广告数据'];
export default {
    created() {

    },
    components: {
        iTabs,
        iRow,
        iCol,
        'iPane': iTabs.Pane,
        tyTableView,
        tyFields,
        auditModal,
        showFodderModal
    },
    mixins: [mixinFunction],
    mounted() {
       this.refresh();
    },
    methods: {
         refresh() {
            var ADid = this.$route.query.ADid;
            if (ADid || ADid === 0) {
                this.$get(this.$api.getAadvierInfo, {
                    id: ADid
                }).then((result) => {
                    this.AadvierInfo = result.data;
                }).catch((e) => {
                    this.$Message.error(e.message);
                })
                
            }
        },
        goback(){
            this.$router.replace({name: 'adServing'});
        },
        showBigFodder() {
            if (!this.AadvierInfo.data) {
                return
            }
            this.$refs.showFodderModal.toggle();
        },
        optionSuccess(){
            this.refresh();
        }
    },
    computed:{
        styleColor(){
            let className = ['#fbb321', '#f0857d', '#7c87d7', '#4cabe1', '#f13d2f', '#7edd9e', ''];
            let curr = className.length;
            switch (this.AadvierInfo.advertisementStatus){
                case this.ADState.pendingCommit:
                    curr = 0;
                    break;
                case this.ADState.pendingAuditing:
                    curr = 1;
                    break;
                case this.ADState.pendingDelivery:
                    curr = 2;
                    break;
                case this.ADState.deliverying:
                    curr = 3;
                    break;
                case this.ADState.abruptlyTerminated:
                    curr = 4;
                    break;
                case this.ADState.finished:
                    curr = 5;
                    break;
            }
            let obj = {
                background: className[curr]
            }
            return obj
        },
        ids(){
            return {
                contractId: this.AadvierInfo.contractId,
                customerId: this.AadvierInfo.customerId,
                id: this.AadvierInfo.id,
                advertisementName: this.AadvierInfo.advertisementName
            }
        },
        StyleCenter(){
            let status = this.AadvierInfo.advertisementStatus;
            let flag = false;
            // 如果是业务员 ， 在待审核状态上
            if(status == this.ADState.pendingAuditing && !this.$store.state.check(this.$m.adManage, this.$p.u) && !this.$store.state.check(this.$m.adManage, this.$p.audit)){
                flag = true;
            }
            if(status == this.ADState.abruptlyTerminated || status ==  this.ADState.finished){
                flag = true;
            }
            return {
                header_btnCenter: flag
            }
        }
    },
    data() {
        return {
           errorImg: require('assets/img/client/client_dafault_icon.png'),
           AadvierInfo: {},
           tabValue: 'base',//用于tab切换
           ADState: mixADState.Status,
           OptionStatus: mixADState.OptionStatus,
           url: this.$api.getAdAreaCountList,
           params: {
               id: this.$route.query.ADid
           },
           columns1: [
                {
                   title: '编号',
                   width: 220,
                   key: '_NUMBER_',
                   align: 'center'
                },
                { title: '城市名称', key: 'cityName', align: 'center', ellipsis: true, },
                { title: '所在省份', key: 'provinceName', align: 'center', ellipsis: true},
                { title: '投放门店数量', key: 'storeCount', align: 'center',  width: 220, ellipsis: true, },
           ]
        }
    }

}
</script>

<style lang="scss" scoped>
@import '~assets/css/base.scss';
.advierInfo {
    background-color: #f1f1f1;
    
    .tyTable{
        padding-top: 20px;
    }
    .header{
        .header_btnBox{
            right: 60px;
        }
        .header_btnCenter{
            bottom: 0;
            top: 50%;
            height: 35px;
            transform: translate3d(0,-50%, 0);
        }
    }
    .header_tool{
        .iconTextButton{
            margin-right: 0; 
        }
    }
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
        // background-color: #4cabe0;
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


.fields_video_box{
    display: flex;
    margin-top: 10px;
    .field_title{
        min-width: 70px;
        font-size: 14px;
        color: #999999;
        
    }
    .field_video{
        flex: 1;
        font-size: 14px;
    }
    .text_box{
        color: #333333;
    }
    .img_box{
        position: relative;
        width: 160px;
        height: 80px;
        cursor: pointer;
        transition: all 1s;
        .cover{
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            background-color: rgba(0, 0, 0, 0.6);
            color: #fff;
            display: none;
        
            .cover-box{
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate3d(-50%, -50%, 0);
                z-index: 99;
            }
            i{

            }
            span{

            }
        }
        &:hover {
            
            .cover{
                display: block;
            }
        }
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

.contractSetting {
    padding-top: 20px;
    .contractAgreementContent {
        font-size: 14px;
        color: #333333;
        max-height: 90px;
        margin-top: 20px;
    }
}

.adSetting{
    padding-top: 20px;
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
    
    .contractRemarkContent {
        min-height: 40px;
        font-size: 14px;
        color: #333333;
        // max-height: 90px;
        margin-top: 10px;
    }
}
</style>
<style  lang="scss">
.advierInfo {
    .field_title {
        width: 130px;
        text-align: right;
    }
    .contractAgreement .field_title {
        max-width: 120px;
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