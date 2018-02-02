<style  lang="scss">
@import '~assets/css/base.scss';
//组织机构管理页面模态框样式
$modelHeight: 550px;
//
.addBaseStructure {
	.addBaseStructureForm{
		width: 325px;
		margin: 0 auto;
		box-sizing: border-box;
		padding: 30px 0 40px;
		.selectResponsibilityBox{
			width:100%;
			.label{
				font-size: 16px;
				line-height: 36px;
			}
			.selectResponsibility{
				line-height: 32px;
				width: 100%;
				box-sizing: border-box;
				border: 1px solid #dddee1;
				margin-bottom: 24px;
				border-radius: 4px;
				padding: 0 7px;
				cursor: pointer;
			}
			.addResponsibility{
				float: left;
				font-size: 14px;
			}
			.icon{
				float: right;
			}
		}
		button{
			width: 120px;
			height: 34px;
			border:0;
			outline: none;
		}
		.saveBtn{
			color: #fff;
			background-color: #4cabe0;	
			border-radius:3px;
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
    // 覆盖 模态框标题颜色
	.ivu-modal-header {
		background-color: $mainColor;
	} // 覆盖 模态框标题字体颜色   右上角关闭按钮颜色
	.ivu-modal-header p,
	.ivu-modal-header-inner,
	.ivu-modal-close .ivu-icon-ios-close-empty {
		color: #ffffff;
	} // 隐藏脚步
	.ivu-modal-footer {
		display: none;
	}
	.table-tool{
		height: 38px;
	}
	
}
</style>
<template>
<div>			
	<iModal class="addBaseStructure" :title="modalTitle" v-model="modal" :mask-closable="false" :width="600" @on-cancel="cancel">
		<div class="addBaseStructureForm">
			<iForm :model="addBaseStructureForm" >
		        <iFormitem label="组织机构名称" style="width: 100%;">
		            <iInput v-model="formData.name"></iInput>
					<!-- <p>请输入名称</p> -->
		        </iFormitem>
		        <iFormitem label="上级组织机构" v-show="ctrlData.root">
		            <iInput v-model="addBaseStructureForm.structure" :disabled="ctrlData.root"  ></iInput>
		            
		        </iFormitem>
				<div class="selectResponsibilityBox" @click="selectOrganzationStructure()" v-show="!ctrlData.root">
		        	<div class="label">上级组织机构</div>
		        	<div class="selectResponsibility">
		        		<span class="addResponsibility" v-text="formData.rootOrganzationName||'添加组织机构'"></span>
		        		<span class="icon iconfont icon-jia1"></span>
		        		<div class="clear"></div>
		        	</div>
		        </div>
		        <div class="selectResponsibilityBox" @click="selectResponsibility">
		        	<div class="label">负责人</div>
		        	<div class="selectResponsibility">
		        		<span class="addResponsibility" v-text="formData.leaderName||'添加负责人'"></span>
		        		<span class="icon iconfont icon-jia1"></span>
		        		<div class="clear"></div>
		        	</div>
		        </div>
		    </iForm>
		    <button class="saveBtn" @click="save">保存</button>
		    <button class="cancelBtn" @click="cancel">取消</button>
		    <div class="clear"></div>
		</div>
	</iModal>
	 <tyAddStructureModal ref="addStructure" @leaderData="leaderData"></tyAddStructureModal>
	 <tySelectOrganzationStructureModal ref="selectOrganzationStructure"  @rootOrganzationData="rootOrganzationData" :currentId="selectOrganizationData.length?selectOrganizationData[0].id:''"></tySelectOrganzationStructureModal>
	</div>	
</template>
<script>
import iModal from 'iview/src/components/modal';
import iForm from 'iview/src/components/form';
import iInput from 'iview/src/components/input';
import { Select as iSelect, Option as iOption }  from 'iview/src/components/select';
import tyAddStructureModal from './tyAddStructureModal';
import tySelectOrganzationStructureModal from './tySelectOrganzationStructureModal';

export default {
	data() {
		return {
			modal: false,
			isChangeData:false,
			modalTitle:'',
			addBaseStructureForm: {
                    total: '',
                    structure: '',
                    man:'' 
				},
				emptyShow:{
					 name:false,
					 ownerId:false,
					 parentId:false,
				},
				formData:{
					name:'',
					leaderName:'',
					rootOrganzationName:'',
					ownerId:'',
					parentId:'',
				}
		}
	},
	props:["ctrlData","selectOrganizationData"],
	watch:{
		ctrlData(){
			if(this.ctrlData.type=="create"){
				this.clearData()
				this.modalTitle='新增';
				this.selectOrganizationData=[];
			}
			if(this.ctrlData.type=="edit"){
				this.modalTitle='编辑'
            this.formData={
				    name:this.selectOrganizationData[0].name,
					leaderName:this.selectOrganizationData[0].ownerName,
					rootOrganzationName:this.selectOrganizationData[0].parentOrganizationName,
					ownerId:this.selectOrganizationData[0].ownerId,
					parentId:this.selectOrganizationData[0].parentId,
					id:this.selectOrganizationData[0].id,
			}
			//保存判断是否修改
			this.saveData={
				    name:this.selectOrganizationData[0].name,
					leaderName:this.selectOrganizationData[0].ownerName,
					rootOrganzationName:this.selectOrganizationData[0].parentOrganizationName,
					ownerId:this.selectOrganizationData[0].ownerId,
					parentId:this.selectOrganizationData[0].parentId,
					id:this.selectOrganizationData[0].id,
			}
			}
			if(this.ctrlData.root){
				this.modalTitle=this.modalTitle+'根组织机构'
			}else{
				this.modalTitle=this.modalTitle+'组织机构'
			} 
		},

	},
	methods: {
		clearData(){
			  this.formData={
				    name:'',
					leaderName:'',
					rootOrganzationName:'',
					ownerId:'',
					parentId:'',
			  };
		},
		//接收负责人的id和名字
		leaderData(data){
		  this.formData.leaderName=data.row.nickname; 
		  this.formData.ownerId=data.row.id; 
		  this.modal = true;
		},
		//接受组织机构的id和名字
		rootOrganzationData(data){
        this.formData.parentId=data.row.id;
        this.formData.rootOrganzationName=data.row.name;
		},
		//弹出负责人的模态框
		selectResponsibility(){
			this.$refs.addStructure.modal = true;
		},
		//弹出组织列表的模态框
		selectOrganzationStructure(){
            this.$refs.selectOrganzationStructure.modal = true; 
		},
		save () {
			if(this.ctrlData.type=="edit"){
		 	  for(var k in this.formData){
				if(this.formData[k] != this.saveData[k]){
				  this.isChangeData=true;
				  break 
				}
			}
			if(!this.isChangeData){
            this.modal = false;
			  return
			}
			}
          if(this.$formVerify.verifyString(this.formData.name)){
				this.$Message.error({
					content: '请输入有效的组织机构名称'
				});
				return;
			}
			
            if(this.ctrlData.root==false&&this.$formVerify.verifyString(this.formData.rootOrganzationName)){
				this.$Message.error({
					content: '请选择有效的上级组织机构'
				});
				return;
			}
			
            if(this.$formVerify.verifyString(this.formData.leaderName)){
				this.$Message.error({
					content: '请选择有效的组织机构负责人'
				});
				return;
			}
			var url=this.$api.addOrganizationUrl;
			if(this.ctrlData.type=="edit"){
				url=this.$api.updateOrganizationPersonUrl;
			}
			this.$post(url,this.formData)
			.then((data)=>{
            if(data.successed){
				this.$Message.success({
					content:this.ctrlData.type=="edit"?'修改成功':'新增成功',
				})
				this.clearData();
				this.modal = false;
				this.$emit('refreshTree');
			}	
			},(mes)=>{
                this.$Message.error({
					content:mes.message
				})	
				
			})
	        
	    },
	    cancel () {
			this.clearData();
	        this.modal = false;
	    }
	},
	components: {
		iModal,
		iForm,
		iInput,
		iSelect,
		iOption,
		'iFormitem': iForm.Item,
		tyAddStructureModal,
		tySelectOrganzationStructureModal
	}
}
</script>
