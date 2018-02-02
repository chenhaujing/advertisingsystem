<style lang="scss" scoped>
	.error404{
		width: 100%;
		height: 100%;
		position: relative;
		.error404-bg{
			position: absolute;
			top: 40%;
			left: 50%;
			margin-top: -116px;
			margin-left: -367.5px;
		}
		.backToIndex{
			position: absolute;
			top: 65%;
			left: 50%;
			margin-top: -9px;
			margin-left: -55px;
		}
	}
</style>

<template>
	<div class="error404">
		<img src="../../assets/img/common/error404.png" class="error404-bg"/>
		<router-link to="/index">
			<img src="../../assets/img/common/backToindex.png" class="backToIndex"/>
		</router-link>
	</div>
</template>

<script>
	
</script>