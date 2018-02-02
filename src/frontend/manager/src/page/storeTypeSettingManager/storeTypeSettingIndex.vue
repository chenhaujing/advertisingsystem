<template>
    <div>
        <div>
            <div class="headerTool">
                <div class="headerTool-title">门店类别</div>
                <tyAddButton v-if="$store.state.check($m.storeTypeConfig,$p.c)" text="添加类别标准" class="addButton" @click.native="gotoAdd('addTypeStandardModal')"></tyAddButton>
                <div class="clear"></div>
            </div>
            <tyTableView @dblclick="gotoEdit" 
            :number="true"
            ref="storeTypeTyTable" :columns="storeTypeColumns" :url="storeTypeUrl" :height="541" notDataText="暂无门店类别" :params="storeTypParams">
            </tyTableView>
            <tyAddTypeStandardModal editTitle="编辑类别标准" addTitle="添加类别标准" ref="addTypeStandardModal" @addSuccessEvent="refreshTable('storeTypeTyTable')"></tyAddTypeStandardModal>
        </div>
        <div class="typeAdConfigMode">
            <div class="headerTool">
                <div class="headerTool-title">类别广告位配置</div>
                <tyAddButton v-if="$store.state.check($m.storeAdsPosition,$p.c)" text="配置类别广告位" class="addButton" @click.native="gotoAdd('addTypeAdModal')"></tyAddButton>
                <div class="clear"></div>
            </div>
            <tyTableView @dblclick="adsPosEdit" 
            :number="true"
            ref="adsTyTable" :columns="adsTypeColumns" :url="adsTypeUrl" :height="541" notDataText="没有找到任何相关人员信息" :params="adsTypeParams">
            </tyTableView>
            <tyAddTypeAdModal editTitle="编辑类别广告位数量" addTitle="添加类别广告位数量" ref="addTypeAdModal" @addAdSuccessEvent="refreshTable('adsTyTable')">
            </tyAddTypeAdModal>
        </div>
    </div>
</template>

