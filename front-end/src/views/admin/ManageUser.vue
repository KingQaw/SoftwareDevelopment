<template>
    <div class="manage-user-container">
        <div style="display: flex; justify-content: space-between;">
            <h2 class="page-title">用户管理</h2>
            <div>
                <el-button type="success" @click="importData" style="margin-right: 40px; margin-top: 20px;">导入数据</el-button>
                <el-button type="info" @click="exportData" style="margin-right: 40px; margin-top: 20px;">导出数据</el-button>
            </div>
        </div>
        <el-table :data="userList" border style="width: 100%">
            <el-table-column prop="id" label="用户ID" width="100" align="center" sortable/>
            <el-table-column label="头像" width="100" align="center">
                <template #default="{row}">
                    <el-avatar v-if="!row.avatar" :size="50" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                    <el-avatar v-else :size="50" :src="'data:image/png;base64,' + row.avatar" />
                </template>
            </el-table-column>
            <el-table-column prop="account" label="账号" width="180" align="center" />
            <el-table-column prop="nickname" label="昵称" width="180" align="center" />
            <el-table-column prop="guidesPublishedCount" label="发布攻略数" width="120" align="center" sortable/>
            <el-table-column prop="likesReceivedCount" label="获赞数" width="120" align="center" sortable/>
            <el-table-column prop="favoritesGivenCount" label="收藏数" width="120" align="center" sortable></el-table-column>
            <el-table-column prop="createTime" label="注册时间" width="180" align="center" />
            <el-table-column label="权限管理" width="180" align="center">
                <template #default="{row}">
                    <el-select 
                        v-model="row.status" 
                        @change="handleStatusChange(row)"
                        placeholder="选择权限"
                    >
                        <el-option label="正常" value="正常" />
                        <el-option label="封号" value="封号" />
                        <el-option label="禁言" value="禁言" />
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="120" align="center">
                <template #default="{row}">
                    <el-button type="primary" @click="handleResetPassword(row.id)">修改密码</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import Swal from 'sweetalert2'
import { netError } from '@/utils/swal'
import request from '@/utils/request'
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'

const userList = ref([
    // 示例数据
    {
        id: 1,
        avatar: '',
        account: 'user1',
        nickname: '用户1',
        guidesPublishedCount: 5,
        likesReceivedCount: 10,
        favoritesGivenCount: 5,
        createTime: '2024-06-01 10:00:00',
        status: ''
    }
])

const getUserList = async () => {
    try {
        const res = await request.get('/admin/users')
        if (res.status === 200 && res.data.code === 0) {
            console.log(res)
            console.log(res.data.data.records)
            userList.value = res.data.data.records
        } else {
            Swal.fire({
                icon: 'error',
                title: '获取用户列表失败',
                text: '请稍后重试',
           })
        }
    } catch (error) {
        console.log(error)
        netError()
    }
}

const handleStatusChange = async (user) => {
    if (user.status === '封号' || user.status === '禁言') {
        await Swal.fire({
            title: '确认操作',
            text: `确定要将用户 ${user.nickname} ${user.status}吗？`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then((result) => {
            if (!result.isConfirmed) {
                // 取消操作则恢复原状态
                user.status = '正常'
                return;
            }
        })
    }
    const res = await request.post('/admin/users/status', { id: user.id, status: user.status })
    if (res.status === 200 && res.data.code === 0) {
        Swal.fire({
            icon: 'success',
            title: '操作成功',
            text: `用户 ${user.nickname} 的权限已更新为 ${user.status}`,
        }) 
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
        'id' : '用户ID',
        'account' : '账号',
        'nickname' : '昵称',
        'guidesPublishedCount' : '发布攻略数',
        'likesReceivedCount' : '获赞数',
        'favoritesGivenCount' : '收藏数',
        'createTime' : '注册时间',
        'status' : '状态'
    }
    const header = Object.values(customFieldName)
    const data = userList.value.map(item => {
        return header.map(header => {
            const key = Object.keys(customFieldName).find(key => customFieldName[key] === header)
            return item[key]
        })
    })
    const worksheetData = [header, ...data]
    const worksheet = XLSX.utils.aoa_to_sheet(worksheetData)
    const workbook = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(workbook, worksheet, '用户列表')  
    const wbout = XLSX.write(workbook, { type: 'binary' })
    const blob = new Blob([s2ab(wbout)], { type: 'application/octet-stream' })
    saveAs(blob, '用户列表.xlsx')
}

const importData = async () => {
    // 导入数据逻辑
    console.log('导入数据')
    // 弹出 Swal 对话框，提示用户选择文件
    let mresult = null
    await Swal.fire({
        title: '选择文件',
        input: 'file',
        inputAttributes: {
            'accept': '.xlsx, .xls',  // 只接受 Excel 文件
            'aria-label': '上传文件'
        },
        showCancelButton: true,
        confirmButtonText: '上传',
        cancelButtonText: '取消',
        inputValidator: (value) => {
            if (!value) {
                return '请选择文件'
            }
        }
    }).then((result) => {
        if (!result.isConfirmed) {
            return;
        } else {
            console.log(result)
            const file = result.value
            const reader = new FileReader()
            reader.onload = async (e) => {
                // 将指定字段值映射为自定义字段名，将每行数据转换为对象，存储在数组中
                const data = XLSX.read(e.target.result, { type: 'binary' })
                const sheetName = data.SheetNames[0]  // 获取第一个工作表
                const worksheet = data.Sheets[sheetName]  // 获取第一个工作表的数据
                const jsonData = XLSX.utils.sheet_to_json(worksheet)  // 将工作表转换为 JSON 格式
                console.log(jsonData)
                const customFieldName = {
                    '账号': 'account',
                    '昵称': 'nickname',
                    '密码': 'password',
                }
                // 不是上述字段的字段，直接跳过
                const mappedData = jsonData.map(item => {
                    const mappedItem = {
                        account: item['账号'],
                        nickname: item['昵称'],
                        password: item['密码'],
                    } 
                    return mappedItem
                })
                console.log(mappedData)
                // TODO: 验证数据格式
                // 调用接口，将数据发送到后端
                const res = await request.post('/admin/users/import', mappedData)
                console.log(res)
                if (res.status === 200 && res.data.code === 0) {
                    // 存储未导入的数据
                    mresult = res.data.data
                    const total = res.data.data.total;
                    const error = res.data.data.error;
                    const errorAccounts = res.data.data.errorAccounts;
                    Swal.fire({
                        icon:'success',
                        title: `共 ${total} 条数据，导入失败 ${error} 条`,
                        text: `导入失败的账号：${errorAccounts.join(', ')}`,
                    }) 
                    getUserList()
                }
            }
            reader.readAsArrayBuffer(file)
        }
    })
}

const handleResetPassword = async (userId) => {
    await Swal.fire({
        title: '重置密码',
        text: '请输入新密码',
        input: 'password'
    }).then((result) => {
        if (result.isConfirmed) {
            const newPassword = result.value
            const res = request.post('/admin/modifyPassword', { id: userId, password: newPassword });
            if (res.status === 200 && res.data.code === 0) {
                Swal.fire({
                    icon: 'success',
                    title: '密码修改成功',
                    text: '密码已成功修改',
                }) 
            }
        }
    })
}

onMounted(() => {
    getUserList()
})
</script>

<style lang="scss" scoped>
.manage-user-container {
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    
    .page-title {
        margin-bottom: 20px;
        text-align: left;  // 修改这里：从center改为left
        color: #333;
    }
    
    .el-table {
        margin-top: 20px;
    }
}
</style>