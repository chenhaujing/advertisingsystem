<style lang="scss" scoped>
.contractReceivablesSetting {
	width: 100%;
	box-sizing: border-box;
	padding: 20px;
	.crSettingBox {
		width: 100%;
		background-color: #fff;
		height: 400px;
		box-sizing: border-box;
		padding: 20px;
		.crSetting-title {
			width: 100%;
			margin-bottom: 40px;
			font-size: 18px;
			color: #666;
		}
		.crSetting-model {
			width: 33.33%;
			float: left;
			font-size: 16px;
		}
		.name {
			width: 100%;
			color: #999;
			margin-bottom: 20px;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
		}
		.name-info {
			width: 80%;
			color: #333;
			height: 34px;
			border: 1px solid #dddee1;
			border-radius: 4px;
			box-sizing: border-box;
			padding: 0 7px;
		}
		.editBox {
			width: 33.33%;
			float: right;
			margin-top: 80px;
			margin-right: 80px;
		}
		button {
			width: 120px;
			height: 34px;
			border: 0;
			outline: none;
			border-radius: 3px;
			cursor: pointer;
		}
		.editBtn {
			float: right;
			color: #fff;
			background-color: #4cabe0;
		}
		.saveBox {
			float: right;
			margin-top: 80px;
			margin-right: 80px;
		}
		.saveBtn {
			color: #fff;
			background-color: #4cabe0;
			margin-right: 30px;
		}
		.saveBtn:active,
		.editBtn:active {
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
}
</style>
<template>
	<div class="contractReceivablesSetting">
		<div class="crSettingBox">
			<div class="crSetting-title">乙方收款信息配置</div>
			<div class="crSetting-model">
				<div class="name">乙方户名</div>
				<input type="text" class="name-info" v-model="userInfo.name" :disabled="!edit" />
			</div>
			<div class="crSetting-model">
				<div class="name">乙方开户行</div>
				<input type="text" class="name-info" v-model="userInfo.bank" :disabled="!edit" />
			</div>
			<div class="crSetting-model">
				<div class="name">乙方开户账号</div>
				<input type="text" class="name-info" v-model="userInfo.bankAccount" :disabled="!edit" />
			</div>
			<div class="clear"></div>
			<div class="editBox" v-if="!edit">
				<button class="editBtn" @click="toggleStatusByEdit(true)">编辑</button>
				<div class="clear"></div>
			</div>
			<div class="saveBox" v-if="edit">
				<button class="saveBtn" @click="toggleStatusByEdit('save')">保存</button>
				<button class="cancelBtn" @click="cancel()">取消</button>
			</div>
		</div>
	</div>
</template>
<script>
export default {
	created() {
		this.$post(this.$api.getContractReceivablesInfoUrl).then((result) => {
			if (!result.data) {
				return
			}
			this.userInfo.name = result.data.name;
			this.userInfo.bank = result.data.bank;
			this.userInfo.bankAccount = result.data.bankAccount;
			this.defaultInfo = result.data;
		}).catch((error) => {
			this.$Message.error({
				content: error.message || "获取合同信息失败"
			})
		});
	},
	data() {
		return {
			//默认编辑为false
			edit: false,
			userInfo: {
				//开户名
				name: '',
				//开户银行
				bank: '',
				//开户账号
				bankAccount: ''
			},
			defaultInfo: {
				//开户名
				name: '',
				//开户银行
				bank: '',
				//开户账号
				bankAccount: ''
			}
		}
	},
	methods: {
		cancel() {
			this.edit = false;
			this.userInfo = {
				//开户名
				name: this.defaultInfo.name,
				//开户银行
				bank: this.defaultInfo.bank,
				//开户账号
				bankAccount: this.defaultInfo.bankAccount,
			}
		},
		//控制收款信息配置开关
		toggleStatusByEdit(saveControl) {
			this.edit = true;
			if (saveControl == 'save') {
				if (this.$formVerify.verifyString(this.userInfo.name)) {
					this.$Message.error('开户名不能为空！');
					return;
				}
				if (this.$formVerify.verifyString(this.userInfo.bank)) {
					this.$Message.error('开户银行不能为空！');
					return;
				}
				if (this.$formVerify.verifyString(this.userInfo.bankAccount)) {
					this.$Message.error('开户账号不能为空！');
					return;
				}
				this.$post(this.$api.updateContractReceivablesInfoUrl, this.userInfo).then((result) => {
					this.$Message.success('信息保存成功！');
					// this.defaultInfo = this.userInfo;
					this.edit = false;
				}).catch((error) => {
					error.message = error.message || '信息保存失败！'
					this.$Message.error(error.message);
				});
			}
		}
	},
}
</script>
