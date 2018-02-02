<style lang="scss" scoped>
.login {
	width: 100%;
	text-align: center;
	.loginBg {
		width: 100%;
	}
	.loginForm {
		width: 580px;
		margin: 0 auto;
		background-color: #fff;
		box-sizing: border-box;
		padding: 50px 70px 50px;
		position: relative;
		z-index: 10;
		margin-top: -90px;
		box-shadow: 6px 0 16px rgba(126, 126, 126, 0.2);
	}
	.formTitle {
		margin-bottom: 55px;
		font-size: 30px;
		color: #4cabe0;
	}
	.inputGroup {
		width: 100%;
		box-sizing: border-box;
		border: 1px solid #eee;
		border-radius: 6px;
		height: 52px;
		overflow: hidden;
		margin-bottom: 20px;
	}
	.inputGroupFocus {
		border: 1px solid #4cabe0;
	}
	.inputGroupError {
		border: 1px solid #f00;
	}
	.inputGroup label {
		float: left;
		display: inline-block;
		text-align: center;
		line-height: 50px;
		width: 13%;
		color: #999;
		font-size: 20px;
	}
	.inputGroup input {
		float: left;
		height: 50px;
		box-sizing: border-box;
		padding-left: 10px;
		font-size: 16px;
		width: 87%;
		border: 0;
		border-radius: 6px;
		outline: none;
		background-color: #fff;
	}
	.errorTips {
		position: relative;
		top: -10px;
		text-align: left;
		color: #f00;
	}
	.ivu-btn {
		width: 100%;
		background-color: #4cabe0;
		border: 1px solid #4cabe0;
		border-radius: 30px;
	}
	.remember {
		text-align: left;
		width: 100%;
		margin-bottom: 20px;
		overflow: hidden;
	}
	.remember .checkbox {
		float: left;
	}
	.remember .forgetPassword {
		float: right;
		font-size: 16px;
		color: #4cabe0;
	}
	.loginBtn {
		width: 100%;
		background-color: #4cabe0;
		border: 1px solid #4cabe0;
		border-radius: 30px;
		color: #fff;
		height: 50px;
		margin-bottom: 10px;
		outline: none;
		font-size: 24px;
	}
	.loginBtn:active {
		color: #4cabe0;
		background-color: #fff;
		border: 1px solid #4cabe0;
		outline: none;
	}


	/*重置密码表单数据Box*/
	.resetPwForm {
		width: 580px;
		margin: 0 auto;
		background-color: #fff;
		box-sizing: border-box;
		padding: 50px 70px;
		position: relative;
		z-index: 10;
		margin-top: -90px;
		box-shadow: 6px 0 16px rgba(126, 126, 126, 0.2);
		/*重置密码表单标题*/
		.formTitle {
			margin-bottom: 55px;
			font-size: 30px;
			color: #4cabe0;
		}
		/*输入框样式组*/
		.inputGroup {
			width: 100%;
			box-sizing: border-box;
			border: 1px solid #eee;
			border-radius: 6px;
			height: 52px;
			overflow: hidden;
			margin-bottom: 20px;
		}
		.inputGroupFocus {
			border: 1px solid #4cabe0;
		}
		.inputGroupError {
			border: 1px solid #f00;
		}
		.inputGroup label {
			float: left;
			display: inline-block;
			text-align: center;
			line-height: 50px;
			width: 13%;
			color: #999;
			font-size: 20px;
		}
		.inputGroup input {
			float: left;
			height: 50px;
			box-sizing: border-box;
			padding-left: 10px;
			font-size: 16px;
			width: 87%;
			border: 0;
			border-radius: 6px;
			outline: none;
		}
		/*获取验证码输入框组样式*/
		.codeGroup {
			width: 100%;
			overflow: hidden;
			margin-bottom: 20px;
		}
		.codeBox {
			width: 65%;
			box-sizing: border-box;
			border: 1px solid #eee;
			border-radius: 6px;
			height: 52px;
			overflow: hidden;
			float: left;
		}
		.codeBoxFocus {
			border: 1px solid #4cabe0;
		}
		.codeBoxError {
			border: 1px solid #f00;
		}
		.codeBox label {
			float: left;
			font-size: 20px;
			display: inline-block;
			text-align: center;
			line-height: 50px;
			width: 20%;
			color: #999;
		}
		.codeBox input {
			float: left;
			height: 50px;
			box-sizing: border-box;
			padding-left: 10px;
			font-size: 16px;
			width: 80%;
			border: 0;
			border-radius: 6px;
			outline: none;
		}
		.codeBox button {
			height: 40px;
			font-size: 12.8px;
		}
		/*确定按钮样式*/
		.sureBtn {
			width: 100%;
			background-color: #4cabe0;
			border: 1px solid #4cabe0;
			border-radius: 30px;
			color: #fff;
			height: 50px;
			margin-bottom: 10px;
			outline: none;
			font-size: 16px;
		}
		.sureBtn:active {
			color: #4cabe0;
			background-color: #fff;
			border: 1px solid #4cabe0;
			outline: none;
		}
		/*重置密码成功提示*/
		.successTips {
			width: 100%;
			height: 200px;
			line-height: 200px;
			font-size: 12px;
		}
		/*返回首页*/
		.backtoIndex {
			color: #4cabe0;
			font-size: 16px;
		}
	}
}

