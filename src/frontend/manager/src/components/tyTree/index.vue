<template>
     <iTree class="zTree" :data="baseData"></iTree>
</template>

<script>
import iTree from 'iview/src/components/tree';
export default{
    data () {
            return {
                 baseData: [{
                title: '华南地区华南地区华南地区华南地区华南地区',
                children: [{
                    title: '深圳广告团队',
                    children: [{
                        title: '福田团队',
                    }, {
                        title: '南山团队',
                    }]
                }, {
                    title: '广州广告团队',
                    children: [{
                        title: '<span style="color: red">leaf</span>',
                    }]
                }]
            }],
            }
        },
        components:{
            iTree
        }
}
</script>

<style>
.zTree>.ivu-tree-children>li>span i{
   font-family: "iconfont" !important;
    font-size: 16px;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
}
.zTree>.ivu-tree-children>li>span i::before{
     content: '\E619';
}
</style>