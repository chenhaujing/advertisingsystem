<style  lang="scss" scoped>
.selFodder{
    .content{
        height:420px;
        background: #fff;
    }
.left{
    border-right: 1px solid #dcdee0;
    height: 100%;
      .sel{
          height: 50px;
          line-height: 50px;
          font-size: 16px;
          text-align: center;
          cursor: pointer;
      }  
}
.tipTitle{
    float: right;
    line-height: 40px;
     margin-right: 20px;
     font-size: 14px;
     color: #adadad;
    i{
        color: #fcb322;
        padding-right: 5px;
    }
}
.updateBtnBox{
    float: right;
    position: relative;
   width: 120px;
        height: 40px;
        margin-right: 20px;
    input{
        position: absolute;
        width: 120px;
        height: 40px;
        left: 0;
        top:0;
        opacity:0;
        cursor: pointer;
    }
}
.updateBtn {
    padding: 0;
    font-size: 16px;
    text-align: center;
    line-height: 36px;
    width: 120px;
    height: 40px;
    margin-right: 20px;
    border-radius: 4px;
    cursor: pointer;
}
.right-top{
    padding: 10px 0;
    border-bottom: 1px solid #dcdee0; 
}
.right-content{
    text-align: center;
    padding:0 10px; 
    .imgRow{
        height: 280px;
        .imgCol{
         position: relative;
         padding:20px 10px 0 10px;
         overflow: hidden;
         cursor: pointer;
         .box{
            overflow: hidden; 
         }
          .checkFodder,.playIcon{
            width: 152px;
            height: 92px;
            background-color: rgba(0,0,0,0.6);
            position: absolute;
            left:11px;
            top:20px;
             i{
                 color: #fff;
                 line-height: 92px;
                 text-align: center;
                 font-size: 40px;
             }
            .playIcon{
                 background-color: rgba(0,0,0,0.3);
            }
            }
        img{
            width: 148px;
            height: 92px;
        }
        video{
            height: 92px;
        }
        span{
    display: block;
    padding: 0 20px;
    overflow: hidden; /*自动隐藏文字*/
    text-overflow: ellipsis;/*文字隐藏后添加省略号*/
    white-space: nowrap;/*强制不换行*/
/*不允许出现半汉字截断*/
        }
        }
        
       
    }
}
.active{
    background:#dcdee0; 
}
}
.iPage{
    float:right;
    margin-top: 30px;
    margin-right:10px;
    
}
.buttonTools_finish{
    background: #4cabe0;
    background: #fff;
}
.buttonTools_finish[disabled="disabled"]{
    background:#f7f7f7;
    color:#495060;
}
.noDataText{
    font-size: 16px;
    line-height: 280px;
}
</style>
<style>
.selFodder .ivu-table-wrapper{
    border: none;
}
</style>
<template>
	<iModal class="selFodder baseModal" title="选择广告素材" v-model="showFlag" :mask-closable="false" :width="850">
	    <iRow class="content">
            <iCol span="4" class="left">
                <div class="sel" :class="{active:tab==1}" @click="changeTab(1)">{{"图片("+totalNum[1]+")"}}</div>
                <div class="sel" :class="{active:tab==3}" @click="changeTab(2)">{{"视频("+totalNum[3]+")"}}</div>
                <div class="sel" :class="{active:tab==2}" @click="changeTab(3)">{{"文本("+totalNum[2]+")"}}</div>
                </iCol>    
            <iCol span="20" v-show="tab==1">
                <iRow class="right-top">
                     <label class="updateBtnBox" for="updataInput">
                            <iButton type="primary" class="updateBtn" >本地上传
                                 
                            </iButton>
                            <input type="file" id="updataInput" @change="updateImg" accept=".png,.jpg,.jpeg,.bmp" value="updataInput" />
                    </label>
                    <div class="tipTitle">                  
                    <i class="iconfont icon-jinggao"></i>支持png/jpg/jpeg/bmp图片格式</div>           
                </iRow>
                <iRow class="right-content">
                    <iRow class="imgRow">
                     <p class="noDataText" v-show="!dataList||!dataList.length">该广告客户暂无图片素材</p>   
                    <iCol span="6" class="imgCol" v-for="(data,index) in dataList" :key="index" @click.native="checkFodder(index,data)">
                        <div v-show="checkFodderDataIndex===index" class="checkFodder"><i class="iconfont icon-gou"></i></div>    
                        <img :src="data.data" alt="">
                        <span>{{data.fileName}}</span>
                    </iCol>
                    </iRow>
                    <iPage v-if="showFlag" class="iPage" :total="total" :page-size="8" @on-change="changePage"></iPage>
                </iRow>        
                </iCol> 
                 <iCol span="20" v-show="tab==3">
                <iRow class="right-top">
                     <label class="updateBtnBox" for="updataInput">
                            <iButton type="primary" class="updateBtn">本地上传
                                
                            </iButton>
                            <input type="file"id="updataInput" @change="updateVideo" accept=".flv,.avi,.mp4,.mkv,.wmv" value="updataInput" />
                    </label>
                    <div class="tipTitle">
                    <i class="iconfont icon-jinggao"></i>支持flv/avi/mp4/mkv/wmv视频格式，文件大小不超过20M</div>    
                </iRow>
                <iRow class="right-content">
                    <iRow class="imgRow">
                        <p class="noDataText" v-show="!dataList||!dataList.length">该广告客户暂无视频素材</p>   
                   <iCol span="6" class="imgCol" v-for="(data,index) in dataList" :key="index" @click.native="checkFodder(index,data)">
                       <div class="box">
                         
                        <!-- <img src="../../assets/img/putAds/video.png" alt=""> -->
                        <div class="playIcon"><i class="iconfont icon-cplay1"></i></div>    
                        <div v-show="checkFodderDataIndex===index" class="checkFodder"><i class="iconfont icon-gou"></i></div>   
                        <video :src="data.data" alt=""></video>
                        <span>{{data.fileName}}</span>
                        </div>
                    </iCol>
                    </iRow>
                    <iPage v-if="showFlag" class="iPage" :total="total" :page-size="8" @on-change="changePage"></iPage>
                </iRow>        
                </iCol>
                <!-- <iCol span="20" v-show="tab==2">
                <tyTableView ref="textTable" :columns="textTableColumns" :url="textTableUrl" :height="350" :width="708" notDataText="暂无匹配的广告客户数据" :params="params"> </tyTableView>     
                </iCol>          -->
        </iRow>
        <div class="buttonTools" slot="footer">
			<iButton class="buttonTools_finish" :disabled="checkFodderDataIndex===''" :loading="finishLoading" @click="finish">确认</iButton>
			<iButton class="buttonTools_cancel" @click="close">取消</iButton>
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
import tyTableView from 'components/tyTableView';
import iPage from 'iview/src/components/page';

