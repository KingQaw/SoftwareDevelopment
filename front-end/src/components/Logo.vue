<script setup>
const props = defineProps({
  title: {
    type: String,
    default: '趣旅网 - 旅游小指南'
  },
  showTitle: {
    type: Boolean,
    default: true
  },
  titleColor: {
    type: String,
    default: '#1c1c1c'
  }
});
</script>



<template>
  <span class="logo">
    <img src="/favicon.jpg" fit="fill" style="width: 50px; height: 50px;"></img>
    <div v-if="showTitle">
      <span :style="{ color: titleColor, display: 'block'}">{{ title }}</span>
    </div>
  </span>
</template>



<style lang="scss" scoped>
.logo {
  color: rgb(8, 24, 16) !important;
  font-weight: bold;
  font-size: 22px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  user-select: none;
  font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;

  span {
    margin-left: 8px;
    color: #666;
  }
}
</style>