<script setup>
import Editor from '@/components/Editor.vue';
import { ref } from 'vue';
import request from '@/utils/request';
import { netError } from '@/utils/swal';
import Swal from 'sweetalert2';
import { useRouter } from 'vue-router';

const router = useRouter();

let pictureUrl = ref('');

const strategy = ref({
  title: '',
  cover: null,
  content: ''
})

const contentFromEditor = (content) => {
  strategy.value.content = content;
}

const handleUploadChange = (file, fileList) => {
  console.log('handleUploadChange:', file, fileList);
  pictureUrl.value = URL.createObjectURL(file.raw);
  const reader = new FileReader();
  reader.onload = (event) => {
    strategy.value.cover = event.target.result.split(',')[1];
  };
  reader.readAsDataURL(file.raw);
}

const beforeUpload = (file) => {
  console.log('beforeUpload:', file);
  return false;
}

const publishStrategy = async () => {
  if (strategy.value.title === '') {
    Swal.fire({
      icon: 'error',
      title: '标题不能为空',
      text: '请输入标题后再次尝试发布！',
      confirmButtonText: '确定'
    });
    return;
  }
  if (strategy.value.cover === null) {
    Swal.fire({
      icon: 'error',
      title: '缺失封面图片',
      text: '请选择封面图片后再次尝试发布！',
      confirmButtonText: '确定'
    });
    return;
  }
  if (strategy.value.content === '') {
    Swal.fire({
      icon: 'error',
      title: '内容不能为空',
      text: '请输入内容后再次尝试发布！',
      confirmButtonText: '确定'
    });
    return;
  }
  try {
    const res = await request.post('/guides', strategy.value);
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      Swal.fire({
        icon: 'success',
        title: '发布成功',
        text: '您的攻略已经发布成功，快去查看吧！',
        confirmButtonText: '确定',
      }).then(result => {
        router.push('/strategy');
      });
    }
  } catch (error) {
    console.log(error);
    netError();
  }
}

const cancelEdit = () => {
  Swal.fire({
    icon: 'warning',
    title: '您确定要取消编辑吗？',
    text: '取消后内容将不会保存！',
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    showCancelButton: true,
  }).then(result => {
    if (result.isConfirmed) {
      router.push('/strategy');
    }
  });
}

</script>



<template>
  <div class="publish-strategy-container" style="margin: 0 auto; max-width: 900px; border: 1px solid #ebeef5; border-radius: 4px; padding: 20px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);">
    <el-form label-position="top" label-width="auto" style="max-width: 900px; min-width: 600px;">
      <el-form-item label="标题" prop="title">
        <el-input v-model="strategy.title" placeholder="请输入标题"></el-input>
      </el-form-item>
      <el-form-item label="封面" prop="cover">
        <el-upload action="#" show-file-list="false" accept="image/jpg, image/jpeg, image/png" auto-upload="false" 
        :on-change="handleUploadChange" :before-upload="beforeUpload" style="width: 500px; height: 300px;"
          v-model="strategy.cover">
          <img v-if="strategy.cover" :src="pictureUrl" alt="" style="width: 100%; height: 100%; object-fit: fill;">
          <div v-else class="upload-cover-container" style="display: flex; flex-direction: column; align-items: center;">
            <el-icon size="100" color="#ccc">
              <UploadFilled />
            </el-icon>
            <span>点击上传图片（支持 jpg、jpeg、png 格式）</span>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <Editor receive-content="" @on-receive="contentFromEditor" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="publishStrategy">发布指南</el-button>
        <el-button type="danger" @click="cancelEdit">取消编辑</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>



<style lang="scss" scoped>

</style>