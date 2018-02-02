<style lang="scss" scoped>
.addRoleForm {
	margin: 0 auto;
	padding: 45px 70px;
	.roleForm {
		width: 100%;
		.roleBox-left {
			width: 46%;
			float: left;
		}
		.roleBox-right {
			width: 46%;
			float: right;
		}
	}
	button {
		width: 120px;
		height: 34px;
		border: 0;
		outline: none;
		border-radius: 3px;
	}
	.saveBtn {
		float: right;
		margin-right: 30px;
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
} 
</style>
<template>
	<iModal class="addRole baseModal" :title="modalTitle" v-model="showFlag" :mask-closable="false" :width="850">
		<div class="addRoleForm">
			<iForm :model="roleForm" class="roleForm">
				<div class="roleBox-left">
					<iFormitem label="角色名称">
						<iInput v-model="roleForm.roleName" placeholder="请输入角色名称"></iInput>
					</iFormitem>
				</div>
				<div class="roleBox-right">
					<iFormitem label="角色类型">
						<iSelect v-model="roleForm.roleType" placeholder="请选择角色">
							<iOption v-for="role in roleList" :value="role.value" :key="role.value">{{role.label}}</iOption>
						</iSelect>
					</iFormitem>
				</div>
				<div class="clear"></div>
				<iFormitem label="角色备注">
					<iInput v-model="roleForm.description" type="textarea" :autosize="{minRows: 4,maxRows: 8}" placeholder="请输入角色备注">
					</iInput>
				</iFormitem>
			</iForm>
			<!-- <div class="buttonTool">
					<button class="cancelBtn" @click="cancel">取消</button>
					<button class="saveBtn" @click="ok" v-text="$formVerify.verifyString(roleForm.id) ? '添加' : '保存'"></button>
					<div class="clear"></div>
				</div> -->
		</div>
		<div class="buttonTools" slot="footer">
			<iButton class="buttonTools_finish" :loading="finishLoading" @click="finish">{{finishBtnText}}</iButton>
			<iButton class="buttonTools_cancel" @click="close">取消</iButton>
		</div>
	</iModal>
</template>
<script>
import ModalMixins from 'components/tyModal/baseModal';
import iModal from 'iview/src/components/modal';
import iForm from 'iview/src/components/form';
import iInput from 'iview/src/components/input';
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
export default {
	components: {
		iModal,
		iForm,
		'iFormitem': iForm.Item,
		iInput,
		iSelect,
		iOption
	},
	mixins: [ModalMixins],
	data() {
		return {
			modal: false,
			modalTitle: '添加角色',
			roleTypeName: '',
			roleList: [{
				value: 2,
				label: '管理人员'
			}, {
				value: 3,
				label: '业务员'
			}],
			roleForm: {
				id: '',
				roleName: '',
				roleType: 3,
				description: '',
			}
		}
	},
	watch: {
		modal() {
			if (!this.modal) {
				this.roleForm.roleType = 3;
				this.roleForm.id = '';
				this.roleForm.roleName = '';
				this.roleForm.description = '';
			} else {
				this.updateTitle();
			}
		}
	},
	methods: {
		updateTitle() {
			if (this.$formVerify.verifyString(this.roleForm.id)) {
				this.modalTitle = '添加角色'
			} else {
				this.modalTitle = '编辑角色'
			}
		},
		finish() {
			if (this.$formVerify.verifyString(this.roleForm.roleName)) {
				this.$Message.error({
					content: '请输入有效的角色名称'
				});
				return;
			}
			if (this.roleForm.roleType == null) {
				this.$Message.error({
					content: '请选择角色类型'
				});
				return;
			}
			this.finishLoading = true;
			//判断roleId是否为空，如果为空则为添加角色操作
			if (this.$formVerify.verifyString(this.roleForm.id)) {
				this.$post(this.$api.addRoleUrl, this.roleForm).then((result) => {
					this.$Message.success({
						content: '添加成功！'
					});
					this.$emit('addSuccessEvent');
					this.resetLoading();
					this.toggle();
				}).catch((error) => {
					error.message = error.message || '操作失败，请稍后再试试！';
					this.$Message.error({
						content: error.message
					});
				});
			}
			//判断roleId是否为空，如果不为空则为更改角色操作
			if (!this.$formVerify.verifyString(this.roleForm.id)) {
				this.$post(this.$api.updateRoleUrl, this.roleForm).then((result) => {
					this.$Message.success({
						content: '更改成功！'
					});
					this.$emit('addSuccessEvent');
					this.resetLoading();
					this.toggle();
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
