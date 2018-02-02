<style lang="scss">
@import '~assets/css/base.scss';
//人员管理页面模态框样式
$modelHeight: 550px;
//
.addMember {
	.addMemberForm {
		width: 325px;
		margin: 0 auto;
		box-sizing: border-box;
		padding: 30px 0 40px;
		.memberForm {
			width: 100%;
			.memberBox-left {
				width: 46%;
				float: left;
			}
			.memberBox-right {
				width: 46%;
				float: right;
			}
			.selectOrganzationBox {
				width: 100%;
				.label {
					font-size: 16px;
					line-height: 36px;
				}
				.selectOrganzation {
					line-height: 32px;
					width: 100%;
					box-sizing: border-box;
					border: 1px solid #dddee1;
					margin-bottom: 24px;
					border-radius: 4px;
					padding: 0 7px;
					cursor: pointer;
				}
				.disableClass.selectOrganzation {
					cursor: not-allowed;
				}
				.addOrganzation {
					float: left;
					font-size: 14px;
				}
				.icon {
					float: right;
				}
			}
		}
		button {
			width: 120px;
			height: 34px;
			border: 0;
			outline: none;
			border-radius: 3px;
			cursor: pointer;
		}
		.saveBtn {
			color: #fff;
			background-color: #4cabe0;
		}
		.saveBtn:active {
			color: #4cabe0;
			background-color: #fff;
			border: 1px solid #4cabe0;
		}
		.cancelBtn {
			float: right;
			background-color: #dcdee0;
			color: #999;
		}
		.cancelBtn:active {
			background-color: #999;
			color: #dcdee0;
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
	// 	height: 34px;
	// 	.ivu-select-placeholder {
	// 		height: 100%;
	// 	}
	// }
	.ivu-tree-title {
		font-size: 16px;
		color: #666666;
	}
	.mainContent {
		height: $modelHeight;
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
	<div>
		<iModal class="addMember" :title="membertype == 'create' ? '添加人员' : '编辑人员'" v-model="modal" :mask-closable="false" :width="600">
			<div class="addMemberForm">
				<iForm :model="memberForm" class="memberForm">
					<div class="memberBox-left">
						<iFormitem label="人员姓名">
							<iInput v-model="memberForm.nickname"></iInput>
						</iFormitem>
					</div>
					<div class="memberBox-right">
						<iFormitem label="角色名称">
							<iSelect v-model="memberForm.roleId" placeholder="请选择角色" @on-change="changeRole" label-in-value>
								<iOption v-for="data in optionData" :value="data.id" :key="data.id" :label="data.roleName"></iOption>
							</iSelect>
						</iFormitem>
					</div>
					<div class="clear"></div>
					<div class="memberBox-left">
						<iFormitem label="联系电话">
							<iInput v-model="memberForm.phoneNumber"></iInput>
						</iFormitem>
					</div>
					<div class="memberBox-right">
						<div class="selectOrganzationBox">
							<div class="label">从属组织</div>
							<input type="text" class="selectOrganzation disableClass" disabled v-show="disableOrganzation" v-model="memberForm.organizationName">
							<div class="selectOrganzation" @click="selectOrganzation" v-show="!disableOrganzation">
								<span class="addOrganzation" v-text="memberForm.organizationName"></span>
								<span class="icon iconfont icon-jia1"></span>
								<div class="clear"></div>
							</div>
						</div>
					</div>
					<div class="clear"></div>
				</iForm>

				<button class="saveBtn" @click="ok" v-text="membertype == 'create' ? '添加' : '保存'"></button>
				<button class="cancelBtn" @click="cancel">取消</button>
				<div class="clear"></div>
			</div>
		</iModal>
		<tySelectOrganzationStructureModal ref="SelectOrganzationStructureModal" @rootOrganzationData="rootOrganzationData"></tySelectOrganzationStructureModal>
	</div>
</template>
<script>
import iModal from 'iview/src/components/modal';
import iForm from 'iview/src/components/form';
import iInput from 'iview/src/components/input';
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
import tySelectOrganzationStructureModal from '../sys/tySelectOrganzationStructureModal';

var saveData={};

//判断类型

export default {
	components: {
		iModal,
		iForm,
		'iFormitem': iForm.Item,
		iInput,
		iSelect,
		iOption,
		tySelectOrganzationStructureModal
	},
	props: ['personData', 'membertype'],
	data() {
		return {
			modal: false,
			optionData: [],
			roleTypeName: '',
			disableOrganzation: false,
			disableClasses: '',
			isManager:false,
			memberForm: {
				phoneNumber: '',
				organizationId: '',
				organizationName: '',
				nickname: '',
				roleId: '',
			}
		}
	},
	watch: {
		modal(v){
			if (!v) {
			saveData={};
			this.clearData();
			} else {
			saveData=Object.assign({},this.personData)	
		    this.$post(this.$api.getRoleAllListUrl).then((data) => {
			this.optionData = data.data;
			//判断是否是管理人员进入
			var k = null;
			for (var i = 0; i < this.optionData.length; i++) {
				if (this.optionData[i].id == saveData.roleId) {
					k = this.optionData[i].roleType;
					if(k==this.$roleType.manager){
					this.isManager=true;
					}else{
					this.isManager=false;	
					}
				}
				var val = this.personData;
				if (this.membertype == 'edit') {
					this.memberForm = {
						phoneNumber: val.phoneNumber,
						organizationId: val.organizationId,
						organizationName: val.organizationName,
						nickname: val.nickname,
						roleId: val.roleId,
						id: val.id
					}
				} else {
					saveData={};
					this.clearData();
				}

			}
		}, (mes) => {
		})

			}
		},
		isManager(v){
				this.disableOrganzation=v;
		}
	},
	methods: {
		rootOrganzationData(data) {
			this.memberForm.organizationId = data.row.id;
			this.memberForm.organizationName = data.row.name;
		},
		clearData() {
			this.memberForm = {
				phoneNumber: '',
				organizationId: '',
				organizationName: '',
				nickname: '',
				roleId: '',
			}
		},
		selectOrganzation() {
			this.$refs.SelectOrganzationStructureModal.modal = true;
		},
		changeRole(data) {
			var k = null;
			for (var i = 0; i < this.optionData.length; i++) {
				if (this.optionData[i].id == data.value) {
					k = this.optionData[i].roleType
				}
			}
				if (k == this.$roleType.manager) {
				this.disableOrganzation=true;
				if(this.membertype=='create' || !this.isManager){
				this.memberForm.organizationId='';
				this.memberForm.organizationName='';
				}
			   if(this.membertype=='edit' && this.isManager){
				if(this.memberForm.organizationId!=saveData.organizationId){
				this.memberForm.organizationId=saveData.organizationId||'';
				this.memberForm.organizationName=saveData.organizationName||'';
				}
			}}else{
				this.disableOrganzation=false;
				this.memberForm.organizationId=saveData.organizationId||'';
				this.memberForm.organizationName=saveData.organizationName||'';
			}
			//判断类型
			// this.decideType();	
		},
		// decideType(){
        //         var k=null;
		// 		if (k == this.$roleType.manager) {
		// 		this.disableOrganzation=true;
		// 		console.log(this.disableOrganzation)
		// 		if(this.membertype=='create' || !this.isManager){
		// 		this.memberForm.organizationId='';
		// 		this.memberForm.organizationName='';
		// 		}
		// 	   if(this.membertype=='edit' && this.isManager){
		// 		if(this.memberForm.organizationId!=saveData.organizationId){
		// 		this.memberForm.organizationId=saveData.organizationId||'';
		// 		this.memberForm.organizationName=saveData.organizationName||'';
		// 		}
		// 	}}else{
		// 		this.disableOrganzation=false;
		// 		this.memberForm.organizationId=saveData.organizationId||'';
		// 		this.memberForm.organizationName=saveData.organizationName||'';
		// 	}
        //     },
		ok() {
			if (this.$formVerify.verifyString(this.memberForm.nickname)) {
				this.$Message.error({
					content: '人员姓名不能为空！'
				});
				return;
			}
			if (this.$formVerify.verifyString(this.memberForm.roleId)) {
				this.$Message.error({
					content: '角色类型不能为空！'
				});
				return;
			}
			if (this.$formVerify.verifyString(this.memberForm.phoneNumber)) {
				this.$Message.error({
					content: '联系电话不能为空！'
				});
				return;
			}
			// if(this.$formVerify.verifyString(this.memberForm.organizationName)){
			// 	this.$Message.error({
			// 		content: '从属组织不能为空！'
			// 	});
			// 	return;
			// }

			//判断roleId是否为空，如果为空则为添加角色操作
			if (this.$formVerify.verifyString(this.memberForm.id)) {
				this.$post(this.$api.addMemberUrl, this.memberForm).then((result) => {
					if (result.successed) {
						this.$Message.success({
							content: '添加成功！'
						});
						this.modal = false;
						this.$emit('addSuccessEvent');
					}
				}).catch((error) => {
					error.message = error.message || '操作失败，请稍后再试试！';
					this.$Message.error({
						content: error.message
					});
				});
			}
			//判断roleId是否为空，如果不为空则为更改角色操作
			if (!this.$formVerify.verifyString(this.memberForm.id)) {
				this.$post(this.$api.updateMemberUrl, this.memberForm).then((result) => {
					if (result.successed) {
						this.$Message.success({
							content: '更改成功！'
						});
						this.modal = false;
						this.$emit('addSuccessEvent');
					}
				}).catch((error) => {
					error.message = error.message || '操作失败，请稍后再试试！';
					this.$Message.error({
						content: error.message
					});
				});
			}
		},
		cancel() {
			this.modal = false;
		},
	},
}
</script>
