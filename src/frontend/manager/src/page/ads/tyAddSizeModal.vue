// <style  lang="scss" scoped>
@import '~assets/css/base.scss';
//广告配置页面模态框样式
$modelHeight: 550px;
//
.addSize {
	.addSizeForm {
		width: 325px;
		margin: 0 auto;
		box-sizing: border-box;
		padding: 30px 0 40px;
		.sizeInputGroup {
			width: 100%;
			.label {
				font-size: 16px;
				line-height: 36px;
			}
			.sizeInputBox {
				height: 34px;
				width: 100%;
				margin-bottom: 24px;
				.sizeInput {
					float: left;
					width: 45%;
					border: 1px solid #dddee1;
					height: 34px;
					box-sizing: border-box;
					padding: 0 7px;
					border-radius: 4px;
				}
				.sizeDevide {
					float: left;
					width: 10%;
					line-height: 34px;
					text-align: center;
				}
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
	} // .ivu-select-single .ivu-select-selection{
	// 	height: 34px;
	// 	.ivu-select-placeholder{
	// 		height: 100%;
	// 	}
	// }
}
</style>
<template>
	<iModal class="addSize baseModal" :title="modalTitle" v-model="showFlag" :mask-closable="false" :width="600" ref="addSizeModal">
		<div class="addSizeForm">
			<iForm :model="sizeForm">
				<iFormitem label="尺寸名称">
					<iInput v-model="sizeForm.sizeName"  @on-keyup="$format.setString(sizeForm,'sizeName')"></iInput>
				</iFormitem>
				<div class="sizeInputGroup">
					<div class="label">分辨率</div>
					<div class="sizeInputBox">
						<input type="text" class="sizeInput" v-model="sizeForm.horizontalResolution" @keyup="$format.setNumber(sizeForm,'horizontalResolution')"/>
						<div class="sizeDevide">*</div>
						<input type="text" class="sizeInput" v-model="sizeForm.verticalResolution" @keyup="$format.setNumber(sizeForm,'verticalResolution')"/>
						<div class="clear"></div>
					</div>
				</div>
				<div class="sizeInputGroup">
					<div class="label">宽高比</div>
					<div class="sizeInputBox">
						<input type="text" class="sizeInput" v-model="sizeForm.widthRatio" @keyup="$format.setNumber(sizeForm,'widthRatio')"/>
						<div class="sizeDevide">:</div>
						<input type="text" class="sizeInput" v-model="sizeForm.highRatio" @keyup="$format.setNumber(sizeForm,'highRatio')"/>
						<div class="clear"></div>
					</div>
				</div>
			</iForm>
			<div class="clear"></div>
		</div>
		<div class="buttonTools" slot="footer">
			<iButton class="buttonTools_finish" :loading="finishLoading" @click="finish">{{finishBtnText}}</iButton>
			<iButton class="buttonTools_cancel" @click="close">取消</iButton>
		</div>
	</iModal>
</template>
<script>
import iModal from 'iview/src/components/modal';
import iForm from 'iview/src/components/form';
import iInput from 'iview/src/components/input';
import ModalMixins from 'components/tyModal/baseModal';

export default {
	components: {
		iModal,
		iForm,
		'iFormitem': iForm.Item,
		iInput,
	},
	data() {

		return {
			modal: false,
			modalTitle: '添加广告尺寸',
			sizeForm: {
				"highRatio": "",
				"horizontalResolution": "",
				"sizeName": "",
				"verticalResolution": "",
				"widthRatio": "",
			},
			url: this.$api.addSizeConfigUrl
		}
	},
	watch: {
		showFlag(val) {
			if (val) {
				if (this.formData) {
					Object.assign(this.sizeForm, this.formData);
					this.url=this.$api.updateSizeConfigUrl
				}
			} else {
				this.clearData(this.sizeForm);
				this.formData=null;
				this.url=this.$api.addSizeConfigUrl
			}
		}
	},
	mixins: [ModalMixins],
	methods: {
		finish() {
			// if (this.$formVerify.verifyString(this.sizeForm.sizeName)) {
			// 	this.$Notice.error({
			// 		title: '错误',
			// 		desc: '请输入有效的尺寸名称'
			// 	});
			// 	return;
			// }
			// if (this.$formVerify.verifyString(this.sizeForm.sizeResolving1)) {
			// 	this.$Notice.error({
			// 		title: '错误',
			// 		content: '请输入有效的分辨率'
			// 	});
			// 	return;
			// }
			// if (this.$formVerify.verifyString(this.sizeForm.sizeResolving2)) {
			// 	this.$Notice.error({
			// 		title: '错误',
			// 		content: '请输入有效的分辨率'
			// 	});
			// 	return;
			// }
			// if (this.$formVerify.verifyString(this.sizeForm.width)) {
			// 	this.$Notice.error({
			// 		title: '错误',
			// 		content: '请输入有效的宽高比'
			// 	});
			// 	return;
			// }
			// if (this.$formVerify.verifyString(this.sizeForm.height)) {
			// 	this.$Notice.error({
			// 		title: '错误',
			// 		content: '请输入有效的宽高比'
			// 	});
			// 	return;
			// }

			this.$post(this.url, this.sizeForm).then((result) => {
				this.$Notice.success({
					title: '保存成功',
				});
				this.$emit('successEvent','addSizeTable')
				this.toggle();
			}).catch((error) => {
				this.$Notice.error({
					title: '错误',
					desc: error.message || "添加广告尺寸失败"
				});
			})
		}
	},
}
</script>
