<style scoped lang="scss">
    .codeBtn{
    	width: 30%;
    	float: left;
    	margin-left: 5%;
    	font-size: 16px;
    	height: 50px;
    	background-color: #fff;
    	border: 1px solid #eee;
    	float: left;
    	border-radius: 6px;
    	color: #4cabe0;
    	outline: none;
    }
    .codeBtn:active{
    	outline: none;
    	color: #fff;
    	background-color: #4cabe0;
    }
    .disabled{
    	background-color: #eee;
    }
    
@media only screen and (min-width: 1280px) and (max-width: 1920px) {
	.codeBtn{
		font-size: 12.8px;
    	height: 40px;
    	border-radius: 4.8px;
    }
}    
</style>

<template>
    <button class="codeBtn">
    	{{handlerTimer}}
    </button>
</template>

<script>
    export default {
    	props:['totalTime'],
    	computed:{
    		handlerTimer() {
	    		if(this.timer == this.totalTime) {
	                return '获取验证码'
	            }
	            return this.timer + 'S';
	    	}
    	},
    	data(){
    		return {
            	timer: this.totalTime
        	}
    	},
    	methods:{
    		start() {
	            if(this.timer < this.totalTime) {
	                return;
	            }
	            this.timer--;
	            var interval = setInterval(() => {
	                if (this.timer < this.totalTime) {
	                    this.timer--;
	                }
	                if(this.timer == 0) {
	                    this.timer = this.totalTime;
	                    clearInterval(interval);
	                }
	            }, 1000);
	        }
    	}
    }
</script>
