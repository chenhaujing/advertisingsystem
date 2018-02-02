<template>
    <div class="selContract">
        <iRow :gutter="20">
            <!-- 客户数据 -->
            <iCol span="6">
                <div class="leftTable">
                    <div class="titleBox">
                        <h4 class="title">广告客户</h4>
                        <tySearchInput @search="clientTableSearch"  placeholder="请输入广告客户名称" class="searchInput" v-model="clientTableParams.customerName"></tySearchInput>
                    </div>
                    <tyTableView ref="clientTable" :columns="clientTableColumns" :url="clientTableUrl" :pageSizeOpts="[15, 25, 35]" notDataText="暂无匹配的广告客户数据" :height="760" :params="clientTableParams" @on-selection-change="getPendingExecutionContracts"> </tyTableView>
                </div>
            </iCol>

            <iCol span="18">
                <div class="rightTable">

                    <div class="titleBox">

                        <h3 class="title">广告合同</h3>
                        <!-- <searchAD class="selectInputBox" :selectList="selData" 
                        :selectLabelArray="selectLabelArray" 
                        @searchAD="contractTableSearch"></searchAD> -->
                        <iRow>
                            <iCol span="4" class="selectInput">
                                <iSelect v-model="selType">
                                    <iOption v-for="data in selData" :key="data.value" :value="data.value">
                                        {{ data.label }}
                                    </iOption>
                                </iSelect>
                            </iCol>
                            <iCol span="6">
                                <tySearchInput @search="contractTableSearch" class="searchInput" :placeholder="selType==1?'请输入广告合同名称':'请输入广告合同编号'" v-model="searchTempData"></tySearchInput>
                            </iCol>
                        </iRow>
                    </div>
                    <!-- 合同数据 -->
                    <tyTableView ref="contractTable" 
                    :number="true"
                    :columns="contractTableColumns" :url="contractTableUrl" :height="760" :pageSizeOpts="[15, 25, 35]" notDataText="暂无匹配的广告合同数据" :params="contractTableParams"> </tyTableView>
                </div>
            </iCol>
        </iRow>
    </div>
</template>
<script>
import iModal from 'iview/src/components/modal';
import tySearchInput from 'components/tySearchInput';
import tyAddButton from 'components/tyAddButton';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';
import searchAD from './searchAD.vue';
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';

