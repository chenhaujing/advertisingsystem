<style  lang="scss" scoped>
@import '~assets/css/base.scss';
//广告配置页面模态框样式
$modelHeight: 550px;
//
.addTimes {
	.timesForm{
		width: 380px;
		margin: 0 auto;
		box-sizing: border-box;
		padding: 30px 0 40px;
		.addTimesGroupLeft{
			float: left;
			width: 45%;
		}
		.addTimesGroupRight{
			float: right;
			width: 45%;
		}
		button{
			width: 120px;
			height: 34px;
			border:0;
			outline: none;
			border-radius:3px;
		}
		.saveBtn{
			color: #fff;
			background-color: #4cabe0;	
		}
		.saveBtn:active{
			color: #4cabe0;
			background-color: #fff;
			border:1px solid #4cabe0;
		}
		.cancelBtn{
			float: right;
			background-color: #dcdee0;
			color: #999;
		}
		.cancelBtn:active{
			background-color: #999;
			color: #dcdee0;
		}
	}
	// 覆盖模态框主内容区域padding
	overflow: hidden;
	.ivu-modal-body {
		padding: 0;
	}
	.ivu-form-item-label{
		font-size: 16px;
	}
	.ivu-input{
		height: 34px;
	}
	// .ivu-select-single .ivu-select-selection{
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

	.table-tool{
		height: 38px;
	}
	
}
</style>

<template>
	<iModal class="baseModal addTimes" :title="modalTitle" v-model="showFlag" :mask-closable="false" :width="600">
		<div class="timesForm">
			<iForm :model="durationForm" >
		        <div class="addTimesGroupLeft">
		        	<iFormitem label="时长">
			            <iInput v-model="durationForm.duration"  @on-keyup="$format.setNumber(durationForm,'duration')"></iInput>
			        </iFormitem>
		        </div>
		        <div class="addTimesGroupRight">
		        	<iFormitem label="时间单位">
			            <iSelect v-model="durationForm.durationUnit" :placeholder="placeholder">
			                <iOption 
							v-for="item in unitList"
							:value="item.value" 
							:key="item.value">
							{{ item.label }}
							</iOption>
			            </iSelect>
			        </iFormitem>
		        </div>
		        <div class="clear"></div>
			</iForm>	        
		</div>
		<div class="buttonTools" slot="footer">
			<iButton class="buttonTools_finish"
			:loading="finishLoading" @click="finish">{{finishBtnText}}</iButton>
			<iButton class="buttonTools_cancel" @click="close">取消</iButton>
		</div>
	</iModal>
</template>
<script>
import iModal from 'iview/src/components/modal';
import iForm from 'iview/src/components/form';
import iInput from 'iview/src/components/input';
import ModalMixins from 'components/tyModal/baseModal';
import { Select as iSelect, Option as iOption }  from 'iview/src/components/select';
export default {
	props:['inputName','inputCompany','companyArray','placeholder','title'],
	components:{
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
			defaultForm:{
            "duration": '',
            "durationUnit": 3
		    },
			durationForm:{
            "duration": '',
            "durationUnit": 3
		   },
		   url:this.$api.addDurationConfigUrl,
		   unitList:[
			   {label:"时",value:1},
			   {label:"分",value:2},
			   {label:"秒",value:3},
		   ]
		}
	},
	watch: {
		showFlag(val) {
			if (val) {
				if (this.formData) {
					Object.assign(this.durationForm, this.formData);
					this.url=this.$api.updateDurationConfigUrl
				}
			} else {
				this.clearData(this.durationForm,this.defaultForm);
				this.formData=null;
				this.url=this.$api.addDurationConfigUrl
			}
		}
	},
	mixins: [ModalMixins],
	methods: {
		finish () {
			if(!this.durationForm.duration||this.durationForm.duration==0){
				this.$Notice.error({
					title:'错误',
					desc: '请输入有效的广告时长'
				});
				return;
			}
			this.$post(this.url,this.durationForm).then(result=>{
				this.$Notice.success({
				desc: '保存成功'
				})
				this.toggle();
				this.$emit('successEvent','addDurationTable')
			}).catch(error=>{
                    this.$Notice.error({
					title:'错误',
					desc: error.message||'保存失败'
				});
			})
	        
	    }
	},
}
</script>
