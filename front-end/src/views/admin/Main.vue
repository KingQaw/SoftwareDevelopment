<script setup>
import Logo from '@/components/Logo.vue';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';
import { RouterView } from 'vue-router';

const router = useRouter();

let userInfo = ref({
  avatar: null,
  nickname: 'admin'
})

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
      router.push('/login');
    }
  })
}

onMounted(() => {
  router.push('/dataOverview');
})

</script>



<template>
  <div class="common-layout" style="height: 100vh;">
    <el-container>
      <el-header
        style="height: 8vh; display: flex; flex-direction: row; justify-content: space-between; border-bottom: 1px solid rgb(231, 231, 231);">
        <Logo title="趣旅网 - 后台管理" />
        <div class="admin-info" style="margin-right: 10px; margin-top: 10px;">
          <el-menu>
            <el-sub-menu>
              <template #title>
                <el-avatar v-if="!userInfo.avatar"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                <el-avatar v-else size="default" :src="'data:image/png;base64,' + userInfo.avatar" />
                <span style="margin-left: 10px;"> {{ userInfo.nickname }}</span>
              </template>
              <el-menu-item @click="logout">退出登陆</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px" style="height: 92vh; font-weight: bold; border-bottom: k1px solid rgb(231, 231, 231);">
          <el-menu router default-active="/dataOverview">
            <el-menu-item index="/dataOverview"
              style="font-size: 18px; border-bottom: 1px solid rgb(231, 231, 231);">数据总览</el-menu-item>
            <el-menu-item index="/auditStrategy"
              style="font-size: 18px; border-bottom: 1px solid rgb(231, 231, 231);">审核指南</el-menu-item>
            <el-menu-item index="/manageStrategy"
              style="font-size: 18px; border-bottom: 1px solid rgb(231, 231, 231);">指南管理</el-menu-item>
            <el-menu-item index="/manageUser"
              style="font-size: 18px; border-bottom: 1px solid rgb(231, 231, 231);">用户管理</el-menu-item>
            <el-menu-item index="/loginRecord"
              style="font-size: 18px; border-bottom: 1px solid rgb(231, 231, 231);">访问记录</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main style="height: 92vh">
          <RouterView />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>



<style scoped>

</style>