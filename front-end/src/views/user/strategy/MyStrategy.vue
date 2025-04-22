<script setup>
import { netError } from '@/utils/swal'
import Swal from 'sweetalert2'
import { ref } from 'vue'
import request from '@/utils/request'
import { useRouter } from 'vue-router'

const router = useRouter();

let tableData = ref([]);

const handleView = (strategyId) => {
  console.log('查看' + strategyId);
  router.push('/strategy/' + strategyId);
}

const handleDelete = () => {
  console.log('删除')
  Swal.fire({
    title: '确定删除吗？',
    text: "你将无法恢复该操作！",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then((result) => {
    if (result.isConfirmed) {

    }
  })
}

const getData = async (audtiStatus) => {
  console.log('获取数据')
  const params = {
    page: 1,
    size: 1000,
    auditStatus: audtiStatus
  }
  try {
    const res = await request.get('/me/guides' + "?" + new URLSearchParams(params));
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      console.log(typeof res.data.data.records);
      for (let i = 0; i < res.data.data.records.length; i++) {
        res.data.data.records[i].auditStatus = res.data.data.records[i].auditStatus === 'approved' ? '审核通过' : res.data.data.records[i].auditStatus === 'rejected' ? '审核拒绝' : '正在审核';
      }
      tableData.value = res.data.data.records;
    } else {
      console.log('获取数据失败')
      Swal.fire({
        icon: 'error',
        title: '获取数据失败',
        text: '请稍后重试',
        confirmButtonText: '确定'
      })
    }
  } catch (error) {
    console.log(error)
    netError();
  }
}

getData('');

</script>



<template>
  <el-col style="width: 80%; margin-left: 10%; margin-top: 40px;">
    <el-row justify="space-between">
      <el-col span="4">
        <h1>我的创作</h1>
      </el-col>
      <el-col span="4">
        <div class="select">
          <el-button type="primary" @click="getData('')">全部</el-button>
          <el-button type="success" @click="getData('approved')">审核通过</el-button>
          <el-button type="info" @click="getData('pending')">正在审核</el-button>
          <el-button type="danger" @click="getData('rejected')">审核拒绝</el-button>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-table style="width: 100%; margin-left: 0; margin-top: 40px;" :data="tableData" table-layout="auto">
        <el-table-column prop="id" label="编号" width="180" sortable></el-table-column>
        <el-table-column prop="title" label="标题" width="380" sortable></el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="180" sortable></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="280" sortable></el-table-column>
        <el-table-column label="操作" width="400">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row.id)">查看</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </el-col>
</template>



<style lang="scss" scoped></style>