<script>
import iButton from 'iview/src/components/button';
import iModal from 'iview/src/components/modal';
import tySearchInput from 'components/tySearchInput';
import tyAddButton from 'components/tyAddButton';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';
import tyAddTypeStandardModal from './tyAddTypeStandardModal';
import tyAddTypeAdModal from './tyAddTypeAdModal';
export default {
    components: {
        iButton,
        iModal,
        tyIconTextButton,
        tySearchInput,
        tyAddButton,
        tyTableView,
        tyAddTypeStandardModal,
        tyAddTypeAdModal
    },
    methods: {
        adsPosEdit(row) {
            var isEdit = this.$store.state.check(this.$m.storeAdsPosition, this.$p.u);
            if (!isEdit) {
                return;
            }
            this.$refs.addTypeAdModal.setParams(
                row
            );
            this.$refs.addTypeAdModal.toggle(true);
        },
        gotoEdit(row) {
            var isEdit = this.$store.state.check(this.$m.storeTypeConfig, this.$p.u);
            if (!isEdit) {
                return;
            }
            var tradingDay = row.avgDailyTradingAmountMin + '-' + row.avgDailyTradingAmountMax;
            var goodsNum = row.commodityAmountMin + '-' + row.commodityAmountMax;
            this.$refs.addTypeStandardModal.setParams(
                row.id,
                row.storeType,
                tradingDay,
                goodsNum,
                row.storeCategoryStandardName
            );
            this.$refs.addTypeStandardModal.toggle(true);
        },
        gotoAdd(refName) {
            if (refName == 'addTypeAdModal') {
                var allRows = this.$refs.adsTyTable.getAllRows();
                var typeA = false, typeB = false, typeC = false;
                for (let i = 0; i < allRows.length; i++) {
                    if (allRows[i].storeType == 1) {
                        typeA = true;
                    }
                    if (allRows[i].storeType == 2) {
                        typeB = true;
                    }
                    if (allRows[i].storeType == 3) {
                        typeC = true;
                    }
                }
                if (typeA && typeB && typeC) {
                    this.$Message.error('已经配置所有类别广告位最大数量，无法继续新增');
                    return;
                }
            }
            this.$refs[refName].toggle();
        },
        refreshTable(columRefName) {
            this.$refs[columRefName].refresh();
        },
        confirm(url, urlparams, tyTable) {
            iModal.confirm({
                title: '删除提示',
                content: '<p>你确定要删除该选项吗？</p>',
                onOk: () => {
                    this.$post(url, {}, {}, urlparams).then((result) => {
                        this.$refs[tyTable].refresh();
                        this.$Message.success("删除成功！");
                    }).catch((e) => {
                        e.message = e.message || '操作失败，请稍后再试！';
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
            storeTypeUrl: this.$api.getStoreTypeListUrl,
            storeTypParams: {},
            storeTypeColumns: [
                {
                    title: '编号',
                    key: '_NUMBER_' ,
                    align: 'center',
                },
                {
                    title: '类别名称', key: 'storeCategoryStandardName', align: 'center'
                },
                {
                    title: '类别类型', key: 'storeTypeName', align: 'center'
                },
                {
                    align: 'center',
                    title: '商品数量',
                    key: 'commodityAmountMin' + 'commodityAmountMax ',
                    render: (h, params) => {

                        var text = '';
                        if (params.row.commodityAmountMax.toString().indexOf('999999') != -1) {
                            text = ' x ≥' + params.row.commodityAmountMin;
                        } else {
                            text = params.row.commodityAmountMin + '< x <' + params.row.commodityAmountMax;
                        }
                        return h('span', text);
                    }
                },
                {
                    align: 'center',
                    title: '平均每日交易订单数',
                    key: 'avgDailyTradingAmountMin' + 'avgDailyTradingAmountMax ',
                    render: (h, params) => {
                        var text = '';
                        if (params.row.avgDailyTradingAmountMax.toString().indexOf('999999') != -1) {
                            text = ' x ≥' + params.row.avgDailyTradingAmountMin;
                        } else {
                            text = params.row.avgDailyTradingAmountMin + '< x <' + params.row.avgDailyTradingAmountMax;
                        }
                        return h('span', text);
                    }
                },
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
                                    this.gotoEdit(params.row);

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

                                    var deleteUrl = this.$api.deleteStoreTypeListUrl;
                                    var deleteParams = {
                                        "id": params.row.id
                                    };
                                    this.confirm(deleteUrl, deleteParams, 'storeTypeTyTable');
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
                        var isEdit = this.$store.state.check(this.$m.storeTypeConfig, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.storeTypeConfig, this.$p.d);
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
            ],
            adsTypeUrl: this.$api.getAdTypeListUrl,
            adsTypeParams: {},
            adsTypeColumns: [
                {
                    title: '编号',
                    key: '_NUMBER_' ,
                    align: 'center',
                },
                {
                    title: '类别类型', key: 'storeTypeName', align: 'center'
                },
                {
                    align: 'center',
                    title: '最大广告位数量',
                    key: 'adCount',
                    render: (h, params) => {
                        var text = params.row.adCount || '-';
                        return h('span', text);
                    }
                },
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
                                    this.adsPosEdit(params.row);

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
                                    var deleteUrl = this.$api.deleteAdTypeListUrl;
                                    var deleteParams = {
                                        "id": params.row.id
                                    };
                                    this.confirm(deleteUrl, deleteParams, 'adsTyTable');
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
                        var isEdit = this.$store.state.check(this.$m.storeAdsPosition, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.storeAdsPosition, this.$p.d);
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
            ],
        }
    },
}
</script>

<style scoped lang="scss">
.typeAdConfigMode {
    padding-top: 30px;
}

.headerTool {
    width: 100%;
    background-color: #fff;
    height: 78px;
    box-sizing: border-box;
    padding: 10px;
    position: relative;

    .headerTool-title {
        font-size: 14px;
        color: #333;
        float: left;
    }
    .searchComponent {
        background-color: #ffffff;
        width: 380px;
        float: left;
    }
    .addButton {
        position: absolute;
        top: 20px;
        height: 38px;
        right: 20px;
        width: 160px;
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
