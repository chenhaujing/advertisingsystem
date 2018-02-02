<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app',
  data() {
    return {
      transitionName: 'slide-left'
    }
  }
}
</script>

<style>
html {
  height: 100%;
  width: 100%;
  min-height: 100%;
}

body {
  overflow: hidden;
  height: 100%;
  width: 100%;
  min-width: 1200px;
  overflow-x: auto;
  min-height: 660px;
  position: relative;
}

#app {
  height: 100%;
  width: 100%;
}
</style>
