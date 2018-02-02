<template>
    <div class="clientInfo">
        <div class="header">
            <div class="header_title">客户信息</div>
            <div class="header_content">
                <div class="heaer_logo">
                    <img :src="baseInfo.headPortrait" v-imgError="errorImg" />
                </div>
                <div class="header_name" v-text="baseInfo.name"></div>
                <div class="header_tool">
                    <tyIconTextButton v-if="$store.state.check($m.clientMng,$p.u)" iconClass="icon-bianji" text="编辑" @click.native="goToEdit"></tyIconTextButton>
                    <tyIconTextButton v-if="$store.state.check($m.clientMng,$p.allocation)" iconClass="icon-fenpei" text="分配" @click.native="allocation"></tyIconTextButton>
                    <tyIconTextButton v-if="$store.state.check($m.clientMng,$p.d)" iconClass="icon-laji" text="删除" @click.native="deleteClient"></tyIconTextButton>
                    <tyIconTextButton v-if="$store.state.check($m.contract,$p.c)" iconClass="icon-add" text="新建合同" @click.native="newContract"></tyIconTextButton>
                </div>
            </div>
        </div>
        <div class="mainContent">
            <div class="content">
                <iTabs v-model="tabValue">
                    <iPane label="基本详情" name="base" class="tabItemPane">
                        <div class="tabItemContent">
                            <!-- 客户 基础信息模块 -->
                            <iRow class="clientBaseInfo">
                                <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                    <tyFields title="客户编号：" :value="baseInfo.customerNumber "></tyFields>
                                    <tyFields title="联系人：" :value="baseInfo.contacts"></tyFields>
                                    <tyFields title="联系电话：" :value="baseInfo.contactNumber"></tyFields>
                                    <tyFields title="邮箱：" :value="baseInfo.mailAdress"></tyFields>
                                    <tyFields title="详细地址：" :value="baseInfo.adressDetail"></tyFields>
                                </iCol>
                                <iCol span="8" class="clientBaseInfo_center clientBaseInfo_item">
                                    <tyFields title="所在省份：" :value="baseInfo.provinceName"></tyFields>
                                    <tyFields title="所在城市：" :value="baseInfo.cityName "></tyFields>
                                    <tyFields title="从属行业：" :value="baseInfo.industry"></tyFields>
                                    <tyFields title="子行业：" :value="baseInfo.subIndustry"></tyFields>
                                    <tyFields title="维护人员：" :value="baseInfo.ownerName"></tyFields>
                                </iCol>
                                <iCol span="8" class="clientBaseInfo_right clientBaseInfo_item">
                                    <tyFields title="是否正在投放广告：" :value="baseInfo.delivering ? '是' : '否'"></tyFields>
                                    <tyFields title="累积投放广告次数：" :value="baseInfo.advertisementDeliveryTimes"></tyFields>
                                    <tyFields title="累积投放广告金额：" :value="$format.toKeepPoint(baseInfo.advertisementTotalAmount)"></tyFields>
                                    <tyFields title="最后投放广告时间：" :value="baseInfo.lastestDeliveryTime "></tyFields>
                                </iCol>
                            </iRow>
                            
                        </div>
                        <!-- 客户备注 -->
                        <div class="tabItemContent">
                            <div class="clientRemark">
                                <div class="remarkTitle">备注：</div>
                                <div class="clientRemarkContent" v-text="baseInfo.remark"></div>
                            </div>
                        </div>
                        <tyDivider title="投放广告" class="divider"></tyDivider>
                        <tyTable 
                        :columns="columns1" 
                        :url="url1" 
                        @dblclick="dblclickEvent"
                        :number="true"
                        :params="params"
                        :height="556" 
                        notDataText="该客户尚无投放广告数据">
                        </tyTable>
                    </iPane>
                    <iPane label="合同信息" name="contract" class="tabItemPane">
                        <div class="tabItemContent">
                            <iRow class="clientBaseInfo">
                                <iCol span="8" class="clientBaseInfo_left clientBaseInfo_item">
                                    <tyFields title="客户编号：" :value="baseInfo.customerNumber "></tyFields>
                                    <tyFields title="联系人：" :value="baseInfo.contacts"></tyFields>
                                    <tyFields title="联系电话：" :value="baseInfo.contactNumber"></tyFields>
                                    <tyFields title="邮箱：" :value="baseInfo.mailAdress"></tyFields>
                                    <tyFields title="详细地址：" :value="baseInfo.adressDetail"></tyFields>
                                </iCol>
                                <iCol span="8" class="clientBaseInfo_center clientBaseInfo_item">
                                    <tyFields title="所在省份：" :value="baseInfo.provinceName"></tyFields>
                                    <tyFields title="所在城市：" :value="baseInfo.cityName"></tyFields>
                                    <tyFields title="从属行业：" :value="baseInfo.industry"></tyFields>
                                    <tyFields title="子行业：" :value="baseInfo.subIndustry"></tyFields>
                                    <tyFields title="维护人员：" :value="baseInfo.ownerName"></tyFields>
                                </iCol>
                                <iCol span="8" class="clientBaseInfo_right clientBaseInfo_item">
                                    <tyFields title="是否正在投放广告：" :value="baseInfo.delivering ? '是' : '否'"></tyFields>
                                    <tyFields title="累积投放广告次数：" :value="baseInfo.advertisementDeliveryTimes"></tyFields>
                                    <tyFields title="累积投放广告金额：" :value="$format.toKeepPoint(baseInfo.advertisementTotalAmount)"></tyFields>
                                    <tyFields title="最后投放广告时间：" :value="baseInfo.lastestDeliveryTime "></tyFields>
                                </iCol>
                            </iRow>
                        </div>
                        <tyDivider title="广告合同" class="divider"></tyDivider>
                        <tyTable 
                        :columns="columns2"
                        :url="url2" 
                        :height="556" 
                        @dblclick="lookContractInfo"
                        :number="true"
                        notDataText="该客户尚无广告合同数据">
                        </tyTable>
                    </iPane>
                    <iPane label="广告素材" name="ads" class="tabItemPane adsSoures">
                        <tyTable 
                        :columns="columns3" 
                        :number="true"
                        @dblclick="lookADMateria"
                        :url="url3" 
                        :params="params"
                        class="adsSourceTable"
                        :height="556" 
                        notDataText="该客户尚无广告素材数据">
                        </tyTable>
                    </iPane>
                </iTabs>
            </div>
        </div>
        <tyAllocatModal modalTitle="分配顾客" ref="modal" :userInfoData="baseInfo"></tyAllocatModal>
        <showFodderModal ref="showFodderModal" :url="selectFileUrl" :type="selectType"></showFodderModal>
    </div>
