<template>
    <div class="upperAdsBox">
        <searchAD @searchAD="searchAD"></searchAD>
        <div class="upperAdsMain">   
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
         </div>
    </div>
</template>
<script>

import tyTableView from 'components/tyTableView';
import searchAD from './searchAD.vue';
import mixADState from './adState';
import mixinFunction from './mixinFunction.js';

const NO_DATA_ARRAY = ['暂无可上架的广告数据', '暂无匹配可上架的广告数据'];
export default {
    created() {

    },
    components: {
        tyTableView,
        searchAD
    },
    mixins: [mixinFunction],
    mounted() {
       
    },
    methods: {
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
            this.$refs.tyTable.setParams(this.params);
            this.$refs.tyTable.refresh();
        },
        refresh(){
              this.$refs.tyTable.refresh();
        },
        putAds(){

        }
    },
    data() {
        return {
            notDataText: NO_DATA_ARRAY[0],
            NO_DATA_ARRAY: NO_DATA_ARRAY,
            ADState: mixADState.Status,
            tableRef:'tyTable',
            params:{
                advertisementName: '',
                contractName: '',
                customerName: '',
                advertisementStatus: mixADState.Status.pendingDelivery
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
.upperAdsBox{
    margin-bottom: 15px;
}
.upperAdsMain{
    margin-top: 20px;
}

</style>


