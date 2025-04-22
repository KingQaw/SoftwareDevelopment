<script setup>
import Logo from '@/components/Logo.vue';
import { netError } from '@/utils/swal';
import Swal from 'sweetalert2';
import { useRouter } from 'vue-router';
import request from '@/utils/request';
import { onMounted, ref } from 'vue';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const userStore = useUserStore();

const userInfo = {
  nickname: userStore.userInfo.nickname,
  avatar: userStore.userInfo.avatar
}

const logout = () => {
  console.log('logout');
  Swal.fire({
    title: '确定要退出吗？',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then((result) => {
    if (result.isConfirmed) {
      userStore.clearUser();
      router.push('/login');
    }
  })
};

onMounted(() => {
  router.push('/strategy');
})

</script>



<template>
  <div class="user-container">
    <el-container style="min-height: 100vh; width: 100%; background-color: white;">
      <el-header style="margin-top: 10px; border-bottom: 1px solid rgb(231, 231, 231);">
        <el-menu default-active="/strategy" class="el-menu-demo" mode="horizontal" :ellipsis="false" router>
          <el-menu-item index="0">
            <Logo />
          </el-menu-item>
          <el-menu-item index="/strategy">旅游指南</el-menu-item>
          <!-- <el-menu-item index="" disabled>景点信息</el-menu-item>
          <el-menu-item index="3">公告通知</el-menu-item> -->
          <el-menu-item index="/publishStrategy">发布指南</el-menu-item>
          <el-menu-item index="/myStrategy">我的创作</el-menu-item>
          <el-menu-item index="/myFavorite">我的收藏</el-menu-item>
          <el-sub-menu index="5">
            <template #title>
              <el-avatar v-if="!userInfo.avatar"
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
              <el-avatar v-else size="default" :src="'data:image/png;base64,' + userInfo.avatar" />
              <span style="margin-left: 10px;"> {{ userInfo.nickname }}</span>
            </template>
            <el-menu-item index="/userInfo">个人信息</el-menu-item>
            <el-menu-item index="/resetPassword">修改密码</el-menu-item>
            <el-menu-item @click="logout">退出登陆</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
      <el-footer></el-footer>
    </el-container>
  </div>
</template>



<style lang="scss" scoped>
.user-container {
  .el-menu--horizontal>.el-menu-item:nth-child(1) {
    margin-right: auto;
  }
}
</style>