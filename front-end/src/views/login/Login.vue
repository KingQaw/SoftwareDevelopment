<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';
import request from '@/utils/request';

import Logo from '@/components/Logo.vue';
import { netError } from '@/utils/swal';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const userStore = useUserStore();

const loginForm = ref({
  account: '',
  password: ''
})

const login = async () => {
  const welcomeText = (role) => {
    if (role === "admin") {
      return '正在进入管理员后台';
    } else if (role === "user") {
      return '欢迎来到趣旅网，查看你的旅游指南';
    }
  }

  const navigate = (role) => {
    if (role === "管理员") {
      router.push('/admin');
    } else if (role === "普通用户") {
      router.push('/user');
    }
  }
  console.log(loginForm.value);
  const { account, password } = loginForm.value;
  if (account === '' || password === '') {
    Swal.fire({
      icon: 'error',
      title: '数据缺失',
      text: '账号或密码不能为空',
    });
    return;
  }
  const param = {
    account: account,
    password: password
  };
  try {
    const res = await request.post('/auth/login', param);
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      console.log(res.data.data);
      userStore.setUser(res.data.data)
      console.log(userStore.userInfo);
      const role = userStore.userInfo.role;
      console.log(role);
      Swal.fire({
        icon: 'success',
        title: '登录成功',
        text: welcomeText(role),
        timer: 1500,
        showConfirmButton: false,
      });

      setTimeout(() => {
        navigate(role);
      }, 1500);
    } else if (res.status === 200 && res.data.code === 1) {
      Swal.fire({
        icon: 'error',
        title: '账号或密码错误',
        text: '请检查账号或密码',
      });
    } else if (res.status === 200 && res.data.code === 2) {
      Swal.fire({
        icon: 'error',
        title: '账号已被封禁',
        text: '请联系管理员解封',
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

const register = () => {
  console.log('register');
  router.push('/register');
};
</script>



<template>
  <div class="login-container">
    <div class="login-panel">
      <div class="logo">
        <Logo />
      </div>
      <div class="text">
        <input class="account" type="text" placeholder="请输入账号" v-model="loginForm.account" />
      </div>
      <div class="text">
        <input class="password" type="password" placeholder="请输入密码" v-model="loginForm.password" />
      </div>
      <div>
        <button class="login-btn" @click="login">立即登录</button>
      </div>
      <div class="tip">
        <p>没有账号？<span class="no-account" @click="register">点此注册</span></p>
      </div>
    </div>
  </div>
</template>



<style lang="scss" scoped>
* {
  user-select: none;
}

.login-container {
  background-color: white;
  background-image: url('@/assets/welcome_bg.jpeg');
  background-size: cover;
  width: 100%;
  height: 100vh;
  // 使用 flex 弹性布局
  display: flex;
  // 设置主轴方向为横向
  flex-direction: row;
  // 设置元素沿着主轴居中
  justify-content: center;
  // 设置元素沿着交叉轴居中
  align-items: center;

  .login-panel {
    background-color: white;
    width: 300px;
    height: auto;
    padding: 40px 30px 16px 30px;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(8, 66, 132, 0.498);
    opacity: 0.7;

    .logo {
      margin: 10px 0 30px 0
    }

    .account, 
    .password {
      background-color: white;
      height: 53px;
      width: 100%;
      margin: 8px 0;
      margin-top: 13px;
      padding: 0 10px;
      // 将盒子设置为刚体
      // 避免 padding 改变盒子的尺寸
      box-sizing: border-box;
      font-size: 18px;
      border: 1px solid rgb(232, 230, 230);
      border-radius: 5px;
    }

    .account:focus, 
    .password:focus {
      outline: none;
      border: 1px solid rgb(166, 182, 230);
      transition: 1s;
    }

    .login-btn {
      height: 45px;
      width: 100%;
      margin-top: 20px;
      background-color: rgb(93, 175, 191);
      border: none;
      border-radius: 5px;
      text-align: center;
      font-size: 16px;
      color: white;
      cursor: pointer;
    }

    .tip {
      margin: 20px 0;

      p {
        padding: 3px 0;
        margin: 0;
        font-size: 14px;
        color: #647897;

        i {
          margin-right: 3px;
        }

        span {
          color: #3b3c3e;
          border-radius: 2px;
          margin: 0 6px;
        }

        .no-account:hover {
          color: #3e77c2;
          cursor: pointer;
        }
      }
    }
  }
}
</style>