export default {
    data() {
        return {
            selData: [
                { label: "合同名称", value: 1 },
                { label: "合同编号", value: 2 },
            ],
            selectLabelArray: ["请输入广告合同名称", "请输入广告合同编号"],
            selType:1,
            clientTableUrl: this.$api.getCustomerContractStatistic,
            contractTableUrl: this.$api.getPendingExecutionContracts,
            searchTempData:'',
            clientTableParams: {
                customerName:'',
            },
            contractTableParams: {
                contractCode: '',
                contractName: '',
                contractStatus: 0,
                customerIds: [],
            },
            clientTableColumns: [
                {
                    type: 'selection',
                    align: 'center',
                    label: true,
                },
                { title: '客户名称', key: 'customerName', align: 'center'},
                { title: '待执行合同数量', key: 'pendingExecutionContractCount', align: 'center',width:110},

            ],
            contractTableColumns: [
                { title: '编号', key: '_NUMBER_' , align: 'center', width: 60 },
                { title: '合同编号', key: 'contractCode', align: 'center' },
                { title: '合同名称', key: 'contractName', align: 'center' },
                { title: '广告客户', key: 'customerName', align: 'center' },
                { title: '合同开始时间', key: 'startTime', align: 'center' },
                { title: '签约人', key: 'signerName', align: 'center' },
                { title: '维护人', key: 'ownerName', align: 'center' },
                {
                    title: '操作',
                    key: 'action',
                    width: 150,
                    align: 'center',
                    render: (h, params) => {
                        var editBtn = h(tyIconTextButton, {
                            class: {
                                'editBtn': true
                            },
                            attr: {
                                href: 'javascript:void(0);'
                            },
                            nativeOn: {
                                click: () => {
                                    this.toDetail(params.row);
                                }
                            },
                            props: {
                                text: '查看',
                                iconClass: 'icon-chakan'
                            }
                        });
                        var delBtn = h(tyIconTextButton, {
                            attr: {
                                href: 'javascript:void(0);'
                            },
                            nativeOn: {
                                click: () => {
                                    this.$router.replace({name:"addPutAds",query:{contractId:params.row.id,customerId:params.row.customerId}});
                                }
                            },
                            props: {
                                text: '选定',
                                iconClass: 'icon-xuanding'
                            }
                        });
                        var optionBtn = [];
                        var isEdit = true
                        var isDel = true
                        isEdit && optionBtn.push(editBtn);
                        isDel && optionBtn.push(delBtn);
                        return h('div', {
                            class: {
                                'toolbarRight': true
                            }
                        }, [optionBtn]);
                    }
                }
            ]
        }
    },

    watch:{
          selType(val){
              if(val==1){
                  this.contractTableParams.contractCode='';
                  this.contractTableParams.contractName=this.searchTempData;
              }else if(val==2){
                  this.contractTableParams.contractCode=this.searchTempData;
                  this.contractTableParams.contractName='';
              }
          },
           searchTempData(val){
              if(this.selType==1){
                  this.contractTableParams.contractCode='';
                  this.contractTableParams.contractName=val;
              }else if(this.selType==2){
                  this.contractTableParams.contractCode=val;
                  this.contractTableParams.contractName='';
              }
          }  
    },
    components: {
        iModal,
        tyIconTextButton,
        tySearchInput,
        tyAddButton,
        tyTableView,
        iSelect,
        iOption,
        iRow,
        iCol,
        searchAD,
    },
    methods: {
        getPendingExecutionContracts(data) {
            this.contractTableParams.customerIds=[];
              if(data.length){
                  data.forEach((v,i)=>{
                      this.contractTableParams.customerIds.push(v.customerId)
                  })
              }
            this.contractTableSearch();
        },
        clientTableSearch(){
            this.$refs.clientTable.refresh()
            this.contractTableParams.customerIds=[];
            this.$refs.contractTable.refresh()
        },
        contractTableSearch(){
            this.$refs.contractTable.refresh()
        },
        toDetail(row){
            this.$router.push({
                name: 'contractInfo', query: {
                    cid: row.id
                }
            });
        }
    }
}
</script>
<style lang="scss" scoped>
.selContract {
    overflow: hidden;
    .searchInput {
        background: #edf1f4;
        height: 40px;
        border-radius: 4px;
        margin-bottom: 20px;
    }
    .leftTable {
        background-color: rgb(255, 255, 255);
    }
    .titleBox {
        padding: 0 20px;
        .title {
            font-size: 16px;
            line-height: 56px;
            font-weight: 400;
        }
    }

    .rightTable {
        background-color: rgb(255, 255, 255);
        .searchInput {
            width: 380px;
        }
        .selectInput {
            width: 160px;
            height: 40px;
            margin-right: 30px;
        }
    }
}


</style>
<style lang="scss">
.selContract .rightTable .selectInput .ivu-select-single .ivu-select-selection {
    height: 40px;
    position: relative;
    background: #edf1f4;
    line-height: 40px;
}

.selContract .selectInput .ivu-select-single .ivu-select-selection .ivu-select-placeholder,
.selContract .selectInput .ivu-select-single .ivu-select-selection .ivu-select-selected-value {
    line-height: 40px;
}

.selContract .bottomPage {
    background: #f1f1f1;
}

.selContract {
    .searchLine {
        margin-bottom: 20px;
    }
    .selectBox .ivu-select-selection, .searchBox {
        background: #edf1f4 !important;
    }
}

.selContract .tyTable {
    padding-bottom: 0 !important;
}
.selContract .leftTable .ivu-table-body{
    height: 500px;
}
.selContract .leftTable .ivu-table-tip{
    height: 500px;
    line-height: 500px;
}
.selContract .iconTextButton{
    margin-right:15px;
}
</style>
