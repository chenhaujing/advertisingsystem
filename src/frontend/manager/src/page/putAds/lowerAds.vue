<template>
    <div class="contractIndex">
        <searchAD @searchAD="searchAD"></searchAD>
        <div class="adStatusBox">
            <iRow :gutter="16">
                <iCol span="8" class="col">
                    <a class="topItem allItem" :class="{'action': ADState.deliverying == currTab}"  @click="changeTab(ADState.deliverying)">
                        <div class="topItemMain">
                            <div class="topItem-info">
                                <div class="topItem-info-number" v-text="statusToTal.deliveringTotals || 0"></div>
                                <div class="topItem-info-tag">投放中</div>
                            </div>
                            <div class="topItem-icon iconfont icon-toufangzhong"></div>
                        </div>
                    </a>
                </iCol>
                <iCol span="8" class="col">
                    <a class="topItem waitSignItem" :class="{'action': ADState.highRisk == currTab}"  @click="changeTab(ADState.highRisk)">
                        <div class="topItemMain">
                            <div class="topItem-info">
                                <div class="topItem-info-number" v-text="statusToTal.highRiskTotals || 0"></div>
                                <div class="topItem-info-tag">高风险投放</div>
                            </div>
                            <div class="topItem-icon iconfont icon-gaofengxiantoufang"></div>
                        </div>
                    </a>
                </iCol>
                <iCol span="8" class="col">
                    <a class="topItem waitVerifyItem" :class="{'action': ADState.abruptlyTerminated == currTab}"  @click="changeTab(ADState.abruptlyTerminated)">
                        <div class="topItemMain">
                            <div class="topItem-info">
                                <div class="topItem-info-number" v-text="statusToTal.takeOffTotals || 0"></div>
                                <div class="topItem-info-tag">已下架</div>
                            </div>
                            <div class="topItem-icon iconfont icon-yixiajia"></div>
                        </div>
                    </a>
                </iCol>
               
            </iRow>
        </div>
        <tyTableView 
        @dblclick="dblclickEvent"
        :pageSizeOpts="[15, 25, 35]"
        :height="760"
        :number="true"
        ref="tyTable"
        :columns="columns1"
        :url="url" 
        :notDataText="notDataText" 
        :params="params">
        </tyTableView>
        <auditModal ref="auditModal" @optionSuccess="refresh"></auditModal>
    </div>
</template>
<script>
import tyAddButton from 'components/tyAddButton';
import iCheckbox from 'iview/src/components/checkbox';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import { Select as iSelect, Option as iOption }  from 'iview/src/components/select';
import tySearchInput from 'components/tySearchInput';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';
import searchAD from './searchAD.vue';
import mixADState from './adState';
import mixinFunction from './mixinFunction.js';
import auditModal from './auditModal';

