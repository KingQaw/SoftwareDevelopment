<script setup>
import Swal from 'sweetalert2';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request';
import { useUserStore } from '@/stores/user';

const router = useRouter();

let originalPassword = ref('');
let newPassword = ref('');
let newPasswordConfirm = ref('');

const resetPassword = async () => {
  console.log('resetPassword');
  if (originalPassword.value === '' || newPassword.value === '' || newPasswordConfirm.value === '') {
    Swal.fire({
      icon: 'error',
      title: '数据缺失',
      text: '请将表单填写完整',
    });
    return;
  }
  if (newPassword.value !== newPasswordConfirm.value) {
    Swal.fire({
      icon: 'error',
      title: '两次密码不一致',
      text: '请重新输入密码',
    });
    return;
  }
  if (newPassword.value === originalPassword.value) {
    Swal.fire({
      icon: 'error',
      title: '新密码与原密码相同',
      text: '请检查新密码',
    });
    return;
  }
  // TODO: 添加修改密码逻辑
  const param = {
    oldPassword: originalPassword.value,
    newPassword: newPassword.value,
  };
  try {
    const res = await request.post('/me/password', param);
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      Swal.fire({
        icon: 'success',
        title: '修改成功',
        text: '请重新登录',
        timer: 1500,
        showConfirmButton: false,
      });
      setTimeout(() => {
        const userStore = useUserStore();
        userStore.clearUser();
        router.push('/login');
      }, 1500);
    } else {
      Swal.fire({
        icon: 'error',
        title: '原密码错误',
        text: '请重新输入原密码',
      })
    }
  } catch (error) {
    console.log(error);
    Swal.fire({
      icon: 'error',
      title: '网络错误',
      text: '请检查您的网络连接',
    });
  }
}

</script>



<template>
  <div class="password-container" style="display: flex; flex-direction: column; justify-content: center; align-items: center; height: 450px; border: 1px solid #ebeef5; border-radius: 8px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); padding: 20px; width: 80%; max-width: 400px; margin: 200px auto;">
    <div class="title">
      <h1>修改密码</h1>
    </div>
    <div class="password-box">
      <el-form label-position="top" label-width="auto" style="max-width: 600px" size="lagre">
        <el-form-item label="原密码：">
          <el-input type="password" show-password style="width: 200px;" v-model="originalPassword"/>
        </el-form-item>
        <el-form-item label="新密码：">
          <el-input type="password" show-password style="width: 200px;" v-model="newPassword"/>
        </el-form-item>
        <el-form-item label="确认密码：">
          <el-input type="password" show-password style="width: 200px;" v-model="newPasswordConfirm"/>
        </el-form-item>
      </el-form>
    </div>
    <el-button type="primary" @click="resetPassword">修改密码</el-button>
  </div>
</template>



<style lang="scss" scoped>

</style>