<template>
    <iModal class="baseModal selectStoreModal hideModal" :title="title" v-model="showFlag" :mask-closable="false" :width="850">
        <iForm class="content">
            <iFormitem :label="label" prop="remark">
                <iInput :maxlength="200" size="large" :rows="10" v-model="remark" type="textarea"></iInput>
            </iFormitem>
        </iForm>
        <div class="buttonLine" v-if="mode == auditMode">
            <iButton @click.native="audit(true)" :loading="auditLoading">审核通过</iButton>
            <iButton @click.native="audit(false)" :loading="auditLoading">驳回</iButton>
        </div>
        <div class="buttonLine" v-if="mode == signMode">
            <iButton @click.native="sign(true)" :loading="auditLoading">签约成功</iButton>
            <iButton @click.native="sign(false)" :loading="auditLoading">签约失败</iButton>
        </div>
        <div class="buttonLine" v-if="mode == overMode">
            <div class="clearfix">
            <iButton @click.native="over()" :loading="auditLoading">终止合同</iButton>
            <iButton @click.native="showFlag = false" :loading="auditLoading">取消</iButton>
            </div>
            <div class="tipTitle clearfix" v-show="showWarn">
                                <i class="iconfont icon-jinggao"></i>
            <span>终止执行将会自动下架已关联的广告,请谨慎操作</span>
            </div>
        </div>

    </iModal>
</template>

<script>
import ContractState from './contractState';
import iButton from 'iview/src/components/button';
import iForm from 'iview/src/components/form';
import iInput from 'iview/src/components/input';
import ModalMixins from 'components/tyModal/baseModal';
import iModal from 'iview/src/components/modal';
export default {
    mixins: [ModalMixins],
    data() {
        return {
            auditMode: 1,
            signMode: 2,
            overMode: 3,
            mode: 1,
            contractId: '',
            auditLoading: false,
            remark: '',
            label: '审核说明',
            title: '合同审核',
            showWarn:false
        }
    },
    watch: {
        showFlag() {
            if(!this.showFlag) {
                this.remark = '';
                this.auditLoading = false;
            }
            if(this.showFlag&&this.mode==this.overMode){
            this.$get(this.$api.hasUnDeliveryAdvertisement,{contractId:this.contractId}).then(result=>{
                this.showWarn=result.data;
            })
        }
        }
    },
    methods: {
        setParams(contractId, mode) {
            this.mode = mode;
            this.contractId = contractId;
            if (this.mode == this.auditMode) {
                this.label = '审核说明';
                this.title = '合同审核';
            }
            if (this.mode == this.signMode) {
                this.label = '签约备注';
                this.title = '合同签约';
            }
            if (this.mode == this.overMode) {
                this.label = '终止原因';
                this.title = '终止合同';
            }
        },
        over() {
            if (!this.remark || this.remark.trim().length == 0) {
                this.$Notice.error({
                    title: '错误',
                    desc: '请输入终止合同原因备注'
                })
                return;
            }
            this.auditLoading = true;
            this.$post(this.$api.optionContranctUrl, {
                "contractId": this.contractId,
                "operation": ContractState.OptionStatus.run,
                "remark": this.remark,
                "successed": false
            }).then(() => {
                this.auditLoading = false;
                this.$Notice.success({
                    title: '提示',
                    desc: '操作成功'
                })
                this.$emit('optionSuccess');
                this.showFlag = false;
            }).catch((e) => {
                this.auditLoading = false;
                this.$Notice.error({
                    title: '提示',
                    desc: e.message
                })
            })
        },
        sign(flag) {
            if (!flag) {
                if (!this.remark || this.remark.trim().length == 0) {
                    this.$Notice.error({
                        title: '错误',
                        desc: '请输入合同签约失败原因'
                    })
                    return;
                }
            }
            this.auditLoading = true;
            this.$post(this.$api.optionContranctUrl, {
                "contractId": this.contractId,
                "operation": ContractState.OptionStatus.sign,
                "remark": this.remark,
                "successed": flag
            }).then(() => {
                this.auditLoading = false;
                this.$Notice.success({
                    title: '提示',
                    desc: '操作成功'
                })
                this.$emit('optionSuccess');
                this.showFlag = false;
            }).catch((e) => {
                this.auditLoading = false;
                this.$Notice.error({
                    title: '提示',
                    desc: e.message
                })
            })
        },
        audit(flag) {
            if (!flag) {
                if (!this.remark || this.remark.trim().length == 0) {
                    this.$Notice.error({
                        title: '错误',
                        desc: '请输入驳回该合同说明内容'
                    })
                    return;
                }
            }

            this.auditLoading = true;
            this.$post(this.$api.optionContranctUrl, {
                "contractId": this.contractId,
                "operation": ContractState.OptionStatus.audit,
                "remark": this.remark,
                "successed": flag
            }).then(() => {
                this.auditLoading = false;
                if (flag) {
                    this.$Notice.success({
                        title: '提示',
                        desc: '审核已通过'
                    })
                } else {
                    this.$Notice.warning({
                        title: '提示',
                        desc: '已驳回'
                    })
                }


                this.$emit('optionSuccess');
                this.showFlag = false;
            }).catch((e) => {
                this.auditLoading = false;
                this.$Notice.error({
                    title: '提示',
                    desc: e.message
                })
            })
        }
    },
    components: {
        iButton,
        iInput,
        iForm,
        'iFormitem': iForm.Item,
        iModal
    }
}
</script>
<style  lang="scss" scoped>
.content {
    padding: 30px;
    background: #edf1f4;
}
.buttonLine {
    margin: 0 auto;
    height: 127px;
    width: 360px;
    overflow: hidden;
    .clearfix{
        overflow: hidden;
    }
    .tipTitle{
        text-align: center;
        line-height: 60px;
        font-size: 14px;
        i{
            color: #fcb322;
        }
    }
    button {
        &:nth-of-type(1) {
            color: #ffffff;
            font-size: 16px;
            background-color: #7edd9c;
            width: 120px;
            float: left;
            &:hover {
                border-color: #7edd9c;
            }
        }

        &:nth-of-type(2) {
            font-size: 16px;
            color: #ffffff;
            background-color: #f9857d;
            width: 120px;
            float: right;
            &:hover {
                border-color: #f9857d;
            }
        }
    }
}
.showWarn{
    text-align: center;
    font-size: 14px;
}
</style>

<style lang="scss" >
@import '~components/tyModal/modalStyle.scss';
.selectStoreModal{
.ivu-modal-body{
    background: #edf1f4;
}
}

</style>
