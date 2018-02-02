<template>
<div v-if="showFlag">
    <iModal class="baseModal btnModal" :title="modalTitle" v-model="showFlag" :mask-closable="true" :width="850" @on-ok="okEvent">
        <iForm ref="iFrom" :model="menuFormData" :rules="rules" class="modalContent">
            <div class="row">
            <iFormitem label="目录名称" prop="menuName" class="baseInfo-formItem col ">
                <iInput v-model="menuFormData.menuName" placeholder="请输入目录名称"></iInput>
            </iFormitem>
            <iFormitem label="从属根目录" prop="parentId" class="baseInfo-formItem col ">
                <iSelect v-model="menuFormData.parentId">
                    <iOption v-for="item in supperMenu" :value="item.value" :key="item.value">{{ item.label }}</iOption>
                </iSelect>
            </iFormitem>
            </div>
            <iFormitem label="Url链接" prop="url" class="baseInfo-formItem">
                <iInput v-model="menuFormData.url" placeholder="请输入Url链接"></iInput>
            </iFormitem>
        </iForm>
        <div class="buttonTools" slot="footer">
            <iButton class="buttonTools_finish" :loading="finishLoading" @click="finish">{{finishBtnText}}
            </iButton>
            <iButton class="buttonTools_cancel" @click="close">取消</iButton>
        </div>
    </iModal>
</div>    
</template>

<script>
import { Select as iSelect, Option as iOption } from 'iview/src/components/select';
import iInput from 'iview/src/components/input';
import iForm from 'iview/src/components/form';
import iModal from 'iview/src/components/modal';
import ModalMixins from 'components/tyModal/baseModal';
export default {
    data() {
        return {
            rules: {
                menuName: [{ required: true, message: '请填写目录名称', trigger: 'blur' }],
                url: [{ required: true, message: '请填写路由链接', trigger: 'blur' }]
            },
            supperMenu: [{
                value: '0',
                label: '根目录'
            }],
            loading: true,
            rule: [],
            menuFormData: {
                parentId: '0',
                menuName: '',
                url: ''
            },
            modalTitle: '添加目录'
        }
    },
    watch: {
        showFlag() {
            if (this.showFlag) {
                this.$get(this.$api.getAllSysMenu).then((result) => {
                    // 只需找出一级菜单
                    if (result.data) {
                        for (let i = 0; i < result.data.length; i++) {
                            this.supperMenu.push({
                                value: result.data[i].id,
                                label: result.data[i].menuName
                            })
                        }
                    }
                }).catch((e) => {
                    this.$Message.error(e.message);
                })
            } else {
                this.supperMenu = [{
                    value: '0',
                    label: '根目录'
                }],
                    this.menuFormData = {
                        parentId: '0',
                        menuName: '',
                        url: ''
                    }
            }
        }
    },
    mixins: [ModalMixins],
    methods: {
        finish() {
            if (this.menuFormData.id) {
                var url = this.$api.updateSysMenu;
            } else {
                var url = this.$api.addSysMenu;
            }
            this.$refs.iFrom.validate((flag) => {
                this.finishLoading = true;
                // 表单校验通过
                if (flag) {
                    this.$post(url, this.menuFormData).then(() => {
                        this.$Message.success("操作成功")
                        this.$emit('refreshTable');
                        this.resetLoading();
                        this.showFlag = false;
                    }).catch((e) => {
                        this.resetLoading();
                        this.$Message.error(e.message);
                    })
                } else {
                    this.resetLoading();
                }
            })
        }
    },
    components: {
        iSelect,
        iOption,
        'iFormitem': iForm.Item,
        iModal,
        iInput,
        iForm
    }
}
</script>

<style lang="scss" scoped>
.modalContent {
    width: 70%;
    padding: 30px;
    margin: 0 auto;
}
.row{
    position: relative;
    z-index: 1;
}
.col{
    float:left;
    width: 50%;
}
.baseInfo-formItem{
    padding: 20px;
}
</style>

