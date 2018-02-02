<style lang="scss" scoped>
	.pwInputBox{
		width: 100%;
		margin-bottom: 45px;
		position: relative;
		.pwLabel{
			font-size: 16px;
			color: #5d5d5d;
			margin-bottom: 10px;
		}
		.pwInput{
			width: 100%;
			height: 34px;
			border: 1px solid #e6e8eb;
			border-radius: 6px;
			box-sizing: border-box;
			padding-left: 10px;
			padding-right: 36px;
		}
		.pwInput:active{
			border: 1px solid #4cabe0;
		}
		.pwValSight{
			position: absolute;
			top: 40px;
			right: 10px;
			cursor: pointer;
		}
		.formVerifyClass{
        position: absolute;
			top: 70px;
			left: 0;
		font-size: 16px;
		color: red;
		line-height: 32px;
		}
	}
	
</style>
<template>
	<div class="pwInputBox">
		<div class="pwLabel" v-text="labelName"></div>
		<input v-show="isEyesClose" type="password" class="pwInput" v-model="pwVal" @blur="checkPwVal(pwVal)"/><div v-show="isEyesClose" div class="pwValSight iconfont icon-eye-close" @click="pwValSight"></div>
		<input v-show="!isEyesClose" type="text" class="pwInput" v-model="pwVal" @blur="checkPwVal(pwVal)"/><div v-show="!isEyesClose"  class="pwValSight iconfont icon-yanjing"  @click="pwValSight"></div>
		<p v-show="isEmpty" class="formVerifyClass">请输入{{labelName}}</p>
	</div>
</template>
<script>
import Message from 'iview/src/components/message';	
export default {
	props:['labelName'],
	data(){
		return{
			pwVal:'',
			isEyesClose:true,
			isEmpty:false
		}
	},
	methods: {
		pwValSight(){
		  this.isEyesClose=!this.isEyesClose;
		},
		checkPwVal(val){
          if(val){
			  this.isEmpty=false;
		  }else{
			  this.isEmpty=true;
		  } 			 
		}
	},
}
</script>
