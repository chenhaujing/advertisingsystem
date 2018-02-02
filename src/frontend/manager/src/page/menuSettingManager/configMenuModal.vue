<template>
    <iModal class="baseModal configMenuModal hideModal" :loading="true" title="配置目录权限" v-model="modal" :mask-closable="true" :width="900">
        <iRow class="content">
            <iCol :span="5" class="left">
                <a class="ruleItem" v-for="(r,index) in allRole" :key="r.id" v-text="r.roleName" :class="{'active': currSelectRoleIndex == index }" @click="selectRole(index)"></a>
            </iCol>
            <iCol :span="19" class="right">
                <div class="btnBox">
                    <iButton type="primary" size="large" class="btn" @click="saveMenuConfig">保存</iButton>
                    <iButton size="large" class="btn" @click="modal=false">取消</iButton>
                </div>
                <iTree class="zTree" :data="treeData" show-checkbox ref="menuTree">
                </iTree>
            </iCol>
        </iRow>
    </iModal>
</template>

<script>
import iTree from 'iview/src/components/tree';
import iButton from 'iview/src/components/button';
import { Row as iRow, Col as iCol } from 'iview/src/components/grid';
import iModal from 'iview/src/components/modal';
import ModalMixins from 'components/tyModal/baseModal';


export default {
    methods: {
        //  查询所有系统角色，
        getAllRoleAndMenu() {
            return this.$post(this.$api.getAllRoleAndMenuUrl).then((result) => {
                this.allRole = result.data;
            }).catch((e) => {

            })
        },
        // 查询所有系统菜单
        getAllSysMenu() {
            return this.$get(this.$api.getAllSysMenu).then((result) => {
                this.treeDataAdaprer(result);
                this.treeData = result.data;
                
            }).catch((e) => {
            
            })
        },
        saveMenuConfig() {
            var arr = [];
            function pushIdToarr(treeData){
            for(var i=0;i<treeData.length;i++){
                var flag=true;
            for(var j=0;j<treeData[i].children.length;j++){
                if(!treeData[i].children.length){
                    break;
                }
                if(treeData[i].children[j].checked&&flag){
                    arr.push(treeData[i].id)
                    flag=false;
                }
                if(treeData[i].children[j].checked){
                  arr.push(treeData[i].children[j].id)
                }
            }
            }
        };
        pushIdToarr(this.treeData)
            this.formData = {
                ids: arr,
                roleId: this.allRole[this.currSelectRoleIndex].id
            }
            this.$post(this.$api.distributeRole, this.formData).then((result) => {
                this.$Message.success("分配成功")
                this.modal = false
            }).catch((error) => {
                this.$Message.success({
                    content: error.message || '分配失败'
                })
            })
        },
        resetData() {
            Promise.all([this.getAllSysMenu(), this.getAllRoleAndMenu()]).then(value => {
                this.selectRole(0)
            })
        },
        selectRole(index) {
            this.currSelectRoleIndex = index;
            this.$post(this.$api.getMenuPower, {}, {}, {
                roleId: this.allRole[this.currSelectRoleIndex].id
            }).then(result => {
                this.treeDataAdaprer(result);
                //  console.log(result.data)
                this.treeData = result.data;
                //  console.log(this.treeData)
            }).catch(error => {
                this.$Message.error({
                    content: error.message
                })
            })
        },
        // 找出当前选定角色已拥有的菜单
        treeDataAdaprer(obj) {
            //判断第一次进入
               var children=obj.data||obj.children;

            
            if (!children || children.length == 0) {
                this.flag=true;
                return;
            }
            for (let i = 0; i < children.length; i++) {
                if(!children[i].checked){
                 obj.checked=false;
                }
                children[i].title = children[i].menuName;
                if (children[i].children) {
                    this.treeDataAdaprer(children[i]);
                }
            }
        },
    },
    mixins: [ModalMixins],
    data() {
        return {
            currSelectRoleIndex: 0,
            allRole: [],
            treeData: [],
            modal: false,
            formData: {
                ids: [],
                roleId: ''
            }
        }
    },
    watch: {
        modal(val) {
            if (val) {
                this.resetData();
            }
        }
    },
    components: {
        iTree,
        iRow,
        iCol,
        iModal,
        iButton
    }
}
</script>

<style lang="scss" scoped>
@import '~components/tyModal/modalStyle.scss';
.content {
    height: 600px;
}
.btnBox {
    width: 100%;
    text-align: right;
    height: 80px;
    line-height: 80px;
    border-bottom: 1px solid #ccc;
    .btn {
        margin: 0 10px;
    }
}

.left {
    height: 100%;
    border-right: 1px solid #e0e0e0;
    overflow: auto;
    a {
        width: 100%;
        display: block;
        text-align: center;
        line-height: 50px;
        height: 50px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .active {
        background-color: #dcdee0;
    }
}

.ruleItem {
    font-size: 16px;
    color: #333333;
}
.right{
    position: relative;
    height: 100%;
    overflow: hidden;
}
.zTree {
    padding-left: 50px;
    padding-top: 30px;
    height: 80%;
    overflow: auto;
}
</style>
<style lang="scss">
.configMenuModal {
    .ivu-icon-arrow-right-b:before {
        font-size: 25px;
    }
    .ivu-checkbox-inner {
        width: 20px;
        height: 20px;
        top: -5px;
        left: 2px;
    }
    .ivu-checkbox-checked .ivu-checkbox-inner:after {
        top: 4px;
        left: 6px;
    }
    .ivu-tree-title {
        vertical-align: super;
        font-size: 16px;
        color: #666666;
        line-height: 22px;
        padding: 0;
    }
}
</style>
