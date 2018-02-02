<template>
    <div class="sysIndex">
        <div class="leftMenu">
            <div class="leftMenu-header">
                <div class="leftMenu-btns">
                    <iTooltip v-if="$store.state.check($m.organiztion,$p.addRootOrgin)" class="leftMenu-header-item" content="添加根组织机构" placement="bottom" @click.native="addBaseStructureShow({root:true,type:'create'})">
                        <a class="leftMenu-header-item-btn">
                            <p class="iconfont icon-add addRootBtn"></p>
                        </a>
                    </iTooltip>
                    <iTooltip v-if="$store.state.check($m.organiztion,$p.addChildOrgin)" class="leftMenu-header-item" content="添加组织机构" placement="bottom" @click.native="addBaseStructureShow({root:false,type:'create'})">
                        <a class="leftMenu-header-item-btn">
                            <p class="iconfont icon-jia addBtn"></p>
                        </a>
                    </iTooltip>
                    <iTooltip v-if="$store.state.check($m.organiztion,$p.u)" class="leftMenu-header-item" content="编辑组织" placement="bottom" :class="[actionState?'':disableClass]" @click.native="editOrganization()">
                        <a class="leftMenu-header-item-btn">
                            <p class="iconfont icon-combinedshape editBtn"></p>
                        </a>
                    </iTooltip>
                    <iTooltip v-if="$store.state.check($m.organiztion,$p.d)" class="leftMenu-header-item" content="删除组织" placement="bottom" :class="[actionState?'':disableClass]" @click.native="deleteOrganization()">
                        <a class="leftMenu-header-item-btn">
                            <p class="iconfont icon-laji deleteBtn"></p>
                        </a>
                    </iTooltip>
                </div>

            </div>
            <div class="zTreeName">组织机构</div>
            <div class="treeBox">
            <iTree class="zTree" :data="baseData" @on-select-change="v=>{selectOrganzation(v)}">
            </iTree>
            </div>
        </div>
        <div class="rightContent">
            <div class="handlerBox">
                <tySearchInput
                v-if="$store.state.check($m.peopleManager,$p.s)"
                 class="search" placeholder="请输入员工姓名" v-model="params.nickname" @search="search"></tySearchInput>
                <div class="handlerBoxButton">
                    <!-- <tyAddButton v-if="$store.state.check($m.organiztion,$p.delByMultiple)" text="批量删除" iconClass="icon-jia" class="deleteBtm" @click.native="deletePersonList"></tyAddButton> -->
                    <!-- <tyAddButton text="批量禁用" class="disableBtn" iconClass="icon-jia"></tyAddButton> -->
                </div>

            </div>

            <tyTableView :columns="modalColums" @on-selection-change="selectPersonList" :notAutoLoad=true :url="url" :params='params' class="modalColums" :height="355" ref="organizationUserTable" notDataText="暂无匹配的员工数据">
            </tyTableView>

        </div>
        <tyAddBaseStructureModal ref="addBaseStructure" :ctrlData="ctrlData" :selectOrganizationData="selectOrganizationData" @refreshTree="refreshTree"></tyAddBaseStructureModal>
    </div>
</template>

<script>
import tySearchInput from 'components/tySearchInput';
import tyIconTextButton from 'components/tyIconTextButton';
import tyTableView from 'components/tyTableView';
import iTree from 'iview/src/components/tree';
import iModal from 'iview/src/components/modal';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import iTooltip from 'iview/src/components/tooltip';
import iButton from 'iview/src/components/button';
import tyAddButton from 'components/tyAddButton';
import tyAddBaseStructureModal from './tyAddBaseStructureModal';
import ModalMixins from 'components/tyModal/baseModal';

var selTreeData = null;
//设置树状结构的递归
function setTitle(obj) {
    for (var i = 0; i < obj.length; i++) {
        obj[i].title = obj[i].name
        if (obj[i].children && obj[i].children.length) {
            setTitle(obj[i].children)
        }
    }
    return
}

