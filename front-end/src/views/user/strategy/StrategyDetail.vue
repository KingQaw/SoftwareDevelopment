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
  liked: false,
  favorited: false
});

const getStrategyDetail = async () => {
  try {
    const res = await request.get(`/guides/${strategyId}`);
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      strategyDetail.value = res.data.data;
      const queryParam = {
        page: 1,
        size: 1000
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

const likeStrategy = async () => {
  if (strategyDetail.value.liked) {
    try {
      const res = await request.delete(`/guides/${strategyId}/like`);
      console.log(res);
      if (res.status === 200 && res.data.code === 0) {
        Swal.fire({
          icon: 'success',
          title: '取消点赞成功',
        })
        getStrategyDetail();
        return;
      } else {
        Swal.fire({
          icon: 'error',
          title: '取消点赞失败',
          text: '请稍后重试',
        })
      }
    } catch (error) {
      console.log(error);
      netError();
    }
  } else {
    try {
      const res = await request.post(`/guides/${strategyId}/like`);
      if (res.status === 200 && res.data.code === 0) {
        Swal.fire({
          icon: 'success',
          title: '点赞成功',
        })
        getStrategyDetail();
        return;
      } else if (res.status === 200 && res.data.code === 3) {
        Swal.fire({
          icon: 'error',
          title: '点赞失败',
          text: '不能点赞自己的指南',
        })
      } else{
        Swal.fire({
          icon: 'error',
          title: '点赞失败',
          text: '请稍后重试',
        })
      }
    } catch (error) {
      console.log(error);
      netError();
    }
  }
}

const favoriteStrategy = async () => {
  const params = {
    strategyId: strategyId
  }
  if (strategyDetail.value.favorited) {
    try {
      console.log(params);
      const res = await request.delete(`/guides/${strategyId}/favorite`);
      console.log(res);
      if (res.status === 200 && res.data.code === 0) {
        Swal.fire({
          icon: 'success',
          title: '取消收藏成功',
        })
        getStrategyDetail();
        return;
      }
    } catch (error) {
      console.log(error);
      netError();
    }
  } else {
    try {
      const res = await request.post(`/guides/${strategyId}/favorite`);
      if (res.status === 200 && res.data.code === 0) {
        Swal.fire({
          icon: 'success',
          title: '收藏成功',
        })
        getStrategyDetail();
        return;
      } else if (res.status === 200 && res.data.code === 3) {
        Swal.fire({
          icon: 'error',
          title: '收藏失败',
          text: '不能收藏自己的指南',
        })
      } else {
        Swal.fire({
          icon: 'error',
          title: '收藏失败',
          text: '请稍后重试',
        })
      }
    } catch (error) {
      console.log(error);
      netError();
    }
  }
}

const publishComment = async () => {
  const params = {
    content: myComment.value,
  }
  if (myComment.value === '') {
    Swal.fire({
      icon: 'error',
      title: '评论不能为空',
    })
    return;
  }
  try {
    const res = await request.post(`/guides/${strategyId}/comment`, params);
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      Swal.fire({
        icon: 'success',
        title: '评论成功',
      })
      myComment.value = '';
      getStrategyDetail();
      return;
    } else if (res.status === 200 && res.data.code === 1) {
      Swal.fire({
        icon: 'warning',
        title: '您已被禁言'
      })
    } else {
      Swal.fire({
        icon: 'error',
        title: '评论失败',
        text: '请稍后重试',
      })
      return;
    }
  } catch (error) {
    console.log(error);
    netError();
  }
}

getStrategyDetail();

</script>



<template>
  <div class="strategy-detail-container">
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
  <div class="comment-container">
    <div class="user-play"
      style="width: 90%; display: flex; flex-direction: row; justify-content: space-between; margin-left: 100px;">
      <div class="comment" style="width: 70%;">
        <el-input placeholder="良言一句三冬暖，恶语相向六月寒" v-model="myComment" style="width: 90%;"></el-input>
        <el-button type="primary" plain @click="publishComment">{{ "发布评论  " + strategyDetail.commentCount }}</el-button>
      </div>
      <div class="likes-favorites">
        <el-button icon="check" color="brown" :plain="!strategyDetail.liked" @click="likeStrategy">{{ "点赞 " +
          strategyDetail.likeCount }}</el-button>
        <el-button icon="star" color="green" :plain="!strategyDetail.favorited" @click="favoriteStrategy">{{ "收藏 " +
          strategyDetail.favoriteCount }}</el-button>
      </div>
    </div>
    <div class="comment-list" style="margin-top: 10px;">
      <div class="comment-item" v-for="(item, index) in strategyDetail.commentList" :key="index"
        style="margin-bottom: 10px; margin-left: 100px; margin-right: 100px; font-size: large;">
        <div class="comment-author">
          <el-avatar size="small" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            v-if="!item.avatar" />
          <el-avatar size="small" :src="'data:image/png;base64,' + item.avatar" v-else></el-avatar>
          <span style="font-weight: bold;">{{ item.nickname }}</span>
          <span style="margin-left: 10px;">{{ item.createTime.split(' ')[0] }}</span>
        </div>
        <div class="comment-content" style="margin-left: 10px;">{{ item.content }}</div>
      </div>
    </div>
  </div>
</template>



<style scoped>
.strategy-detail-container {
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
</style>