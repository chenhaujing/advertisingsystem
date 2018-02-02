<style lang="scss" scoped>
@import '~assets/css/base.scss';
//门店类别配置页面模态框样式
$modelHeight: 550px;
.addTypeStandard {
	.addTypeStandardForm {
		width: 710px;
		margin: 0 auto;
		box-sizing: border-box;
		padding: 40px 0 40px;
		.typeForm {
			width: 100%;
			.typeBox-left {
				width: 46%;
				float: left;
			}
			.typeBox-right {
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
				.addOrganzation {
					float: left;
					font-size: 14px;
				}
				.icon {
					float: right;
				}
			}
		}
	}
}
</style>
<template>
	<iModal class="addTypeStandard baseModal" :title="modalTitle" v-model="showFlag" :width="850" @on-ok="ok">
		<div class="addTypeStandardForm">
			<iForm :model="typeForm" class="typeForm">
				<div class="typeBox-left">
					<iFormitem label="类别标准名称">
						<iInput :maxlength="20" v-model="typeForm.storeCategoryStandardName" placeholder="请输入类别标准名称"></iInput>
					</iFormitem>
				</div>
				<div class="typeBox-right">
					<iFormitem label="类别类型">
						<iSelect v-model="typeForm.storeType" placeholder="请选择类别类型">
							<iOption :value="storeTypeItem.value" v-for="storeTypeItem in storeTypeData" :key="storeTypeItem.value" v-text="storeTypeItem.label"></iOption>
						</iSelect>
					</iFormitem>
				</div>
				<div class="clear"></div>
				<div class="typeBox-left">
					<iFormitem label="商品数量">
						<iSelect v-model="typeForm.goodsTotal">
							<iOption :value="goodsNumItem.value" v-for="goodsNumItem in goodsNum" :key="goodsNumItem.value" v-text="goodsNumItem.label"></iOption>
						</iSelect>
					</iFormitem>
				</div>
				<div class="typeBox-right">
					<iFormitem label="平均每日交易订单数量">
						<iSelect v-model="typeForm.tradingDay">
							<iOption :value="tradingDayItem.value" v-for="tradingDayItem in tradingDayList" :key="tradingDayItem.value" v-text="tradingDayItem.label"></iOption>
						</iSelect>
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
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';

