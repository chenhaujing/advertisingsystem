// <style lang="scss">
// .ivu-select {
// 	height: 34px;
// }

// .ivu-select-single .ivu-select-selection {
// 	height: 34px;
// }
// </style>
<style scoped lang="scss">
.uniteDropMenu {
	width: 225px;
	float: left;
	margin-right: 45px;
}

.lastUniteDropMenu {
	margin-right: 0;
}
</style>
<template>
	<div>
		<iForm :rules="ruleValidate">
			<iFormItem label="省份" prop="currProvince" class="uniteDropMenu">
				<iSelect v-model="currProvince.name" placeholder="请选择省份" @on-change="provinceChange">
					<iOption v-for="provinceItem in provinceData" :key="provinceItem.id" v-text="provinceItem.name" :value="provinceItem.name">
					</iOption>
				</iSelect>
			</iFormItem>
			<iFormItem label="城市" prop="currCity" class="uniteDropMenu">
				<iSelect placeholder="请选择城市" v-model="currCity.name" @on-change="cityChange" :disabled="!cityData || cityData.length == 0">
					<iOption v-for="cifyItem in cityData" :key="cifyItem.id" v-text="cifyItem.name" :value="cifyItem.name">
					</iOption>
				</iSelect>
			</iFormItem>
			<iFormItem label="地区" prop="currArea" class="uniteDropMenu">
				<iSelect placeholder="请选择地区" v-model="currArea.name" @on-change="areaChange" :disabled="!areaData || areaData.length == 0">
					<iOption v-for="areaItem in areaData" :key="areaItem.id" v-text="areaItem.name" :value="areaItem.name">
					</iOption>
				</iSelect>
			</iFormItem>
		</iForm>
	</div>
</template>
<script>
import iForm from 'iview/src/components/form';
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
export default {
	created() {
		this.$get(this.$api.getAreaByIdUrl, {
			areaId: 0,
		}).then((result) => {
			this.provinceData = result.data;
		}).catch((e) => {

		})
	},
	data() {
		return {
			v: {},
			currCity: {
				name: '',
				id: 0
			},
			currArea: {
				name: '',
				id: '0'
			},
			currProvince: {
				name: '',
				id: 0
			},
			province: null,
			provinceData: [],
			cityData: [],
			areaData: [],
			isSet: false,
			ruleValidate: {
				currProvince: [
					{ required: true, message: '请选择省份', trigger: 'blur' }
				],
				currCity: [
					{ required: true, message: '请选择城市', trigger: 'blur' }
				],
				currArea: [
					{ required: true, message: '请选择地区', trigger: 'blur' }
				],
			}
		}
	},
	methods: {
		initData(pName, p, c, cName, a, aName) {
			this.currProvince.name = pName;
			this.currCity.name = cName;
			this.currArea.name = aName;
			this.currProvince.id = p;
			this.currCity.id = c;
			this.currArea.id = a;
			this.$get(this.$api.getAreaByIdUrl, {
				areaId: p,
			}).then((result) => {
				this.cityData = result.data;
				this.$get(this.$api.getAreaByIdUrl, {
					areaId: c,
				}).then((result) => {
					this.areaData = result.data;
				}).catch((e) => {

				})
			}).catch((e) => {

			})
			// this.$getAreaData().then((result) => {
			// 	this.provinceData = result;
			// 	this.setData(p, c, a);
			// }).catch((e) => {
			// 	console.log(e);
			// })

			// StorageUtils.getDataByGlobay(StorageUtils.keys.AREA_DATA)
			// 	.then((result) => {
			// 		console.log('result')
			// 		console.log(result);
			// 		this.provinceData = result.data;
			// 		this.setData(p, c, a);
			// 	})
			// 	.catch((e) => {
			// 		//获取地区下拉列表的信息
			// 		this.$get(this.$api.areaInfoList).then((result) => {
			// 			this.provinceData = result.data;
			// 			// 缓存7天。
			// 			StorageUtils.saveDataByGlobay(
			// 				StorageUtils.keys.AREA_DATA,
			// 				this.provinceData,
			// 				new Date().getTime() + (1000 * 60 * 60 * 24 * 7)
			// 			)
			// 			this.setData(p, c, a);
			// 		}).catch((e) => {
			// 			e.message = e.message || '获取列表数据失败！';
			// 		});
			// 	})
		},
		setData(p, c, a) {
			this.isSet = true;
			this.currProvince.name = p;
			this.currCity.name = c;
			this.currArea.name = a;
		},
		getAreaData() {
			return {
				p: this.currProvince,
				c: this.currCity,
				a: this.currArea
			}
		},

		areaChange(value) {
			for (let i = 0; i < this.areaData.length; i++) {
				if (this.areaData[i].name == value) {
					this.currArea.id = this.areaData[i].id;
					break;
				}
			}
		},
		cityChange(value) {
			for (let i = 0; i < this.cityData.length; i++) {
				if (this.cityData[i].name == value) {
					this.currCity.id = this.cityData[i].id;
					this.$get(this.$api.getAreaByIdUrl, {
						areaId: this.cityData[i].id,
					}).then((result) => {
						this.areaData = result.data;
					}).catch((e) => {

					})
					break;
				}
			}
		},
		// 省份选中时触发
		provinceChange(value) {
			if (!this.isSet) {
				this.cityData = [];
				this.areaData = [];
			} else {
				this.isSet = false;
			}
			for (let i = 0; i < this.provinceData.length; i++) {
				if (this.provinceData[i].name == value) {
					this.currProvince.id = this.provinceData[i].id;
					this.$get(this.$api.getAreaByIdUrl, {
						areaId: this.provinceData[i].id,
					}).then((result) => {
						this.cityData = result.data;
					}).catch((e) => {
					})
					break;
				}
			}
		}
	},
	components: {
		iForm,
		'iFormItem': iForm.Item,
		iSelect,
		iOption
	}
}
</script>
