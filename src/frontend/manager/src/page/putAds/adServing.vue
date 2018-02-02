<template>
    <div class="contractIndex">
        
        <div class="tr">
            <tyAddButton class="tyAddButton" text="新建广告" @click.native="addAD"></tyAddButton>
        </div>
        <searchAD @searchAD="searchAD"></searchAD>
        <div class="adStatusBox">
            <iRow :gutter="16">
                <iCol span="6" class="col">
                    <a class="topItem allItem" :class="{'action': ADState.all == currTab}"  @click="changeTab(ADState.all)">
                        <div class="topItemMain">
                            <div class="topItem-info">
                                <div class="topItem-info-number" v-text="statusToTal.total"></div>
                                <div class="topItem-info-tag">全部广告</div>
                            </div>
                        </div>
                    </a>
                </iCol>
                <iCol span="6" class="col">
                    <a class="topItem waitVerifyItem" 
                    :class="{'action': ADState.pendingAuditing == currTab}" 
                     @click="changeTab(ADState.pendingAuditing)">
                        <div class="topItemMain">
                            <div class="topItem-info">
                                <div class="topItem-info-number" v-text="statusToTal.pendingAuditing"></div>
                                <div class="topItem-info-tag">待审核</div>
                            </div>
                        </div>
                    </a>
                </iCol>
                <iCol span="6" class="col">
                    <a class="topItem waitSignItem"
                    :class="{'action': ADState.pendingDelivery == currTab}" 
                    @click="changeTab(ADState.pendingDelivery )">
                        <div class="topItemMain">
                            <div class="topItem-info">
                                <div class="topItem-info-number" v-text="statusToTal.pendingDelivery"></div>
                                <div class="topItem-info-tag">待投放</div>
                            </div>
                        </div>
                    </a>
                </iCol>
                <iCol span="6" class="col">
                    <a class="topItem waitRunItem"
                    :class="{'action': ADState.deliverying == currTab}" 
                    @click="changeTab(ADState.deliverying)">
                        <div class="topItemMain">
                            <div class="topItem-info">
                                <div class="topItem-info-number" v-text="statusToTal.deliverying"></div>
                                <div class="topItem-info-tag">投放中</div>
                            </div>
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

const NO_DATA_ARRAY = ['暂无广告数据', '暂无匹配的广告数据'];
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
            this.$post(this.$api.getAdListTotal, this.params).then((result) => {
                this.statusToTal = result.data;
            }).catch((e) => {
                this.$Notice.error({
                    title: '错误',
                    desc: e.message
                })
            })
        },
        // 新建广告
        addAD() {
             this.$router.push({name:"selContract"})
        },
        // 广告的关键字搜索
        searchAD(form){
            this.clearSearchKeyWord()
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
            
            if(form.selectKey){
                this.notDataText = this.NO_DATA_ARRAY[1];
            }else{
                this.notDataText = this.NO_DATA_ARRAY[0];
            }
            this.$refs.tyTable.refresh();
            this.getAdListTotal();
        },
        refresh(){
             this.$refs.tyTable.refresh();
             this.getAdListTotal();
        },
        // 更改广告状态列表
        changeTab(status){
            if(this.currTab == status){
                return;
            }
            this.currTab = status;
            this.params.advertisementStatus = status;

            this.$refs.tyTable.refresh();
        },
    },
    data() {
        return {
            statusToTal: {},
            notDataText: NO_DATA_ARRAY[0],
            NO_DATA_ARRAY: NO_DATA_ARRAY,
            currTab: 0,
            ADState: mixADState.Status,
            tableRef:'tyTable',
            params: {
                advertisementName: '',
                contractName: '',
                customerName: '',
                advertisementStatus: mixADState.Status.all
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
    transition: .3s;
    display: block;
    height: 130px;
    background-color: #ffffff;
}
.topItemMain {
    width: 160px;
    margin: 0 auto;
    position: relative;
    position: relative;
    height: 70px;
    top: 50%;
    transform: translateY(-50%);

    .topItem-info {
        text-align: center;
        color: #333333;
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


