<script setup>
import { ref } from 'vue';
import { netError } from '@/utils/swal';
import request from '@/utils/request';
import Swal from 'sweetalert2';
import { useRouter } from 'vue-router';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';

const router = useRouter();

let startDate = ref('');
let endDate = ref('');
let totalToAdudit = ref(0);
let value = ref([]);
let searchValue = ref('');
let tableData = ref([
  {
    strategyId: 1,
    title: "title",
    scenic: "scenic",
    content: "content",
    author: "author",
    createTime: "2022-01-01"
  }
]);
const getData = async () => {
  console.log('getData');

  if (value.value !== null) {
    startDate.value = value.value[0] || '';
    endDate.value = value.value[1] || ''
  } else {
    startDate.value = '';
    endDate.value = '';
  }
  const param = {
    page: 1,
    size: 1000,
    title: searchValue.value,
    auditStatus: 'pending',
    startAt: startDate.value,
    endAt: endDate.value
  }
  try {
    console.log(param);
    const res = await request.get('/guides' + "?" + new URLSearchParams(param));
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      totalToAdudit.value = res.data.data.total;
      tableData.value = res.data.data.records;
    } else {
      console.log('获取数据失败')
      Swal.fire({
        icon: 'error',
        title: '获取数据失败',
        text: '请稍后重试',
      })
    }
  } catch (error) {
    console.log(error);
    netError();
  }
}

const s2ab = (s) => {
  const buf = new ArrayBuffer(s.length);
  const view = new Uint8Array(buf);
  for (let i = 0; i < s.length; i++) {
    view[i] = s.charCodeAt(i) & 0xFF;
  }
  return buf;
}
const exportData = () => {
  console.log('导出数据');
  // 去除tableData中不在列中的字段，
  const updateList = tableData.value.map(({auditTime, avatar, content, cover, ...rest }) => rest)
  const customFieldName = {
    'id': '编号',
    'title': '标题',
    'userId': '作者ID',
    'nickname': '作者',
    'createTime': '创建时间',
    'favoritesCount': '收藏数',
    'likesCount': '点赞数',
    'commentsCount': '评论数',
    'auditStatus': '审核状态',
  }

  const header = Object.values(customFieldName);
  const data = updateList.map(item => {
    return header.map(header => {
      const key = Object.keys(customFieldName).find(key => customFieldName[key] === header)
      return item[key]
    })
  })
  const worksheetData = [header, ...data]
  const worksheet = XLSX.utils.aoa_to_sheet(worksheetData);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, '待审核攻略列表');
  const wbout = XLSX.write(workbook, { type: 'binary' });
  const blob = new Blob([s2ab(wbout)], { type: 'application/octet-stream' });
  saveAs(blob, '待审核攻略列表.xlsx');
}

const auditStrategyDetail = (strategyId) => {
  console.log('审核详情' + strategyId);
  router.push('/auditStrategyDetail/' + strategyId);
}

getData();
</script>



<template>
  <div class="audit-strategy-container" style="margin-top: 30px;">
    <el-col>
      <el-row justify="space-between">
        <el-col span="4">
          <h1 style="font-size: 20px;">待审核指南：{{ totalToAdudit }}</h1>
        </el-col>
        <el-col span="4">
          <div class="seachInput" style="display: flex; justify-content: flex-end;">
            <el-date-picker type="daterange" v-model="value" value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable
              start-placeholder="发布时间起始" end-placeholder="发布时间结束" />
            <el-input placeholder="请输入搜索内容" v-model="searchValue" clearable style="width: 200px;"></el-input>
            <el-button type="primary" @click="getData">搜索</el-button>
            <el-button type="info" @click="exportData">导出数据</el-button>
          </div>
        </el-col>
      </el-row>
      <el-row style="height: 80vh;">
        <el-table :data="tableData" table-layout="auto" height="100%">
          <el-table-column prop="id" label="编号" width="180"></el-table-column>
          <el-table-column prop="title" label="标题" width="380"></el-table-column>
          <el-table-column prop="nickname" label="作者" width="200"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" sortable></el-table-column>
          <el-table-column prop="操作">
            <template #default="scope">
              <el-button type="primary" @click="auditStrategyDetail(scope.row.id)">审核</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </el-col>
  </div>
</template>



<style scoped></style>