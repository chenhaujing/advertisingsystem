<template>
    <div class="fields">
        <div class="field_title" v-text="title">是否正在投放广告 ：</div>
        <div class="field_value" v-text="value" :title="value">XU76716371</div>
    </div>
</template>

<script>
export default {
	props:['title','value'],
}
</script>

<style lang="scss" scoped>
$fontSize: 14px;
.fields{
	
    height: 38.4px;
    line-height: 38.4px;
    
}
.field_title {
    min-width: 70px;
    float: left;
    font-size: $fontSize;
    color: #999999;
}

.field_value {
    font-size: $fontSize;
    color: #333333;
    float: left;
    max-width: 60%;
    white-space:nowrap; 
    overflow:hidden; 
    text-overflow:ellipsis;
}
</style>
