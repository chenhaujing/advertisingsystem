<style  lang="scss" scoped>
@import '~assets/css/base.scss';
//组织机构管理页面模态框样式
$modelHeight: 550px;
.addStructure {
	.addStructureForm {
		width: 100%;
		box-sizing: border-box;
		padding: 20px;
		background-color: #edf1f4;
		.searchBox {
			width:240px;
			background-color: #edf1f4;
			margin-bottom: 20px;
			.searchComponent{
				background-color: #fff;
			}
		}
	} // 覆盖模态框主内容区域padding
	overflow: hidden;
	.ivu-modal-body {
		padding: 0;
	}
	.ivu-form-item-label {
		font-size: 16px;
	}
	.ivu-input {
		height: 34px;
	}
	// .ivu-select-single .ivu-select-selection {
	// 	height: 26px;
	// 	.ivu-select-placeholder {
	// 		height: 100%;
	// 	}
	// }
	.ivu-tree-title {
		font-size: 16px;
		color: #666666;
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
		height: 38px;
	}
}
</style>
<template>
	<iModal class="addStructure" :title="modalTitle" v-model="modal" :mask-closable="false" :width="900">
		<div class="addStructureForm">
			<div class="searchBox">
				<iSearchInput class="searchComponent" @search="search" v-model="params.name" :notShowIcon=true :params="params" placeholder="请输入组织结构的名称"></iSearchInput>
				<!-- <a class="searchBtn" @click="search">查询</a> -->
			</div>
			<tyTableView ref="tyTable"
			:number="true" 
			:notAutoLoad=true :columns="columns1" :url="url" :height="280" notDataText="暂无任何匹配的组织机构数据" :params="params">
			</tyTableView>
		</div>
	</iModal>
</template>
<script>
import iModal from 'iview/src/components/modal';
import iSearchInput from 'components/tySearchInput';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';
export default {
	data() {
		return {
			url: this.$api.getOrganizationListUrl,
			modal: false,
			modalTitle: '选择组织机构',
			addBaseStructureForm: {
				total: '',
				structure: '',
				man: ''
			},
			params: {
				pageIndex: 0,
				pageSize: 20,
				name: "",
				currentId:""
			},
			columns1: [
				{ title: '编号',key: '_NUMBER_' , align: 'center', width: 65 ,ellipsis: true,},
				{ title: '组织机构名称', key: 'name', align: 'center', width: 150,ellipsis: true,},
				{ title: '负责人', key: 'ownerName', align: 'center', width: 115,ellipsis: true, },
				{ title: '上级组织', key: 'parentOrganizationName', align: 'center', width: 150,ellipsis: true, },
				{ title: '创建人', key: 'creatorName', align: 'center', width: 110 ,ellipsis: true,},
				{ title: '创建时间', key: 'createdTime', align: 'center', width: 140, },
				{
					title: '操作',
					key: 'action',
					width: 110,
					align: 'center',
					render: (h, params) => {
						return h('div', [
							h(tyIconTextButton, {
								nativeOn: {
									click: () => {
										this.emitData(params)
									}
								},
								  props: {
								text: '选择',
								iconClass:'icon-tiaozhuan'
                            }
							}),
						]);
					}
				}
			]
		}
	},
	props:["currentId"],
	watch: {
		modal(val) {
			if (val) {
                this.params.currentId=this.currentId;
				this.$refs.tyTable.refresh();
			}
		}
	},
	methods: {
		ok() {
			this.modal = false;
		},
		emitData(params) {
			//数据
			this.$emit('rootOrganzationData', params);
			this.modal = false;
		},
		cancel() {
			this.modal = false;
		},
		search(value) {
				this.$refs.tyTable.setParams(this.params);
				this.$refs.tyTable.refresh();
		},
	},
	components: {
		iModal,
		iSearchInput,
		tyTableView,
	}
}
</script>
