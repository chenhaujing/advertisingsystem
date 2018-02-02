<style  lang="scss">
@import '~assets/css/base.scss';
//客户管理页面模态框样式
$modelHeight: 550px;
.clientManagerModal {
	// 覆盖模态框主内容区域padding
	overflow: hidden;
	.ivu-modal-body {
		padding: 0;
	}
	.ivu-tree-title {
		font-size: 16px;
		color: #666666;
	}
	.mainContent {
		height: $modelHeight;
	}
	.leftMenu {
		float: left;
		height: 100%;
		width: 200px;
		background-color: #e6e8eb;
		.zTree {
			padding-left: 15px;
		}
		.ivu-tree-children {
			font-size: 16px;
		} // 菜单标题文本溢出处理。
		.ivu-tree-title {
			max-width: 90%;
			text-overflow: ellipsis;
			overflow: hidden;
		}
	}
	.rightContent {
		height: 100%;
		padding: 10px;
		float: left;
		background-color: #f2f2f2;
		width: 750px;
	} // 搜索框
	.search {
		background-color: #ffffff;
		width: 240px !important;
		float: left;
	}
	.searchBtn {
		outline: none;
		color: #ffffff;
		border: 0;
		font-size: 16px;
		margin-left: 40px;
		border-radius: 4px;
		width: 120px;
		background-color: $mainColor;
		height: 38px;
		line-height: 38px;
		text-align: center;
		float: left;
	}
	.ivu-tree-title-selected,
	.ivu-tree-title-selected:hover {
		background-color: $mainColor;
		color: #ffffff;
	} // 表格
	.modalColums {
		margin-top: 20px;
	} // 覆盖 模态框标题颜色
	.ivu-modal-header {
		background-color: $mainColor;
	} // 覆盖 模态框标题字体颜色   右上角关闭按钮颜色
	.ivu-modal-header p,
	.ivu-modal-header-inner,
	.ivu-modal-close .ivu-icon-ios-close-empty {
		color: #ffffff;
	} // 隐藏脚步
	.ivu-modal-footer {
		display: none;
	}
	.table-tool {
		width: 100%;
		height: 38px;
	}
}
</style>
<template>
	<iModal class="clientManagerModal" style="float: left;" :title="modalTitle" v-model="modal" :mask-closable="false" :width="950">
		<div v-if="loading == false" class="mainContent">
			<div class="leftMenu">
				<iTree class="zTree" :data="baseData" @on-select-change="v=>{selectArea(v)}"></iTree>
			</div>
			<div class="rightContent">
				<div class="table-tool">
					<tySearchInput class="search" :notShowIcon=true @search="search" v-model="params.nickname" placeholder="请输入员工姓名"></tySearchInput>
					<a class="searchBtn" @click="refreshTable">查询</a>
				</div>
				<tyTableView ref="userTable" :columns="modalColums" :notAutoLoad=true :url="url" class="modalColums" :params="params" notDataText="暂没有找到匹配的员工数据" :height="355">
				</tyTableView>
			</div>
			<!-- <Tree :data="baseData"></Tree>
					
							<div class="modalContent-left">
								
							</div>
							<div class="clear"></div> -->
		</div>
		<div class="demo-spin-container" v-if="loading == true">
			<iSpin size="large" fix></iSpin>
		</div>
	</iModal>
</template>

<script>
import iTree from 'iview/src/components/tree';
import iModal from 'iview/src/components/modal';
import iSpin from 'iview/src/components/spin';
import tyTableView from 'components/tyTableView';
import tySearchInput from 'components/tySearchInput';

var selTreeData = '';
// 对后端返回的 tree 数据进行转化
function adapterBaseData(baseData) {
	if (!baseData || baseData.length == 0) {
		return;
	}
	for (let i = 0; i < baseData.length; i++) {
		baseData[i].title = baseData[i].name;
		baseData[i].selected = false;
		adapterBaseData(baseData[i].children);
	}
}


export default {
	props: ['modalColumns', 'modalTitle', 'userInfoData'],
	data() {
		return {
			loading: true,
			modal: false,
			url: this.$api.getUseruserInfosStatistics,
			baseData: [],
			params: {
				nickname: ''
			},
			modalColums: [
				{
					title: '编号', key: 'customerNumber', align: 'center', render: (h, params) => {
						var text = Number(params.index) + 1;
						return h('span', text);
					}
				},
				{ title: '员工姓名', key: 'nickname', align: 'center' },
				{ title: '手机号', key: 'phoneNumber', align: 'center' },
				{ title: '维护客户数量', key: 'customerCount', align: 'center' },
				{ title: '合同签约数量', key: 'signedContractCount', align: 'center' },
				{
					title: '操作',
					key: 'action',
					width: 150,
					align: 'center',
					render: (h, params) => {
						return h('div', {
							class: {
								'toolbarRight': true
							}
						}, [
								h('a', {
									attr: {
										href: 'javascript:void(0);'
									},
									on: {
										click: () => {
											this.distributedCustomer(params.row)
										}
									}
								}, [
										h('span', {
											class: {
												'iconfont icon-fenpei': true
											}
										}),
										h('span', '分配')
									]),
							]);
					}
				}
			]
		}
	},
	watch:{
		modal(val){
			if (val) {
				this.init()
			}else{
				selTreeData=''
			}
		}
	},
	methods: {
		init:function(){
                this.refreshTree().then(() => {
					this.selectArea(this.baseData)
				});
		},
		refreshTree: function(init) {
			return this.$post(this.$api.getOrganizationUrl).then((data) => {
				adapterBaseData(data.data);
				this.baseData = data.data;
				this.baseData[0].selected =true;
			}).catch((error) => {
				this.$Message.error({
					content: error.message || '加载树数据失败'
				})
			})
		},
		refreshTable() {
			this.$refs.userTable.refresh()
		},
		search() {
			this.refreshTable()
		},
		distributedCustomer(parmas) {
			this.$post(this.$api.distributedCustomerUrl.replace(/\{customerId\}/, this.userInfoData.id).replace(/\{userId\}/, parmas.id)).then((data) => {
				this.$Message.success({
					content: "广告客户分配成功"
				})
				this.$emit('refreshClientTable')
				this.modal = false;
			}).catch((error) => {
				this.$Message.error({
					content: error.message || "广告客户分配失败"
				})
				this.modal = false;
			})
		},
		selectArea(v) {
			console.log(selTreeData)
			if (!v.length) {
				selTreeData = '';
				this.$refs.userTable.setUrlParams({
					organizationId: '0'
				});
				return
			}
			if (selTreeData == v[0].id) {
				return
			}
			this.$refs.userTable.setUrlParams({
				organizationId: v[0].id
			});
			selTreeData = v[0].id;
			this.refreshTable();
			
		},
		show: function() {
			this.modal = true;
			this.loading = false;
		},
	},
	components: {
		iModal,
		iSpin,
		tyTableView,
		tySearchInput,
		iModal,
		iTree
	}
}
</script>
