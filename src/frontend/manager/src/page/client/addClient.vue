
<style scoped lang="scss">
/*添加客户样式*/

.addClientBox {
	padding: 0;
	padding-top: 10px;
	/*添加客户确认取消按钮区域样式*/
	.buttonBox {
		width: 100%;
		float: right;
		text-align: right;
		height: 75px;
		box-sizing: border-box;
		padding-top: 20px;
		padding-right: 40px;
		/*margin-top:-10px;*/
		position: fixed;
		background-color: #f1f1f1;
		top: 96px;
		left: 0;
		z-index: 998;
		button {
			width: 120px;
			height: 34px;
			font-size: 16px;
			border: 0;
			border-radius: 3px;
			outline: none;
			cursor: pointer;
		}
		.sure {
			color: #fff;
			background-color: #4cabe0;
			margin-right: 6px;
		}
		.sure:active {
			color: #4cabe0;
			background-color: #fff;
			border: 1px solid #4cabe0;
		}
		.cancel {
			color: #999;
			background-color: #e6e8eb;
		}
		.cancel:active {
			color: #e6e8eb;
			background-color: #999;
			border: 1px solid #e6e8eb;
		}
	}
	.clear {
		clear: both;
	}
	/*添加客户白色背景区域样式*/
	.addClient {
		min-width: 1229px;
		box-sizing: border-box;
		padding: 105px 65px 30px;
		background-color: #fff;
		/*添加客户白色背景左侧区域样式*/
		.addClient-left {
			float: left;
			.addClient-title {
				font-size: 20px;
				color: #999;
				margin-bottom: 40px;
			}
			.uploadImgBox {
				width: 140px;
				float: left;
				text-align: center;
				.imgShow {
					width: 140px;
					height: 140px;
					border-radius: 50%;
					border: 0;
					overflow: hidden;
					box-sizing: border-box;
					margin-bottom: 30px;
					img {
						object-fit: cover;
						width: 100%;
						height: 100%;
						border-radius: 50%;
					}
				}
				.uploadImg {
					width: 120px;
					display: inline-block;
					height: 34px;
					line-height: 34px;
					background-color: #4cabe0;
					color: #fff;
					border-radius: 6px;
					border: 0;
					font-size: 16px;
					input {
						display: none;
					}
				}
				.uploadImg:active {
					background-color: #fff;
					color: #4cabe0;
					border: 1px solid #4cabe0;
				}
			}
		}
		/*添加客户白色背景右侧区域样式*/
		.addClient-right {
			float: left;
			margin-left: 70px;
			box-sizing: border-box;
			padding-top: 40px;
			/*必填项区域样式*/
			.formValidate {
				.requireInput {
					width: 400px;
				}
				.requireInputBox {
					float: left;
					margin-right: 65px;
				}
				.lastRequireInputBox {
					margin-right: 0;
				}
			}
			/*非必填项区域样式*/
			.notRequireForm {
				margin-top: 20px;
				.notRequireFormSelect {
					width: 234px;
					float: left;
					margin-right: 20px;
				}
			}
		}
	}
}
</style>
<style lang="scss">
/*添加客户样式*/
.addClient-right {
	.ivu-form .ivu-form-item-label {
		font-size: 16px;
	}
	.ivu-input {
		height: 34px;
	}
	.ivu-form-item-error-tip {
		font-size: 16px;
	}
}
</style>
<template>
	<div class="addClientBox">
		<div class="buttonBox">
			<button class="sure" @click="sureAddClient">保存</button>
			<button class="cancel" @click="$router.go(-1)">取消</button>
		</div>
		<div class="clear"></div>
		<div class="addClient">
			<div class="addClient-left">
				<div class="addClient-title">客户信息</div>
				<div class="uploadImgBox">
					<div class="imgShow">
						<img v-imgError="error" class="imgShow" :src="requireFormValidate.headPortrait" />
					</div>
					<label class="uploadImg">上传图片
						<input type="file" v-if="bugFixChange" @change="fileChange" accept=".png,.jpg,.jpeg,.bmp" />
						</label>
				</div>
			</div>
			<div class="addClient-right">
				<template>
					<iForm ref="formValidate" :model="requireFormValidate" :rules="ruleValidate" class="formValidate">
						<iFormitem label="客户名称" prop="name" class="requireInputBox">
							<iInput class="requireInput" v-model="requireFormValidate.name" ref="name"></iInput>
						</iFormitem>
						<div class="clear"></div>
						<iFormitem label="联系人" prop="contacts" class="requireInputBox">
							<iInput class="requireInput" v-model="requireFormValidate.contacts" ref="contacts"></iInput>
						</iFormitem>
						<iFormitem label="联系电话" prop="contactNumber" class="requireInputBox lastRequireInputBox">
							<iInput class="requireInput" v-model="requireFormValidate.contactNumber" ref="contactNumber"></iInput>
						</iFormitem>
						<div class="clear"></div>
						<iFormitem label="从属行业" prop="industry" class="requireInputBox">
							<iInput class="requireInput" v-model="requireFormValidate.industry" ref="industry"></iInput>
						</iFormitem>
						<iFormitem label="从属子行业" class="requireInputBox lastRequireInputBox">
							<iInput class="requireInput" v-model="requireFormValidate.subIndustry" ref="subIndustry"></iInput>
						</iFormitem>
						<div class="clear"></div>
						<iFormitem label="邮箱" class="requireInputBox">
							<iInput class="requireInput" v-model="requireFormValidate.mailAdress" ref="mailAdress"></iInput>
						</iFormitem>
						<div class="clear"></div>
						<div class="notRequireForm">
							<tyUniteDropMenu ref="uniteDropMenu">
							</tyUniteDropMenu>
							<div class="clear"></div>
							<iFormitem label="具体地址">
								<iInput v-model="requireFormValidate.adressDetail"></iInput>
							</iFormitem>
							<iFormitem label="备注">
								<iInput v-model="requireFormValidate.remark" type="textarea" :autosize="{minRows: 5,maxRows: 8}"></iInput>
							</iFormitem>
						</div>
					</iForm>
				</template>
			</div>
			<div class="clear"></div>
		</div>
		<tyAvatarUpload ref="tyAvatarUpload" @finish="uploadFinishEvent" @sizeVerfyOut="sizeVerfyOut" :sizeVerify="sizeVerify"></tyAvatarUpload>
	</div>
