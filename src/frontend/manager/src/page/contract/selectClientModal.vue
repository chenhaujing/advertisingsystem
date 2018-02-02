<template>
    <iModal :class-name="controllHeight" class="baseModal hideModal selectClientModal" :title="modalTitle" v-model="showFlag" :mask-closable="false" :width="950">
        <div v-show="!loading" class="mainContent">
            <iTree class="leftMenu" :data="baseData" @on-select-change="treeSelect" ref="leftTree">
            </iTree>
            <div class="rightContent">
                <div class="table-tool">
                    <tySearchInput class="search" @search="search" v-model="params.customername" placeholder="请输入广告客户名称"></tySearchInput>
                    <a class="searchBtn" @click="search">查询</a>
                </div>
                <tyTableView width="730" ref="tyTable" :params="params" :columns="columns" :url="url" notAutoLoad=true class="modalColums" :height="355" notDataText="没有找到任何相关客户数据">
                </tyTableView>
            </div>
        </div>
        <iSpin size="large" fix v-show="loading" class="mainContent"></iSpin>
    </iModal>
</template>

<script>
import iTree from 'iview/src/components/tree';
import tySearchInput from 'components/tySearchInput';
import tyTableView from 'components/tyTableView';
import ModalMixins from 'components/tyModal/baseModal';
import iSpin from 'iview/src/components/spin';
import iModal from 'iview/src/components/modal';
import tyOrganizationTree from 'components/tyOrganizationTree';
import tyIconTextButton from 'components/tyIconTextButton';
export default {
    components: {
        tyIconTextButton,
        tySearchInput,
        tyTableView,
        iModal,
        iSpin,
        iTree,
        tyOrganizationTree
    },
    mixins: [ModalMixins],
    watch: {
        showFlag() {
            if (this.showFlag) {
                this.$post(this.$api.getAreaListByValid).then((result) => {
                    this.baseData = this.adapterBaseData(result.data);
                    if (this.baseData && this.baseData.length > 0) {
                        this.baseData[0].selected = true;
                    }
                    this.loading = false;
                    this.treeSelect(this.baseData)
                }).catch((e) => {
                    this.loading = false;
                    this.$Message.error(e.message);
                })
            }else{
             this.params.areaId=null
             this.params.customername=''
            }
            }
    },
    methods: {
        treeSelect(node) {
            if (!node) {
                return;
            }
            if (node[0].id == this.params.areaId&&node[0].id!=null) {
                return;
            }
            this.params.areaId = node[0].id;
            this.$refs.tyTable.setParams(this.params);
            this.$refs.tyTable.refresh();
        },
        search() {
            this.$refs.tyTable.setParams(this.params);
            this.$refs.tyTable.refresh();
        },
        // 对后端返回的 ztree 数据进行转化
        adapterBaseData(baseData) {
            if (!baseData || baseData.length == 0) {
                return;
            }
            for (let i = 0; i < baseData.length; i++) {
                baseData[i].title = baseData[i].name;
                baseData[i].children = baseData[i].areaList;
                baseData[i].selected = false;
                this.adapterBaseData(baseData[i].children);
            }
            var temp = [{
                title: '广告客户',
                id: null,
                selected: false,
                children: baseData
            }]
            return temp;
        },
        selectClient(rows) {
            this.$router.push({
                name: 'addContract',
                query: {
                    clientId: rows.id
                }
            })
            this.toggle();
        }
    },
    data() {
        return {
            controllHeight: 'controllHeight',
            keyword: '',
            url: this.$api.clientListUrl,
            params: {
                areaId: null,
                customername: ''
            },
            baseData: [],
            loadListener: 0,
            loading: true,
            modalTitle: '选择广告客户',
            columns: [
                { title: '编号', key: 'customerNumber', align: 'center' },
                { title: '客户名称', key: 'name', align: 'center' },
                { title: '所在城市', key: 'cityName', align: 'center' },
                { title: '所属行业', key: 'industry', align: 'center' },
                { title: '维护业务员', key: 'ownerName', align: 'center' },
                {
                    width: 120,
                    title: '操作',
                    key: 'action',
                    align: 'center',
                    render: (h, params) => {
                        return h('div', [
                            h(tyIconTextButton, {
                                props: {
                                    text: '选定',
                                    iconClass: 'icon-bianji'
                                },
                                nativeOn: {
                                    click: () => {
                                        this.selectClient(params.row);
                                    }
                                }
                            }),
                        ])
                    }
                }
            ]
        }
    }
}
</script>

<style lang="scss">
.selectClientModal {
    .search {
        width: 290px;
    }
    .mainContent {
        height: 100%;
    }
    .controllHeight {
        .ivu-modal-content {
            height: 540px;
        }
    }
}
</style>
