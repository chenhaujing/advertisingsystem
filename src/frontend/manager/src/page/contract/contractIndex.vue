<template>
    <div class="contractIndex">
        <iRow :gutter="16">
            <iCol span="6" class="col">
                <a class="topItem allItem" :class="{'action': contractState.all == currTab}" @click="changeTab(contractState.all)">
                    <div class="topItemMain">
                        <div class="topItem-icon topItem-icon-all">
                        </div>
                        <div class="topItem-info">
                            <div class="topItem-info-number" v-text="statusToTal.total"></div>
                            <div class="topItem-info-tag">全部合同</div>
                        </div>
                    </div>
                </a>
            </iCol>
            <iCol span="6" class="col">
                <a class="topItem waitVerifyItem" :class="{'action': contractState.pendingAuditing == currTab}" @click="changeTab(contractState.pendingAuditing)">
                    <div class="topItemMain">
                        <div class="topItem-icon topItem-icon-waitVerify"></div>
                        <div class="topItem-info">
                            <div class="topItem-info-number" v-text="statusToTal.toAudit"></div>
                            <div class="topItem-info-tag">待审核</div>
                        </div>
                    </div>
                </a>
            </iCol>
            <iCol span="6" class="col">
                <a class="topItem waitSignItem" :class="{'action': contractState.pendingSign == currTab}" @click="changeTab(contractState.pendingSign)">
                    <div class="topItemMain">
                        <div class="topItem-icon topItem-icon-waitSign"></div>
                        <div class="topItem-info">
                            <div class="topItem-info-number" v-text="statusToTal.toSign"></div>
                            <div class="topItem-info-tag">待签约</div>
                        </div>
                    </div>
                </a>
            </iCol>
            <iCol span="6" class="col">
                <a class="topItem waitRunItem" :class="{'action': contractState.pendingExecutiom == currTab}" @click="changeTab(contractState.pendingExecutiom)">
                    <div class="topItemMain">
                        <div class="topItem-icon topItem-icon-waitRun"></div>
                        <div class="topItem-info">
                            <div class="topItem-info-number" v-text="statusToTal.toExecute"></div>
                            <div class="topItem-info-tag">待执行</div>
                        </div>
                    </div>
                </a>
            </iCol>
        </iRow>
        <iRow :gutter="16" class="tableList">
            <iCol span="18">
                <tyTableView 
                @dblclick="dblclick" 
                ref="tyTable" 
                :columns="columns1" :url="url" 
                :pageSizeOpts="[15, 25, 35]"
                :height="760" 
                :params="params">
                </tyTableView>
            </iCol>
            <iCol span="6">
                <tyAddButton class="tyAddButton" text="新建合同" @click.native="addContract" v-if="permission[$p.c]"></tyAddButton>
                <div class="searchBox" v-if="permission[$p.s]">
                    <tySearchInput v-model="keyword" class="searchComponent" placeholder="搜索" @search="searchContract"></tySearchInput>
                    <div class="checkBoxList">
                        <iCheckbox class="itemCheckBox" v-model="checkStateContract" @on-change="checkChangeEvent('checkStateContract')">合同编号</iCheckbox>
                        <iCheckbox class="itemCheckBox" v-model="checkStateName" @on-change="checkChangeEvent('checkStateName')">合同名称</iCheckbox>
                    </div>
                </div>
            </iCol>
        </iRow>
        <AuditModal ref="auditModal" @optionSuccess="refresh"></AuditModal>
        <tySelectClientModal addTitle="选择客户" ref="tySelectClientModal"></tySelectClientModal>
    </div>
