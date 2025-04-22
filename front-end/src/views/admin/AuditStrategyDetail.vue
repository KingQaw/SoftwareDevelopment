<script setup>
import { ref, onMounted, onBeforeMount } from 'vue';
import request from '@/utils/request';
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';
import { netError } from '@/utils/swal';

//获取当前路由的动态值
const router = useRouter();
const strategyId = parseInt(router.currentRoute.value.params.id);

let myComment = ref('');

const strategyDetail = ref({
  title: '',
  author: {
    id: null,
    nickname: '',
    avatar: null
  },
  cover: null,
  content: '',
  createTime: '',
  likeCount: 0,
  favoriteCount: 0,
  commentCount: 0,
  commentList: [],
  audtiStatus: '',
  id: 0,
  isLiked: false,
  isFavorited: false
});

const getStrategyDetail = async () => {
  try {
    const res = await request.get(`/guides/${strategyId}`);
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      strategyDetail.value = res.data.data;
      const queryParam = {
        page: 1,
        size: 1000,
        
      }
      const comments = await request.get(`/guides/${strategyId}/comments` + '?' + new URLSearchParams(queryParam));
      console.log(comments);
      strategyDetail.value.commentList = comments.data.data.records;
      return;
    } else if (res.status === 200 && res.data.code === 2) {
      Swal.fire({
        icon: 'error',
        title: '该攻略不存在',
        text: '即将返回首页',
        timer: 2000,
        showConfirmButton: false,
        willClose: () => {
          router.push('/strategy');
        }
      })
    }
  } catch (error) {
    console.log(error);
    netError();
  }
}

// 新增审核处理方法
const handleApprove = () => {
  Swal.fire({
    title: '确认通过审核？',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  }).then((result) => {
    if (result.isConfirmed) {
      if (result.isConfirmed) {
      const param = {
        id: strategyId,
        status: "approved",
      }
      const res = request.post('/admin/audit', param)
        Swal.fire({
          icon: 'success',
          title: '审核成功',
          text: '即将返回首页',
          timer: 1000,
          showConfirmButton: false,
          willClose: () => {
            router.push('/auditStrategy');
          }
        })
    }
    }
  });
};

const handleReject = () => {
  Swal.fire({
    title: '确认拒绝通过？',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  }).then((result) => {
    if (result.isConfirmed) {
      const param = {
        id: strategyId,
        status: "rejected",
      }
      const res = request.post('/admin/audit', param)
        Swal.fire({
          icon: 'success',
          title: '审核成功',
          text: '即将返回首页',
          timer: 1000,
          showConfirmButton: false,
          willClose: () => {
            router.push('/auditStrategy');
          }
        })
      } 
  });
};

const handlePending = () => {
  Swal.fire({
    title: '确认设为待议？',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  }).then((result) => {
    if (result.isConfirmed) {
      router.push('/auditStrategy');
    }
  });
};

getStrategyDetail();

</script>



<template>
  <div class="strategy-detail-container">
    <!-- 新增审核按钮组 -->
    <div class="audit-buttons">
      <el-button type="success" size="large" @click="handleApprove" style="margin-left: 11px;">通过</el-button>
      <el-button type="danger" size="large" @click="handleReject">拒绝</el-button>
      <el-button type="warning" size="large" @click="handlePending">待议</el-button>
    </div>
    
    <!-- 原有内容 -->
    <div style="width: 100%; height: 600px; overflow: hidden; position: relative;">
      <img :src="'data:image/png;base64,' + strategyDetail.cover" style="object-fit: cover; height: 100%; width: 100%;">
    </div>
    <div class="title" style="font-size: 32px; margin-block: 10px; font-weight: bold;">{{ strategyDetail.title }}
    </div>
    <div class="author">
      <el-avatar size="small" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        v-if="!strategyDetail.author.avatar" />
      <el-avatar size="small" :src="'data:image/png;base64,' + strategyDetail.author.avatar" v-else />
      <span style="margin-left: 10px; font-weight: bold;">{{ strategyDetail.author.nickname }}</span>
    </div>
    <div class="info">
      <span style="margin-left: 10px;">{{ strategyDetail.createTime.split(" ")[0] }}</span>
    </div>
    <div class="content" v-html="strategyDetail.content"></div>
  </div>
</template>


<style scoped>
.strategy-detail-container {
  position: relative;
  display: flex; 
  flex-direction: column; 
  align-items: center;
  /* 添加边框 */
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  margin: 20px;
}

.content {
  width: 80%;
}

.audit-buttons {
  position: fixed;
  left: 250px;
  top: 50%;
  /* transform: translateY(-50%); */
  display: flex;
  flex-direction: column;
  gap: 15px;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.9);
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.audit-buttons .el-button {
  width: 100px;
}
</style>