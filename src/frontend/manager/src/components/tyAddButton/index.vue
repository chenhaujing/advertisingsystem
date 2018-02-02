<template>
    <iButton type="ghost" :icon="icon" class="addButton" >
        {{text}}
        </iButton>
</template>

<script>
import  iButton  from 'iview/src/components/button';
export default {
    components: { 
        iButton
    },
    created() {
    },
    methods: {
        hideIcon() {
            this.icon = '';
        },
        showIcon() {
            if(this.icon == 'plus-round'){
                return;
            }
            this.icon = 'plus-round';
        }
    },
    data() {
        return {
            icon: "plus-round"
        }
    },
    props:['text']
}
</script>

<style scoped lang="scss">

@import '~assets/css/base.scss';
.ivu-icon{
    font-size: 14px;
    font-weight: bolder;
}
.addButton {
    padding: 0;
    height: 38px;
    width: 100%;
    padding: 0;
    min-width: 160px;
    color: #fff;
    font-size: 14px;
    background-color: $mainColor;
}
</style>
