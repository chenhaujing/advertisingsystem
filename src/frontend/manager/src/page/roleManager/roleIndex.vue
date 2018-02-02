<template>
    <div>
        <div class="headerTool">
            <!--<tySearchInput class="searchComponent" @search="search" v-model="params.customername" placeholder="请输入客户名称"></tySearchInput>-->
            <tyAddButton  v-if="$store.state.check($m.roleManager,$p.c)" text="添加角色" class="addButton" @click.native="gotoAddRole"></tyAddButton>
        </div>
        <tyTableView ref="tyTable"
         :number="true"
         :columns="columns" :url="url" :height="541" notDataText="没有找到任何相关人员信息" :params="params">
        </tyTableView>
        <tyAddRoleModal 
        editTitle="编辑角色"
        addTitle="添加角色"
        ref="addRoleModal" @addSuccessEvent="refreshTable"></tyAddRoleModal>
    </div>
</template>

<script>
import iModal from 'iview/src/components/modal';
import tySearchInput from 'components/tySearchInput';
import tyAddButton from 'components/tyAddButton';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';
import tyAddRoleModal from './tyAddRoleModal';
export default {
    components: {
        tyIconTextButton,
        tySearchInput,
        tyAddButton,
        tyTableView,
        tyAddRoleModal
    },
    methods: {
        gotoAddRole() {
            //重置模态框
            this.$refs.addRoleModal.toggle();
        },
        refreshTable() {
            this.$refs.tyTable.refresh();
        },
        confirm() {

            iModal.confirm({
                title: '删除提示',
                content: '<p>你确定要删除该选项吗？</p>',
                onOk: () => {

                    this.$post(this.deleteUrl, {}, {}, this.deleteParams).then((result) => {
                        this.$refs.tyTable.refresh();
                        this.$Message.success("删除角色成功！");
                    }).catch((e) => {
                        e.message = e.message || '操作失败，请稍后再试试！';
                        this.$Message.error(e.message);
                    });
                },
                onCancel: () => {

                }
            });
        },
    },
    data() {
        return {
            url: this.$api.getRoleListUrl,
            params: {
                roleName: '',
            },
            columns: [
                {
                    title: '编号',
                    key: '_NUMBER_' ,
                    align: 'center',
                },
                {
                    title: '角色名称', key: 'roleName', align: 'center'
                },
                {
                    title: '角色类型',
                    key: 'roleType',
                    align: 'center',
                    render: (h, params) => {
                        var text = params.row.roleType;
                        if (this.$formVerify.verifyString(params.row.roleType)) {
                            text = '-';
                        }
                        return h('span', text);
                    }
                },
                // {
                //     align: 'center',
                //     title: '是否禁用', 
                //     key: 'enable',
                //     render: (h, params) => {
                //         var text = params.row.enable ? '是' : '否';
                //         return h('span', text);
                //     }
                // },
                { title: '创建人', key: 'creator', align: 'center' },
                {
                    title: '创建时间',
                    key: 'createdTime',
                    align: 'center',
                    render: (h, params) => {
                        var text;
                        if (this.$formVerify.verifyString(params.row.createdTime)) {
                            text = '-';
                        }
                        if (!this.$formVerify.verifyString(params.row.createdTime)) {
                            text = params.row.createdTime.substr(0, 10);
                        }
                        return h('span', text);
                    }
                },
                { title: '备注', key: 'description', align: 'center' },
                {
                    title: '操作',
                    key: 'action',
                    width: 220,
                    align: 'center',
                    render: (h, params) => {
                        var editBtn = h(tyIconTextButton, {
                            attr: {
                                href: 'javascript:void(0);'
                            },
                            nativeOn: {
                                click: () => {
                                    this.$refs.addRoleModal.roleForm.id = params.row.id;
                                    this.$refs.addRoleModal.roleForm.roleName = params.row.roleName;
                                    this.$refs.addRoleModal.roleTypeName = params.row.roleType;
                                    this.$refs.addRoleModal.roleForm.description = params.row.description;
                                    this.$refs.addRoleModal.toggle(true);
                                }
                            },
                            props: {
                                text: '编辑',
                                iconClass: 'icon-bianji'
                            },
                            class: {
                                controlBtn: true,
                            }
                        });
                        var delBtn = h(tyIconTextButton, {
                            attr: {
                                href: 'javascript:void(0);'
                            },
                            nativeOn: {
                                click: () => {
                                    this.deleteUrl = this.$api.deleteRoleUrl;
                                    this.deleteParams = {
                                        "id": params.row.id
                                    };
                                    this.confirm();
                                }
                            },
                            props: {
                                text: '删除',
                                iconClass: 'icon-laji'
                            },
                            class: {
                                controlBtn: true,
                            }
                        });
                        var isEdit = this.$store.state.check(this.$m.roleManager, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.roleManager, this.$p.d);
                        var optionsBtn = [];
                        isEdit && optionsBtn.push(editBtn);
                        isDel && optionsBtn.push(delBtn);
                        return h('div', {
                            class: {
                                'toolbarRight': true
                            }
                        }, optionsBtn

                            // h(tyIconTextButton, {
                            //     nativeOn: {
                            //         click: () => {
                            //         	this.banUrl = this.$api.banRoleUrl;
                            //         	this.banRoleParams = {
                            //                 "id": params.row.id
                            //             };
                            //             this.$post(this.banUrl,{},{},this.banRoleParams).then((result) => {
                            //                 this.$refs.tyTable.refresh();
                            //                 this.$Message.success('状态切换成功！');
                            //             }).catch((e) => {
                            //             	e.message = e.message || '操作失败，请稍后再试试！';
                            //                 this.$Message.error(e.message);
                            //             });
                            //         }
                            //     },
                            //     props: {
                            //         text: params.row.enable ? '取消禁用' : '禁用',
                            //         iconClass: params.row.enable ? 'icon-suo1' : 'icon-suo'
                            //     },
                            //     class:{
                            //     	controlBtn:true,
                            //     },
                            // }),


                        );
                    }
                }
            ]
        }
    },
}
</script>

<style scoped lang="scss">
.headerTool {
    height: 50px;
    .searchComponent {
        background-color: #ffffff;
        width: 380px;
        float: left;
    }
    .addButton {
        width: 160px;
        float: right;
    }
}
</style>
<style lang="scss">
.controlBtn {
    margin-right: 5px;
}
</style>
