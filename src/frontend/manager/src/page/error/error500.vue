<style lang="scss" scoped>
	.error500{
		width: 100%;
		height: 100%;
		position: relative;
		.error500-bg{
			position: absolute;
			top: 40%;
			left: 50%;
			margin-top: -116px;
			margin-left: -367.5px;
		}
	}
</style>

<template>
	<div class="error500">
		<div class="" class="error500-bg">
			<img src="../../assets/img/common/error500.png"/>
			<div>
				<router-link to="/index">
					<img src="../../assets/img/common/backToindex.png" class="backToIndex"/>
				</router-link>
			</div>
		</div>
		
	</div>
</template>

<script>
	
</script>