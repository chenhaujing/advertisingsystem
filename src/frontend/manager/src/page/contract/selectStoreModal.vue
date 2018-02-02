<template>
    <iModal class="baseModal selectStoreModal hideModal" :title="title" v-model="showFlag" :mask-closable="false" :width="950">
        <div v-show="!loading" class="mainContent">
            <iTree ref="itree" class="leftMenu" :data="baseData" @on-check-change="treeSelect" show-checkbox>
            </iTree>
            <div class="rightContent">
                <div class="table-tool">
                    <!-- <iForm :label-width="80" ref="iFrom" :model="menuFormData" class="fromBox">
                        <iFormitem label="广告时长" prop="menuName" class="fromItem-adslongTimeBox">
                            <iSelect v-model="adsTimelong">
                                <iOption v-for="item in adsTimelongList" :value="item.value" :key="item.value">{{ item.label }}</iOption>
                            </iSelect>
                        </iFormitem>
                        <iFormitem label="广告次数" prop="menuName" class="fromItem-adsCountBox">
                            <iSelect v-model="adsCouent">
                                <iOption v-for="item in adsCountList" :value="item.value" :key="item.value">{{ item.label }}</iOption>
                            </iSelect>
                        </iFormitem>

                    </iForm> -->

                </div>
                <div class="total">
                    <span class="total_selected" v-text="currSelectCount"></span>
                    <span class="total_cut">/</span>
                    <span class="total_target" v-text="targetSelect"></span>
                </div>
                <tyDivider :title="selectTitle" class="divider"></tyDivider>
                <div class="buttonLine">
                    <iButton @click.native="oneKeySelect" :loading="onkeyLoading">一键选取</iButton>
                    <iButton @click.native="clearSelect" :loading="clearSelectLoading">清空</iButton>
                </div>

                <tyTableView   :beforeCallback="beforeCallback" @loadedEvent="loadedEvent" @select="select" @selectCancel="selectCancel" ref="tyTable" :notAutoLoad="true" :width="685" :columns="columns1" :url="url" :height="353" notDataText="请选择左侧广告投放地区" :params="params">
                </tyTableView>
            </div>
        </div>
        <iSpin size="large" fix v-show="loading" class="mainContent"></iSpin>
    </iModal>
</template>