export default {
    methods: {
        //弹出新增跟组织结构的模态框
        addBaseStructureShow(obj) {
            this.ctrlData = {
                type: obj.type,
                root: obj.root
            }
            this.$refs.addBaseStructure.modal = true;
        },
        editOrganization() {
            var obj = { root: false, type: 'edit' }
            if (this.actionState) {
                if (this.selectOrganizationData[0].parentId != 0) {
                    obj.root = false;
                } else {
                    obj.root = true;
                }
                this.addBaseStructureShow(obj)
            } else {
                return
            }
        },
        refreshTree() {
            selTreeData = null;
            this.selectOrganizationData = [];
            this.$post(this.$api.getOrganizationUrl).then((data) => {
                if (data.data.length) {
                    setTitle(data.data)
                    this.baseData = data.data
                }
            }, (mes) => {

            })
        },
        search() {
            if(!this.actionState){
             this.$Message.error("请先选择组织机构");
                     return
            }
            this.refreshTable()
        },
        deleteModalShow() {
          
        },
        deleteOrganization() {
              if (!this.actionState) {
                return
            }
            iModal.confirm({
                title:"删除提示",
                content:"<p>你确定要删除该组织机构吗？</p>",
                 onOk: () => {
                   var id = this.selectOrganizationData[0].id;
            this.$post(this.$api.deleteOrganizationUrl.replace(/\{id\}/, id)).then((data) => {
                this.$Message.success(
                    {
                        content: '删除成功'
                    })
                this.refreshTree();
                this.isDeleteModalShow = false;
                this.resetLoading()
                this.$refs.organizationUserTable.clearData();
            }).catch((e) => {
                e.message = e.message || '操作失败，请稍后再试试！';
                this.$Message.error(e.message);
                this.resetLoading()
            });
                },
                onCancel: () => {
                }
            })
        },
        refreshTable() {
            this.$refs.organizationUserTable.refresh()
        },
        selectOrganzation(v) {
            if (v.length && selTreeData !== v[0].id) {
                this.actionState = true;
                this.selectOrganizationData = v;
                // this.$set(this.$data,'url')
                // this.url = this.$api.getOrganizationUser.replace(/\{id\}/,this.selectOrganizationData[0].id);
                this.$refs.organizationUserTable.setUrlParams({
                    id: this.selectOrganizationData[0].id
                });
                selTreeData = this.selectOrganizationData[0].id;
                this.refreshTable()
            } else {
                this.selectOrganizationData = v;
                this.actionState = false;
            }


        },
        selectPersonList(data) {
            this.selectPersonListData = data
        },
        deletePersonList(){
            var arr=[];
            var organizationId=this.selectPersonListData[0].organizationId;
            for (var i=0;i<this.selectPersonListData.length;i++){ 
                if(!this.selectPersonListData[i].enable){
                      this.$Message.error('有员工被禁用,不允许删除');
                      return
                }else{
                    arr.push(this.selectPersonListData[i].id);
                }
            }

            iModal.confirm({
                title: '删除提示',
                content: '<p>你确定要删除人员吗？</p>',
                onOk: () => {
                    this.$post(this.$api.deleteOrganizationPersonListUrl.replace(/\{organizationId\}/, organizationId), {
                        list: arr
                    }
                    ).then((data) => {
                        this.$Message.success(
                            {
                                content: '删除成功'
                            })
                        this.$refs.organizationUserTable.refresh();
                    }).catch((e) => {
                        e.message = e.message || '操作失败，请稍后再试试！';
                        this.$Message.error(e.message);
                    });
                },
                onCancel: () => {
                }
            });
        },
        deletePerson(params) {
            if(!params.row.enable){
                this.$Message.error('该员工已被禁用，不允许删除');
                 return
            }
            iModal.confirm({
                title: '删除提示',
                content: '<p>你确定要删除该人员吗？</p>',
                onOk: () => {
                    this.$post(this.$api.deleteOrganizationPersonUrl.replace(/\{userId\}/, params.row.id).replace(/\{organizationId\}/, params.row.organizationId)
                    ).then((data) => {

                        this.$Message.success(
                            {
                                content: '删除成功'
                            })
                        this.$refs.organizationUserTable.refresh()
                    }).catch((e) => {
                        e.message = e.message || '操作失败，请稍后再试试！';
                        this.$Message.error(e.message);
                    });
                },
                onCancel: () => {
                }
            });

        },
        enablePerson(params) {
            this.$post(this.$api.toggledEnabledMemberUrl.replace(/\{id\}/, params.row.id)).then((data) => {
                this.$Message.success(
                    {
                        content: '操作成功'
                    })
                this.$refs.organizationUserTable.refresh()
            }).catch((e) => {
                e.message = e.message || '操作失败，请稍后再试试！';
                this.$Message.error(e.message);
            });
        }
    },
    mixins: [ModalMixins],
    components: {
        tyAddBaseStructureModal,
        tySearchInput,
        tyIconTextButton,
        tyTableView,
        iTree,
        iRow,
        iCol,
        iModal,
        iTooltip,
        iButton,
        tyAddButton
    },
    watch: {
        selectOrganizationData(val) {
            this.actionState = val.length
        }
    },
    created() {
        this.refreshTree();
    },
    data() {
        return {
            ctrlData: {
                type: 'create',
                root: true
            },
            selectPersonListData: [],
            actionState: false,
            isDeleteModalShow: false,
            loading: true,
            disableClass: 'disableClass',
            selectOrganizationData: {},
            addBaseStructureModalShow: false,
            baseData: [],
            organizationId: '',
            url: this.$api.getOrganizationUser,
            params: {
                pageIndex: 0,
                pageSize: 20,
                nickname: '',
            },
            modalColums: [
                // 批量操作
                //  {
                //         type: 'selection',
                //         width: 60,
                //         align: 'center'
                //     },
                { title: '员工姓名', key: 'nickname', align: 'center', ellipsis: true},
                { title: '联系方式', key: 'phoneNumber', align: 'center', ellipsis: true},
                { title: '上级领导', key: 'leader', align: 'center', ellipsis: true },
                { title: '从属组织', key: 'organizationName', align: 'center', ellipsis: true},
                {
                    title: '是否禁用', key: 'enable', align: 'center', render: (h, params) => {
                        var text = params.row.enable ? '否' : '是';
                        return h('div', {
                            class: {
                                'enableClasses': !params.row.enable
                            }
                        }, text);
                    }
                },
                { title: '创建人', key: 'creator', align: 'center', ellipsis: true  },
                { title: '创建时间', key: 'createdTime', align: 'center',ellipsis: true,
                	render: (h,params) => {
                		var text = params.row.createdTime.substr(0,10);
                		return h('span',text);
                	} },
                {
                    title: '操作',
                    key: 'action',
                    width: 180,
                    align: 'center',
                    render: (h, params) => {
                        var isDisable = this.$store.state.check(this.$m.peopleManager, this.$p.disable);
                        var isDel = this.$store.state.check(this.$m.peopleManager, this.$p.d);
                        var delBtn = h(tyIconTextButton, {
                            props: {
                                text: '删除',
                                iconClass: 'icon-laji'
                            },
                            nativeOn: {
                                click: () => {
                                    this.deletePerson(params)
                                }
                            },
                        });
                        var disableBtn = h(tyIconTextButton, {
                            props: {
                                text: params.row.enable ? '禁用' : '取消禁用',
                                iconClass: 'icon-suo1'
                            },
                            nativeOn: {
                                click: () => {
                                    this.enablePerson(params)
                                }
                            }
                        });
                        var optionBtns = [];
                        isDisable && optionBtns.push(disableBtn);
                        params.row.roleTypeNo!=this.$roleType.manager&&isDel && optionBtns.push(delBtn);
                        return h('div', optionBtns)
                    }
                }
            ]
        }
    }
}
</script>




