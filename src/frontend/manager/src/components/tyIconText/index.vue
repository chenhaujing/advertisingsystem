<template>
  <div class="iconText">
    <img v-imgError="errorImg" :src="image" class="icon" />
    <div v-text="title" class="text">
    </div>
  </div>
</template>

<script>
export default {
  props: ['image', 'title'],
  data() {
    return {
      errorImg: require('assets/img/client/client_dafault_icon.png'),
    }
  }
}
</script>

<style lang="scss" scoped>
.iconText {
  width: 100%;

  .text {
    padding-left: 9px;
    line-height: 30px;
    font-size: 14px;
    text-align: left;
    color: #333333;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .icon {
    box-sizing: content-box;
    float: left; //   padding-right: 9px;
    width: 30px;
    height: 30px;
    border-radius: 50%;
  }
}
</style>
