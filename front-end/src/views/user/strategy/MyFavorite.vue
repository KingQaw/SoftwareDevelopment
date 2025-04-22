<script setup>
import { netError } from '@/utils/swal';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request';
import Swal from 'sweetalert2';

const router = useRouter();

let tableData = ref([]);

const getData = async () => {
  console.log('获取数据')
  const params = {
    page: 1,
    pageSize: 1000,
  }
  try {
    const res = await request.get('/me/favorites' + "?" + new URLSearchParams(params));
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      tableData.value = res.data.data.records;
    } else {
      console.log('获取数据失败')
      Swal.fire({
        icon: 'error',
        title: '获取数据失败',
        text: '请检查网络或稍后重试',
      })
    }
  } catch (error) {
    console.log(error);
    netError();
  }
}

const handleView = (strategyId) => {
  console.log('查看' + strategyId);
  router.push('/strategy/' + strategyId);
}

const handleDelete = async (strategyId) => {
  Swal.fire({
    title: '确定取消收藏该攻略吗？',
    text: "取消后不可恢复！",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then((result) => {
    if (!result.isConfirmed) {
      return;
    }
  })
  console.log('删除' + strategyId);
  try {
    const res = await request.delete(`/guides/${strategyId}/favorite`);
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      Swal.fire({
        icon: 'success',
        title: '取消收藏成功',
      })
      getData();
    } else {
      Swal.fire({
        icon: 'error',
        title: '取消收藏失败',
      })
    }
  } catch (error) {
    console.log(error);
    netError();
  }
}

getData();
</script>



<template>
  <el-col style="width: 80%; margin-left: 10%; margin-top: 40px;">
    <el-row justify="space-between">
      <el-col span="4">
        <h1>我的收藏</h1>
      </el-col>
    </el-row>
    <el-row>
      <el-table style="width: 100%; margin-left: 0; margin-top: 40px;" :data="tableData" table-layout="auto">
        <el-table-column prop="guideId" label="编号" width="180" sortable></el-table-column>
        <el-table-column prop="title" label="标题" width="380" sortable></el-table-column>
        <el-table-column prop="authorNickname" label="作者" width="180" sortable></el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180" sortable></el-table-column>
        <el-table-column label="操作" width="400">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row.guideId)">查看详情</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.guideId)">取消收藏</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </el-col>
</template>



<style lang="scss" scoped></style>