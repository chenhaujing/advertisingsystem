<style lang="scss" scoped>
 .PriceTypeForm{
	 box-sizing: border-box;
	 padding: 60px 70px;
 }
</style>
<template>
	<iModal  class="baseModal" :title="modalTitle" v-model="showFlag" :mask-closable="false" :width="850">
		<div class="PriceTypeForm">
			<iForm :model="PriceTypeForm">
				<iRow :gutter="30">
					<iCol :span="8" class="addPriceTypeGroupLeft">
						<iFormitem label="门店类别" class="groupLeft">
							<iSelect v-model="PriceTypeForm.storeType" placeholder="请选择门店类别">
								<iOption :value="storeItem.value" v-for="(storeItem,index) in shoreTypeForm" :key="storeItem.value">{{ storeItem.label }}</iOption>
							</iSelect>
						</iFormitem>
					</iCol>
					<iCol :span="8" class="addPriceTypeGroupCenter">
						<iFormitem label="时间周期" class="groupCenter">
							<iSelect v-model="PriceTypeForm.period" placeholder="请选择时间周期">
								<iOption :value="circleList.value" v-for="(circleList,index) in circleArray" :key="circleList.value">{{ circleList.label }}</iOption>
							</iSelect>
						</iFormitem>
					</iCol>
					<iCol :span="8" class="addPriceTypeGroupRight">
						<iFormitem label="价格" class="groupRight">
							<iInput v-model="PriceTypeForm.price" @on-keyup="$format.setPriceNumber(PriceTypeForm,'price')"></iInput>
						</iFormitem>
					</iCol>
				</iRow>
			</iForm>
			<div class="clear"></div>
		</div>
		<div class="buttonTools" slot="footer">
			<iButton class="buttonTools_finish"
			:loading="finishLoading"
			@click="finish">{{finishBtnText}}</iButton>
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
export default {
	components: {
		iRow,
		iCol,
		iModal,
		iForm,
		'iFormitem': iForm.Item,
		iInput,
		iSelect,
		iOption
	},
	mixins: [ModalMixins],
	mounted() {
		for (let i = 1; i < 13; i++) {
			this.circleArray.push({
				value: i,
				label: i + '个月'
			})
		}
	},
	data() {
		return {
			// 门店类别配置
			shoreTypeForm: [{
				value: 1,
				label: 'A类'
			}, {
				value: 2,
				label: 'B类'
			}, {
				value: 3,
				label: 'C类'
			}],
			// 时间周期
			circleArray: [],
			modal: false,
			defaultForm:{
	            storeType: 1,
				period: 1,
				price: ''
			},
			url:this.$api.addAdsPriceConfigUrl,
			PriceTypeForm: {
				storeType: 1,
				period: 1,
				price: ''
			},
		}
	},
	watch: {
		showFlag(val) {
			if (val) {
				if (this.formData) {
					Object.assign(this.PriceTypeForm, this.formData);
					this.url = this.$api.updateAdsPriceConfigUrl
			} else {
				this.clearData(this.PriceTypeForm,this.defaultForm);
				this.formData = null;
				this.url = this.$api.addAdsPriceConfigUrl
			}
		}}
	},
	methods: {
		keyupFormNumberEvent() {
			// 包含小数点
			this.PriceTypeForm.price = this.PriceTypeForm.price.replace(/[^\d,.]/g, '')
		},
		finish() {
			if (this.$formVerify.verifyString(this.PriceTypeForm.storeType)) {
				this.$Notice.error({
					title: '错误',
					desc: '请选择门店类别'
				});
				return;
			}
			if (this.$formVerify.verifyString(this.PriceTypeForm.period)) {
				this.$Notice.error({
					title: '错误',
					desc: '请选择时间周期'
				})
				return;
			}
			if (this.$formVerify.verifyString(this.PriceTypeForm.price)) {
				this.$Notice.error({
					title: '错误',
					desc: '请输入有效的价格!'
				})
				return;
			}

			var re = /^[0-9]+.?[0-9]{0,2}$/;
			if (!re.test(this.PriceTypeForm.price)) {

				this.$Notice.error({
					title: '错误',
					desc: '无效的价格信息，请重新输入（只保留小数点后两位数)'
				})
				return;
			}
			this.finishLoading = true;
				this.$post(this.url, this.PriceTypeForm).then((result) => {
					this.$Notice.success({
						desc: '保存成功'
					})
					this.$emit('successEvent',"addPriceTypeTable");
					this.resetLoading();
					this.toggle();
				}).catch((e) => {
					this.$Notice.error({
						title: '错误',
						desc: e.message
					})
					this.resetLoading();
				})
		}
	},
}
</script>