export default {
	components: {
		iModal,
		iForm,
		'iFormitem': iForm.Item,
		iInput,
		iSelect,
		iOption,
	},
	mixins: [ModalMixins],
	data() {
		return {
			storeTypeData: [{
				value: 1,
				label: 'A类'
			}, {
				value: 2,
				label: 'B类'
			}, {
				value: 3,
				label: 'C类'
			}],
			goodsNum: [{
				value: '0-500',
				label: '0-500'
			}, {
				value: '501-700',
				label: '501-700'
			}, {
				value: '701',
				label: '701以上'
			}],
			tradingDayList: [{
				value: '0-50',
				label: '0-50'
			}, {
				value: '51-80',
				label: '51-80'
			}, {
				value: '81',
				label: '81以上'
			}],
			modal: false,
			modalTitle: '添加类别标准',
			type: '',
			commodityAmount: '',
			avgDailyTradingAmount: '',
			typeForm: {
				tradingDay: '0-50',
				goodsTotal: '0-500',
				id: '',
				storeType: 1,
				avgDailyTradingAmountMax: 0,
				avgDailyTradingAmountMin: 0,
				commodityAmountMax: 0,
				commodityAmountMin: 0,
				storeCategoryStandardName: '',
			}
		}
	},
	watch: {
		showFlag() {
			if (!this.showFlag) {
				this.typeForm = {
					tradingDay: '0-50',
					goodsTotal: '0-500',
					id: '',
					storeType: 1,
					avgDailyTradingAmountMax: 0,
					avgDailyTradingAmountMin: 0,
					commodityAmountMax: 0,
					commodityAmountMin: 0,
					storeCategoryStandardName: '',
				}
			}
		}
	},
	methods: {
		resetDefault() {
			this.typeForm = {
				tradingDay: '0-50',
				goodsTotal: '0-500',
				id: '',
				storeType: 1,
				avgDailyTradingAmountMax: 0,
				avgDailyTradingAmountMin: 0,
				commodityAmountMax: 0,
				commodityAmountMin: 0,
				storeCategoryStandardName: '',
			}
		},
		setParams(id, type, tradingDay, goodsTotal, name) {
			this.typeForm.id = id;
			this.typeForm.storeType = type;
			this.typeForm.storeCategoryStandardName = name;
			if (tradingDay.indexOf('999999') != -1) {
				// 最大值
				this.typeForm.tradingDay = this.tradingDayList[this.tradingDayList.length - 1].value;
			} else {
				this.typeForm.tradingDay = tradingDay;
			}
			if (goodsTotal.indexOf('999999') != -1) {
				this.typeForm.goodsTotal = this.goodsNum[this.goodsNum.length - 1].value;
			} else {
				this.typeForm.goodsTotal = goodsTotal;
			}

		},
		selectOrganzation() {
			this.modal = false;
			this.$refs.SelectOrganzationStructureModal.modal = true;
		},
		// 提交表单之前，对数据进行处理，主要是分出区间的最大值和最小值
		// 这个方法调用时，数据都是校验通过的了
		handlerDataBefore() {
			var trading = this.typeForm.tradingDay.split('-');
			if (trading.length == 2) {
				this.typeForm.avgDailyTradingAmountMin = trading[0];
				this.typeForm.avgDailyTradingAmountMax = trading[1];
			} else {
				this.typeForm.avgDailyTradingAmountMin = trading[0];
				this.typeForm.avgDailyTradingAmountMax = 999999;
			}
			var goodsTotal = this.typeForm.goodsTotal.split('-');
			if (goodsTotal.length == 2) {
				this.typeForm.commodityAmountMin = goodsTotal[0];
				this.typeForm.commodityAmountMax = goodsTotal[1];
			} else {
				this.typeForm.commodityAmountMin = goodsTotal[0];
				this.typeForm.commodityAmountMax = 999999;
			}

		},
		finish() {
			if (this.$formVerify.verifyString(this.typeForm.storeCategoryStandardName)) {
				this.$Message.error({
					content: '类别标准名称不能为空！'
				});
				return;
			}
			if (this.$formVerify.verifyString(this.typeForm.storeType)) {
				this.$Message.error({
					content: '请选择类别类型'
				});
				return;
			}
			if (this.$formVerify.verifyString(this.typeForm.goodsTotal)) {
				this.$Message.error({
					content: '请选择商品数量'
				});
				return;
			}
			if (this.$formVerify.verifyString(this.typeForm.tradingDay)) {
				this.$Message.error({
					content: '请选择平均每日订单数量！'
				});
				return;
			}
			this.handlerDataBefore();
			//判断roleId是否为空，如果为空则为添加角色操作
			this.finishLoading = true;
			this.typeForm.storeCategoryStandardName = this.typeForm.storeCategoryStandardName.trim();
			if (this.$formVerify.verifyString(this.typeForm.id)) {
				this.$post(this.$api.addStoreTypeListUrl, this.typeForm).then((result) => {
					this.$Message.success({
						content: '添加成功！'
					});
					this.resetDefault();
					this.resetLoading();
					this.$emit('addSuccessEvent');
					this.showFlag = false;
				}).catch((error) => {
					this.resetLoading();
					error.message = error.message || '操作失败，请稍后再试试！';
					this.$Message.error({
						content: error.message
					});
				});
				return;
			}
			//判断roleId是否为空，如果不为空则为更改角色操作
			if (!this.$formVerify.verifyString(this.typeForm.id)) {
				this.$post(this.$api.updateStoreTypeUrl, this.typeForm).then((result) => {
					this.$Message.success({
						content: '更改成功！'
					});
					this.resetDefault();
					this.resetLoading();
					this.$emit('addSuccessEvent');
					this.showFlag = false;
				}).catch((error) => {
					this.resetLoading();
					error.message = error.message || '操作失败，请稍后再试试！';
					this.$Message.error({
						content: error.message
					});
				});
				return;
			}
		}
	}
}
</script>
