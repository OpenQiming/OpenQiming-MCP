<template>
  <div class="page-container">
    <div v-if="showHeader" class="header flex-between-center">
      <slot name="header"></slot>
    </div>
    <div
      class="container-content"
      :style="{ backgroundColor: backgroundColor, padding: padding }"
    >
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    showHeader: {
      type: Boolean,
      default: true,
    },
    backgroundColor: {
      type: String,
      default: "#F4F7F9",
    },
    padding: {
      type: String,
      default: 0,
    },
  },
  data() {
    return {};
  },
  created() {},
};
</script>

<style lang="less" scoped>
.page-container {
  .header {
    background-color: #f4f7f9;
    padding: 12px 20px;
  }

  .container-content {
    min-height: calc(100vh - 130px);
  }
}
</style>