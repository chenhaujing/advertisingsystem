<template>
    <div class="addPutAds">
        <div class="header">
            <iButton class="header-btns-cancel" @click="cancel">取消</iButton>
            <iButton class="header-btns-save" @click="submit">保存</iButton>
        </div>
        <div class="content">
            <h3 class="title">广告信息</h3>
            <div class="main">
                <iForm>
                    <iRow :gutter="300">
                        <iCol span="12">
                            <iFormitem label="广告名称">
                                <iInput v-model="formData.advertisementName" @on-keyup="$format.setString(formData,'advertisementName')" placeholder="请输入广告名称"></iInput>
                            </iFormitem>
                        </iCol>
                        <iCol span="12">
                            <iFormitem label="广告类型">
                                <iSelect v-model="formData.advertisementType" @on-change="changeType">
                                    <iOption v-for="data in adsTypeData" :key="data.value" :value="data.value">
                                        {{data.label}}
                                    </iOption>
                                </iSelect>
                            </iFormitem>
                        </iCol>
                    </iRow>
                    <!-- 图片 -->
                    <iRow v-show="formData.advertisementType==1">
                        <iCol span="18" class="btnGroup">
                            <div class="label">广告素材</div>
                            <label class="updateBtnBox" for="updataInput">
                                <iButton type="primary" class=" updateBtn">上传素材
                                    
                                </iButton>
                                <input type="file" @change="imgFileChange" id="updataInput" accept=".png,.jpg,.jpeg,.bmp" value="updataInput" />
                            </label>
                            <iButton type="success" class="updateBtn" @click.native="showSelFodderModal">选择素材</iButton>
                            <div class="tipTitle">
                                <i class="iconfont icon-jinggao"></i>
                                <span>{{ "广告位置："+contractInfo.sizeName+"\n广告尺寸：" }}</span>
                                <span v-show="contractInfo.horizontalResolution&&contractInfo.verticalResolution">{{contractInfo.horizontalResolution+"*"+contractInfo.verticalResolution}}</span>
                            </div>
                        </iCol>
                        <iCol span="6">
                            <div class="smallImg" @click="showBigFodder">
                                <img :src="fileUrl" v-show="fileUrl">
                                <i v-show="!fileUrl"></i>
                            </div>
                        </iCol>
                    </iRow>
                    <!-- 视频 -->
                    <iRow v-show="formData.advertisementType==3">
                        <iCol span="18" class="btnGroup">
                            <div class="label">广告素材</div>
                            <label class="updateBtnBox" for="updataInput">
                                <iButton type="primary" class=" updateBtn">上传素材
                                    
                                </iButton>
                                <input type="file" @change="videoFileChange" id="updataInput" accept=".flv,.avi,.mp4,.mkv,.wmv" value="updataInput" />
                            </label>
                            <iButton type="success" class="updateBtn" @click.native="showSelFodderModal">选择素材</iButton>
                            <div class="tipTitle">
                                <i class="iconfont icon-jinggao"></i>
                                <span>支持flv/avi/mp4/mkv/wmv视频格式，文件大小不超过20M</span>
                            </div>
                        </iCol>
                        <iCol span="6">
                            <div class="smallImg video" @click="showBigFodder">
                                <!-- <img src="../../assets/img/putAds/video.png" v-show="fileUrl"> -->
                                <video :src="fileUrl" v-show="fileUrl"></video>
                                <i v-show="!fileUrl"></i>
                                <div v-show="fileUrl" class="playIcon">
                                    <i class="iconfont icon-cplay1"></i>
                                </div>
                            </div>
                        </iCol>
                    </iRow>
                    <!-- 文本 -->
                    <iRow v-show="formData.advertisementType==2">
                        <iFormitem label="广告文本内容">
                            <!-- <iInput type="textarea" :rows="6" placeholder="请输入广告文本投放内容" v-model="formData.data" :maxlength="60"></iInput> -->
                            <tyTextarea :max=60 v-model="formData.data" ></tyTextarea>
                        </iFormitem>
                    </iRow>
                    <iRow class="dataGroup" :gutter="300">
                        <iCol span="12">
                            <iFormitem label="开始时间">
                                <iDatePicker type="datetime" :options="dateOption" @on-open-change="getHistory" placeholder="选择日期" v-model="formData.startTime" :editable="false" format="yyyy-MM-dd HH:mm"></iDatePicker>
                            </iFormitem>
                        </iCol>
                        <iCol span="12">
                            <iFormitem label="结束时间">
                                <iDatePicker type="datetime" :options="dateOption" @on-open-change="getHistory" placeholder="选择日期" v-model="formData.endTime" :editable="false" format="yyyy-MM-dd HH:mm"></iDatePicker>
                            </iFormitem>
                        </iCol>
                    </iRow>
                    <iRow v-show="contractHistoryData.length" class="history">
                         <div class="tipTitle">
                                <i class="iconfont icon-jinggao"></i>
                                <span>该合同已多次投放,有效周期数据如下:</span>
                            </div>
                        <iTable ref="contractTime" :columns="columns" :data="contractHistoryData"></iTable>
                    </iRow>
                    <iRow>
                        <iFormitem label="广告备注">
                            <iInput type="textarea" :rows="6" placeholder="请输入..." v-model="formData.remark" :maxlength="60"></iInput>
                        </iFormitem>
                    </iRow>
                </iForm>

            </div>
        </div>
        <div class="mescontent">
            <h3 class="title">合同广告信息</h3>
            <iRow class="main">
                <div>投放门店总数:
                    <span>{{contractInfo.totalStores}}</span>
                </div>
                <div>A类门店数量:
                    <span>{{contractInfo.storeACount}}</span>
                </div>
                <div>B类门店数量:
                    <span>{{contractInfo.storeBCount}}</span>
                </div>
                <div>C类门店数量:
                    <span>{{contractInfo.storeCCount}}</span>
                </div>
                <div>广告总价:
                    <span>{{$format.toKeepPoint(contractInfo.totalCost)}}</span>
                </div>
            </iRow>
            <iRow class="main">
                <div>投放广告位置:
                    <span>{{contractInfo.sizeName}}</span>
                </div>
                <div>广告尺寸:
                    <span v-show="contractInfo.horizontalResolution&&contractInfo.verticalResolution">{{contractInfo.horizontalResolution+"*"+contractInfo.verticalResolution}}</span>
                </div>
                <div>广告时长:
                    <span>{{contractInfo.duration+contractInfo.durationUnitName}}</span>
                </div>
                <div>广告展示次数:
                    <span>{{contractInfo.displayTimes+"次/"+contractInfo.timeUnittName}}</span>
                </div>
            </iRow>
        </div>
        <selFodderModal @updateVideo="videoFileChange" @updateImg="imgFileChange" @finishCheckFodder="finishCheckFodder" ref="selFodderModal" :customerId="$route.query.customerId" :tab="formData.advertisementType"> </selFodderModal>
        <showFodderModal ref="showFodderModal" :url="fileUrl" :type="formData.advertisementType"></showFodderModal>
    </div>
