<style lang="scss" >
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
</style>
<template>
    <div class="adSetting">
        <div class="adSettingModel">
            <div class="adSettingModel-title">广告尺寸</div>
            <tyAddButton text="添加尺寸" class="addButton" @click.native="openModal('addSize')" v-if="$store.state.check($m.adsSetting, $p.c)"></tyAddButton>
            <div class="clear"></div>
            <tyTableView class="adSettingModel-content" 
            ref="addSizeTable" :url="sizeUrl" 
            :columns="columnsSize" 
            :height="330" 
            :number="true"
            notDataText="暂无任何广告尺寸数据">
            </tyTableView>
        </div>
        <div class="adSettingModel">
            <div class="adSettingModel-title">广告时长</div>
            <tyAddButton text="添加时长" class="addButton" @click.native="openModal('addDuration')" v-if="$store.state.check($m.adsSetting, $p.c)"></tyAddButton>
            <div class="clear"></div>
            <tyTableView class="adSettingModel-content" 
            ref="addDurationTable" 
            :columns="columnsDuration" 
            :height="330" 
            :url="durationUrl" 
            :number="true"
            notDataText="暂无任何广告时长数据">
            </tyTableView>
        </div>
        <div class="adSettingModel">
            <div class="adSettingModel-title">广告展示次数</div>
            <tyAddButton text="添加展示次数" class="addButton" @click.native="openModal('addDisplaytime')" v-if="$store.state.check($m.adsSetting, $p.c)"></tyAddButton>
            <div class="clear"></div>
            <tyTableView class="adSettingModel-content"
             ref="addDisplaytimeTable" 
             :columns="columnsTime" 
             :height="330" 
             :number="true"
             :url="displaytimeUrl" notDataText="暂无任何广告展示次数数据">
            </tyTableView>
        </div>
        <div class="adSettingModel">
            <div class="adSettingModel-title">广告类别价格</div>
            <tyAddButton text="添加类别价格" class="addButton" @click.native="openModal('addPriceType')" v-if="$store.state.check($m.adsSetting,$p.c)"></tyAddButton>
            <div class="clear"></div>
            <tyTableView 
            :params="params" 
            class="adSettingModel-content" 
            ref="addPriceTypeTable" 
            :url="adsPriceConfigUrl" 
            :columns="columnsAdsType" 
            :height="330"
            :number="true"
            notDataText="暂无任何广告类别价格数据">
            </tyTableView>
        </div>
        <!--添加尺寸模态框-->
        <tyAddSizeModal ref="addSize" editTitle="编辑广告尺寸" addTitle="添加广告尺寸" @successEvent="addSuccess"></tyAddSizeModal>

        <!--添加时长模态框-->
        <tyAddDurationModal ref="addDuration" editTitle="编辑广告时长" addTitle="添加广告时长" @successEvent="addSuccess">
        </tyAddDurationModal>

        <!--添加展示次数模态框-->
        <tyAddTimesModal ref="addDisplaytime" editTitle="编辑广告展示次数" addTitle="添加广告展示次数" @successEvent="addSuccess">
        </tyAddTimesModal>

        <!--添加广告类别价格模态框-->
        <tyAddPriceTypeModal editTitle="编辑类别价格" addTitle="添加类别价格" ref="addPriceType" @successEvent="addSuccess">
        </tyAddPriceTypeModal>
    </div>
</template>
<script>
import tyIconTextButton from 'components/tyIconTextButton';
import tyIconText from 'components/tyIconText';
import tyTableView from 'components/tyTableView';
import tyAddButton from 'components/tyAddButton';
import tyAddPriceTypeModal from './tyAddPriceTypeModal';
import tyAddTimesModal from './tyAddTimesModal';
import tyAddSizeModal from './tyAddSizeModal';
import tyAddDurationModal from './tyAddDurationModal';

