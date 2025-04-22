<script setup>
import Logo from '@/components/Logo.vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';
import request from '@/utils/request';
import { netError } from '@/utils/swal';

const router = useRouter();

let account = ref('');
let nickname = ref('');
let password = ref('');
let passwordConfirm = ref('');

const register = async () => {
  console.log(account.value, nickname.value, password.value, passwordConfirm.value);
  if (account.value === '' || password.value === '' || nickname.value === '' || passwordConfirm.value === '') {
    Swal.fire({
      icon: 'error',
      title: '数据缺失',
      text: '请将表单填写完整',
    });
    return;
  }
  if (password.value !== passwordConfirm.value) {
    Swal.fire({
      icon: 'error',
      title: '前后密码输入不一致',
      text: '请重新输入密码',
    });
    return;
  }
  const param = {
    account: account.value,
    nickname: nickname.value,
    password: password.value,
  };

  try {
    const res = await request.post('/auth/register', param);
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      Swal.fire({
        icon: 'success',
        title: '注册成功',
        text: '将跳转到登录页',
        timer: 1500,
        showConfirmButton: false,
      });
      setTimeout(() => {
        router.push('/login');
      }, 1500);
    } else if (res.status === 200 && res.data.code === 1) {
      Swal.fire({
        icon: 'error',
        title: '账号已存在',
        text: '请重新输入账号',
      });
    } else {
      Swal.fire({
        icon: 'error',
        title: '网络异常',
        text: '请检查网络连接',
      });
    }
  } catch (error) {
    console.log(error);
    netError();
  }
};

const login = () => {
  console.log('login');
  router.push('/login');
};


</script>



<template>
  <div class="register-container">
    <div class="register-panel">
      <div class="logo">
        <Logo title="没有账号？注册一个吧！" />
      </div>
      <div class="text">
        <input class="account" type="text" placeholder="请输入账号" v-model="account" />
      </div>
      <div class="text">
        <input class="nickname" type="text" placeholder="请输入昵称" v-model="nickname" />
      </div>
      <div class="text">
        <input class="password" type="password" placeholder="请输入密码" v-model="password" />
      </div>
      <div class="text">
        <input class="passwordConfirm" type="password" placeholder="请再次输入密码" v-model="passwordConfirm" />
      </div>
      <div>
        <button class="register-btn" @click="register">立即注册</button>
      </div>
      <div class="tip">
        <p>已有账户？<span class="no-act" @click="login">返回登录</span></p>
      </div>
    </div>
  </div>
</template>



<style lang="scss" scoped>
* {
  user-select: none;
}

.register-container {
  width: 100%;
  height: 100vh;
  background-image: url('@/assets/welcome_bg.jpeg');
  background-size: cover;
  display: flex;
  /* 启用Flexbox布局 */
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
  flex-direction: column;
  /* 如果需要垂直居中，确保子元素也是这样排列 */

  .register-panel {
    margin: 0 auto;
    width: 300px;
    height: auto;
    padding: 40px 30px 16px 30px;
    border-radius: 10px;
    background-color: white;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.06);
    opacity: 0.7;

    .logo {
      margin: 10px 0 30px 0;
    }

    .account,
    .password,
    .nickname,
    .passwordConfirm {
      margin: 8px 0;
      height: 53px;
      line-height: 53px;
      width: 100%;
      padding: 0 8px;
      background-color: rgb(248, 248, 248);
      box-sizing: border-box;
      border: 1px solid rgb(248, 248, 248);
      border-radius: 5px;
      font-size: 18px;
      padding: 0 15px;
      margin-top: 13px;
    }

    .account:focus,
    .password:focus,
    .nickname:focus,
    .passwordConfirm:focus {
      outline: none;
      border: 1px solid rgb(206, 205, 205);
      transition: 1.2s;
    }

    .role {
      display: inline-block;
      color: rgb(30, 102, 147);
      font-size: 14px;
      padding-right: 10px;
    }
  }

  .register-btn {
    display: inline-block;
    text-align: center;
    border-radius: 3px;
    margin-top: 20px;
    height: 43px;
    line-height: 43px;
    width: 100%;
    background-color: rgb(155, 191, 93);
    font-size: 16px !important;
    border: none;
    color: rgb(250, 250, 250);
    padding: 0 !important;
    cursor: pointer;
    user-select: none;
  }

  .tip {
    margin: 20px 0;

    p {
      padding: 3px 0;
      font-size: 14px;
      margin: 0;
      color: #647897;

      i {
        margin-right: 3px;
      }

      span {
        color: #3b3c3e;
        border-radius: 2px;
        margin: 0 6px;
      }

      .no-act:hover {
        color: #568ed7;
        cursor: pointer;
      }

    }
  }

}
</style>