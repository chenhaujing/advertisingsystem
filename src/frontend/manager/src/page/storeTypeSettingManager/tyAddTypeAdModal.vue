<style  lang="scss" scoped>
@import '~assets/css/base.scss';
.addTimes {
	.ivu-input-number {
		width: 100%;
	}
	.timesForm {
		margin: 0 auto;
		box-sizing: border-box;
		padding: 30px 40px;
		.addTimesGroupLeft {
			float: left;
			width: 45%;
		}
		.addTimesGroupRight {
			float: right;
			width: 45%;
		}
	} // 覆盖模态框主内容区域padding
	.ivu-modal-body {
		padding: 0;
	}
	.ivu-form-item-label {
		font-size: 16px;
	}
	.ivu-input {
		height: 34px;
	}
}
</style>
<template>
	<iModal class="addTimes baseModal" :title="modalTitle" v-model="showFlag" :mask-closable="false" :width="600">
		<div class="timesForm">
			<iForm :model="formData">
				<div class="addTimesGroupLeft">
					<iFormitem label="类别类型">
						<iSelect v-model="formData.storeType" placeholder="请选择类别类型">
							<iOption :value="typeItem.value" v-for="(typeItem,index) in typeList" :key="typeItem.value" v-text="typeItem.label"></iOption>
						</iSelect>
					</iFormitem>
				</div>
				<div class="addTimesGroupRight">
					<iFormitem label="最大广告数量">
						<iInput @on-keyup="keyupFormNumberEvent" v-model="formData.adCount" placeholder="请输入最大广告位数量"></iInput>
					</iFormitem>
				</div>

				<div class="clear"></div>
			</iForm>
			<div class="clear"></div>
		</div>
		<div class="buttonTools" slot="footer">
			<iButton class="buttonTools_finish" :loading="finishLoading" @click="finish">{{finishBtnText}}
			</iButton>
			<iButton class="buttonTools_cancel" @click="close">取消</iButton>
		</div>
	</iModal>
</template>
<script>
import ModalMixins from 'components/tyModal/baseModal';
import iModal from 'iview/src/components/modal';
import iForm from 'iview/src/components/form';
import iInput from 'iview/src/components/input';
import iInputNumber from 'iview/src/components/input-number';
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
export default {
	props: ['inputName', 'inputCompany', 'companyArray', 'placeholder', 'title'],
	mixins: [ModalMixins],
	components: {
		iInputNumber,
		iModal,
		iForm,
		'iFormitem': iForm.Item,
		iInput,
		iSelect,
		iOption
	},
	watch: {
		showFlag() {
			if (!this.showFlag) {
				this.formData = {
					storeType: 1,
					adCount: ''
				}
			}
		}
	},
	data() {
		return {
			modal: false,
			modalTitle: '配置类别广告数量',
			storeName: '',
			typeList: [{
				value: 1,
				label: 'A类'
			}, {
				value: 2,
				label: 'B类'
			}, {
				value: 3,
				label: 'C类'
			}],
			formData: {
				storeType: 1,
				adCount: ''
			}
		}
	},
	methods: {
		keyupFormNumberEvent() {
			this.formData.adCount = this.formData.adCount.replace(/[^\d]/g, '')
		},
		setParams(data) {
			this.formData.id = data.id;
			this.formData.storeType = data.storeType;
			this.formData.adCount = data.adCount;
		},
		finish() {
			if (this.$formVerify.verifyString(this.formData.storeType)) {
				this.$Message.error({
					content: '请选择类别类型'
				});
				return;
			}
			if (this.$formVerify.verifyString(this.formData.adCount)) {
				this.$Message.error({
					content: '请输入最大广告位数量！'
				});
				return;
			}
			if (this.formData.adCount <= 0) {
				this.$Message.error({
					content: '无效的广告位数量！'
				});
				return;
			}
			if (this.formData.adCount >= 9999999) {
				this.$Message.error({
					content: '广告位置最大数量为9999999'
				});
				return;
			}
			this.finishLoading = true;
			if (this.$formVerify.verifyString(this.formData.id)) {
				this.$post(this.$api.addAdTypeListUrl, this.formData).then((result) => {
					this.$emit('addAdSuccessEvent');
					this.$Message.success('添加成功');
					this.showFlag = false;
					this.resetLoading();
				}).catch((error) => {
					this.$Message.error(error.message);
					this.resetLoading();
				});
				return;
			}
			if (!this.$formVerify.verifyString(this.formData.id)) {
				this.$post(this.$api.updateAdTypeListUrl, this.formData).then((result) => {
					this.$emit('addAdSuccessEvent');
					this.$Message.success('修改成功');
					this.showFlag = false;
					this.resetLoading();
				}).catch((error) => {
					this.resetLoading();
					this.$Message.error(error.message);
				});
			}

		}
	},
}
</script>