<script>
import tyDivider from 'components/tyDivider';
import iForm from 'iview/src/components/form';
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
import ModalMixins from 'components/tyModal/baseModal';
import iSpin from 'iview/src/components/spin';
import iButton from 'iview/src/components/button';
import iTree from 'iview/src/components/tree';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';
import iModal from 'iview/src/components/modal';
export default {
    components: {
        tyDivider,
        iButton,
        'iFormitem': iForm.Item,
        iForm,
        iSelect,
        iOption,
        tyIconTextButton,
        tyTableView,
        iModal,
        iSpin,
        iTree
    },
    methods: {
        checkSelect() {
            if (this.currSelectCount > this.targetSelect) {
                this.$Notice.warning({
                    title: '警告',
                    desc: '当前已选门店数量大于目标配置数量，请调整'
                })
            }
        },
        // 列表数据加载完成回调。
        loadedEvent() {
            this.onkeyLoading = false;
            this.clearSelectLoading = false;
        },
        // 一键选取。
        oneKeySelect() {
            if(this.targetSelect == this.currSelect) {
                this.$Notice.warning({
                    title:'提示',
                    desc: '门店选择已完成'
                })
                return;
            }
            var selectNodes = this.$refs.itree.getCheckedNodes();
            var areaId = this.getCurrSelectAaeaId(selectNodes);
            var waitSelect = this.targetSelect;
            // var waitSelect = this.targetSelect - this.currSelectCount;
            // if (waitSelect == 0) {
            //     return;
            // }
            // if (waitSelect < 0) {
            //     this.$Notice.error({
            //         title: '错误',
            //         desc: '选择的门店数量已超出'
            //     })
            //     return;
            // }


            if (areaId && selectNodes && selectNodes.length > 0) {
                this.onkeyLoading = true;
                this.$post(this.$api.onkeySelectStoreUrl, {
                    areaIds: areaId,
                    recordCount: waitSelect,
                    storeType: this.params.storeType,
                    contractId: this.params.contractId,
                }).then((result) => {
                    this.onkeyLoading = false;
                    this.currSelectCount = result.data;
                    this.$refs.tyTable.refresh();
                    if (this.currSelectCount != this.targetSelect) {
                        this.$Notice.warning({
                            title: '提示',
                            desc: '当前' + this.storeTypeText + '类门店数量小于配置数量,不足以一键选取'
                        })
                    }
                }).catch((e) => {
                    this.onkeyLoading = false;
                    this.$Notice.error({
                        title: '错误',
                        desc: e.message
                    })
                });
            } else {
                this.$Notice.warning({
                    title: '错误',
                    desc: '请选择地区'
                })
            }
        },
        clearSelect() {
            this.clearSelectLoading = true;
            this.$post(this.$api.clearSelectStoreUrl, {
                contractId: this.params.contractId,
                storeType: this.params.storeType
            }).then((result) => {
                this.currSelectCount = 0;
                this.$Notice.success({
                    title: '提示',
                    desc: '已清空'
                })
                this.loadedEvent();
                this.$refs.tyTable.refresh();
            }).catch((e) => {
                this.loadedEvent();
                this.$Notice.error({
                    title: '错误',
                    desc: e.message
                })
            })
        },
        // table需要_checked字段，对后端返回的数据进行处理。
        beforeCallback(list) {
            for (let i = 0; i < list.length; i++) {
                list[i]._checked = list[i].isChoose == 1;
            }
        },
        clearData() {
            this.baseData = [];
            this.$refs.tyTable.clearData();
        },
        // 取消选中
        selectCancel(select) {
            if (select.row) {
                this.$post(this.$api.deleteStoreByContractUrl, {
                    contractId: this.params.contractId,
                    storeId: select.row.id
                }).then(() => {
                    if (this.currSelectCount != 0) {
                        this.currSelectCount--;
                    }
                    for (let i = 0; i < this.$refs.tyTable.dataList.length; i++) {
                        if (this.$refs.tyTable.dataList[i].id == select.row.id) {
                            this.$refs.tyTable.dataList[i]._checked = false;
                            this.$refs.tyTable.dataList[i].usedCount--;
                        }
                    }
                    this.$nextTick(() => {
                    })
                }).catch((e) => {
                    this.$Notice.error({
                        title: '错误',
                        desc: e.message
                    })
                })
            }
        },
        select(select) {
            // selection ： 已选择的门店，目前只能单选
            if (select.row) {
                this.$post(this.$api.addStoreContractUrl, {
                    contractId: this.params.contractId,
                    storeId: select.row.id,
                    storeType: this.params.storeType
                }).then((result) => {
                   this.currSelectCount = result.data;
                    for (let i = 0; i < this.$refs.tyTable.dataList.length; i++) {
                        if (this.$refs.tyTable.dataList[i].id == select.row.id) {
                            this.$refs.tyTable.dataList[i]._checked = true;
                            this.$refs.tyTable.dataList[i].usedCount++;
                        }
                    }
                    this.$nextTick(() => {
                    })
                    this.checkSelect();
                }).catch((e) => {
                    this.$Notice.error({
                        title: '错误',
                        desc: e.message
                    })
                })
            }
        },
        setParams(params, targetSelect, currSelectCount) {
            this.params = params;
            this.targetSelect = targetSelect;
            this.currSelectCount = currSelectCount;
            this.checkSelect();
            switch (this.params.storeType) {
                case 1:
                    this.storeTypeText = 'A';
                    this.title = '选择A类网点';
                    this.selectTitle = '已选中A类门店';
                    break;
                case 2:
                    this.storeTypeText = 'B';
                    this.title = '选择B类网点';
                    this.selectTitle = '已选中B类门店';
                    break;
                case 3:
                    this.storeTypeText = 'C';
                    this.title = '选择C类网点';
                    this.selectTitle = '已选中C类门店';
                    break;
            }
            // this.$refs.tyTable.setParams(this.params);
            // this.$refs.tyTable.refresh();
        },
        getCurrSelectAaeaId(row) {
            var areaId = '';
            if (row && row.length > 0) {
                for (let i = 0; i < row.length; i++) {
                    areaId += row[i].id + ',';
                }
                if (areaId.charAt(areaId.length - 1) == ',') {
                    areaId = areaId.substr(0, areaId.length - 1);
                }
            }
            return areaId;
        },
        treeSelect(row) {
            // 根据已勾选的节点，拼接areaid
            var areaId = this.getCurrSelectAaeaId(row);
            if (areaId && row && row.length > 0) {
                this.params.areaIds = areaId;
                this.$refs.tyTable.setParams(this.params);
                this.$refs.tyTable.refresh();
            }

        },
        // 对后端返回的 ztree 数据进行转化
        adapterBaseData(baseData) {
            if (!baseData || baseData.length == 0) {
                return;
            }
            for (let i = 0; i < baseData.length; i++) {
                baseData[i].title = baseData[i].name;
                baseData[i].children = baseData[i].areaList;
                this.adapterBaseData(baseData[i].children);
            }
        }
    },
    data() {
        return {
            storeTypeText: 'A',
            onkeyLoading: false,
            clearSelectLoading: false,
            url: this.$api.getStoreByTypeUrl,
            currSelectCount: 0, // 当前选中数量
            params: {},
            menuFormData: {},
            adsTimelong: 1,
            adsCouent: 1,
            title: '选择A类网点',
            selectTitle: '',
            adsTimelongList: [{
                value: 1,
                label: '15秒'
            }],
            adsCountList: [{
                value: 1,
                label: '80次/天'
            }],
            targetSelect: 0,
            baseData: [],
            columns1: [
                { type: 'selection', width: '50px' },
                { title: '门店名称', key: 'storeName', align: 'center', width: '200px' },
                { title: '设备编码', key: 'equipmentCode', align: 'center', width: '100px' },
                { title: '地区', key: 'cityId', align: 'center', width: '185px' },
                { title: '已投放广告数量', key: 'usedCount', align: 'center', width: '150px' },
            ]
        }
    },
    mixins: [ModalMixins],
    watch: {
        showFlag() {
            if (this.showFlag) {
                this.$post(this.$api.getAreaListByValidStore).then((result) => {
                    this.adapterBaseData(result.data);
                    this.baseData = result.data;
                    this.loading = false;
                }).catch((e) => {
                    this.loading = false;
                    this.$Message.error(e.message);
                })
            } else {
                this.$emit('closeModal', {
                    currSelect: this.currSelectCount,
                    waitSelect: this.targetSelect - this.currSelectCount,
                    storeType: this.params.storeType

                })
                this.clearData();
            }
        }
    },
}
</script>
<style scoped lang="scss">
.total {
    clear: both;
    font-size: 30px;
    text-align: center;
    .total_selected {
        color: #f0857d;
    }
}

.divider {
    font-size: 16px;
    color: #666666;
    width: 507px;
    margin: 0 auto;
}

.fromBox {
    height: 50px;
}

.fromItem-adslongTimeBox {
    width: 35%;
    float: left;
}

.fromItem-adsCountBox {
    width: 35%;
    float: right;
}

.buttonLine {
    padding: 5px 0;
    button {
        &:nth-child(1) {
            width: 120px;
            color: #ffffff;
            background-color: #7edd9c;
            &:hover {
                border-color: #7edd9c;
            }
        }
        &:nth-child(2) {
            width: 120px;
            color: #ffffff;
            background-color: #f0857d;
            &:hover {
                border-color: #f0857d;
            }
        }
    }
}
</style>

<style lang="scss" >
@import '~components/tyModal/modalStyle.scss';
// 隐藏全选
.selectStoreModal {

    .mainContent {
        height: 720px!important;
    }
    th {
        .ivu-checkbox-input,
        .ivu-checkbox-inner {
            display: none;
        }
    }
}
</style>
