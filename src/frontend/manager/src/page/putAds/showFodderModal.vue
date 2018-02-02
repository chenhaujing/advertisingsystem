<style  lang="scss">
.textBOX {
				.text {
					background-color: #ffffff;
					padding: 20px;
					height: 100%;
					text-align: left;
				}
}
.openImg {
	.ivu-modal-footer{
		display: block !important;
		background: #fff;
	}
}
.showFodderModal {
	.ivu-modal-header-inner {
		text-align: left;
	}
	.ivu-modal-footer{
		display: none;
	}
	.ivu-modal-wrap {
		text-align: center;
	}
	.ivu-modal {
		display: inline-block;
		width: auto !important;
		height:500px;
		margin:0 auto; // margin: 0 0;
		margin-bottom: 200px;
	}
	.ivu-modal-content {
		text-align: center;
				width: 850px;
				min-height: 500px;
				position: relative;
				.ivu-modal-body{
					height: 90%;
				}

		 img,video{
					max-width: 80%;
					// max-height: 80%;
					position: relative;
					margin: 30px auto;
			}
	}
}
</style>
<template>
	<iModal class="baseModal showFodderModal" :class="[{'textBOX': type==2},{'openImg': type==1}]" :title="title" v-model="showFlag" :width="850">
		<!-- <div  slot="close">
			</div> -->
		<img :src="url" v-if="type==1">
		<div v-if="type==2" class="text">{{url}}</div>
		<video controls :src="url" v-if="type==3&&showFlag"></video>
		<div class="buttonTools" slot="footer" v-if="type==1">
			<iButton class="buttonTools_finish" :loading="finishLoading" @click="openImg">查看原图</iButton>
		</div>
	</iModal>
	
</template>
<script>
import iModal from 'iview/src/components/modal';
import iForm from 'iview/src/components/form';
import iButton from 'iview/src/components/button';
import iInput from 'iview/src/components/input';
import ModalMixins from 'components/tyModal/baseModal';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';

export default {
	components: {
		iModal,
		iForm,
		'iFormitem': iForm.Item,
		iInput,
		iRow,
		iButton,
		iCol
	},
	props: ["url", "type"],
	data() {

		return {
			title: ''
		}
	},
	watch: {
		showFlag(val) {
			if (val) {

			} else {

			}
		},
	},
	updated() {
		if (this.type == 1) {
			this.title = '查看广告图片';
		} else if (this.type == 2) {
			this.title = '查看广告文本';
		} else if (this.type == 3) {
			this.title = '查看广告视频';
		}
	},
	mounted() {


	},
	methods:{
        openImg(){
			window.open(this.url)
		}
	},
	mixins: [ModalMixins],
}
</script>