<style lang="scss" scoped>
@import '~assets/css/base.scss';
.sysIndex {
    height: 100%;
    width: 100%;
    .leftMenu {
        position: absolute;
        left: 20px;
        width: 320px;
    }
    .rightContent {
        background-color: #edf1f4;
        float: right;
        width: 100%;
        padding-left: 340px;
    }
    .iconfont {
        color: #ccc;
        font-size: 36px;
    }
}

.handlerBox {
    height: 100px;
    .search {
        float: right;
        width: 300px;
        background-color: #ffffff;
    }
    .handlerBoxButton {
        clear: both;
        .deleteBtm {
            width: 134px;
            height: 34px;
            line-height: 34px;
            text-align: center;
            display: block;
            float: left;
            background-color: #f0857d;
            color: #ffffff;
        }
        .disableBtn {
            margin-left: 30px;
            width: 134px;
            height: 34px;
            line-height: 34px;
            text-align: center;
            display: block;
            float: left;
            background-color: #fcb322;
            color: #fcb322;
        }
    }
}


.leftMenu {
    height: 100%;
    background-color: #ffffff;

    .leftMenu-header {
        width: 315px;
        height: 50px;
        margin: 20px auto 20px;

        .leftMenu-header-item {
            text-align: center;
            float: left;
            width: 25%;
        }
        .leftMenu-header-item.disableClass {
            // background-color: #f3f3f3;
            opacity: 1;
            cursor: not-allowed;

            a {
                p {
                    color: #ccc;
                }
                cursor: not-allowed;
            }
        }
        .leftMenu-header-item-btn {
            display: block;
            padding-top: 2px;
            width: 100%;
            height: 50px;
        }
        .leftMenu-header-item-btnText {
            font-size: 12px;
            margin-top: 2px;
            color: #999999;
        }
        .addBtn {
            color: $mainColor;
        }
        .editBtn {
            color: #aeeac1;
        }
        .addRootBtn {
            color: #fcb425;
        }
        .deleteBtn {
            color: #f2848d;
        }
    }
    .zTreeName {
        border-top: 1px solid #eaeaea;
        font-size: 16px;
        color: #666;
        box-sizing: border-box;
        padding-top: 20px;
        padding-left: 20px;
    }
    .zTree {

        padding-left: 15px;
    }
    .treeBox{
            overflow: auto;
    height:85%;
    }
}
</style>



<style lang="scss">
@import '~assets/css/base.scss';
.sysIndex {
    .ivu-tree-empty {
        padding: 5px;
        font-size: 16px;
        margin-top: 50px;
        text-align: center;
    }
    .search {
        padding-right: 10px!important;
    }
    .ivu-row {
        height: 100%;
    }
    .ivu-tree-children {
        font-size: 16px;
    } // 菜单标题文本溢出处理。
    .ivu-tree-title {
        max-width: 90%;
        text-overflow: ellipsis;
        overflow: hidden;
    }
    .ivu-tree-title-selected,
    .ivu-tree-title-selected:hover {
        background-color: $mainColor;
        color: #ffffff;
    }
}
</style>
