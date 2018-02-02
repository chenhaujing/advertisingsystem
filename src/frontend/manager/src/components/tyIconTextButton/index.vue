<template>
  <a class="iconfont iconTextButton" v-text="text"
  :class="[classGenerate,{'disableClass':disable}]" 
  ></a>
</template>

<script>
export default {
    name: 'icon-button',
    props: ['text','iconClass','disable'],
    computed: {
        classGenerate(){
            return this.iconClass;
        }
    }
}
</script>

<style scoped>
.iconTextButton{
    font-size: 14px;
    margin-right: 5px;
    line-height: 14px;
    color: #495060;
}
.disableClass{
    color: #ccc;
}
 .iconTextButton:before{
    margin-right: 5px;
}

</style>