</template>

<script>
import tyAllocatModal from 'components/tyAllocatModal';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import iTabs from 'iview/src/components/tabs';
import tyTable from 'components/tyTableView';
import tyFields from 'components/tyFields';
import tyDivider from 'components/tyDivider';
import tyIconTextButton from 'components/tyIconTextButton';
import showFodderModal from '../putAds/showFodderModal.vue';
const materialTypeName = {
    TEXT: '文本',
    IMG: '图片',
    VIDEO: '视频',
}
export default {
    mounted() {
        var self = this;
        //获取客户基本信息及其请求
        var baseInfoUrl = self.$api.clientInfo + self.$router.apps[0]._route.query.clientId;
        var baseInfoParams = {
            id: self.$router.apps[0]._route.query.clientId
        };
        self.$get(this.$api.clientInfo, {
            id: self.$router.apps[0]._route.query.clientId
        }).then((result) => {
            self.baseInfo = result.data;
        }).catch((e) => {
        });
    },
    methods: {
        deleteClient() {
            this.$Modal.confirm({
                title: '删除提示',
                content: '<p>你确定要删除"'+this.baseInfo.name+'"选项吗？</p>',
                onOk: () => {
                    this.$post(this.$api.deleteClientInfo,{},{}, {
                            id: this.baseInfo.id
                        }).then((result) => {
                        this.$router.go(-1);
                        this.$Notice.success({
                            title: '提示',
                            desc: '删除成功'
                        });
                    }).catch((e) => {
                        this.$Message.error(e.message);
                    });
                },
                onCancel: () => {
                }
            });
        },
        newContract() {
            this.$router.push({
                name: 'addContract',
                query: {
                    clientId: this.baseInfo.id
                }
            })
        },
        allocation() {
            this.$refs.modal.show();
        },
        goToEdit() {
            var clientId = this.$router.apps[0]._route.query.clientId;
            this.$router.push({ name: 'editClient', query: { clientId: clientId } })
        },
        // 查看广告详情
        dblclickEvent(row){
            if(!row.canView){
                return
            }
             this.$router.push({ name: 'advierInfo', query: { ADid: row.id } });
        },
        // 查看广告素材
        lookADMateria(row){
            let url = '';
            let type = '';
            if(row.materialTypeName == materialTypeName.TEXT){
                url = row.materialName
                type = 2;
            }else if(row.materialTypeName == materialTypeName.IMG){
                url = row.url;
                type = 1;
            }else if(row.materialTypeName == materialTypeName.VIDEO){
                url = row.url;
                type = 3;
            }
            this.selectFileUrl = url;
            this.selectType = type;
            if (!this.selectFileUrl) {
                return
            }
            this.$refs.showFodderModal.toggle();
        },
        // 查看合同详情
        lookContractInfo(row){
           if(!row.canView){
                return
            }
            this.$router.push({ name: 'contractInfo', query: { cid: row.id } })
        }
    },
    data() {
        return {
            params: {
                customerId: this.$route.query.clientId
            },
            selectFileUrl: '',
            selectType: '',
            //客户基础信息
            baseInfo: {},
            //客户头像加载错误时默认显示的图片
            errorImg: require('assets/img/client/client_dafault_icon.png'),
            // 投放广告列表
            columns1: [
                {
                    title: '编号',
                    width: 60,
                    key: '_NUMBER_',
                    align: 'center',
                },
                { title: '广告名称', key: 'advertisementName', align: 'center' },
                { title: '广告类型', key: 'advertisementType', align: 'center' },
                { title: '合同名称', key: 'contractName', align: 'center' },
                { title: '状态', key: 'advertisementStatusName', align: 'center' },
                { title: '开始时间', key: 'startTime', align: 'center',},
                { title: '结束时间', key: 'endTime', align: 'center'},
                { title: '维护人', key: 'owner', align: 'center' },
                {
                    title: '操作', key: 'keepPerson',width:120, align: 'center',
                    render: (h, params) => {
                        return h(tyIconTextButton, {
                            props: {
                                text: '查看',
                                iconClass: 'icon-yanjing',
                                disable:!params.row.canView
                            },
                            nativeOn: {
                                click: () => {
                                    this.dblclickEvent(params.row); 
                                }
                            }
                        })
                    }
                }
            ],
            // 合同列表
            columns2: [
                 {
                    title: '编号',
                    width: 60,
                    key: '_NUMBER_',
                    align: 'center',
                },
                { title: '合同名称', key: 'contractName', align: 'center' },
                { title: '合同编号', key: 'contractCode', align: 'center' },
                { title: '状态', key: 'contractStatus', align: 'center' },
                { title: '开始时间', key: 'startTime', align: 'center'},
                { title: '结束时间', key: 'endTime', align: 'center'},
                { title: '维护人', key: 'owner', align: 'center' },
                { 
                    title: '总额', 
                    key: 'totalCost', 
                    align: 'center',
                    render:(h, params)=>{
                        var text = this.$format.toKeepPoint(params.row.totalCost);
                        return h('span', text);
                    }
                },
                {
                    title: '操作', key: 'keepPerson',width:120, align: 'center',
                    render: (h, params) => {
                        return h(tyIconTextButton, {
                            props: {
                                text: '查看',
                                iconClass: 'icon-yanjing',
                                disable:!params.row.canView
                            },
                            nativeOn: {
                                click: () => {
                                    this.lookContractInfo(params.row)
                                }
                            },
                        })
                    }
                }
            ],
            // 广告素材列表
            columns3: [
                {   
                    title: '素材', 
                    align: 'center',
                    render: (h, params) => {
                        let img = '' 
                        let html =''
                        if(params.row.materialTypeName == materialTypeName.TEXT){
                            return;    
                        }else if(params.row.materialTypeName == materialTypeName.IMG){
                            img = params.row.url;
                            
                           
                        }else if(params.row.materialTypeName == materialTypeName.VIDEO){
                            img = require('../../assets/img/putAds/video.png');
                            
                        }
                        html = 'img'
                        return h(html, {
                            attrs: {
                                src: img,
                                width: 40,
                                height: 40
                            },
                        })
                    } 
                },
                { 
                    title: '素材名称', 
                    key: 'materialName', 
                    align: 'center',
                    render: (h, params) => {
                        let text = params.row.materialName;
                        if(params.row.materialTypeName == materialTypeName.TEXT){
                           return h('span',{
                                'class': {
                                    'ivu-table-cell-ellipsis': true,
                                },
                                domProps: {
                                    innerHTML: text
                                },
                            })
                        }      
                        return h('span', text);
                    }
                },
                { title: '素材类型', key: 'materialTypeName', align: 'center' },
                { 
                    title: '素材宽高', 
                    key: 'materialSpecification', 
                    align: 'center',
                    render: (h, params) => {
                        if(params.row.materialTypeName == materialTypeName.IMG){
                            let text = params.row.materialSpecification;
                            return h('span', text)
                        }
                    }
                },
                { 
                    title: '素材大小', 
                    key: 'materialSize', 
                    align: 'center',
                    render: (h, params) => {
                        if(params.row.materialTypeName == materialTypeName.TEXT){
                           return;
                        }
                        let text = params.row.materialSize; 
                        return h('span', text)
                    }
                },
                { title: '上传时间', key: 'createdTime', align: 'center' },
                { title: '上传人', key: 'creatorName', align: 'center' },
                {
                    title: '操作', key: 'keepPerson',width:120, align: 'center',
                    render: (h, params) => {
                        return h(tyIconTextButton, {
                            props: {
                                text: '查看',
                                iconClass: 'icon-yanjing'
                            },
                            nativeOn: {
                                click: () => {
                                    this.lookADMateria(params.row);
                                }
                            }
                        })
                    }
                }
            ],
            url1: {},
            url2: {},
            url3: {},
            tabValue: 'base',//用于tab切换
        }
    },
    created(){
        
        this.url1 = this.$api.customerADList
        this.url2 = this.$api.getContractDetailList.replace(/\{customerId\}/,this.$route.query.clientId)
        this.url3 = this.$api.customerMaterialList
    },
    watch: {
    },
    components: {
        tyAllocatModal,
        tyDivider,
        iTabs,
        'iPane': iTabs.Pane,
        iRow,
        iCol,
        tyFields,
        tyTable,
        tyIconTextButton,
        showFodderModal
    }
}
</script>
<style>
.clientInfo .ivu-tabs-nav .ivu-tabs-tab {
    font-size: 16px;
}

