<template>
    <div class="clientManager">
        <div class="headerTool">
            <tySearchInput class="searchComponent" @search="search" v-model="params.customername" placeholder="请输入客户名称"></tySearchInput>
            <tyAddButton v-if="$store.state.check($m.clientMng,$p.c)" text="添加客户" class="addButton" @click.native="gotoAddClient">
            </tyAddButton>
        </div>
        <tyTableView  
        @dblclick="dblclickEvent" 
        ref="tyTable" 
        :columns="columns1" 
        :url="url" 
        :pageSizeOpts="[15, 25, 35]"
        :height="760" 
        notDataText="没有找到任何匹配的客户数据" :params="params">
        </tyTableView>
        <tyAllocatModal modalTitle="分配顾客" ref="modal" :userInfoData="userInfoData"  @refreshClientTable="refreshTable"></tyAllocatModal>
        
    </div>
</template>
<script>
import iModal from 'iview/src/components/modal';
import tyTableView from 'components/tyTableView';
import tySearchInput from 'components/tySearchInput';
import tyAddButton from 'components/tyAddButton';
import tyAllocatModal from 'components/tyAllocatModal';
import tyIconTextButton from 'components/tyIconTextButton';
import tyIconText from 'components/tyIconText';
export default {
    components: {
        tyIconText,
        tyAddButton,
        tySearchInput,
        tyTableView,
        tyAllocatModal,
        tyIconTextButton
    },
    data() {
        return {
            //删除的接口链接
            deleteUrl: '',
            //删除的接口的参数
            deleteParams: {},
            keyword: '',
            userInfoData: {},
            url: this.$api.clientListUrl,
            deleteUrl: this.$api.deleteClientInfo,
            params: {
                customername: ''
            },
            columns1: [
                { title: '客户编号', key: 'customerNumber', align: 'center' },
                {
                    title: '客户名称', key: 'name', align: 'center',
                    ellipsis: true,
                    width: '180px',
                    render: (h, params) => {
                        return h(tyIconText, {
                            props: {
                                image: params.row.headPortrait,
                                title: params.row.name
                            }
                        })
                    }
                },
                { title: '从属行业', key: 'industry', align: 'center' },
                { title: '所在城市', key: 'cityName', align: 'center' },
                { title: '维护人', key: 'ownerName', align: 'center' },     
                {
                    align: 'center',
                    title: '正在投放广告', key: 'isAdvertising',
                    render: (h, params) => {
                        var text = params.row.delivering ? '是' : '否';
                        return h('div', {
                            class: {
                                'adsValidTag': params.row.delivering
                            }
                        }, text);
                    }
                },
                { title: '投放次数', key: 'advertisementDeliveryTimes', align: 'center' },
                { 
                    title: '广告总额', 
                    key: 'advertisementTotalAmount', 
                    align: 'center',
                    width: 120,
                    render:(h, params)=>{
                        var text = this.$format.toKeepPoint(params.row.advertisementTotalAmount);
                        return h('span', text);
                    }
                },
                {
                    title: '操作',
                    key: 'action',
                    width: 220,
                    align: 'center',
                    render: (h, params) => {
                        var optionBtn = [];
                        var isEdit = this.$store.state.check(this.$m.clientMng, this.$p.u);
                        var isAll = this.$store.state.check(this.$m.clientMng, this.$p.allocation);
                        var isDel = this.$store.state.check(this.$m.clientMng, this.$p.d);
                        var editBtn = h(tyIconTextButton, {
                            attr: {
                                href: 'javascript:void(0);'
                            },
                            nativeOn: {
                                click: () => {
                                    this.$router.push({ name: 'editClient', query: { clientId: params.row.id } })
                                }
                            },
                            props: {
                                text: '编辑',
                                iconClass: 'icon-bianji'
                            }
                        });
                        var allocationBtn = h(tyIconTextButton, {
                            nativeOn: {
                                click: () => {
                                    this.userInfoData = params.row;
                                    this.$refs.modal.show();
                                }
                            },
                            props: {
                                text: '分配',
                                iconClass: 'icon-fenpei'
                            }
                        })
                        var deleteBtn = h(tyIconTextButton, {
                            attr: {
                                href: 'javascript:void(0);'
                            },
                            nativeOn: {
                                click: () => {
                                    this.confirm(params.row);
                                }
                            },
                            props: {
                                text: '删除',
                                iconClass: 'icon-laji'
                            }
                        })
                        var optionBtn = [];
                        isEdit && optionBtn.push(editBtn);
                        isAll && optionBtn.push(allocationBtn);
                        isDel && optionBtn.push(deleteBtn);
                        return h('div', {
                            class: {
                                'toolbarRight': true
                            }
                        }, optionBtn);
                    }
                }
            ]
        }
    },
    methods: {
        // 双击触发的时间
        dblclickEvent(row) {
            this.$router.push({ name: 'clientInfo', query: { clientId: row.id } })
        },
        gotoAddClient() {
            this.$router.push({ name: 'addClient' });
        },
        search(value) {
            this.params.customername = value;
            this.$refs.tyTable.setParams(this.params);
            this.$refs.tyTable.refresh();
        },
        refreshTable() {
            this.$refs.tyTable.refresh();
        },
        confirm(parmas) {
            iModal.confirm({
                title: '删除提示',
                content: '<p>你确定要删除&quot'+parmas.name+'&quot吗？</p>',
                onOk: () => {
                    this.$post(this.deleteUrl.replace(/\{id\}/, parmas.id)).then((result) => {
                        this.$refs.tyTable.refresh();
                        this.$Message.success("删除客戶成功！");
                    }).catch((e) => {
                        this.$Message.error(e.message);
                    });
                },
                onCancel: () => {
                }
            });
        },
    }
}
</script>
<style lang="scss">
@import '~assets/css/base.scss';
.clientManager {

    .toolbarRight {
        a {
            color: #666666;
        }
        &>a {
            margin-right: 10px;
        }
    }
    .adsValidTag {
        text-align: center;
        width: 30px;
        color: #ffffff;
        background-color: rgba(126, 221, 156, 1);
        line-height: 30px;
        height: 30px;
        border-radius: 4px;
        margin: 0 auto;
    }
}
</style>

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
