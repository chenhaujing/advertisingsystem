<style scoped lang="scss">
/*提交审核页面灰色区域*/

.submitAuditBox {
	width: 100%;
	padding: 30px 95px;
	/*提交审核页面白色区域*/
	.submitAudit {
		width: 100%;
		background-color: #fff;
		box-sizing: border-box;
		padding: 60px 125px;
		/*合同标题*/
		.auditTitle {
			font-size: 24px;
			color: #333;
			text-align: center;
			border-bottom: 1px solid #999;
			padding-bottom: 10px;
		}
		/*合同编号*/
		.auditNum {
			font-size: 14px;
			color: #666;
			text-align: center;
			margin-top: 10px;
			margin-bottom: 30px;
		}
		/*合同成功提示*/
		.auditTip {
			font-size: 18px;
			color: #7edd9c;
			text-align: center;
		}
		.auditNextTip {
			padding-top: 20px;
			text-align: center;
			font-size: 16px;
			color: #666666;
		}
		.buttonTools {
			margin-top: 30px;
		}
		/*确定按钮样式*/
		.sureBtn {
			background-color: #4cabe0;
			color: #fff;
			width: 160px;
			height: 40px;
			font-size: 16px;
			border: 0;
			border-radius: 6px;
			display: block;
			cursor: pointer;
			outline: none;
		}
		.sureBtn:active {
			color: #4cabe0;
			background-color: #fff;
			border: 1px solid #4cabe0;
		}
	}
	.buttonTools {
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.buttonItem {
		width: 160px;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 34px;
		line-height: 34px;
		text-align: center;
		color: #ffffff;
		font-size: 16px;
		border-radius: 6px;
	}
	.buttonIcon {
		margin-right: 5px;
		width: 16px;
		height: 16px;
		background-size: 100% 100%;
	}
	.againIcon {
		background-image: url(~assets/img/contract/contractAgainEdit.png);
	}
	.auditIcon {
		background-image: url(~assets/img/contract/contractAudit.png);
	}
	.msgIcon {
		background-image: url(~assets/img/contract/contractMsg.png);
	}
	.againEditButton {
		background-color: #4cabe0;
	}
	.auditButton {
		margin-left: 40px;
		background-color: #f0857d;
	}
	.msgButton {
		margin-left: 40px;
		background-color: #fcb322;
	}
}
</style>
<template>
	<div class="submitAuditBox">
		<div class="submitAudit" v-show="!loading">
			<div class="auditTitle" v-text="contractData.contractName"></div>
			<div class="auditNum">[合同编号：{{contractData.contractCode}}]</div>
			<div class="auditTip">
				{{ type == 'save'? '已保存': '已成功提交审核'}}
			</div>
			<div class="auditNextTip" v-if="type == 'save'">是否继续操作？</div>
			<div class="buttonTools" v-if="type == 'submit'">
				<a class="buttonItem againEditButton" @click="$router.push({path: '/contract'})">
					确定
				</a>
			</div>
			<div class="buttonTools" v-if="type == 'save'">

				<a class="buttonItem againEditButton" @click="edit">
					<div class="buttonIcon againIcon"></div>
					<div class="agaginButtonText">再次编辑</div>
				</a>
				<a class="buttonItem auditButton" @click="submitAudit">
					<div class="buttonIcon auditIcon"></div>
					<div class="agaginButtonText">提交审核</div>
				</a>
				<a class="buttonItem msgButton" @click="$router.push({path: '/contract'})">
					<div class="buttonIcon msgIcon"></div>
					<div class="agaginButtonText">稍后再说</div>
				</a>
			</div>

			<!-- <div class="auditTitle" v-text="contractData.contractName"></div>
											<div class="auditNum">[合同编号：{{contractData.contractCode}}]</div>
											<div class="autitTips" v-text="title"></div>
											<button class="sureBtn">确定</button> -->
		</div>
		<iSpin size="large" fix v-show="loading" class="mainContent"></iSpin>
	</div>
</template>
<script>
import ContractState from './contractState';
import iSpin from 'iview/src/components/spin';
import iButton from 'iview/src/components/button';
export default {
	mounted() {
		this.loading = false;
		this.id = this.$route.query.id;
		this.type = this.$route.query.type;
		console.log('合同状态页');
		console.log(this.$route);
		if (this.id == null || this.id == undefined) {
			this.$Notice.error({
				title: '错误',
				desc: '无效的合同信息'
			})
			return;
		}
		this.$get(this.$api.getContractInfo, {
			id: this.id
		}).then((result) => {
			this.loading = false;

			this.contractData = result.data;
		}).catch((e) => {
			this.loading = false;
			this.$Notice.error({
				title: '错误',
				desc: e.message
			})
		})
	},
	data() {
		return {
			constractStatus: ContractState.Status,
			id: null,
			loading: true,
			contractData: {},
			type: 'save'
		}
	},
	components: {
		iButton,
		iSpin
	},
	methods: {
		edit() {
			this.$router.push({
				name: 'editContract',
				query: {
					contractId: this.id
				}
			})
		},
		submitAudit() {
			this.$Modal.confirm({
				title: '提示',
				loading: true,
				content: '<p>确定将该合同提交审核吗？</p>',
				onOk: () => {
					this.$post(this.$api.optionContranctUrl, {
						"contractId": this.id,
						"operation": ContractState.OptionStatus.submit,
						"successed": true,
					}).then(() => {
						this.$Notice.success({
							title: '提示',
							desc: '已提交审核'
						});
						this.type = 'submit'
						this.$Modal.remove();
					}).catch((e) => {
						this.$Notice.error({
							title: '错误',
							desc: e.message
						});
						this.$Modal.remove();
					})
				},
				onCancel: (e) => {

				}
			});
		},
	}
}

</script>