</template>
<script>

import iInput from 'iview/src/components/input';
import iForm from 'iview/src/components/form';
import tyUniteDropMenu from 'components/tyUniteDropMenu';
import tyAvatarUpload from 'components/tyAvatarUpload';
const MAXSIZE = 1024 * 1024 * 2;
const MINSIZE = 1024 * 1024 * 2;



export default {
	components: {
		iInput,
		iForm,
		tyUniteDropMenu,
		tyAvatarUpload,
		'iFormitem': iForm.Item,
	},
	mounted() {
		//如果是修改客户信息的时候，则获取客户的原始数据
		var clientId = this.$router.apps[0]._route.query.clientId;
		var baseInfoParams = {
			id: clientId
		};
		//当clientId存在的时候，才会加载以下代码
		if (clientId) {
			this.$get(this.$api.clientInfo, baseInfoParams).then((result) => {
				this.requireFormValidate = result.data;
				this.$refs.uniteDropMenu.initData(
					this.requireFormValidate.provinceName,
					this.requireFormValidate.provinceId,
					this.requireFormValidate.cityId,
					this.requireFormValidate.cityName,
					this.requireFormValidate.regionId,
					this.requireFormValidate.regionName);
			}).catch((e) => {
				this.$refs.uniteDropMenu.initData('', '', '');
				e.message = e.message || '获取客户数据失败！';
			});
		} else {
			this.$refs.uniteDropMenu.initData('', '', '');
		}


	},
	data() {
		const validatePhone=(rule, value, callback)=>{
                if (value === '') {
                    callback(new Error('联系电话不能为空'));
					return
                } else {
                    if (!this.$formVerify.verifyPhone(value)) {
                        callback(new Error('联系电话格式不正确'));
						return
                    }
                    callback();
                }
            };
		return {
			//默认客户信息必填表单不让保存
			saveRequireFormValidate: false,
			bugFixChange: true,
			sizeVerify:{
                MIN_WIDTH:200,
                MIN_HEIGHT:160,
                MAX_WIDTH:600,
                MAX_HEIGHT:480,
			},
			error: require('assets/img/common/defaultUploadImg.png'),
			//客户信息必填表单
			requireFormValidate: {
				headPortrait: "",
				adressDetail: "",
				cityId: "",
				contactNumber: "",
				contacts: "",
				industry: "",
				mailAdress: "",
				name: "",
				provinceId: "",
				regionId: "",
				remark: "",
				subIndustry: ""
			},
			ruleValidate: {
				name: [
					{ required: true, message: '客户名称不能为空', trigger: 'blur' }
				],
				contacts: [
					{ required: true, message: '联系人不能为空', trigger: 'blur' }
				],
				mailAdress: [
					{ required: true, message: '邮箱不能为空', trigger: 'blur' }
				],
				adressDetail: [
					{ required: true, message: '详细地址不能为空', trigger: 'blur' }
				],
				//联系电话
				contactNumber: [
					{ required: true,validator:validatePhone,trigger: 'blur' },
				],
				//从属行业
				industry: [
					{ required: true, message: '从属行业不能为空', trigger: 'blur' }
				],
				//从属子行业
				subIndustry: [
					{ required: true, message: '从属子行业不能为空', trigger: 'blur' }
				]
			},
		}
	},
	methods: {
        sizeVerfyOut(){
          	this.$Notice.error({
					title: '错误',
					desc: "请上传不小于200*160,不大于600*480的图片"
			})
		},
		// 图片上传完成事件
		uploadFinishEvent(e) {
			if (e && e.code == 1) {
				this.$loading.close();
				this.$Notice.success({
					title: '提示',
					desc: '上传成功'
				})
				this.requireFormValidate.headPortrait = e.data.fileUrl;
			} else {
				this.$loading.close();
				e.message = e.message || '上传文件失败，请稍后重试！';
				this.$Notice.error({
					title: '错误',
					desc: e.message
				})
			}
		},
		fileChange(e) {
			this.bugFixChange = false;
			setTimeout(()=>{
				this.bugFixChange =true;
			},1)
			var filePathName = e.target.value;
			var loastIndex = filePathName.lastIndexOf('.');
			if (loastIndex == -1) {
				this.$Notice.error({
					title: '错误',
					desc: '请选择.png/.jpg格式的图片'
				})
				return;
			}
			if (e.target.files && e.target.files.length > 0) {
				var size = e.target.files[0].size;
				if (size <= 0) {
					this.$Notice.error({
						title: '错误',
						desc: '无效的文件!'
					})
					return;
				}
				if (size >= MAXSIZE) {
					this.$Notice.error({
						title: '错误',
						desc: '文件不能大于2M'
					})
					return;
				}
			}
			var suffix = filePathName.substring(loastIndex, filePathName.length).toLocaleUpperCase();
			if (suffix === '.PNG' || suffix === '.JPG' || suffix === '.JPEG'||suffix === '.BMP') {
				var file = e.target.files[0];
				var reader = new FileReader();
				reader.onload = (e) => {
					this.$refs.tyAvatarUpload.changeUpload(e.target.result);
				}
				reader.readAsDataURL(file);
			} else {
				this.$Notice.error({
					title: '错误',
					desc: '仅支持.png/.jpg/.jpeg/.bmp格式的图片'
				})
			}
		},
		//添加客户信息
		sureAddClient() {
			var clientId = this.$router.apps[0]._route.query.clientId;
			// this.requireFormValidate.provinceId = this.$refs.uniteDropMenu.firstDrop();
			// this.requireFormValidate.cityId = this.$refs.uniteDropMenu.secondDrop();
			// this.requireFormValidate.regionId = this.$refs.uniteDropMenu.thirdDrop();
			var areaObj = this.$refs.uniteDropMenu.getAreaData();
			//客户名称校验不通过
			if (this.$formVerify.verifyString(this.requireFormValidate.name)) {
				// this.$Message.error('客户名称不能为空！');
				this.$Message.error({
					content: '客户名称不能为空！'
				})
				return;
			}
			//联系人校验不通过
			if (this.$formVerify.verifyString(this.requireFormValidate.contacts)) {
				this.$Message.error({
					content: '联系人不能为空！'
				})
				return;
			}
			//从属行业校验不通过
			if (this.$formVerify.verifyString(this.requireFormValidate.industry)) {
				this.$Message.error({
					content: '请输入客户从属行业！'
				})
				return;
			}
			//联系电话校验不通过
			if (this.$formVerify.verifyString(this.requireFormValidate.contactNumber)) {
				this.$Message.error({
					content: '联系电话不能为空！'
				})
				return;
			}
			//联系电话校验不通过
			if (!this.$formVerify.verifyPhone(this.requireFormValidate.contactNumber)) {
				this.$Message.error({
					content: '联系电话格式有误，请重新输入！'
				})
				return;
			}


			if (this.$formVerify.verifyString(areaObj.p.name)) {
				this.$Message.error({
					content: '请选择省份'
				})
				return;
			}

			if (this.$formVerify.verifyString(areaObj.c.name)) {
				this.$Message.error({
					content: '请选择城市'
				})
				return;
			}

			if (this.$formVerify.verifyString(areaObj.a.name)) {
				this.$Message.error({
					content: '请选择地区'
				})
				return;
			}
			this.requireFormValidate.provinceId = areaObj.p.id;
			this.requireFormValidate.cityId = areaObj.c.id;
			this.requireFormValidate.regionId = areaObj.a.id;
			if (clientId) {
				this.$post(this.$api.updateClientInfo, this.requireFormValidate).then((result) => {
					this.$Notice.success({
						title: '提示',
						desc: '修改客戶信息成功！'
					});
					this.$router.go(-1);
				}).catch((e) => {
					e.message = e.message || '操作失败，请稍后再试试！';
					this.$Notice.error({
						title: '提示',
						desc: e.message
					})
				});
				return;
			} else {
				this.$post(this.$api.addClient, this.requireFormValidate).then((result) => {
					this.$Notice.success({
						title: '提示',
						desc: '添加客戶成功！'
					});
					this.$router.push({ name: 'clientManager' });
				}).catch((e) => {
					e.message = e.message || '操作失败，请稍后再试试！';
					this.$Notice.error({
						title: '提示',
						desc: e.message
					})
				});
				return;
			}
		}
	},
}
</script>