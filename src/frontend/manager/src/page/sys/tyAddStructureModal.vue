<style  lang="scss">
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
			width: 200px;
			background: #fff;
			margin-bottom: 20px;
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
	} // 	.searchBtn{
	// 	outline:none;
	// 	color: #ffffff;
	// 	border: 0;
	// 	font-size: 16px;
	// 	margin-left: 40px;
	// 	border-radius: 4px;
	// 	width: 120px;
	// 	background-color: $mainColor;
	// 	height: 38px;
	// 	line-height: 38px;
	// 	text-align: center;
	// 	float: left;
	// }
}
</style>
<template>
	<iModal class="addStructure" :title="modalTitle" v-model="modal" :mask-closable="false" :width="1000">
		<div class="addStructureForm">
			<div class="searchBox">
				<tySearchInput class="searchComponent" :notShowIcon='true' @search="search" v-model="params.nickname" :params="params" placeholder="请输入员工姓名"  ></tySearchInput>
			</div>
			<tyTableView ref="tyTable" 
			:number="true"
			:notAutoLoad=true :columns="columns1" :url="url" :height="280" notDataText="没有找到任何匹配的员工数据" :params="params">
			</tyTableView>
		</div>
	</iModal>
</template>
<script>
import iModal from 'iview/src/components/modal';
import tySearchInput from 'components/tySearchInput';
import tyTableView from 'components/tyTableView';
import tyIconTextButton from 'components/tyIconTextButton';

export default {
	data() {
		return {
			modal: false,
			modalTitle: '选择负责人',
			addBaseStructureForm: {
				total: '',
				structure: '',
				man: ''
			},
			url: this.$api.searchMemberUrl,
			params: {
				"pageIndex": 0,
				"pageSize": 0,
				"nickname": "",
			},
			columns1: [
				{ title: '编号', key: '_NUMBER_' ,align: 'center', width: 65 ,ellipsis: true,},
				{ title: '员工姓名', key: 'nickname', align: 'center', width: 120,ellipsis: true, },
				{ title: '联系方式', key: 'phoneNumber', align: 'center', width: 120,ellipsis: true, },
				{ title: '上级领导', key: 'leader', align: 'center', width: 120,ellipsis: true, },
				{ title: '从属组织', key: 'organizationName', align: 'center', width:120,ellipsis: true, },
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
					},
					width: 100
				},
				{ title: '创建人', key: 'creator', align: 'center', width: 100 },
				{
					title: '创建时间', key: 'createdTime', align: 'center', width: 100, ellipsis: true,
					render: (h, params) => {
						var text = params.row.createdTime.substr(0, 10);
						return h('span', text);
					}
				},
				{
					title: '操作',
					key: 'action',
					width:100,
					align: 'center',
					render: (h, params) => {
						return h('div', [
							h(tyIconTextButton, {
								nativeOn: {
									click: () => {
										this.show(params)
									}
								}, props: {
									text: '选择',
									iconClass: 'icon-tiaozhuan'
								}
							}),
						]);
					}
				}
			]
		}
	},
	watch: {
		modal(val) {
			if (val) {
				this.$refs.tyTable.refresh();
			}
		}
	},
	methods: {
		ok() {
			this.modal = false;
		},
		show(params) {
			//负责人数据
			this.$emit('leaderData', params);
			this.modal = false;
		},
		cancel() {
			this.modal = false;
		},
		search(value) {
			this.$refs.tyTable.refresh();
		}
	},
	components: {
		iModal,
		tySearchInput,
		tyTableView,
	}
}
</script>
