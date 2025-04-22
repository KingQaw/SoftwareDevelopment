<script setup>
import request from '@/utils/request'
import { ref } from 'vue'
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'

const tableData = ref([
    {
        userId: 1,
        account: '',
        nickname: '',
        avatar: '',
        loginTime: '',
        ip: '',
        region: '',
        status: ''
    }
])

const getData = async () => {
    const res = await request.get('/admin/visits')
    console.log(res)
    if (res.status === 200 && res.data.code === 0) {
        tableData.value = res.data.data.records
    }
}

const s2ab = (s) => {
    const buf = new ArrayBuffer(s.length)
    const view = new Uint8Array(buf)
    for (let i = 0; i < s.length; i++) {
        view[i] = s.charCodeAt(i) & 0xFF
    }
    return buf
}

const exportData = () => {
    // 导出数据逻辑
    console.log('导出数据')
    const customFieldName = {
        'userId': '用户ID',
        'account': '账号',
        'nickname': '昵称',
        'loginTime': '登录时间',
        'ip': 'ip地址',
        'region': '地区',
        'status': '登录状态'
    }
    const header = Object.values(customFieldName)
    const data = tableData.value.map(item => {
        return header.map(header => {
            const key = Object.keys(customFieldName).find(key => customFieldName[key] === header)
            return item[key]
        })
    })
    const worksheetData = [header, ...data]
    const worksheet = XLSX.utils.aoa_to_sheet(worksheetData)
    const workbook = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(workbook, worksheet, '登陆记录列表')  
    const wbout = XLSX.write(workbook, { type: 'binary' })
    const blob = new Blob([s2ab(wbout)], { type: 'application/octet-stream' })
    saveAs(blob, '登陆记录列表.xlsx')
}

getData()
</script>



<template>
<div class="manage-strategy-container" style="margin-top: 30px;">
    <el-col>
      <el-row justify="space-between">
        <el-col span="4">
          <h2>访问记录</h2>
        </el-col>
        <el-col span="4">
          <div class="seachInput" style="display: flex; justify-content: flex-end;">
            <el-button type="info" @click="exportData">导出数据</el-button>
          </div>
        </el-col>
      </el-row>
      <el-row style="height: 80vh;">
        <el-table :data="tableData" table-layout="auto" height="100%">
          <el-table-column prop="userId" label="用户id" width="80"></el-table-column>
          <el-table-column label="头像" width="100" align="center">
                <template #default="{row}">
                    <el-avatar v-if="!row.avatar" :size="50" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                    <el-avatar v-else :size="50" :src="'data:image/png;base64,' + row.avatar" />
                </template>
            </el-table-column>
          <el-table-column prop="account" label="账号" width="200"></el-table-column>
          <el-table-column prop="nickname" label="昵称" width="200"></el-table-column>
          <el-table-column prop="loginTime" label="登陆时间" width="220" sortable></el-table-column>
          <el-table-column prop="ip" label="ip地址" width="150"></el-table-column>
          <el-table-column prop="region" label="地区" width="150">
            <template #default="{row}">
              {{ row.region === undefined || row.region === '' || row.region === null ? '未知' : row.region }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="登陆状态" width="100">
            <template #default="{row}">
              <el-tag :type="row.status === '登录成功' ? 'success' : row.status === '密码错误' ? 'danger' : 'warning'">
                {{ row.status }}
              </el-tag>
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