</template>
<script>
import SelectStoreModal from './selectStoreModal';
import iCheckbox from 'iview/src/components/checkbox';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import tySearchInput from 'components/tySearchInput';
import tySelectClientModal from './selectClientModal';
import tyAddButton from 'components/tyAddButton';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';
import ContractState from './contractState';
import AuditModal from './auditModal';
import contractMixni from './contractMixni';
export default {
    mixins: [contractMixni],
    created() {
    },
    components: {
        AuditModal,
        SelectStoreModal,
        tySelectClientModal,
        tyIconTextButton,
        iCheckbox,
        iCheckboxGroup: iCheckbox.Group,
        iRow,
        iCol,
        tySearchInput,
        tyAddButton,
        tyTableView
    },
    mounted() {
        this.$post(this.$api.getContractTotalData).then((result) => {
            this.statusToTal = result.data;
        }).catch((e) => {
            this.$Notice.error({
                title: '错误',
                desc: e.message
            })
        })
    },
    methods: {
        refresh() {
            this.$refs.tyTable.refresh();
            this.getContractTotalData().then((result) => {
                this.statusToTal = result;
            });
        },
        openOptionModal(row, mode) {
            this.$refs.auditModal.setParams(row.id, mode)
            this.$refs.auditModal.toggle();
        },
        dblclick(row) {
            this.$router.push({
                name: 'contractInfo', query: {
                    cid: row.id
                }
            });
        },
        gotoEdit(row) {
            this.$router.push({
                name: 'editContract',
                query: {
                    contractId: row.id
                }
            })
        },
        // 合同搜索
        searchContract() {
            if (!this.checkStateContract && !this.checkStateName) {
                this.$Message.error("请选择搜索类型");
                return;
            }
            // 按合同编号搜索
            if (this.checkStateContract) {
                this.params.contractName = '';
                this.params.contractCode = this.keyword;
                this.$refs.tyTable.setParams(this.params);
                this.$refs.tyTable.refresh();
                return;
            }
            // 按合同名称搜索
            if (this.checkStateName) {
                this.params.contractCode = '';
                this.params.contractName = this.keyword;
                this.$refs.tyTable.setParams(this.params);
                this.$refs.tyTable.refresh();
                return;
            }
        },
        changeTab(status) {
            if (this.currTab == status) {
                return;
            }
            this.currTab = status;
            this.params.contractStatus = status;
            this.$refs.tyTable.setParams(this.params);
            this.$refs.tyTable.refresh();
        },
        addContract() {
            this.$refs.tySelectClientModal.toggle();
        },
        deleteConfirm(row) {
            this.deleteControll(row).then((r) => {
                this.getContractTotalData().then((result) => {
                    this.statusToTal = result;
                    this.$refs.tyTable.refresh();
                    this.$Notice.success({
                        title: '提示',
                        desc: "删除成功！"
                    });
                }).catch((e) => {
                    this.$Notice.error({
                        title: '错误',
                        desc: e.message
                    });
                });
            })
        },
        // 根据合同状态，返回操作按钮
        getTableButtonByStatus(h, row) {
            var show = h(tyIconTextButton, {
                props: {
                    text: '查看',
                    iconClass: 'icon-chakan'
                },
                nativeOn: {
                    click: () => {
                        this.dblclick(row);
                    }
                }
            });
            var submitAudit = h(tyIconTextButton, {
                props: {
                    text: '提交审核',
                    iconClass: 'icon-tijiaoshenhe'
                },
                nativeOn: {
                    click: () => {
                        this.submitAudit(row.id, true).then((result) => {
                            this.statusToTal = result;
                        });
                    }
                }
            });
            var audit = h(tyIconTextButton, {
                props: {
                    text: '审核',
                    iconClass: 'icon-audit'
                },
                nativeOn: {
                    click: () => {
                        this.openOptionModal(row, 1);
                    }
                }
            });
            var sign = h(tyIconTextButton, {
                props: {
                    text: '签约',
                    iconClass: 'icon-qianyue'
                },
                nativeOn: {
                    click: () => {
                        this.openOptionModal(row, 2);
                    }
                }
            });
            var overRun = h(tyIconTextButton, {
                props: {
                    text: '终止执行',
                    iconClass: 'icon-zhongzhizhihang'
                },
                nativeOn: {
                    click: () => {
                        this.openOptionModal(row, 3);
                    }
                }
            });
            var edit = h(tyIconTextButton, {
                props: {
                    text: '编辑',
                    iconClass: 'icon-bianji'
                },
                nativeOn: {
                    click: () => {
                        this.gotoEdit(row);
                    }
                }
            });
            var del = h(tyIconTextButton, {
                props: {
                    text: '删除',
                    iconClass: 'icon-laji'
                },
                nativeOn: {
                    click: () => {
                        this.deleteConfirm(row);
                    }
                }
            });
            var btns = [];
            var delFlag = this.$store.state.check(this.$m.contract, this.$p.d);
            var editFlag = this.$store.state.check(this.$m.contract, this.$p.u);
            var auditFlag = this.$store.state.check(this.$m.contract, this.$p.audit);
            var overFlag = this.$store.state.check(this.$m.contract, this.$p.over);
            // 不同状态的合同，显示不同的操作按钮
            switch (row.contractStatus) {
                // 待审核
                case this.contractState.pendingAuditing:
                    btns = [show];
                    editFlag && btns.push(edit);
                    auditFlag && btns.push(audit);
                    break;
                // 待提交
                case this.contractState.pendingCommit:
                    btns = [submitAudit];
                    editFlag && btns.push(edit);
                    delFlag && btns.push(del);
                    break;
                // 待签约
                case this.contractState.pendingSign:
                    btns = [show];
                    var signFlag = this.$store.state.check(this.$m.contract, this.$p.sign);
                    signFlag && btns.push(sign)
                    break;
                // 待执行
                case this.contractState.pendingExecutiom:
                    btns = [show, overRun];
                    overFlag && btns.push(overRun);
                    break;
                // 以外终止
                case this.contractState.abruptlyTerminated:
                    btns = [show];
                    break;
                // 执行完成
                case this.contractState.finished:
                    btns = [show];
                    break;
                // 执行中
                case this.contractState.executing:
                    btns = [show,overRun];
                    break;
            }
            return h('div', {
                class: {
                    optionBar: true
                }
            }, btns);
        }
    },
    watch: {
        checkStateContract() {
            this.checkStateContract && (this.checkStateName = false);
        },
        checkStateName() {
            this.checkStateName && (this.checkStateContract = false);
        }
    },
    computed: {
        permission() {
            return this.$store.state.getRolePermission(this.$m.contract);
        }
    },
    data() {
        return {
            statusToTal: {},
            checkStateContract: true,
            checkStateName: false,
            currTab: 0,
            contractState: ContractState.Status,
            url: this.$api.getContractListUrl,
            keyword: '',
            params: {
                contractStatus: 0
            },
            columns1: [
                { title: '合同编号', key: 'contractCode', align: 'center', ellipsis: true },
                { title: '合同名称', key: 'contractName', align: 'center', ellipsis: true },
                { title: '广告客户', key: 'customerName', align: 'center', ellipsis: true },
                { title: '合同状态', key: 'contractStatuName', align: 'center', ellipsis: true },
                { title: '签约时间', key: 'signTime', align: 'center', ellipsis: true },
                { title: '签约人', key: 'signerName', align: 'center', ellipsis: true },
                { title: '维护人', key: 'ownerName', align: 'center', ellipsis: true },
                {
                    ellipsis: true,
                    title: '操作',
                    key: 'action',
                    width: 240,
                    align: 'center',
                    render: (h, params) => {
                        return this.getTableButtonByStatus(h, params.row)
                    }
                }
            ]
        }
    }
}
</script>