@media only screen and (min-width: 1280px) and (max-width: 1920px) {
	.login {
		width: 100%;
		text-align: center;
		.loginBg {
			width: 100%;
		}
		.loginForm {
			width: 464px;
			padding: 40px 56px 40px;
			margin-top: -72px;
			box-shadow: 4.8px 0 12.8px rgba(126, 126, 126, 0.2);
		}
		.formTitle {
			margin-bottom: 44px;
			font-size: 24px;
		}
		.inputGroup {
			border-radius: 4.8px;
			height: 41.6px;
			margin-bottom: 16px;
		}
		.inputGroup label {
			line-height: 40px;
			font-size: 16px;
		}
		.inputGroup input {
			height: 40px;
			padding-left: 8px;
			font-size: 12.8px;
			border-radius: 4.8px;
		}
		.errorTips {
			top: -8px;
		}
		.ivu-btn {
			border-radius: 24px;
		}
		.remember {
			margin-bottom: 16px;
		}
		.remember .forgetPassword {
			font-size: 12.8px;
		}
		.loginBtn {
			border-radius: 24px;
			height: 40px;
			margin-bottom: 8px;
			font-size: 19.2px;
		}


		/*重置密码表单数据Box*/
		.resetPwForm {
			width: 464px;
			padding: 40px 56px;
			margin-top: -72px;
			box-shadow: 4.8px 0 12.8px rgba(126, 126, 126, 0.2);
			/*重置密码表单标题*/
			.formTitle {
				margin-bottom: 44px;
				font-size: 24px;
			}
			/*输入框样式组*/
			.inputGroup {
				border-radius: 4.8px;
				height: 41.6px;
				margin-bottom: 16px;
			}
			.inputGroup label {
				line-height: 40px;
				font-size: 16px;
			}
			.inputGroup input {
				height: 40px;
				padding-left: 8px;
				font-size: 12.8px;
				border-radius: 4.8px;
			}
			/*获取验证码输入框组样式*/
			.codeGroup {
				margin-bottom: 16px;
			}
			.codeBox {
				border-radius: 4.8px;
				height: 41.6px;
			}
			.codeBox label {
				font-size: 16px;
				line-height: 40px;
			}
			.codeBox input {
				height: 40px;
				padding-left: 8px;
				font-size: 12.8px;
				border-radius: 4.8px;
			}
			/*确定按钮样式*/
			.sureBtn {
				border-radius: 24px;
				height: 40px;
				margin-bottom: 8px;
				font-size: 12.8px;
			}
			/*重置密码成功提示*/
			.successTips {
				height: 160px;
				line-height: 160px;
				font-size: 12px;
			}
			/*返回首页*/
			.backtoIndex {
				font-size: 12.8px;
			}
		}
	}
}
</style>
<template>
	<div class="login">
		<img src="~assets/img/common/loginBg.png" class="loginBg" />
		<!--登录表单 start-->
		<div class="loginForm" v-show="loginShow">
			<div class="formTitle">登录</div>
			<!---->
			<form v-on:keyup.13="login" action="javaScript:void(0)" @submit="login">
				<div class="inputGroup phoneNumBox">
					<label class="iconfont icon-yonghudenglu"></label>
					<input type="text" class="inputGroup-input" autocomplete="off" v-model="formInline.user" @blur="blur('user')" placeholder="请输入手机号" />
				</div>
				<div class="errorTips" v-text="userErrorTips"></div>
				<div class="inputGroup pwBox">
					<label class="iconfont icon-mima"></label>
					<input type="password" class="inputGroup-input" autocomplete="new-password" v-model="formInline.password" @blur="blur('password')" placeholder="请输入密码" />
				</div>
				<div class="errorTips" v-text="pwErrorTips" ></div>
				<div class="remember">
					<!--<Checkbox class="checkbox" v-model="formInline.single">30天内记住我</Checkbox>-->
					<!--<a href="javascript:void(0);" class="forgetPassword" @click="showResetPw">忘记密码?</a>-->
				</div>
				<i-button type="primary" :loading="loading" @click="login" class="loginBtn">
					<span v-if="!loading">登录</span>
					<span v-else>登录中...</span>
				</i-button>
			</form>
		</div>
		<!--登录表单 end-->

		<!--重置密码表单 start-->
		<div class="resetPwForm" v-show="resetPwShow">
			<div class="formTitle">重置密码</div>
			<div v-if="success == 0">
				<div class="inputGroup resetPhoneBox" ref="phoneInputBox">
					<label class="iconfont icon-yonghudenglu"></label>
					<input type="text" class="inputGroup-input" v-model="resetFormInline.user" @focus="focusReset('inputGroupFocus','.resetPhoneBox')" @blur="blurReset('inputGroupFocus','resetFormInline.user','.resetPhoneBox')" placeholder="请输入手机号" />
				</div>
				<div class="codeGroup">
					<div class="codeBox">
						<label class="iconfont icon-icon-test6"></label>
						<input type="text" class="inputGroup-codeinput" v-model="resetFormInline.code" @focus="focusReset('codeBoxFocus','.codeBox')" @blur="blurReset('codeBoxFocus','resetFormInline.code','.codeBox')" placeholder="请输入验证码" />
					</div>
					<downCountBtn :totalTime="10" @click.native="downCountGo" ref="downCountButton"></downCountBtn>
				</div>
				<div class="inputGroup resetPwBox">
					<label class="iconfont icon-mima"></label>
					<input type="password" class="inputGroup-input" v-model="resetFormInline.password" @focus="focusReset('inputGroupFocus','.resetPwBox')" @blur="blurReset('inputGroupFocus','resetFormInline.password','.resetPwBox')" placeholder="请输入新密码" />
				</div>
				<button class="sureBtn" @click="sure">确定</button>
			</div>
			<div class="successTips" v-if="success == 1">
				密码已经重置成功，请返回首页重新登录
			</div>
			<a href="javascript:void(0);" class="backtoIndex" @click="showLogin">返回首页</a>
		</div>
		<!--重置密码表单 end-->
	</div>