</template>
<script>
import iModal from 'iview/src/components/modal';
import iButton from 'iview/src/components/button';
import iInput from 'iview/src/components/input';
import iForm from 'iview/src/components/form';
import iTable from 'iview/src/components/table';
import iDatePicker from 'iview/src/components/date-picker';
import tySearchInput from 'components/tySearchInput';
import tyIconTextButton from 'components/tyIconTextButton';
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import selFodderModal from './selFodderModal.vue';
import showFodderModal from './showFodderModal.vue';
import ModalMixins from 'components/tyModal/baseModal';
import tyTextarea from 'components/tyTextarea';

const MAXSIZE = 1024 * 1024 * 20

export default {
    data() {
        return {
            adsTypeData: [
                {
                    label: "图片", value: 1
                },
                {
                    label: "文本", value: 2
                },
                {
                    label: "视频", value: 3
                },],
            editEnter: true,
            dateOption: {
                disabledDate(date) {
                    return date && date.valueOf() < Date.now() - 86400000;
                }
            },
            columns:[{ title: '合同名称', key: 'contractName', align: 'center', ellipsis: true },
               { title: '开始时间', key: 'startTime', align: 'center', ellipsis: true,width:"180"},
               { title: '结束时间', key: 'endTime', align: 'center', ellipsis: true,width:"180"},
               { title: '投放周期', key: 'advertisePeriod', align: 'center', ellipsis: true, render: (h, params) => {
					return h('div', {}, params.row.advertisePeriod+"个月")
					}},
               { title: '已投放次数', key: 'hasAdvertisedTimes', align: 'center', ellipsis: true ,render: (h, params) => {
					return h('div', {}, params.row.hasAdvertisedTimes+"次")
					}},
               { title: '已投放周期', key: 'hasAdvertisedPeriod', align: 'center', ellipsis: true,render: (h, params) => {
					return h('div', {}, params.row.hasAdvertisedPeriod+"天")
					}},
               { title: '有效周期', key: 'effectivePeriod', align: 'center', ellipsis: true,render: (h, params) => {
					return h('div', {}, params.row.effectivePeriod+"天")
					} },
               ],
            contractInfo: {
                totalStores: '',
                storeACount: '',
                storeBCount: '',
                storeCCount: '',
                totalCost: '',
                horizontalResolution: '',
                verticalResolution: '',
                duration: '',
                displayTimes: '',
                sizeName: ''
            },
            modal: false,
            fileUrl: '',
            imgFormData: {
                contractId: '',
                customerId: '',
            },
            contractHistoryData:[],
            formData: {
                advertisementName: '',
                advertisementType: 1,
                contractId: '',
                data: '',
                startTime: '',
                endTime: '',
                materialId: '',
                remark: '',
            }
        }
    },
    components: {
        iModal,
        tyIconTextButton,
        tySearchInput,
        iButton,
        iTable,
        iSelect,
        iOption,
        iRow,
        iCol,
        iInput,
        iForm,
        iFormitem: iForm.Item,
        iDatePicker,
        selFodderModal,
        showFodderModal,
        tyTextarea
    },
    mixins: {
        ModalMixins
    },
    watch: {

    },
    computed:{
    },
    methods: {
        showSelFodderModal() {
            this.$refs.selFodderModal.toggle()
        },
        showBigFodder() {
            if (!this.fileUrl) {
                return
            }
            this.$refs.showFodderModal.toggle();
        },
        changeType() {
            if (this.editEnter) {
                this.editEnter = false;
                return;
            }
            this.fileUrl = '';
            // this.formData.data='';
        },
        finishCheckFodder(data) {
            this.formData.materialId = data.id;
            this.formData.advertisementType = data.materialType;
            this.fileUrl = data.data
        },
        imgFileChange(e) {
            var filePathName = e.target.value;
            var loastIndex = filePathName.lastIndexOf('.');
            if (loastIndex == -1) {
                this.$Notice.error({
                    title: '错误',
                    desc: '请选择.png/.jpg/.jpeg/.bmp格式的图片'
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
            }
            var suffix = filePathName.substring(loastIndex, filePathName.length).toLocaleUpperCase();
            if (suffix === '.PNG' || suffix === '.JPG' || suffix === '.JPEG' || suffix === '.BMP') {
                this.fileUpdate(e)
            } else {
                this.$Notice.error({
                    title: '错误',
                    desc: '仅支持.png/.jpg/.jpeg/.bmp格式的图片'
                })
            }
        },
        videoFileChange(e) {
            var filePathName = e.target.value;
            var loastIndex = filePathName.lastIndexOf('.');
            if (loastIndex == -1) {
                this.$Notice.error({
                    title: '错误',
                    desc: '请选择.flv/.avi/.mp4/.mkv/.wmv格式的视频'
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
                        desc: '文件不能大于20M'
                    })
                    return;
                }
            }
            var suffix = filePathName.substring(loastIndex, filePathName.length).toLocaleUpperCase();
            if (suffix === '.FLV' || suffix === '.AVI' || suffix === '.MP4' || suffix === '.MKV' || suffix === '.WMV') {
                this.fileUpdate(e)
            } else {
                this.$Notice.error({
                    title: '错误',
                    desc: '仅支持.flv/.avi/.mp4/.mkv/.wmv格式的视频'
                })
            }
        },
        fileUpdate(e) {
            var file = e.target.files[0];
            var reader = new FileReader();
            reader.onload = (e) => {
                let formData = new FormData();
                formData.append('file', file);
                this.$loading("上传中");
                this.$upload(
                    this.$api.updateMaterial + "?contractId=" + this.imgFormData.contractId + "&customerId=" + this.imgFormData.customerId + "&materialType=" + this.formData.advertisementType,
                    formData
                ).then((result) => {
                    this.$loading.close();
                    if (this.$refs.selFodderModal.showFlag) {
                        this.$refs.selFodderModal.refresh();
                    } else {
                        this.fileUrl = result.data.data;
                        this.formData.materialId = result.data.id;
                    }

                    this.$Notice.success({
                        title: '提示',
                        desc: '上传成功'
                    })
                }).catch((e) => {
                    this.$loading.close();
                    this.$Notice.error({
                        title: "错误",
                        desc: e.message || "操作失败"
                    })
                })
            }
            reader.readAsDataURL(file);
        },
        getHistory(flag){
                     this.$post(this.$api.advertisingHistory,{
                         contractId:this.$route.query.contractId,
                         startTime:this.formData.startTime?this.$dateFormat(new Date(this.formData.startTime), 'yyyy-MM-dd hh:mm:ss'):'',
                         endTime:this.formData.endTime?this.$dateFormat(new Date(this.formData.endTime), 'yyyy-MM-dd hh:mm:ss'):''}).then(result=>{
                         if(result.data){
                           this.contractHistoryData=[result.data];
                         }
                     })
        },
        submit() {
            if (!this.formData.advertisementName) {
                this.$Notice.error({
                    title: "错误",
                    desc: "请输入广告名称"
                })
                return
            }
            if (!this.formData.startTime) {
                this.$Notice.error({
                    title: "错误",
                    desc: "请选择有效的广告投放开始时间"
                })
                return
            }
            if (!this.formData.endTime) {
                this.$Notice.error({
                    title: "错误",
                    desc: "请选择有效的广告投放结束时间"
                })
                return
            }
            if (!this.formData.materialId && this.formData.advertisementType != 2) {
                this.$Notice.error({
                    title: "错误",
                    desc: "投放不能为空，请上传或选择广告素材"
                })
                return
            }
            if (this.formData.advertisementType == 2 && (!this.formData.data||!this.formData.data.trim())) {
                this.$Notice.error({
                    title: "错误",
                    desc: "请输入广告文本内容"
                })
                return
            }
            //判断是否是文本
            if (this.formData.advertisementType != 2) {
                this.formData.data = '';
            }
            //文本时清空素材Id
            if(this.formData.advertisementType == 2){
                this.formData.materialId='';
                this.fileUrl='';
            }
            //时间类型进行转换
            this.formData.startTime = this.$dateFormat(new Date(this.formData.startTime), 'yyyy-MM-dd hh:mm:ss');
            this.formData.endTime = this.$dateFormat(new Date(this.formData.endTime), 'yyyy-MM-dd hh:mm:ss');
            //合同Id
            this.formData.contractId = this.$route.query.contractId;

            var url = this.formData.id ? this.$api.updatePutAds : this.$api.addPutAds;
            this.$post(url, this.formData).then(result => {
                this.$Notice.success({
                    title: "成功",
                    desc: "保存成功"
                })
                this.$router.replace({
                    name: "adsStatus", query: {
                        contractId: this.$route.query.contractId,
                        customerId: this.$route.query.customerId,
                        id: this.$route.query.id || result.data,
                        type: "save"
                    }
                })
            }).catch(e => {
                this.$Notice.error({
                    title: "错误",
                    desc: e.message || "操作失败"
                })
            })
        },
        cancel() {
            this.$router.back()
        }
    },
    created() {
        if (this.$route.query.id) {
            this.$get(this.$api.getAdsEditInfo, {
                id: this.$route.query.id
            }).then(result => {
                this.formData = result.data;
                this.fileUrl = result.data.data;
                //判断文本
                if (result.data.advertisementType != 2) {
                    this.formData.data = '';
                }
            }).catch(e => {
                this.$Notice.error({
                    title: "错误",
                    desc: e.message || "操作失败"
                })
            })
        }
        //初始化获得合同Id和顾客Id
        this.imgFormData.contractId = this.$route.query.contractId;
        this.imgFormData.customerId = this.$route.query.customerId;
        //初始请求详情
        this.$get(this.$api.getPutAdsContractInfo, { contractId: this.$route.query.contractId }).then(result => {
            this.contractInfo = result.data;
        })
    }
}
</script>
<style lang="scss" scoped>
@import '~assets/css/base.scss';

%btnStyle {
    padding: 0;
    font-size: 16px;
    text-align: center;
    line-height: 34px;
    float: right;
    margin-right: 20px;
    display: block;
    color: #ffffff;
    width: 120px;
    height: 34px;
    border-radius: 5px;
}

.updateBtnBox {
    position: relative;
    width: 140px;
    height: 36px;
    input {
        position: absolute;
        width: 140px;
        height: 36px;
        left: 0;
        opacity: 0;
        cursor: pointer;
    }
}

.updateBtn {
    padding: 0;
    font-size: 16px;
    text-align: center;
    line-height: 36px;
    width: 140px;
    height: 36px;
    margin-right: 20px;
    border-radius: 0px;
    cursor: pointer;
}

.header {
    overflow: hidden;
    margin-bottom: 20px;
}

.header-btns-save {
    @extend %btnStyle;
    background-color: $mainColor;
}

.header-btns-cancel {
    @extend %btnStyle;
    background-color: #dcdee0;
    color: #999;
}

.title {
    font-size: 18px;
    font-weight: 400;
    line-height: 78px;
    margin-left: 30px;
}

.content {
    // height: 560px;
    width: 100%;
    background: #fff;
    font-size: 14px;
     .tipTitle {
                display: inline-block;
                line-height: 36px; //               overflow: hidden; /*自动隐藏文字*/
                // text-overflow: ellipsis;/*文字隐藏后添加省略号*/
                // white-space: nowrap;/*强制不换行*/
                i {
                    color: #fcb322;
                    display: inline-block;
                    padding: 0 5px;
                }
      }
    .main {
        padding: 0 60px;
        .history{
            margin-bottom: 20px;
        }
        .label {
            font-size: 16px!important;
            color: #666666!important;
            line-height: 40px;
        }
        .btnGroup {
            position: relative;
            overflow: hidden;
            margin-bottom: 20px;
        }
        .smallImg {
            float: right;
            margin-top: 10px;
            position: relative;
            cursor: pointer;
            &.video {
                overflow: hidden;
                &>video {
                    height: 64px;
                }
                &>i {
                    background-image: url('~assets/img/putAds/video.png');
                }
            }
            &>i,
            &>img {
                display: block;
                width: 110px;
                height: 64px;
            }
            &>i {
                background-size: 100% 100%;
                background-image: url('~assets/img/putAds/bg.png');
            }
            .playIcon {
                width: 100%;
                height: 64px;
                background-color: rgba(0, 0, 0, 0.3);
                text-align: center;
                position: absolute;
                top: 0;
                left: 0;
                i {
                    line-height: 64px;
                    font-size: 30px;
                    color: #fff;
                }
            }
        }
    }
}

.mescontent {
    height: 166px;
    margin-top: 30px;
    background: #fff;
    .main {
        padding: 0 60px;
        div {
            padding-right: 10px;
            width: 20%;
            float: left;
            font-size: 16px;
            margin-bottom: 20px;
            color: #999;
            span {
                color: #000;
            }
        }
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
</style>
<style lang="scss">
.addPutAds .ivu-form .ivu-form-item-label {
    font-size: 16px!important;
    color: #666666!important;
}

.addPutAds {
    overflow-x: hidden;
    .ivu-input-icon {
        display: none;
    }
    .dataGroup {
        .ivu-form-item-label {
            float: none;
            line-height: 40px;
        }
        .ivu-date-picker {
            width: 100%;
        }
    }
    .ivu-picker-panel-content .ivu-picker-panel-content .ivu-time-picker-cells-list ul li {
        padding: 0 0 0 100px;
    }
    .ivu-picker-panel-content .ivu-picker-panel-content .ivu-time-picker-cells-list {
        width: 216px;
        max-height: 216px;
    }
    .ivu-picker-panel-content .ivu-picker-panel-content .ivu-time-picker-cells-list ul {
        padding: 0 0 0 0;
    }
    .ivu-time-picker-cells-list ul {
        width: 100%;
        margin: 0;
        padding: 0 0 0 0;
        list-style: none;
    }
    .ivu-time-picker-cells-list:nth-child(2) {
        display: none;
    }
}
</style>