export default {
	components: {
		iModal,
		iForm,
		'iFormitem': iForm.Item,
        iInput,
        iRow,
        iButton,
        iPage,
        tyTableView,
       iCol
	},
	data() {
		return {
            active:"active",   
            total:0,
            checkFodderDataIndex:'',
            checkFodderData:'',
            totalNum:{
                "1":0,
                "2":0,
                "3":0,
            },
			sizeForm: {
				"highRatio": "",
				"horizontalResolution": "",
				"sizeName": "",
				"verticalResolution": "",
				"widthRatio": "",
			},
            url: this.$api.addSizeConfigUrl,
            textTableColumns:[
                { title: '编号', type: 'index', align: 'center',width:68},
                { title: '合同编号', key: 'contractCode', align: 'center',width:640 },
            ],
            dataList:[],
            baseParmas:{
                customerId:'',
                materialType:1,
                pageIndex:0,
                pageSize:8
            },
            parmas:{
                customerId:'',
                materialType:1,
                pageIndex:0,
                pageSize:8
            }
		}
	},
	watch: {
		showFlag(val) {
			if (val) {
                this.refresh();
                this.getAdsMaterialNum();
			} else {
			}
        },
    },
    created(){
         
    },
    mixins: [ModalMixins],
    props:["customerId","tab"],
	methods: {
        refresh(){
                this.clearData(this.parmas,this.baseParmas)
                this.checkFodderDataIndex='';
                this.parmas.customerId=this.customerId;
                this.parmas.materialType=this.tab;
                this.getFodder()
        },
        getAdsMaterialNum(){
                    console.log()                     
                    this.$get(this.$api.getAdsMaterialNum,{"customerId":this.customerId}).then(result=>{
                        result.data.forEach((v,i)=>{
                             this.totalNum[v["materialType"]]=v.totalMaterials
                        })
                    }).catch(e=>{
                        this.$Notice.error({
                       title:"错误",
                       desc:e.message||"操作失败"
                   })
                   })
        },
        getFodder(){
                 this.$post(this.$api.getMaterial,this.parmas).then(result=>{
                     this.dataList=result.data.list;
                     this.total=result.data.totalElement
                 }).catch(e=>{
                    this.$Notice.error({
                       title:"错误",
                       desc:e.message||"操作失败"
                   })
        })
        },
        checkFodder(index,data){
            if(this.checkFodderDataIndex===index){
                this.checkFodderDataIndex="";
                return
            }
              this.checkFodderDataIndex=index;
              this.checkFodderData=data
        },
        updateImg(e){
            this.$emit("updateImg",e)
        },
        updateVideo(e){
            this.$emit("updateVideo",e)
        },
        close(){
             this.toggle()
        },
        changePage(pageIndex){
             this.parmas.pageIndex=pageIndex-1;
             this.checkFodderDataIndex="";
             this.checkFodderData="";
             this.getFodder();
        },
		finish() {
            this.$emit("finishCheckFodder",this.checkFodderData)
            this.toggle();
		}
	},
}
</script>
