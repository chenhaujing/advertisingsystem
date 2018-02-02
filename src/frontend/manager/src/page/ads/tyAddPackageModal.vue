<style lang="scss" scoped>
.PriceTypeForm {
	box-sizing: border-box;
	padding: 60px 70px;
}

.row {
	margin-bottom: 40px;
}
</style>
<template>
	<iModal class="baseModal" :title="modalTitle" v-model="showFlag" :mask-closable="false" :width="850">
		<div class="PriceTypeForm">
			<iForm :model="packageForm">
				<iRow :gutter="30" class="row">
					<iCol :span="8" class="addPriceTypeGroupLeft">
						<iFormitem label="套餐名称" class="groupLeft">
							<iInput v-model="packageForm.packageName" placeholder="请输入套餐名称" @on-keyup="$format.setString(packageForm,'packageName')"></iInput>
						</iFormitem>
					</iCol>
					<iCol :span="8" class="addPriceTypeGroupRight">
						<iFormitem label="套餐总价" class="groupRight">
							<iInput v-model="packageForm.totalCost" @on-keyup="$format.setPriceNumber(packageForm,'totalCost')" placeholder="请输入套餐总价"></iInput>
							<!-- <tyNumberInput :type="'number'" :value="packageForm.totalCost"></tyNumberInput> -->
						</iFormitem>
					</iCol>
					<iCol :span="8" class="addPriceTypeGroupCenter">
						<iFormitem label="套餐周期" class="groupCenter">
							<iSelect v-model="packageForm.period" placeholder="请选择套餐周期">
								<iOption :value="list.value" v-for="(list,index) in periodList" :key="list.value">{{ list.label }}</iOption>
							</iSelect>
						</iFormitem>
					</iCol>

				</iRow>
				<iRow :gutter="30" class="row">
					<iCol :span="8" class="addPriceTypeGroupLeft">
						<iFormitem label="A类屏位数量" class="groupLeft">
							<iInput v-model="packageForm.amontOfTypeA" @on-keyup="$format.setNumber(packageForm,'amontOfTypeA')" placeholder="请输入A类屏位数量"></iInput>
						</iFormitem>
					</iCol>
					<iCol :span="8" class="addPriceTypeGroupLeft">
						<iFormitem label="B类屏位数量" class="groupLeft">
							<iInput v-model="packageForm.amontOfTypeB" @on-keyup="$format.setNumber(packageForm,'amontOfTypeB')" placeholder="请输入B类屏位数量"></iInput>
						</iFormitem>
					</iCol>
					<iCol :span="8" class="addPriceTypeGroupLeft">
						<iFormitem label="C类屏位数量" class="groupLeft" >
							<iInput v-model="packageForm.amontOfTypeC" @on-keyup="$format.setNumber(packageForm,'amontOfTypeC')" placeholder="请输入C类屏位数量"></iInput>
						</iFormitem>
					</iCol>
				</iRow>
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

import ModalMixins from 'components/tyModal/baseModal';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import iModal from 'iview/src/components/modal';
import iForm from 'iview/src/components/form';
import iInput from 'iview/src/components/input';
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
import tyNumberInput from '../../components/tyNuminput/index.vue';
import util from '../../store/index';

export default {
	components: {
		iRow,
		iCol,
		iModal,
		iForm,
		'iFormitem': iForm.Item,
		iInput,
		iSelect,
		iOption,
		tyNumberInput
	},
	data() {
		return {
			defaultForm: {
				amontOfTypeA: '',
				amontOfTypeB: '',
				amontOfTypeC: '',
				packageName: '',
				period: 0,
				totalCost: '',
			},
			packageForm: {
				amontOfTypeA: '',
				amontOfTypeB: '',
				amontOfTypeC: '',
				packageName: '',
				period: 0,
				totalCost: '',
			},
			periodList: [
				{ label: "请选择套餐周期", value: 0 },
				{ label: "1个月", value: 1 },
				{ label: "2个月", value: 2 },
				{ label: "3个月", value: 3 },
				{ label: "6个月", value: 6 },
				{ label: "12个月", value: 12 },
			],
			url: this.$api.addPackageConfig
		}
	},
	mixins: [ModalMixins,util],
	watch: {
		showFlag(val) {
			if (val) {
				if (this.formData) {
					Object.assign(this.packageForm, this.formData);
					this.url = this.$api.updatePackageConfig
				}
			} else {
				this.clearData(this.packageForm, this.defaultForm);
				this.formData = null;
				this.url = this.$api.addPackageConfig
			}
		}
	},
	methods:{
		finish(){
			if (this.$formVerify.verifyString(this.packageForm.packageName)) {
				this.$Notice.error({
					title: '错误',
					desc: '请输入套餐名称'
				});
				return;
			}
			if (this.$formVerify.verifyString(this.packageForm.totalCost)) {
				this.$Notice.error({
					title: '错误',
					desc: '请输入套餐总价'
				});
				return;
			}
			if(Number(this.packageForm.totalCost)===0){
				this.$Notice.error({
					title: '错误',
					desc: '请输入有效的广告套餐总价'
				});
				return;
			}
			if (this.packageForm.period==0) {
				this.$Notice.error({
					title: '错误',
					desc: '请选择套餐有效周期'
				});
				return;
			}
			if (this.$formVerify.verifyString(this.packageForm.amontOfTypeA)&&this.$formVerify.verifyString(this.packageForm.amontOfTypeB)&&this.$formVerify.verifyString(this.packageForm.amontOfTypeC)) {
				this.$Notice.error({
					title: '错误',
					desc: '必须配置有效的屏位数量'
				});
				return;
			}
			//转化为0
            if(this.packageForm.amontOfTypeA===''){
				this.packageForm.amontOfTypeA=0
			}
			if(this.packageForm.amontOfTypeB===''){
				this.packageForm.amontOfTypeB=0
			}
			 if(this.packageForm.amontOfTypeC===''){
				this.packageForm.amontOfTypeC=0
			}
				this.$post(this.url, this.packageForm).then((result) => {
				this.$Notice.success({
					title: '保存成功',
				});
				this.$emit('successEvent','addPackageTable')
				this.toggle();
			}).catch((error) => {
				this.$Notice.error({
					title: '错误',
					desc: error.message || "添加广告套餐失败"
				});
			})
		}
	}
}

</script>