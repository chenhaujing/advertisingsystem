<style lang="scss" scoped>
	.updatePw{
		margin: 45px auto;
		width: 1206px;
		background-color: #fff;
		.updatePw-title{
			font-size: 20px;
			color: #999;
		}
		.updatePw-form{
			width: 400px;
			margin: 60px auto;
		}
		.buttonBox{
			width: 100%;
			margin-top: 60px;
			button{
				font-size: 16px;
			}
			.updatePwBtn{
				width: 120px;
				height: 34px;
				background-color: #4cabe0;
				color: #fff;
				border-radius: 3px;
				outline: none;
				border: 0;
				float: left;
			}
			.updatePwBtn:active{
				border: 1px solid #4cabe0;
				background-color: #fff;
				color: #4cabe0;
			}
			.cancelBtn{
				width: 120px;
				height: 34px;
				background-color: #e6e8eb;
				color: #999;
				border-radius: 3px;
				outline: none;
				border: 0;
				float: right;
			}
			.cancelBtn:active{
				background-color: #999;
				color: #e6e8eb;
			}
		}
	}

</style>
<template>
	<div class="updatePw">
		<div class="updatePw-title">修改密码</div>
		<div class="updatePw-form">
			<pwInput labelName="登录密码" ref="loginPw"></pwInput>
			<pwInput labelName="新密码" ref="newPw"></pwInput>
			<pwInput labelName="确认密码" ref="rePw"></pwInput>
			<div class="buttonBox">
				<iButton class="updatePwBtn" type="primary" :loading="updating" @click="updatePw">修改密码</iButton>
				<button class="cancelBtn" @click="$router.go(-1)">取消</button>
				<div class="clear"></div>
			</div>
		</div>
	</div>
</template>
<script>
import iButton from 'iview/src/components/button';
import Message from 'iview/src/components/message';
import pwInput from 'components/tyPwInput';
export default {
	components: {
		iButton,
		pwInput
	},
	data() {
		return {
			loginPwVal:'',
			newPwVal:'',
			rePwVal:'',
			updating:false,
		}
	},
	methods: {
		updatePw(){
			//登录密码的值
			this.loginPwVal = this.$refs.loginPw.pwVal;
			//新密码的值
			this.newPwVal = this.$refs.newPw.pwVal;
			//重复密码的值
			this.rePwVal = this.$refs.rePw.pwVal;
			
			
			if(this.$formVerify.verifyString(this.loginPwVal)){
				Message.error('请输入账号登录密码！');
				return
			}
			if(this.$formVerify.verifyString(this.newPwVal)){
				Message.error('请输入设置的新密码！');
				return
			}
			if(this.$formVerify.verifyString(this.rePwVal)){
				Message.error('请输入确认密码！');
				return
			}
			//当新密码和重复密码一致的时候,并且所有输入框的值都不为空的时候
			if(!this.$formVerify.verifyString(this.loginPwVal) && !this.$formVerify.verifyString(this.newPwVal) && !this.$formVerify.verifyString(this.rePwVal)){
				if(this.newPwVal != this.rePwVal){
					Message.error('新密码和重复密码不一致，请重新输入！');
					return;
				}
				if(this.newPwVal == this.rePwVal){

					if(!((/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/).test(this.newPwVal))){
					   Message.error('请输入6-16位且同时包含字母和数字的密码格式');
					   return;
					}
					this.updating = true;
					var params = {
						'password':this.$Md5(this.loginPwVal.trim()),
						'newPassword':this.$Md5(this.newPwVal.trim())
					};
					this.$post(this.$api.modifyPasswordUrl,params).then((result) =>{
						this.updating = false;
						Message.success('密码修改成功！');
						this.$router.push({name:'login'})
					}).catch((e) =>{
						this.updating = false;
						e.message = e.message || '原密码错误！';
						Message.error(e.message);
					});
				}	
			}
		}
	},
}
</script>
