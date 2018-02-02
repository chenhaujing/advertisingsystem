<template>
    <div ref="tyTable" class="tyTable">
        <i-Table @on-select-cancel="selectCancel" @on-select="select" 
        @on-selection-change="selectChange" :data="dataList" 
        :columns="columns" :height="height"  
        :width="width" :no-data-text="loadingText && !notAutoLoad ? loadingText : notDataText" @on-row-dblclick="dblclick"></i-Table>
        <div class="bottomPage" :style="{ width: width + 'px' }">
            <i-page :page-size="pageParams.pageSize" ref="page" :page-size-opts="pageSizeOpts" @on-change="pageChange" @on-page-size-change="pageSizeChange" class="page" :total="total" :key="total" placement="top" size="small" show-sizer show-total>
            </i-page>
        </div>
    </div>
</template>

<script>
import Table from 'iview/src/components/table';
import Page from 'iview/src/components/page';
const util = {
    assign: function(){
     
    }
}
// console.log(util.assign(1,2,3))
export default {
    name: 'ty-table',
    /**
       * beforeCallback： 在请求结束后，对返回的数据进行处理后再绑定到table上
       */
    props: ['beforeCallback', 'notAutoLoad','number', 'data', 'columns', 'url', 'pageSizeOpts', 'height', 'notDataText', 'params', 'width','optionBtn'],
    created() {
        if(this.params){
            this.pageParams = Object.assign(this.params, this.pageParams);
        }
    },
    data() {
        return {
            loadingText: '加载中...',
            urlParams: null,
            total: 0,
            dataList: [],
            pervData:{},
            pageParams: {
                pageIndex: 0,
                pageSize: (this.pageSizeOpts && this.pageSizeOpts[0]) || 20
            }
        }
    },
    watch: {
        'params': function () {
            this.pageParams = Object.assign(this.params, this.pageParams);
        }
    },
    methods: {
        getAllRows() {
            return this.dataList;
        },
        selectCancel(selection, row) {
            this.$emit('selectCancel', {
                selection: selection,
                row: row
            })
        },
        select(selection, row) {
            this.$emit('select', {
                selection: selection,
                row: row
            })
        },
        selectChange(selection) {
            this.$emit('on-selection-change', selection)
        },
        clearData() {
            this.urlParams = null;
            this.total = 0;
            this.dataList = [];
            this.pageParams = {
                pageIndex: 0,
                pageSize: 20
            }
        },
        setUrlParams(urlParams) {
            this.urlParams = urlParams;
        },
        updateUrl(url) {
            this.url = url;
        },
        dblclick(row) {
            this.$emit('dblclick', row);
        },
        setParams(p) {
            this.pageParams = Object.assign(p, this.pageParams);
        },
        pageChange(pageIndex) {
            var pageSize=this.pageParams.pageSize;
            //用来缓存上次搜索的数据
            if(!(JSON.stringify(this.pervData) == "{}")){
               Object.assign(this.pageParams,this.pervData) 
            }
            this.pageParams.pageSize = pageSize;
            this.pageParams.pageIndex = pageIndex - 1;
            this.getData();
        },
        pageSizeChange(pageSize) {
            var pageIndex=this.pageParams.pageIndex;
            //用来缓存上次搜索的数据
             if(!(JSON.stringify(this.pervData) == "{}")){
               Object.assign(this.pageParams,this.pervData) 
            }
            this.pageParams.pageSize = pageSize;
            this.getData();
        },
        // 调用该方法刷新数据
        refresh() {
            this.pageParams.pageIndex=0;
            this.total=0;
            this.getData();
        },
        // noPostRefresh(seletction){
        //         this.dataList.forEach((cur,index)=>{
        //         if(seletction.id===cur.id){
        //            cur.usedCount=100
        //         }
        //     })
        // },
        getData() {
            this.$post(this.url, this.pageParams, {}, this.urlParams).then((result) => {
                this.loadingText = '';
                if (this.beforeCallback) {
                    this.beforeCallback(result.data.list);
                }
                Object.assign(this.pervData,this.pageParams)
                if(!result.data || !result.data.list){
                    this.dataList = [];
                }else{
                     // 编号配置
                    if(this.number && result.data.list.length>0){
                        let number = '_NUMBER_'
                        result.data.list.forEach(function(item, index) {
                            item[number] = this.pageParams.pageSize*this.pageParams.pageIndex+Number(index)+1;
                        }, this);
                    }
                    this.dataList = result.data.list;
                }
               
                this.total = result.data.totalElement;
                // 数据加载完成事件
                this.$emit('loadedEvent')
            }).catch((e) => {
                this.loadingText = '';
                this.$emit('loadedEvent')
                this.$Message.info(e.message);
            })
        }
    },
    mounted() {
        // this.height = this.$refs.tyTable.offsetHeight;
        if (!this.notAutoLoad) {
            this.getData();
        }

    },
    components: {
        'i-Table': Table,
        'i-page': Page
    }
}
</script>
<style>
td .ivu-table,
.ivu-table-tip {
    color: #666666;
    font-size: 14px;
}
.ivu-select{
    position: relative
}
</style>

<style lang="scss" scoped>
.tyTable {
    padding-bottom: 30px;
    height: 100%;
}

.bottomPage {
    padding-top:25px; 
    .page {
        float: right;
    }
}
</style>