export default {
    components: {
        tyAddButton,
        tyTableView,
        tyAddSizeModal,
        tyAddTimesModal,
        tyAddDurationModal,
        tyAddPriceTypeModal,
        tyIconText,
        tyIconTextButton
    },
    methods: {
        delete(params, url, tablename, deleteDec) {
            this.$Modal.confirm({
                title: '删除提示',
                content: deleteDec ? '<p>确定删除' + deleteDec + '？</p>' : '<p>你确定要删除该选项吗？</p>',
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
            sizeUrl: this.$api.getSizeConfigUrl,
            durationUrl: this.$api.getDurationConfigUrl,
            displaytimeUrl: this.$api.getDisplayTimesConfigUrl,
            adsPriceConfigUrl: this.$api.getAdsPriceConfigListUrl,
            params: {
                storeType: 0
            },
            columnsAdsType: [
                { title: '编号', align: 'center', key: '_NUMBER_' },
                { title: '门店类别', key: 'storeTypeName', align: 'center' },
                {
                    title: '单位数量', key: 'unitQuantity', align: 'center',
                    render: (h, params) => {
                        return h('div', {}, params.row.unitQuantity + '个')
                    }
                },
                {
                    title: '周期时间', key: 'period', align: 'center',
                    render: (h, params) => {
                        return h('div', {}, params.row.period + '个月')
                    }
                },
                {
                    title: '价格', key: 'price', align: 'center',
                    render: (h, params) => {
                        var price = params.row.price;
                        if (params.row.price) {
                            price = params.row.price.toString();
                            if (price.indexOf('.') == -1) {
                                price += '.00';
                            } else {
                                if (price.length - 1 - price.indexOf('.') == 1) {
                                    price += '0';
                                }
                            }

                        }
                        return h('div', {}, price)
                    }
                },
                { title: '更新时间', key: 'updatedTime', align: 'center' },
                { title: '创建人', key: 'creator', align: 'center' },
                { title: '创建时间', key: 'createdTime', align: 'center' },
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
                                    this.edit('addPriceType', params.row);
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
                                    this.delete({ id: params.row.id }, this.$api.deleteAdsPriceConfigUrl, 'addPriceTypeTable', '广告类别价格');
                                }
                            },
                            props: {
                                text: '删除',
                                iconClass: 'icon-laji'
                            }
                        });
                        var optionBtn = [];
                        var isEdit = this.$store.state.check(this.$m.adsSetting, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.adsSetting, this.$p.d);
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
            columnsSize: [
                { title: '编号', align: 'center', key: '_NUMBER_' },
                { title: '尺寸名称', key: 'sizeName', align: 'center' },
                {
                    title: '宽高比', key: '', align: 'center',
                    render: (h, params) => {
                        return h('div', {}, params.row.widthRatio + ":" + params.row.highRatio)
                    }
                },
                {
                    title: '分辨率', key: 'period', align: 'center',
                    render: (h, params) => {
                        return h('div', {}, params.row.horizontalResolution + '*' + params.row.verticalResolution)
                    }
                },
                {
                    title: '更新时间', key: 'updatedTime', align: 'center',
                },
                { title: '创建人', key: 'creator', align: 'center' },
                { title: '创建时间', key: 'createdTime', align: 'center' },
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
                                    this.edit('addSize', params.row);
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
                                    this.delete({ id: params.row.id }, this.$api.deleteSizeConfigUrl, 'addSizeTable', '广告尺寸');
                                }
                            },
                            props: {
                                text: '删除',
                                iconClass: 'icon-laji'
                            }
                        });
                        var optionBtn = [];
                        var isEdit = this.$store.state.check(this.$m.adsSetting, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.adsSetting, this.$p.d);
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
            columnsDuration: [
                { title: '编号', align: 'center', key: '_NUMBER_'},
                { title: '展示时长', key: 'duration', align: 'center' },
                {
                    title: '时间单位', key: 'unitName', align: 'center',
                },
                {
                    title: '更新时间', key: 'updatedTime', align: 'center',
                },
                { title: '创建人', key: 'creator', align: 'center' },
                { title: '创建时间', key: 'createdTime', align: 'center' },
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
                                    this.edit('addDuration', params.row);
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
                                    this.delete({ id: params.row.id }, this.$api.deleteDurationConfigUrl, 'addDurationTable', '广告时长');
                                }
                            },
                            props: {
                                text: '删除',
                                iconClass: 'icon-laji'
                            }
                        });
                        var optionBtn = [];
                        var isEdit = this.$store.state.check(this.$m.adsSetting, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.adsSetting, this.$p.d);
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
            columnsTime: [
                { title: '编号', align: 'center', key: '_NUMBER_' },
                {
                    title: '展示次数', key: 'displayTimes', align: 'center', 
                    render: (h, params) => {
                        return h('div', {}, params.row.displayTimes + '次')
                    }
                },
                {
                    title: '时间单位', key: 'timeUnitName', align: 'center',
                },
                {
                    title: '更新时间', key: 'updatedTime', align: 'center',
                },
                { title: '创建人', key: 'creator', align: 'center' },
                { title: '创建时间', key: 'createdTime', align: 'center' },
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
                                    this.edit('addDisplaytime', params.row);
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
                                    this.delete({ id: params.row.id }, this.$api.deleteDisplayTimesConfigUrl, 'addDisplaytimeTable', '广告展示次数');
                                }
                            },
                            props: {
                                text: '删除',
                                iconClass: 'icon-laji'
                            }
                        });
                        var optionBtn = [];
                        var isEdit = this.$store.state.check(this.$m.adsSetting, this.$p.u);
                        var isDel = this.$store.state.check(this.$m.adsSetting, this.$p.d);
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
