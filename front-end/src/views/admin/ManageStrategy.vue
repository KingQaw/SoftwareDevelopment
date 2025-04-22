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
let totalStrategies = ref(0);
let value = ref([]);
let searchValue = ref('');
let tableData = ref([
  {
    id: 1,
    title: "攻略标题",
    nickname: "作者",
    auditStatus: "published",
    likesCount: 100,
    favoritesCount: 50,
    createTime: "2022-01-01"
  }
]);

const getData = async () => {
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
    startAt: startDate.value,
    endAt: endDate.value
  }
  try {
    const res = await request.get('/guides' + "?" + new URLSearchParams(param));
    if (res.status === 200 && res.data.code === 0) {
      console.log(res.data.data);
      totalStrategies.value = res.data.data.total;
      tableData.value = res.data.data.records;
    } else {
      Swal.fire({
        icon: 'error',
        title: '获取数据失败',
        text: '请稍后重试',
      })
    }
  } catch (error) {
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
  XLSX.utils.book_append_sheet(workbook, worksheet, '攻略列表');
  const wbout = XLSX.write(workbook, { type: 'binary' });
  const blob = new Blob([s2ab(wbout)], { type: 'application/octet-stream' });
  saveAs(blob, '攻略列表.xlsx');
}

const viewDetail = (strategyId) => {
  router.push('/strategyDetail/' + strategyId);
}

const handleStatusChange = (strategy) => {
  console.log(strategy);
  if (strategy.auditStatus === "pending" || strategy.auditStatus === "rejected") {
    Swal.fire({
      title: '确认操作',
      text: `确定要将攻略 "${strategy.title}" ${strategy.auditStatus === 'pending' ? '未审核' : '未通过'}吗？`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    }).then((result) => {
      if (!result.isConfirmed) {
        strategy.auditStatus = 'approved'
        const param = {
          id: strategy.id,
          status: "approved",
        }
        request.post('/admin/audit', param)
      } else {
        const param = {
          id: strategy.id,
          status: strategy.auditStatus,
        }
        request.post('/admin/audit', param)
      }
    })
    return;
  } else if (strategy.auditStatus === "approved") {
    const param = {
      id: strategy.id,
      status: strategy.auditStatus, 
    }
    console.log(param);
    request.post('/admin/audit', param)
  }
}

const handleDelete = async (strategyId) => {
  console.log(strategyId);
  await Swal.fire({
    title: '确认操作',
    text: `确定要删除攻略 "${strategyId}" 吗？`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then((result) => {
    if (result.isConfirmed) {
      const res = request.delete('/admin/guides/' + strategyId);
      if (res.status === 200 && res.data.code === 0) {
        Swal.fire({
          icon: 'success',
          title: '删除成功',
        }) 
      }
    }
  })
  getData();
}

getData();
</script>

<template>
  <div class="manage-strategy-container" style="margin-top: 30px;">
    <el-col>
      <el-row justify="space-between">
        <el-col span="4">
          <h1 style="font-size: 20px;">攻略总数：{{ totalStrategies }}</h1>
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
          <el-table-column prop="id" label="编号" width="80"></el-table-column>
          <el-table-column prop="title" label="标题" width="450"></el-table-column>
          <el-table-column prop="nickname" label="作者" width="120"></el-table-column>
          <el-table-column prop="auditStatus" label="状态" width="100" sortable>
            <template #default="{row}">
              <el-tag :type="row.auditStatus === 'approved' ? 'success' : row.auditStatus === 'pending' ? 'warning' : 'danger'">
                {{ row.auditStatus === 'approved' ? '已通过' : row.auditStatus === 'pending' ? '未审核' : '未通过' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="likesCount" label="点赞数" width="100" sortable></el-table-column>
          <el-table-column prop="favoritesCount" label="收藏数" width="100" sortable></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="150" sortable></el-table-column>
          <el-table-column label="操作" width="340">
            <template #default="scope">
              <el-button type="primary" @click="viewDetail(scope.row.id)">查看详情</el-button>
              <el-select 
                v-model="scope.row.auditStatus" 
                @change="handleStatusChange(scope.row)"
                placeholder="修改状态"
                size="small"
                style="margin-left: 10px; width: 100px;"
              >
                <el-option label="已通过" value="approved" />
                <el-option label="未审核" value="pending" />
                <el-option label="未通过" value="rejected" />
              </el-select>
              <el-button type="danger" @click="handleDelete(scope.row.id)" style="margin-left: 10px;">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </el-col>
  </div>
</template>

<style scoped>
.manage-strategy-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>