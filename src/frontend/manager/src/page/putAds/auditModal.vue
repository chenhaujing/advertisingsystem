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
            <iButton @click.native="over(false)" :loading="auditLoading">下架广告继续投放</iButton>
            <iButton @click.native="over(true)" :loading="auditLoading">合同广告投放完成</iButton>
        </div>
    </iModal>
</template>

<script>
import adState from './adState';
import iButton from 'iview/src/components/button';
import iForm from 'iview/src/components/form';
import iInput from 'iview/src/components/input';
import ModalMixins from 'components/tyModal/baseModal';
import iModal from 'iview/src/components/modal';
export default {
    mixins: [ModalMixins],
    data() {
        return {
            signMode: 1,
            auditMode: 2,
            overMode: 4,
            mode: 1,
            advertisementId: '',
            auditLoading: false,
            remark: '',
            label: '审核说明',
            title: '合同审核'
        }
    },
    watch: {
        showFlag() {
            if(!this.showFlag) {
                this.remark = '';
                this.auditLoading = false;
            }
        }
    },
    methods: {
        setParams(advertisementId, mode) {
            this.mode = mode;
            this.advertisementId = advertisementId;
            if (this.mode == this.auditMode) {
                this.label = '审核说明:';
                this.title = '广告审核';
            }
            if (this.mode == this.signMode) {
                this.label = '签约备注';
                this.title = '合同签约';
            }
            if (this.mode == this.overMode) {
                this.label = '广告下架';
                this.title = '下架原因';
            }
        },
        over(flag) {
            if (!this.remark || this.remark.trim().length == 0) {
                    this.$Notice.error({
                        title: '错误',
                        desc: '下架原因不能为空'
                    })
                    return;
                }
            this.auditLoading = true;
            this.$post(this.$api.adsOperate, {
                "advertisementId": this.advertisementId,
                "operation": adState.OptionStatus.over,
                "remark": this.remark,
                "successed": false,
                "finishContract":flag
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
                        desc: '审核说明不能为空'
                    })
                    return;
                }
            }
            this.auditLoading = true;
            this.$post(this.$api.adsOperate, {
                "advertisementId": this.advertisementId,
                "operation": adState.OptionStatus.audit,
                "remark": this.remark,
                "successed": flag,
                "finishContract":true
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
}

.buttonLine {
    margin: 0 auto;
    height: 127px;
    width: 480px;
    button {
        &:first-child {
            color: #ffffff;
            font-size: 16px;
            background-color: #7edd9c;
            float: left;
            &:hover {
                border-color: #7edd9c;
            }
        }

        &:last-child {
            font-size: 16px;
            color: #ffffff;
            background-color: #f9857d;
            float: right;
            &:hover {
                border-color: #f9857d;
            }
        }
    }
}
</style>

<style lang="scss" >
@import '~components/tyModal/modalStyle.scss';
</style>