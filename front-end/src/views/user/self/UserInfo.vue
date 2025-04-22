<script setup>
import request from '@/utils/request';
import { netError } from '@/utils/swal';
import { onMounted, ref } from 'vue';
import Swal from 'sweetalert2';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

let pictureUrl = ref('');
let account = ref('');
let avatar = ref('');
let nickname = ref('');
let email = ref('');
let accountStatus = ref('正常');
let commentStatus = ref('正常');
let accountStatusColor = ref('');
let commentStatusColor = ref('');
let dialogFormVisible = ref(false);
let likesReceivedCount = ref(0)
let favoritesReceivedCount = ref(0)

let newNickname = ref('');
let newEmail = ref('');
let newAvatar = ref('');


const handleUploadChange = (file, fileList) => {
  console.log('handleUploadChange:', file, fileList);
  pictureUrl.value = URL.createObjectURL(file.raw);
  const reader = new FileReader();
  reader.onload = (event) => {
    newAvatar.value = event.target.result.split(',')[1];
  };
  reader.readAsDataURL(file.raw);
}

const beforeUpload = (file) => {
  console.log('beforeUpload:', file);
  return false;
}
const getInfo = async () => {
  try {
    const res = await request.get('/me');
    if (res.status === 200 && res.data.code === 0) {
      account.value = res.data.data.account;
      avatar.value = res.data.data.avatar;
      nickname.value = res.data.data.nickname;
      email.value = res.data.data.email;
      newNickname.value = nickname.value;
      newEmail.value = email.value;
      likesReceivedCount.value = res.data.data.likesReceivedCount;
      favoritesReceivedCount.value = res.data.data.favoritesReceivedCount;
      const status = res.data.data.status;
      if (status === 'muted') {
        commentStatus = '禁言';
        commentStatusColor = 'red';
      } else if (status === 'banned') {
        accountStatus = '封号';
        accountStatusColor = 'red';
      }
      userStore.updateUserInfo({
        avatar: avatar.value,
        nickname: nickname.value,
      })
    }
  } catch (error) {
    netError();
  }
}

const modifyInfo = async () => {
  dialogFormVisible.value = false;
  const params = {
    newAvatar: newAvatar.value !== '' ? newAvatar.value: avatar.value,
    newNickname: newNickname.value,
  }
  console.log('params: ', params);
  try {
    const res = await request.post('/me', params);
    if (res.status === 200 && res.data.code === 0) {
      Swal.fire({
        icon: 'success',
        title: '修改成功',
      })
      getInfo();
    } else {
      Swal.fire({
        icon: 'error',
        title: '修改失败',
        text: '请稍后重试',
      })
    }
  } catch (error) {
    console.log(error)
    netError();
  }
}


onMounted(() => {
  getInfo();
})

</script>



<template>
  <div class="info-container"
    style="width: 100%; height: 800px; display: flex; flex-direction: column; justify-content: center; align-items: center;">
    <el-descriptions title="个人信息" direction="vertical" column="2" border style="margin-top: 20px; width: 400px;"
      size="large">
      <el-descriptions-item :rowspan="2" :width="140" label="头像" align="center">
        <el-image style="width: 100px; height: 100px" :src="'data:image/png;base64,' + avatar" v-if="avatar" />
        <el-image style="width: 100px; height: 100px"
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" v-else />
      </el-descriptions-item>
      <el-descriptions-item label="账号" :width="140">{{ account }}</el-descriptions-item>
      <el-descriptions-item label="昵称">{{ nickname }}</el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          帐号状态
          <el-tooltip class="item" effect="dark" content="被封号将不能访问本网站，您已被放逐" placement="right">
            <el-icon color>
              <InfoFilled />
            </el-icon>
          </el-tooltip>
        </template>
        <el-tag size="small" :color="accountStatusColor">{{ accountStatus }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          留言状态
          <el-tooltip class="item" effect="dark" content="被禁言将不能发表评论，请友善发言" placement="right">
            <el-icon color>
              <InfoFilled />
            </el-icon>
          </el-tooltip>
        </template>
        <el-tag size="small" :color="commentStatusColor">{{ commentStatus }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="获赞数">{{ likesReceivedCount }}</el-descriptions-item>
      <el-descriptions-item label="被收藏数">{{ favoritesReceivedCount }}</el-descriptions-item>
    </el-descriptions>
    <el-button style="margin-top: 10px;" type="primary" @click="dialogFormVisible = true">修改个人信息</el-button>
  </div>
  <<el-dialog title="修改个人信息" width="600px" v-model="dialogFormVisible" @close="onClose">
    <el-form label-position="top" label-width="auto">
      <el-form-item label="头像">
        <el-upload action="#" show-file-list="false" accept="image/jpg, image/jpeg, image/png" auto-upload="false"
          :on-change="handleUploadChange" :before-upload="beforeUpload" style="width: 200px; height: 200px;"
          v-model="avatar">
          <img v-if="newAvatar" :src="pictureUrl" alt="" style="width: 200px; height: 200px; object-fit: fill;">
          <div v-else class="upload-cover-container"
            style="display: flex; flex-direction: column; align-items: center;">
            <el-icon size="100" color="#ccc">
              <UploadFilled />
            </el-icon>
            <span>点击上传图片（支持 jpg、jpeg、png 格式）</span>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="newNickname"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="modifyInfo">确 定</el-button>
      </div>
    </template>
    </el-dialog>
</template>



<style lang="scss" scoped>

</style>