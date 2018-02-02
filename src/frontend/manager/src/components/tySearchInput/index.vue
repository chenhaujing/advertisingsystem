<template>
	<div class="tySearch">
		<form class="search" action="javaScript:void(0)" @submit="search">
	    <input type="text" 
	    	:placeholder="placeholder" 
	    	v-model="currentValue" 
	    	@keyup="fuzzySearch" 
	    	@focus="searchBoxfade(true)" 
	    	/>
	    <i class="iconfont icon-sousuo" @click="search" v-if="!notShowIcon"></i>
	  </form>
		<a class="searchBtn" @click="search" v-if="notShowIcon">查询</a>
	  <!-- <div class="searchBox" v-show="searchBoxShow">
			<div class="noData" v-show="searchResultForm.length == 0">"没有找到任何相关客户数据"</div>
			<ul v-if="searchResultForm.length != 0" class="searchResultForm">
				<li v-for="(searchResultList,index) in searchResultForm" 
						v-text="searchResultList" class="searchResultList" @click="quickResult(searchResultList)" @blur.prevent.self>
				</li>
			</ul>
		</div> -->
	</div>
</template>

<script>
export default {
  props: ['placeholder', 'value','notShowIcon'],
  methods: {
    search() {
      this.$emit('search', this.currentValue);
    },
    //模糊搜索结果的开关
    searchBoxfade(Boolean){
    	this.searchBoxShow = Boolean;
    },
    //模糊搜索
    fuzzySearch(){
    	
    },
    //将模糊搜索的结果打印到搜索框上
    quickResult(val){
    	console.log(val)
    	this.currentValue = val;
    	this.searchBoxShow = false;
    }
  },
  data() {
    return {
    	//默认模糊收缩框为隐藏
    	searchBoxShow:false,
      currentValue: null,
			searchResultForm:[],
    }
  },
  watch: {
    value(val) {
      this.currentValue = val;
      
    },
    currentValue(val) {
      this.$emit('input', val);
      //当获取焦点输入框值为空的时候，将搜索结果数据列表的数据清空
	    if(this.$formVerify.verifyString(this.currentValue)){
	  		this.searchResultForm = [];
	  		return;
	  	}
      //当获取焦点输入框值不为空的时候，将搜索结果数据列表的展示模糊搜索的数据
    	if(!this.$formVerify.verifyString(this.currentValue)){
    		this.searchResultForm = ['1','2222222','牧户搜索','大大','大大好得很要和','大大'];
    		return;
    	}
    }
  }
}
</script>

<style lang="scss"  scoped>
@import '~assets/css/base.scss';
.tySearch{
	position: relative;
}
.search {
  border-radius: 4px;
  width:100%;
  height: 38px;
  padding-left: 20px;
  padding-right: 20px;
  input {
		background: transparent;
    font-size: 14px;
    outline: none;
    width: 80%;
    border: 0;
    height: 38px;
    line-height: 38px;
  }
  .icon-sousuo {
    line-height: 38px;
    float: right;
  }
  /*模糊搜索结果Box样式*/
  .searchBox{
  	// width: 400px;
  	margin-left: -20px;
  	position: relative;
  	z-index: 10;
  	background-color: rgba(0,0,0,0.3);
  	color: #fff;
  	margin-top: 5px;
  	/*没有结果时候的样式*/
  	.noData{
  		width: 100%;
  		height: 30px;
  		line-height: 30px;
  		box-sizing: border-box;
  		padding-left: 40px;
  		background-color: rgba(0,0,0,0.6);
  		color: #fff;
  	}
  	/*有结果的时候的样式*/
  	.searchResultForm{
	  	width: 100%;
	  	font-size: 12px;
	  	text-align: left;
	  	line-height: 30px;
	  	max-height: 100px;
	  	overflow-y: scroll;
	  }
	  .searchResultList{
	  	box-sizing: border-box;
  		padding-left: 40px;
	  }
	  .searchResultList:hover{
	  	background-color: rgba(0,0,0,0.6);
	  	color: #fff;
	  }
  }
  
}
	.searchBtn{
		outline:none;
		color: #ffffff;
		border: 0;
		font-size: 16px;
		border-radius: 4px;
		width: 120px;
		background-color: $mainColor;
		height: 38px;
		line-height: 38px;
		text-align: center;
		position:absolute;
		right:-160px;
		top: 0;
	}
</style>
