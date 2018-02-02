<template>
    <iModal v-model="show" width="800" @on-ok="okCallback">
        <div class="cropper">
            <vueCropper 
            ref="cropper"
	:img="example2.img"
	:outputSize="example2.size"
	:outputType="example2.outputType"
	:info="example2.info"
	:canScale="example2.canScale"
	:autoCrop="example2.autoCrop"
	:autoCropWidth="example2.width"
	:autoCropHeight="example2.height"
	:fixed="example2.fixed"
	:fixedNumber="example2.fixedNumber"
></vueCropper>
</div>
    </iModal>
</template>

<script>
import iModal from 'iview/src/components/modal';
import vueCropper from 'vue-cropper';

function readBlobAsDataURL(blob, callback) {
    var a = new FileReader();
    a.onload = function(e) { callback(e.target.result); };
    a.readAsDataURL(blob);
}

export default {
    components: {
        iModal,
        vueCropper
    },
    data() {
        return {
            previews: {},
            show: false,
            example2: {
                width: 220,
                height: 180,
				info: true,
				size: 1,
				outputType: 'jpeg',
				canScale: true,
				autoCrop: true,
				// 只有自动截图开启 宽度高度才生效
				// autoCropWidth: 300,
				// autoCropHeight: 250,
				// 开启宽度和高度比例
				// fixed: true,
				// fixedNumber: [4, 2]
            }

        }
    },
    props: ['sizeVerify'],
    methods: {
        okCallback() {
            this.$refs.cropper.getCropBlob((data) => {
                readBlobAsDataURL(data, dataurl => {
                    this.verify(this.sizeVerify, dataurl, flag => {
                        if (!flag) {
                            this.$emit("sizeVerfyOut")
                            return
                        } else {
                            this.$loading("请稍后...")
                            let formData = new FormData();
                            formData.append('file', data);
                            this.$upload(
                                this.$api.imageUploadUrl,
                                formData
                            ).then((result) => {
                                this.$emit('finish', result);
                            }).catch((e) => {
                                this.$emit('finish', e);
                            })
                        }
                    })
                });

            })
        },
        verify(obj, data, fn) {
            var flag = false;
            var img = new Image();
            img.onload = function() {
                if (obj.MAX_WIDTH > img.width && obj.MIN_WIDTH < img.width && obj.MAX_HEIGHT > img.height && obj.MIN_HEIGHT < img.height) {
                    flag = true
                }
                if (fn) {
                    fn(flag)
                }
            }
            img.src = data
        },
        realTime(data) {
            this.previews = data
        },
        changeUpload(file) {
            this.show = !this.show;
            this.example2.img = file;
        }
    }
}
</script>

<style>
.show-preview {
    width: 65px;
    height: 65px;
    overflow: hidden;
}

.show-preview img {
    max-width: 100%;
    max-height: 100%;
}

.cropper {
    height: 560px;
}
</style>
