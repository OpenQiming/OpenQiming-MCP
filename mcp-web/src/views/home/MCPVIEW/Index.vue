<script>
export default {
  data() {
    return {

    }
  },
  created() {

    if (this.$route.path == '/mcpview') {
      this.$router.push({
        name: 'FirstIndex'
      });

    }


  },



}
</script>
<template>

  <div class="parent">
    <keep-alive>
      <router-view v-if="$route.meta.cache"></router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.cache"></router-view>

  </div>

</template>
<style lang="less" scoped>
// body {
//   background-color: #f9fafd;
// }


</style>
<style lang="less" >
body {
  background-color: #f9fafd;
  padding: 0 !important;
}
body::-webkit-scrollbar {
  width: 0;
}
.el-tooltip__popper.is-dark {
  background-color: #3F3E62FC !important;
  border-radius: 6px;
  line-height: 18px;
}
.popper__arrow{
  display: none !important;
}

</style>