</template>
<script>
import Button from 'iview/src/components/button';
import iForm from 'iview/src/components/form';
import Message from 'iview/src/components/message';
import downCountBtn from 'components/tyDownCountButton'
var self = this;
export default {
	components: {
		iForm,
		'i-button': Button,
		downCountBtn
	},
	data() {
		return {
			//默认为登录表单显示
			loginShow: true,
			//默认重置密码表单隐藏
			resetPwShow: false,

			//默认loading为false
			loading: false,
			//登录表单数据
			formInline: {
				//手机号
				user: '',
				//密码
				password: '',
				//checkbox
				single: false,
			},
			//默认手机号检验不通过
			userRight: false,
			//默认密码检验不通过
			pwRight: false,
			//手机号错误提示语
			userErrorTips: null,
			//密码错误提示语
			pwErrorTips: null,


			//重置密码表单数据
			resetFormInline: {
				//手机号
				user: '',
				//验证码
				code: '',
				//新密码
				password: '',
			},
			//默认重置手机号检验不通过
			userResetRight: false,
			//默认重置验证码检验不通过
			codeResetRight: false,
			//默认重置新密码检验不通过
			pwResetRight: false,
			//默认重置表单校验不通过
			success: 0
		}
	},
	methods: {
		//隐藏登录表单，显示重置密码表单
		showResetPw() {
			this.loginShow = false;
			this.resetPwShow = true;
		},
		//隐藏重置密码表单，显示登录表单
		showLogin() {
			this.loginShow = true;
			this.resetPwShow = false;
		},
		checkParams() {

			if (this.$formVerify.verifyString(this.formInline.user)) {
				this.userErrorTips = '手机号不能为空';
			} else {
				if (!this.$formVerify.verifyPhone(this.formInline.user)) {
					this.userErrorTips = '手机号格式不对，请重新输入';
				}
				else {
					this.userErrorTips = null;
				}
			}


			if (this.$formVerify.verifyString(this.formInline.password)) {
				this.pwErrorTips = '请输入密码';
			} else {
				this.pwErrorTips = null;
			}
		},
		blur(key) {
			this.checkParams();
		},
		// /*======================登录表单验证=========================*/
		// focus(className, parentClassName) {
		// 	document.querySelector(parentClassName).classList.add(className);
		// },
		// blur(className, val, parentClassName) {
		// 	document.querySelector(parentClassName).classList.remove(className);
		// 	//手机号验证
		// 	if (val == 'formInline.user') {
		// 		if (this.$formVerify.verifyString(this.formInline.user)) {
		// 			document.querySelector(parentClassName).classList.add('inputGroupError');
		// 			this.userErrorTips = '手机号不能为空';
		// 			return;
		// 		}
		// 		if (!this.$formVerify.verifyPhone(this.formInline.user)) {
		// 			document.querySelector(parentClassName).classList.add('inputGroupError');
		// 			this.userErrorTips = '手机号格式不对，请重新输入';
		// 			return;
		// 		}
		// 		if (this.$formVerify.verifyPhone(this.formInline.user)) {
		// 			document.querySelector(parentClassName).classList.remove('inputGroupError');
		// 			this.userErrorTips = '';
		// 			this.userRight = true;
		// 			return;
		// 		}
		// 	}
		// 	//密码验证
		// 	if (val == 'formInline.password') {
		// 		if (this.$formVerify.verifyString(this.formInline.password)) {
		// 			document.querySelector(parentClassName).classList.add('inputGroupError');
		// 			this.pwErrorTips = '密码不能为空';
		// 			return;
		// 		}
		// 		if (!this.$formVerify.verifyString(this.formInline.password)) {
		// 			document.querySelector(parentClassName).classList.remove('inputGroupError');
		// 			this.pwErrorTips = '';
		// 			this.pwRight = true;
		// 			return;
		// 		}
		// 	}
		// },
		//登录检测
		login() {
			var self = this;
			if (!this.userErrorTips && !this.pwErrorTips) {
				var params = {
					username: this.formInline.user,
					password: this.$Md5(this.formInline.password),
					"remember-me": true
				};
				this.loading = true;
				this.$submit(this.$api.loginUrl, params).then((result) => {
					this.$store.commit(this.$mutations.BREADCRUMD_CLEAR);
					this.$router.push({ name: 'clientManagerIndex' });
					
					this.$root.pathname='clientManagerIndex';
					this.loading = false;
				}).catch((error) => {
					this.loading = false;
					error.message = error.message || '操作失败，请稍后再试试！';
					Message.error(error.message);
				});
			}
		},

		/*======================重置密码表单验证=========================*/
		downCountGo() {
			this.$refs.downCountButton.start();
		},
		//focus的时候高亮控制
		focusReset(className, parentClassName) {
			document.querySelector(parentClassName).classList.add(className);
		},
		//blur的时候高亮关闭并进行输入框值的校验
		blurReset(className, val, parentClassName) {
			document.querySelector(parentClassName).classList.remove(className);
			//手机号验证
			if (val == 'resetFormInline.user') {
				if (this.resetFormInline.user == '') {
					document.querySelector(parentClassName).classList.add('inputGroupError');
					Message.error('手机号不能为空');
				} else if (this.$formVerify.verifyPhone(this.resetFormInline.user)) {
					document.querySelector(parentClassName).classList.remove('inputGroupError');
					this.userResetRight = true;
				} else {
					document.querySelector(parentClassName).classList.add('inputGroupError');
					Message.error('手机号格式不对，请重新输入');
				}
			}
			//短信验证码验证
			if (val == 'resetFormInline.code') {
				//这里写短信验证码验证逻辑
				console.log('这里写短信验证码的验证');
				if (this.resetFormInline.code == '') {
					document.querySelector(parentClassName).classList.add('codeBoxError');
					Message.error('验证码不能为空');
				} else {
					document.querySelector(parentClassName).classList.remove('codeBoxError');
					this.codeResetRight = true;
				}
			}
			//新密码验证
			if (val == 'resetFormInline.password') {
				var pwValLength = this.resetFormInline.password.trim().length;
				if (this.resetFormInline.password == '') {
					document.querySelector(parentClassName).classList.add('inputGroupError');
					Message.error('密码不能为空');
				} else if (pwValLength < 6) {
					document.querySelector(parentClassName).classList.add('inputGroupError');
					Message.error('账号或密码不正确，请重新输入');
				} else {
					document.querySelector(parentClassName).classList.remove('inputGroupError');
					this.pwResetRight = true;
				}
			}
		},
		//确定校验
		sure() {
			if (this.userResetRight && this.codeResetRight && this.pwResetRight) {
				this.success = 1;
			}
		}
	},
}
</script>