const NO_MATE_DATA_ARRAY = ['暂无匹配可下架的广告数据', '暂无匹配可下架的高风险投放广告数据', '暂无匹配已下架的广告数据'];
const NO_DATA_ARRAY = ['暂无可下架的广告数据', '暂无可下架的高风险投放广告数据', '暂无已下架的广告数据'];
export default {

    created() {

    },
    components: {
        tyTableView,
        tyAddButton,
        tyIconTextButton,
        iRow,
        iCol,
        iSelect,
        iOption,
        tySearchInput,
        searchAD,
        auditModal
    },
    mixins: [mixinFunction],
    mounted() {
       this.getAdListTotal()
    },
    methods: {
        // 获取广告列表数量
        getAdListTotal(){
            this.$post(this.$api.getDeliverStadtistics, this.params).then((result) => {
                this.statusToTal = result.data[0];
            }).catch((e) => {
                this.$Notice.error({
                    title: '错误',
                    desc: e.message
                })
            })
        },
        // 广告的关键字搜索
        searchAD(form){
            this.clearSearchKeyWord();
            this.searchKey = form.selectKey;
            switch (form.selectValue){
                case 1:
                    this.params.advertisementName = form.selectKey;
                    break;
                case 2:
                    this.params.customerName = form.selectKey; 
                    break;
                case 3:
                    this.params.contractName = form.selectKey;
                    break;
            }
            this.noDatatextEvent();
            this.getAdListTotal();
            this.$refs.tyTable.setParams(this.params);
            this.$refs.tyTable.refresh();
            
        },
        noDatatextEvent(){
            let index = 0;
            switch (this.currTab){
                case this.ADState.deliverying:
                    index = 0;
                    break;
                case this.ADState.highRisk:
                    index = 1;
                    break;
                case this.ADState.abruptlyTerminated:
                    index = 2;
                    break;    
            }
            if(this.searchKey){
                this.notDataText = this.NO_MATE_DATA_ARRAY[index];
            }else{
                this.notDataText = this.NO_DATA_ARRAY[index];
            }
        },
        refresh(){
         this.$refs.tyTable.refresh();
         this.getAdListTotal();
        },
        // 更改广告状态列表
        changeTab(status, obj){
            if(this.currTab == status){
                return;
            }
            this.currTab = status;
            this.params.advertisementStatus = status;
            if(status == this.ADState.highRisk){
                this.params.highRisk = true;
                this.params.advertisementStatus = this.ADState.deliverying;
            }else{
                this.params.highRisk = false;
            }
            this.noDatatextEvent();
            this.$refs.tyTable.setParams(this.params);
            this.$refs.tyTable.refresh();
        },
      
    },
    data() {
        return {
            statusToTal: {},
            notDataText: NO_DATA_ARRAY[0],
            NO_DATA_ARRAY: NO_DATA_ARRAY,
            NO_MATE_DATA_ARRAY: NO_MATE_DATA_ARRAY,
            currTab: mixADState.Status.deliverying,
            ADState: mixADState.Status,
            tableRef:'tyTable',
            searchKey: '',
            params: {
                advertisementName: '',
                contractName: '',
                customerName: '',
                advertisementStatus: mixADState.Status.deliverying
            },
            url: this.$api.getAdListUrl,    
        }
    }

}
</script>

<style lang="scss" scoped>
@import '~assets/css/base.scss';
%mt20 {
    margin-top: 20px;
}
// 待审核
$pendingAuditing: rgba(240,133,125,1);
// 待投放
$pendingDelivery: rgba(252,179,34,1);
// 投放中
$deliverying: rgba(126,221,156,1);
.tr {
    text-align: right;
    margin-bottom: 15px;
}

.tr .tyAddButton {
    width: 160px;
}

.tyAddButton:hover {
    border-color: $mainColor;
}

.adStatusBox{
    margin-top: 20px;
    margin-bottom: 20px;
}

.topItem {
    display: block;
    height: 130px;
    background-color: #ffffff;

}
.topItemMain {
    margin-left: 30px;
    margin-right: 10px;
    position: relative;
    display: flex;
    justify-content: space-around;
    align-items: center;
    height: 90px;
    top: 50%;
    transform: translateY(-50%);
    .topItem-icon{
        font-size: 80px;
        color: #e4e7ea;
    }
    .topItem-info {
        text-align: center;
        color: #333333;
        // margin-top: -10px;
        .topItem-info-number {
            font-size: 30px;
        }
        .topItem-info-tag {
            font-size: 16px;
        }
    }
    
}

.allItem{
    .topItem-info-number{
        color: $mainColor;
    }
    
}
.waitVerifyItem{
    .topItem-info-number{
    color: $pendingAuditing;
    }
}

.waitSignItem{
    .topItem-info-number{
    color: $pendingDelivery;
    }
}

.waitRunItem{
    .topItem-info-number{
    color: $deliverying;
    }
}

// 选中状态样式
.action {
    .topItem-icon{
         color: rgba(255,255,255, 0.2);  
    }
    .topItem-info {
        color: #fff;
    }
    .topItem-info-number{
        color: #fff;
    }
    &.allItem {
        background-color: $mainColor;   
    }
    &.waitVerifyItem {
        background-color: $pendingAuditing;
    }
    &.waitSignItem {
        background-color: $pendingDelivery;
    }
    &.waitRunItem {
        background-color: $deliverying;
    }
    .topItem-info {
        color: #ffffff;
    } // 全部合同
    .topItem-icon-all {
        background-color: #ffffff;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_all_active.png');
        }
    } // 待审核
    .topItem-icon-waitVerify {
        background-color: #ffffff;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_waitVerify_active.png');
        }
    } // 待签约
    .topItem-icon-waitSign {
        background-color: #ffffff;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_waitSign_active.png');
        }
    }
    .topItem-icon-waitRun {
        background-color: #ffffff;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_waitRun_active.png');
        }
    }
}

</style>


