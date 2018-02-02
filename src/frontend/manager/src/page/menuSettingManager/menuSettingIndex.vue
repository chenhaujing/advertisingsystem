<template>
    <div>
        <div class="headerTool hideModal ">
            <span class="title">菜单配置</span>
            <div class="headerTool-buttons">
                <iButton v-if="$store.state.check($m.menuConfig,$p.menuPerConfig)" type="primary" icon="gear-b" class="rightSettingButton" @click.native="configMenu">配置目录权限</iButton>
                <tyAddButton v-if="$store.state.check($m.menuConfig,$p.c)" text="添加目录" class="addButton" @click.native="addMenu"></tyAddButton>
            </div>

        </div>
        <tyTableView ref="tyTable" 
        @dblclick="editMenuSetting" 
        :columns="columns" 
        :url="url" 
        :number="true"
        :height="541" notDataText="没有找到任何菜单信息" :params="params">
        </tyTableView>
        <AddMenuModal editTitle="编辑目录" addTitle="添加目录" ref="addMenuModal" @refreshTable="refreshTable"></AddMenuModal>
        <ConfigMenuModal ref="configMenuModal"></ConfigMenuModal>
    </div>
</template>

<script>
import iButton from 'iview/src/components/button';
import iModal from 'iview/src/components/modal';
import tySearchInput from 'components/tySearchInput';
import tyAddButton from 'components/tyAddButton';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';
import AddMenuModal from './addMenuModal';
import ConfigMenuModal from './configMenuModal';
export default {
    components: {
        ConfigMenuModal,
        AddMenuModal,
        iButton,
        iModal,
        tyIconTextButton,
        tySearchInput,
        tyAddButton,
        tyTableView,
    },
    methods: {
        addMenu() {
            this.$refs.addMenuModal.toggle();
        },
        configMenu() {
            this.$refs.configMenuModal.modal = true;
        },
        refreshTable() {
            this.$refs.tyTable.refresh();
        },
        editMenuSetting(row) {
            this.$refs.addMenuModal.menuFormData = {
                parentId: row.parentId,
                menuName: row.menuName,
                url: row.url,
                id: row.id,
            }
            this.$refs.addMenuModal.toggle(true);
        },
        confirm() {
            iModal.confirm({
                title: '删除提示',
                content: '<p>你确定要删除该选项吗？</p>',
                onOk: () => {
                    this.$post(this.deleteUrl, {}, {}, this.deleteParams).then((result) => {
                        this.$refs.tyTable.refresh();
                        this.$Message.success("删除成功！");
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
            url: this.$api.getMenuByPageUrl,
            params: {
            },
            deleteUrl: this.$api.deleteSysMenu,
            columns: [
                {
                    title: '编号',
                    key: '_NUMBER_' ,
                    align: 'center',
                },
                {
                    title: '目录名称', key: 'menuName', align: 'center'
                },
                {
                    title: '从属目录', key: 'parentMenu', align: 'center'
                },
                {
                    align: 'center',
                    title: '是否根目录',
                    key: 'enable',
                    render: (h, params) => {
                        var text = params.row.rootMenu ? '是' : '否';
                        return h('div', {
                            class: {
                                'adsValidTag': params.row.rootMenu
                            }
                        }, text);
                    },

                },
                { title: 'Url链接', key: 'url', align: 'center' },
                {
                    title: '更新时间',
                    key: 'updatedTime',
                    align: 'center',
                    render: (h, params) => {
                        var text;
                        if (this.$formVerify.verifyString(params.row.updatedTime)) {
                            text = '-';
                        }
                        if (!this.$formVerify.verifyString(params.row.updatedTime)) {
                            text = params.row.updatedTime.substr(0, 10);
                        }
                        return h('span', text);
                    }
                },
                { title: '创建人', key: 'creatorName', align: 'center' },
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
                {
                    title: '操作',
                    key: 'action',
                    width: 180,
                    align: 'center',
                    render: (h, params) => {
                        var editBtn = h(tyIconTextButton, {
                            attr: {
                                href: 'javascript:void(0);'
                            },
                            nativeOn: {
                                click: () => {
                                    this.editMenuSetting(params.row)
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
                        })
                        var isEdit = this.$store.state.check(this.$m.menuConfig, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.menuConfig, this.$p.d);
                        var optionsBtn = [];
                        isEdit && optionsBtn.push(editBtn);
                        isDel && optionsBtn.push(delBtn);
                        return h('div', {
                            class: {
                                'toolbarRight': true
                            }
                        }, optionsBtn);
                    }
                }
            ]
        }
    },
}
</script>

<style scoped lang="scss">
.headerTool {
    width: 100%;
    background-color: #fff;
    height: 78px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    padding:10px;
    position: relative;
    .headerTool-buttons {
        position: absolute;
        bottom: 20px;
        right: 20px;
    }
    .title {
        font-size: 14px;
        color: #333333;
        float: left;
    }
    .addButton {
        width: 160px;
        float: right;
    }
    .rightSettingButton {
        width: 160px;
        float: right;
        border-color: #fcb322;
        background-color: #fcb322;
        height: 38px;
        margin-left: 20px;
        font-size: 14px;
    }
}
</style>
<style lang="scss">
.controlBtn {
    margin-right: 5px;
}
</style>