.clientInfo .ivu-tabs-bar {
    margin-bottom: 0;
    background-color: #ffffff;
}

.clientInfo .ivu-tabs {
    width: 100%;
}

.clientInfo .ivu-tabs-nav-container {
    padding-left: 20px;
}

.clientInfo .field_title {
    text-align: right;
    
}
.clientInfo  .field_value{
    width: auto !important;
}
</style>
<style lang="scss" scoped>
@import '~assets/css/base.scss';

.clientInfo {
    background-color: #f1f1f1;
}

.mainContent {
    .tabItemPane {
        box-sizing: border-box;
        background-color: #f1f1f1; // 标签tab主内容区
        .tabItemContent {
            margin-top: 20px;
            padding: 20px;
            background-color: #ffffff;
        }
    }
}

// 客户信息  基本详情模块
.clientBaseInfo {
    height: 192px;
    .clientBaseInfo_item {
        float: left;
    }
}

// 客户备注模块
.clientRemark {
    height: 140px;
    .remarkTitle {
        font-size: 14px;
        color: #999999;
        margin-bottom: 10px;
    }
    .clientRemarkContent {
        font-size: 14px;
        color: #333333;
        max-height: 90px;
    }
}

// 投放广告分割线
.divider {
    width: 507px;
    margin: 0 auto;
}

.tabAdsContent {
    height: 600px;
}

// 广告素材tabView
.adsSourceTable {
    margin-top: 20px;
}

.header {
    padding: 20px 20px 0 20px;
    background-color: #ffffff;
    $headerContentHeight: 90px; // 顶部标题
    .header_title {
        font-size: 18px;
        color: rgba(153, 153, 153, 1);
    } // 客户名称内容
    .header_content {
        position: relative;
        height: $headerContentHeight;
    } // 客户LOGO
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
    } // 客户名称
    .header_name {
        float: left;
        margin-left: 75px;
        line-height: $headerContentHeight;
        font-size: 18px;
        color: #333333;
    } // 顶部按钮
    .header_tool {
        line-height: $headerContentHeight;
        float: right;
    }
    .iconTextButton {
        margin-right: 20px;
    }
}

</style>
