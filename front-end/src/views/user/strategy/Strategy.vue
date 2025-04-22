<script setup>
import request from '@/utils/request';
import { onBeforeMount, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

let searchValue = ref('');
let tableData = ref([]);
let currentPage = ref(1);
let pageSize = ref(6);
let pageSizes = ref([6]);
let total = ref(0);
let startDate = ref('');
let endDate = ref('');
let value = ref([])

let tableDataIsEmpty = ref(true);

const handleSizeChange = (val) => {
  pageSize.value = val;
  getData();
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  getData();
};
const getData = async () => {
  console.log(value.value);
  if (value.value !== null) {
    startDate.value = value.value[0] || '';
    endDate.value = value.value[1] || '';
  } else {
    startDate.value = '';
    endDate.value = '';
  }
  const params = {
    page: currentPage.value,
    size: pageSize.value,
    title: searchValue.value,
    startAt: startDate.value,
    endAt: endDate.value,
    auditStatus: 'approved'
  }
  console.log('params: ', params);
  try {
    const res = await request.get('/guides' + "?" + new URLSearchParams(params));
    console.log(res);
    if (res.status === 200 && res.data.code === 0) {
      total.value = res.data.data.total;
      tableData.value = res.data.data.records;
      console.log(tableData.value);
      tableDataIsEmpty.value = tableData.value.length === 0;
      console.log(tableData.size);
      console.log(tableDataIsEmpty.value);
    }
  } catch (error) {
    console.log(error);
  }
}

const getStrategyDetail = (strategyId) => {
  console.log(strategyId);
  router.push('/strategy/' + strategyId);
}

onMounted(() => {
  getData();
});
onBeforeMount(() => {
  getData();  
})

</script>



<template>
  <el-col style="width: 80%; margin-left: 10%; margin-top: 40px;">
    <el-row justify="space-between">
      <el-col span="4">
        <h1>旅游指南</h1>
      </el-col>
      <el-col span="4">
        <div class="seachInput" style="display: flex; justify-content: flex-end;">
          <el-date-picker type="daterange" v-model="value" value-format="YYYY-MM-DD" format="YYYY-MM-DD" clearable start-placeholder="发布时间起始" end-placeholder="发布时间结束" />
          <el-input placeholder="请输入搜索内容" v-model="searchValue" clearable style="width: 200px;"></el-input>
          <el-button type="primary" @click="getData">搜索</el-button>
        </div>
      </el-col>
    </el-row>
    <el-row style="display: flex; justify-content: center; width: 100%;">
      <el-empty description="空空如也~" image-size="300px" v-if="tableDataIsEmpty"></el-empty>
      <el-row :gutter="20" class="grid" v-else justify="space-around" style="width: 100%;">
        <el-col :span="8" v-for="(item, index) in tableData" :key="index" style="margin-top: 10px;">
          <el-card :body-style="{ padding: '0px', height: '310px'}" @click="getStrategyDetail(item.id)">
            <img :src="'data:image/png;base64,' + item.cover" class="image" alt="item.title"
              style="object-fit: cover; width: 100%; height: 75%; background-color: aqua;" />
            <div style="margin-left: 3px;">
              <div class="title">{{ item.title }}</div>
              <div class="author-container" style="display: flex; align-items: center;">
                <el-icon color="#666" size="0.9em">
                  <Bell />
                </el-icon>
                <div class="text">{{ item.likesCount }}</div>
                <el-icon color="#666" size="0.9em">
                  <StarFilled />
                </el-icon>
                <div class="text">{{ item.favoritesCount }}</div>
                <el-icon color="#666" size="0.9em">
                  <Comment />
                </el-icon>
                <div class="text">{{ item.commentsCount }}</div>
                <el-icon color="#666" size="0.9em">
                  <User />
                </el-icon>
                <div class="text">{{ item.nickname }}</div>
                <div class="text">{{ item.createTime.split(' ')[0] }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-row>
    <el-row style="display: flex; justify-content: center; margin-top: 10px;">
      <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="pageSizes"
        size="default" background="true" layout="total, sizes, prev, pager, next" :total="total"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
  </el-col>
</template>



<style lang="scss" scoped>
.title {
  background-color: none;
  width: 100%;
  white-space: normal;
  word-wrap: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.5em;
  height: 3em;
  font-weight: bold;
}

.text {
  margin-top: 5px;
  margin-left: 5px;
  color: #666;
  font-size: 0.9em;
  margin-right: 5px;
  margin-bottom: 5px;
}
</style>