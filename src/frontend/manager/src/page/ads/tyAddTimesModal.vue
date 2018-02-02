<style  lang="scss" scoped>
@import '~assets/css/base.scss';
//广告配置页面模态框样式
$modelHeight: 550px;
//
.addTimes {
	.timesForm {
		width: 380px;
		margin: 0 auto;
		box-sizing: border-box;
		padding: 30px 0 40px;
		.addTimesGroupLeft {
			float: left;
			width: 45%;
		}
		.addTimesGroupRight {
			float: right;
			width: 45%;
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
	.ivu-tree-title {
		font-size: 16px;
		color: #666666;
	}
	.mainContent {
		height: $modelHeight;
	}
	.table-tool {
		height: 38px;
	}
}
</style>
<template>
	<iModal class="addTimes baseModal" :title="modalTitle" v-model="showFlag" :mask-closable="false" :width="600">
		<div class="timesForm">
			<iForm :model="timesForm">
				<div class="addTimesGroupLeft">
					<iFormitem label="展示次数">
						<iInput v-model="timesForm.displayTimes" @on-keyup="$format.setNumber(timesForm,'displayTimes')"></iInput>
					</iFormitem>
				</div>
				<div class="addTimesGroupRight">
					<iFormitem label="时间周期">
						<iSelect v-model="timesForm.timeUnit" :placeholder="placeholder">
							<iOption v-for="(list,index) in timeList" :value="list.value" :key="list.value">{{ list.label }}</iOption>
						</iSelect>
					</iFormitem>
				</div>
				<div class="clear"></div>
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
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
import ModalMixins from 'components/tyModal/baseModal';

export default {
	props: ['inputName', 'inputCompany', 'companyArray', 'placeholder', 'title'],
	components: {
		iModal,
		iForm,
		'iFormitem': iForm.Item,
		iInput,
		iSelect,
		iOption
	},
	data() {
		return {
			modal: false,
			modalTitle: this.title,
			timesForm: {
				displayTimes: '',
				timeUnit: 1
			},
			defaultForm: {
				displayTimes: '',
				timeUnit: 1
			},
			url:this.$api.addDisplayTimesConfigUrl,
			timeList: [
				{ label: "每天", value: 1 },
			]
		}
	},
	watch: {
		showFlag(val) {
			if (val) {
				if (this.formData) {
					Object.assign(this.timesForm, this.formData);
					this.url = this.$api.updateDisplayTimesConfigUrl
				}
			} else {
				this.clearData(this.timesForm,this.defaultForm);
				this.formData = null;
				this.url = this.$api.addDisplayTimesConfigUrl
			}
		}
	},
	mixins: [ModalMixins],
	methods: {
		finish() {
			if (this.$formVerify.verifyString(this.timesForm.displayTimes)) {
				this.$Notice.error({
					title: "错误",
					desc: "请输入有效广告展示次数"
				});
				return;
			}
			this.$post(this.url,this.timesForm).then(result=>{
				this.$Notice.success({
				desc: '保存成功'
				})
				this.toggle();
				this.$emit('successEvent','addDisplaytimeTable')
			}).catch(error=>{
                    this.$Notice.error({
					title:'错误',
					desc: error.message||'保存失败'
				});
			})
		},
		cancel() {
			this.modal = false;
		}
	},
}
</script>
