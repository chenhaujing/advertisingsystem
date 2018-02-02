<template>
    <div>
        <div class="headerTool">
            <tySearchInput class="searchComponent" @search="search" v-model="params.nickname" placeholder="请输入人员姓名"></tySearchInput>
            <tyAddButton  v-if="$store.state.check($m.peopleManager,$p.c)" text="添加人员" class="addButton" @click.native="gotoAddMember"></tyAddButton>
        </div>
        <tyTableView ref="tyTable" :number="true" :columns="columns" :url="url" :height="541" notDataText="暂没有找到匹配的员工数据" :params="params" @dblclick="dblclickRow">
        </tyTableView>
        <tyAddMemberModal ref="tyAddMemberModal" @addSuccessEvent="refreshTable" :personData='personData' :membertype='membertype'></tyAddMemberModal>
    </div>
</template>

<script>
import iModal from 'iview/src/components/modal';
import tySearchInput from 'components/tySearchInput';
import tyAddButton from 'components/tyAddButton';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';
import tyAddMemberModal from './tyAddMemberModal';
export default {
    components: {
        iModal,
        tyIconTextButton,
        tySearchInput,
        tyAddButton,
        tyTableView,
        tyAddMemberModal
    },
    methods: {
        refreshTable() {
            this.$refs.tyTable.refresh();
        },
        search(value) {
            this.refreshTable();
        },
        gotoAddMember() {
            this.personData = '';
            this.membertype = 'create'
            this.$refs.tyAddMemberModal.modal = true;
        },
        dblclickRow(row) {
            this.editMember(row)
        },
        editMember(parmas) {
            if (!parmas.enable) {
                this.$Message.error({
                    content: "不能编辑已禁用人员"
                })
                return
            }
            this.$refs.tyAddMemberModal.modal = true;
            this.membertype = 'edit'
            this.personData = parmas;
        },
        enablePerson(parmas) {
            if (!parmas.enable) {
                this.$post(this.$api.toggledEnabledMemberUrl.replace(/\{id\}/, parmas.id)).then((result) => {
                    this.$Message.success({
                        content: "取消禁用成功!"
                    });
                    this.$refs.tyTable.refresh();
                }).catch((e) => {
                    e.message = e.message || '操作失败，请稍后再试试！';
                    this.$Message.error(e.message);
                });
                return
            }
            iModal.confirm({
                title: '禁用提示',
                content: '<p>你确定要禁用该人员吗？</p>',
                onOk: () => {
                    this.$post(this.$api.toggledEnabledMemberUrl.replace(/\{id\}/, parmas.id)).then((result) => {
                        this.$Message.success({
                            content: "禁用成功!"
                        });
                        this.$refs.tyTable.refresh();
                    }).catch((e) => {
                        e.message = e.message || '操作失败，请稍后再试试！';
                        this.$Message.error(e.message);
                    });
                },
                onCancel: () => {
                }
            });

        },
        confirm(params) {
            this.deleteParams.id = params.id;
            if (!params.enable) {
                this.$Message.error({
                    content: "不能删除已禁用人员"
                })
                return
            }
            iModal.confirm({
                title: '删除提示',
                content: '<p>确定删除人员"'+params.nickname+'"？</p>',
                onOk: () => {
                    this.$post(this.deleteUrl, {}, {}, this.deleteParams).then((result) => {
                        this.$Message.success("删除人员成功！");
                        this.$refs.tyTable.refresh();
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
            url: this.$api.getMemberList,
            params: {
                pageIndex: 0,
                pageSize: 20,
                nickname: ''
            },
            deleteParams: {
                id: ''
            },
            membertype: 'create',
            personData: '',
            columns: [
                {
                    title: '编号',
                    key: '_NUMBER_',
                    align: 'center',
                },
                { title: '人员姓名', key: 'nickname', align: 'center', ellipsis: true },
                { title: '角色类型', key: 'roleType', align: 'center', ellipsis: true },
                { title: '联系电话', key: 'phoneNumber', align: 'center', ellipsis: true },
                { title: '从属组织', key: 'organizationName', align: 'center', ellipsis: true },
                { title: '上级领导', key: 'leader', align: 'center', ellipsis: true },
                {
                    align: 'center',
                    title: '是否禁用', key: 'enable',
                    render: (h, params) => {
                        var text = params.row.enable ? '否' : '是';
                        return h('div', {
                            class: {
                                'enableClasses': !params.row.enable
                            }
                        }, text);
                    }
                },
                { title: '创建人', key: 'creator', align: 'center', ellipsis: true, },
                {
                    title: '创建时间',
                    key: 'createdTime',
                    align: 'center',
                    ellipsis: true,
                    render: (h, params) => {
                        var text = params.row.createdTime.substr(0, 10);
                        return h('span', text);
                    }
                },
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
                                    this.editMember(params.row)
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
                                    this.deleteUrl = this.$api.deleteMemberUrl;
                                    this.confirm(params.row);
                                }
                            },
                            props: {
                                text: '删除',
                                iconClass: 'icon-laji'
                            }
                        });
                        var disBtn = h(tyIconTextButton, {
                            nativeOn: {
                                click: () => {
                                    this.enablePerson(params.row)
                                }
                            },
                            props: {
                                text: params.row.enable ? '禁用' : '取消禁用',
                                iconClass: params.row.enable ? 'icon-suo1' : 'icon-suo'
                            },
                            class: {
                                controlBtn: true,
                            }
                        });
                        var isEdit = this.$store.state.check(this.$m.peopleManager, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.peopleManager, this.$p.d);
                        var isDisable = this.$store.state.check(this.$m.peopleManager, this.$p.d);
                        var optionsBtn = [];
                        isEdit && optionsBtn.push(editBtn);
                        isDel && optionsBtn.push(delBtn);
                        isDisable && optionsBtn.push(disBtn);
                        return h('div', {
                            class: {
                                'toolbarRight': true
                            }
                        }, optionsBtn
                        );
                    }
                }
            ]
        }
    },

}
</script>

<style  scoped lang="scss">
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
