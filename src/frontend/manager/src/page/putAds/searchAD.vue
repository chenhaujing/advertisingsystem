<style  lang="scss" scoped>
    .searchLine{
        // display: inline-block;
      
    }
    .selectBox{
        width: 160px;
        display: inline-block;
    }
    // 右侧搜索框
    .searchBox {
        display: inline-block;
        vertical-align: top;
        margin-left: 30px;
        width: 380px;
        border-radius: 4px;
        border: 1px solid #dddee1;
        background-color: #ffffff;
    }
</style>
<style lang="scss">
    .selectBox {
        .ivu-select-selection{
            height: 40px;
            .ivu-select-selected-value{
                height: 40px;
                line-height: 40px;
            }
        }
    }
</style>
<template>
    <div class="searchLine">
        <iSelect class="selectBox" v-model="form.selectValue">
            <iOption 
            v-for="item in selectList"
            :value="item.value" 
            :key="item.value">
            {{ item.label }}
            </iOption>
        </iSelect>
        <div class="searchBox">
            <tySearchInput v-model="form.selectKey" class="searchComponent" :placeholder="selectPlaceholder(form.selectValue)" @search="comtitSearch"></tySearchInput>
        </div>
    </div>
</template>
<script>

import { Select as iSelect, Option as iOption }  from 'iview/src/components/select';
import tySearchInput from 'components/tySearchInput';

const ADarray = [
    {
        label: '广告名称', 
        value: 1
    },
    {
        label: '广告客户名称',
        value: 2
    },
    {
        label: '广告合同名称',
        value: 3
    }
];
export default {
	components: {
        iSelect,
        iOption,
        tySearchInput
	},
    props:{
        selectList:{
            type: Array,
            default: function(){
                return ADarray;
            } 
        },
        searchAD: {},
        selectLabelArray: {
            type: Array,
            default: function(){
                return this.selectList.map((item)=>{
                    return '请输入'+item.label
                });
            } 
        },
    },
	data() {
		return {
            curr: 0,
            form: {
                selectValue: this.selectList[0].value, 
                selectLabel: this.selectList[0].label,
                selectKey: ''
            },
		}
	},
    methods:{
        // 更新搜素框里的字
        selectPlaceholder(value){
            for(let i=0; i < this.selectList.length; i++){
                if(value == this.selectList[i].value){
                    this.curr = i;
                    return this.selectLabelArray[i];
                }  
            }
        },
        comtitSearch(){
            this.form.selectLabel = this.selectList[this.curr].label;
            this.$emit('searchAD', this.form);
        }
    },
    mounted(){
    },

}
</script>
