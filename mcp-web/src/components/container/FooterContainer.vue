<template>
  <div class="footer-container" v-show="visible">
    <slot></slot>
  </div>
</template>

<script>
export default {
  data() {
    return {
      visible: true,
    };
  },
  activated() {
    this.visible = true;
  },
  deactivated() {
    this.visible = false;
  },
  mounted() {
    this.init();
  },
  beforeDestroy() {
    this.remove();
  },
  methods: {
    init() {
      let mainContainer = document.querySelector(".main-container");
      mainContainer.appendChild(this.$el);
    },
    remove() {
      let mainContainer = document.querySelector(".main-container");
      if (mainContainer) {
        mainContainer.removeChild(this.$el);
      }
    },
  },
};
</script>

<style>
.footer-container {
  padding: 10px 20px;
  border-top: 1px solid #ddd;
}
</style>