<style lang="scss" scoped>
@import '~assets/css/base.scss';
%mt20 {
    margin-top: 20px;
}

.tyAddButton:hover {
    border-color: $mainColor;
}

.topItem {
    transition: .3s;
    display: block;
    height: 130px;
    background-color: #ffffff;
}


.topItemMain {
    width: 160px;
    margin: 0 auto;
    position: relative;
    position: relative;
    height: 70px;
    top: 50%;
    transform: translateY(-50%);

    .topItem-icon {
        width: 58px;
        height: 58px;
        position: relative;
        top: 50%;
        float: left;
        border-radius: 50%;
        background-size: 100% 100%;
        transform: translateY(-50%);
        background-color: $mainColor;
    }
    .topItem-icon-all {
        background-color: $mainColor;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_all.png');
        }
    }
    .topItem-icon-waitVerify {
        background-color: #f0857d;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_waitVerify.png');
        }
    }
    .topItem-icon-waitSign {
        background-color: #7edd9c;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_waitSign.png');
        }
    }
    .topItem-icon-waitRun {
        background-color: #fcb322;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_waitRun.png');
        }
    }

    .topItem-info {
        float: right;
        padding-left: 20px;
        color: #333333;
        .topItem-info-number {
            font-size: 30px;
        }
        .topItem-info-tag {
            font-size: 16px;
        }
    }
}

.tableList {
    @extend %mt20;
}



// 右侧搜索框
.searchBox {
    padding: 20px;
    margin-top: 20px;
    height: 145px;
    background-color: #ffffff;
}

.searchComponent,
.search {
    background-color: #edf1f4!important;
}

// 选中搜索类型
.checkBoxList {
    margin-top: 40px;
    .itemCheckBox {
        float: left;
        &:last-child {
            float: right;
        }
    }
}

.ivu-checkbox-wrapper {
    font-size: 14px!important;
    color: #333333;
}

// 选中状态样式
.action {
    &.allItem {
        background-color: $mainColor;
    }
    &.waitVerifyItem {
        background-color: #f0857d;
    }
    &.waitSignItem {
        background-color: #7edd9c;
    }
    &.waitRunItem {
        background-color: #fcb322;
    }
    .topItem-info {
        color: #ffffff;
    } // 全部合同
    .topItem-icon-all {
        background-color: #ffffff;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_all_active.png');
        }
    } // 待审核
    .topItem-icon-waitVerify {
        background-color: #ffffff;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_waitVerify_active.png');
        }
    } // 待签约
    .topItem-icon-waitSign {
        background-color: #ffffff;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_waitSign_active.png');
        }
    }
    .topItem-icon-waitRun {
        background-color: #ffffff;
        &:after {
            content: '';
            @include vhCenter;
            display: block;
            width: 20px;
            height: 24px;
            background-size: 100% 100%;
            background-image: url('~assets/img/contract/contractIndex_waitRun_active.png');
        }
    }
}
</style>
<style lang="scss">
.ivu-checkbox-wrapper {
    margin-right: 0;
}

.optionBar>.iconfont {
    color: #666666;
    margin-right: 6px;
}
</style>
