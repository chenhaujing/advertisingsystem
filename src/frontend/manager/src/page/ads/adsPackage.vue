<style lang="scss" scoped>
//广告设置样式
.adSetting {
    width: 100%;
    padding: 20px;
    box-sizing: border-box; //广告设置模块的样式
    .adSettingModel {
        background-color: #fff;
        padding: 20px;
        width: 100%;
        box-sizing: border-box;
        margin-bottom: 20px; //广告设置模块的标题
        .adSettingModel-title {
            font-size: 18px;
            line-height: 36px;
        } //广告设置模块内容添加按钮
        .addButton {
            width: 160px;
            float: right;
        }
        .adSettingModel-content {
            margin-top: 20px;
        }
    }
    .editBtn {
        margin-right: 5px;
    }
}
.headerTool {
    height: 50px;
    .searchComponent {
        background-color: #EDF1F4;
        width: 380px;
        float: left;
    }
    .addButton {
        width: 160px;
        float: right;
    }
}
</style>
<template>
    <div class="adSetting">
        <div class="adSettingModel">
            <div class="adSettingModel-title">广告套餐</div>
        <div class="headerTool">
            <tySearchInput class="searchComponent" @search="search" v-model="params.packageName" placeholder="请输入套餐名称"></tySearchInput>
             <tyAddButton text="添加广告套餐" class="addButton" @click.native="openModal('addPackage')" v-if="$store.state.check($m.adsPackage, $p.c)"></tyAddButton>
        </div>
           
            <div class="clear"></div>
            <tyTableView class="adSettingModel-content" 
            ref="addPackageTable" 
            :url="url" 
            :number="true"
            :params="params" :columns="columns" :height="500" :notDataText="notDataText">
            </tyTableView>
        </div>
        <!--添加尺寸模态框-->
        <tyAddPackageModal ref="addPackage" editTitle="编辑广告套餐" addTitle="添加广告套餐" @successEvent="addSuccess"></tyAddPackageModal>
    </div>
</template>
<script>
import tyIconTextButton from 'components/tyIconTextButton';
import tyIconText from 'components/tyIconText';
import tyTableView from 'components/tyTableView';
import tyAddButton from 'components/tyAddButton';
import tySearchInput from 'components/tySearchInput';
import tyAddPackageModal from './tyAddPackageModal';

const NO_DATA_ARRAY = ['暂无广告套餐配置数据', '暂无匹配的广告套餐配置数据'];

export default {
    components: {
        tyAddButton,
        tyTableView,
        tyIconText,
        tyIconTextButton,
        tySearchInput,
        tyAddPackageModal
    },
    methods: {
        delete(params, url,tablename,deleteDec) {
            this.$Modal.confirm({
                title: '删除提示',
                content: deleteDec?'<p>你确定要删除"' + deleteDec + '"吗？</p>':'<p>你确定要删除该选项吗？</p>',
                onOk: () => {
                  
                    this.$post(url, {}, {}, params).then((result) => {
                        this.$refs[tablename].refresh();
                        this.$Notice.success({
                            title: '提示',
                            desc: '删除成功'
                        })

                    }).catch((e) => {
                        e.message = e.message || '操作失败，请稍后再试！';
                        this.$Notice.error({
                            title: '错误',
                            desc: e.message
                        });
                    });
                },
                onCancel: () => {
                }
            });
        },
        search(){
            if(this.params.packageName){
                this.notDataText=NO_DATA_ARRAY[1]
            }else{
                this.notDataText=NO_DATA_ARRAY[0]
            }
        this.$refs.addPackageTable.refresh();
        },
        edit(refStr, row) {
            this.$refs[refStr].setParams(row);
            this.$refs[refStr].toggle(true);
        },
        addSuccess(name) {
            this.$refs[name].refresh();
        },
        openModal(val) {
            this.$refs[val].toggle();
        },
    },
    data() {
        return {
            url: this.$api.getPackageConfig,
            params:{
             "packageName": "",
            },
            notDataText:NO_DATA_ARRAY[0],
            columns: [
                { title: '编号', align: 'center',key: '_NUMBER_',width:'60'},
                { title: '套餐名称', key: 'packageName', align: 'center',ellipsis: true },
                {
                    title: 'A类数量', key: 'amontOfTypeA', align: 'center',
                },
                {
                    title: 'B类数量', key: 'amontOfTypeB', align: 'center',
                },
                 {
                    title: 'C类数量', key: 'amontOfTypeC', align: 'center',
                },
                 {
                    title: '屏位总数', key: 'totalAmount', align: 'center',
                },
                {
                    title: '总价', key: 'totalCost', align: 'center',
                    render: (h, params) => {
                        return h('div', {}, this.$format.toKeepPoint(params.row.totalCost))
                    }
                },
                {
                    title: '周期', key: 'period', align: 'center',
                   render: (h, params) => {
                        return h('div', {}, params.row.period + '个月')
                    }
                },
                  {
                    title: '更新人', key: 'updater', align: 'center',
                
                },
                {
                    title: '更新时间', key: 'updatedTime', align: 'center',ellipsis: true,width:130,
                },
                { title: '创建人', key: 'creator', align: 'center' },
                { title: '创建时间', key: 'createdTime', align: 'center', ellipsis: true,width:130, },
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
                                    this.edit('addPackage', params.row);
                                }
                            },
                            props: {
                                text: '编辑',
                                iconClass: 'icon-bianji'
                            }
                        });
                        var delBtn = h(tyIconTextButton, {
                            attr: {
                                href: 'javascript:void(0);'
                            },
                            nativeOn: {
                                click: () => {
                                    this.delete({ id: params.row.id }, this.$api.deletePackageConfig,'addPackageTable', params.row.packageName);
                                }
                            },
                            props: {
                                text: '删除',
                                iconClass: 'icon-laji'
                            }
                        });
                        var optionBtn = [];
                        var isEdit = this.$store.state.check(this.$m.adsPackage, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.adsPackage, this.$p.d);
                        isEdit && optionBtn.push(editBtn);
                        isDel && optionBtn.push(delBtn);
                        return h('div', {
                            class: {
                                'toolbarRight': true
                            }
                        }, [optionBtn]);
                    }
                }
            ],

        }
    }
}
</script>
