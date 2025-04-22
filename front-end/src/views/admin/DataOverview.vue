<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'
import { netError } from '@/utils/swal'

// 审核数据保持不变
const stats = ref({
  pendingGuidesCounts: 42,
  approvedGuidesCounts: 128,
  rejectedGuidesCounts: 95
})

// 平台数据相关
const timeRange = ref('7days')
const chartRef = ref(null)
let chartInstance = null

const platformData = ref({
  '7days': {
    date: ['6/1', '6/2', '6/3', '6/4', '6/5', '6/6', '6/7'],
    userCounts: [120, 132, 101, 134, 90, 230, 210],
    guideCounts: [45, 52, 41, 54, 35, 72, 68]
  }, 
  '1month': {
    date: ['5/1', '5/8', '5/15', '5/22', '5/29'],
    userCounts: [420, 532, 401, 534, 690],
    guideCounts: [145, 182, 151, 194, 175]
  },
 
})

const visitData = ref({
  '7days': {
    date: ['6/1', '6/2', '6/3', '6/4', '6/5', '6/6', '6/7'],
    visitCounts: [320, 432, 301, 434, 390, 530, 510]
  },
  '1month': {
    date: ['5/1', '5/8', '5/15', '5/22', '5/29'],
    visitCounts: [820, 932, 801, 934, 1090]
  }
})

// 新增地区访问数据
const regionData = ref([
  { value: 1048, name: '华东' },
  { value: 735, name: '华北' },
  { value: 580, name: '华南' },
  { value: 484, name: '华中' },
  { value: 300, name: '其他' }
])

const initChart = () => {
  chartInstance = echarts.init(chartRef.value)
  updateChart()
}

// 新增访问量数据相关
const visitTimeRange = ref('7days')
const visitChartRef = ref(null)
let visitChartInstance = null

const regionChartRef = ref(null)
let regionChartInstance = null

const getPlatformData = async () => {
  const res = await request.get('/admin/overview');
  if (res.status === 200 && res.data.code === 0) {
    console.log(res.data.data);
    platformData.value['7days'] = res.data.data.sevenDayData;
    platformData.value['1month'] = res.data.data.monthData;
    stats.value.approvedGuidesCounts = res.data.data.approvedGuidesCounts;
    stats.value.rejectedGuidesCounts = res.data.data.rejectedGuidesCounts;
    stats.value.pendingGuidesCounts = res.data.data.pendingGuidesCounts;
    visitData.value['7days'] = res.data.data.visitSevenData;
    visitData.value['1month'] = res.data.data.visitMonthData;
    regionData.value = res.data.data.regionData;
    initChart()
    initVisitChart()
    initRegionChart()
  } else {
    netError();
  }
}

const updateChart = () => {
  const data = platformData.value[timeRange.value]
  console.log(data.date);
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['用户数量', '攻略数量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,

      data: data.date
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '用户数量',
        type: 'line',
        data: data.userCounts,
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#5470C6'
        },
        itemStyle: {
          color: '#5470C6'
        }
      },
      {
        name: '攻略数量',
        type: 'line',
        data: data.guideCounts,
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#91CC75'
        },
        itemStyle: {
          color: '#91CC75'
        }
      }
    ]
  }
  
  chartInstance.setOption(option)
}

const exportChart = () => {
  if (!chartInstance) return;
  const url = chartInstance.getDataURL({
    type: 'png',
    pixelRatio: 2,
    backgroundColor: '#fff'
  });
  const link = document.createElement('a');
  link.href = url;
  link.download = `平台数据概览-${timeRange.value === '7days' ? '近7天' : '近1个月'}-${new Date().toLocaleDateString()}.png`;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

const exportChart1 = () => {
  if (!visitChartInstance) return;
  const url = visitChartInstance.getDataURL({
    type: 'png',
    pixelRatio: 2,
    backgroundColor: '#fff'
  });
  const link = document.createElement('a');
  link.href = url
  link.download = `访问量数据概览-${visitTimeRange.value === '7days'? '近7天' : '近1个月'}-${new Date().toLocaleDateString()}.png`;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

const exportChart2 = () => {
  if (!regionChartInstance) return;
  const url = regionChartInstance.getDataURL({
    type: 'png',
    pixelRatio: 2,
    backgroundColor: '#fff'
  });
  const link = document.createElement('a');
  link.href = url;
  link.download = `地区访问量数据概览-${new Date().toLocaleDateString()}.png`;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

const initVisitChart = () => {
  visitChartInstance = echarts.init(visitChartRef.value)
  updateVisitChart()
}

const updateVisitChart = () => {
  const data = visitData.value[visitTimeRange.value]
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['访问量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,  // 新增
      data: data.date,
      axisLabel: {
        interval: 1 // 确保所有标签都显示
      }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '访问量',
        type: 'line',  // 改为折线图
        data: data.visitCounts,
        smooth: true,  // 新增平滑效果
        lineStyle: {
          width: 3,  // 新增线宽
          color: '#5470A0'  // 线条颜色
        },
        itemStyle: {
          color: '#5470A0'  // 点颜色
        },
        label: {
          show: true,  // 显示数值标签
          position: 'top'  // 标签位置
        }
      }
    ]
  }
  
  visitChartInstance.setOption(option)
}

const initRegionChart = () => {
  regionChartInstance = echarts.init(regionChartRef.value)
  updateRegionChart()
}

const updateRegionChart = () => {
  const option = {
    title: {
      text: '各地区访问量占比',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      show: false // 隐藏外部图例
    },
    series: [
      {
        name: '访问量',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '50%'],
        label: {
          show: true,
          formatter: '{b}: {d}%', // 显示名称和百分比
          position: 'outside', // 标签显示在外部
          alignTo: 'edge', // 标签对齐边缘
          margin: 20 // 标签与饼图间距
        },
        labelLine: {
          show: true, // 显示引导线
          length: 20, // 第一段线长度
          length2: 30 // 第二段线长度
        },
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        data: regionData.value
      }
    ]
  }
  regionChartInstance.setOption(option)
}

// 在onMounted中添加初始化
onMounted(() => {
  getPlatformData();
})
</script>

<template>
  <div class="audit-data-section">
    <h2 class="section-title">审核数据概览</h2>
    <div class="stats-container">
      <!-- 原有三个卡片保持不变 -->
      <div class="stat-card">
        <h3>待审核</h3>
        <p class="stat-value">{{ stats.pendingGuidesCounts }}</p>
      </div>
      <div class="stat-card">
        <h3>已通过</h3>
        <p class="stat-value">{{ stats.approvedGuidesCounts }}</p>
      </div>
      <div class="stat-card">
        <h3>已拒绝</h3>
        <p class="stat-value">{{ stats.rejectedGuidesCounts }}</p>
      </div>
    </div>
  </div>

  <div class="platform-data-section">
    <div class="section-header">
      <h2 class="section-title">平台数据概览</h2>
      <div class="time-selector">
        <select v-model="timeRange" @change="updateChart">
          <option value="7days">近7天</option>
          <option value="1month">近1个月</option>
        </select>
        <button @click="exportChart" class="export-btn">导出图表</button>
      </div>
    </div>
    <div ref="chartRef" class="chart-container"></div>
  </div>

  <!-- 修改后的访问量模块 -->
  <div class="visit-data-section">
    <div class="section-header">
      <h2 class="section-title">平台访问数据</h2>
      <div class="time-selector">
        <select v-model="visitTimeRange" @change="updateVisitChart">
          <option value="7days">近7天</option>
          <option value="1month">近1个月</option>
        </select>
        <button @click="exportChart1" class="export-btn">导出图表</button>
      </div>
    </div>
    <div ref="visitChartRef" class="chart-container"></div>
    
    <!-- 新增分割线和饼图 -->
    <div class="divider"></div>
    <div style="display: flex; flex-direction:row; justify-content: space-between;">
      <h2 class="section-title">地区访问数据</h2>
      <button @click="exportChart2" class="export-btn">导出图表</button>
    </div>
    <div ref="regionChartRef" class="chart-container"></div>
  </div>
</template>

<style scoped>
.audit-data-section {
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 20px;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.section-title {
  margin: 0 0 20px 0;
  font-size: 20px;
  color: #333;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.stats-container {
  display: flex;
  gap: 20px;
  padding: 20px;
}

.stat-card {
  flex: 1;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  color: white; /* 统一文字颜色为白色 */
}

.stat-card:nth-child(1) {
  background: linear-gradient(135deg, #FF9A9E 0%, #FAD0C4 100%); /* 待审核 - 粉红色渐变 */
}

.stat-card:nth-child(2) {
  background: linear-gradient(135deg, #A1C4FD 0%, #C2E9FB 100%); /* 已审核 - 蓝色渐变 */
}

.stat-card:nth-child(3) {
  background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%); /* 已通过 - 绿色渐变 */
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin-top: 10px;
}

.audit-data-section {
  margin-bottom: 30px;
}

.platform-data-section {
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 20px;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.time-selector {
  display: flex;
  gap: 10px;
  align-items: center;
}

.export-btn {
  padding: 8px 12px;
  border-radius: 4px;
  border: 1px solid #ddd;
  background-color: #f9f9f9;
  cursor: pointer;
  transition: all 0.3s;
}

.export-btn:hover {
  background-color: #e9e9e9;
}
.chart-container {
  width: 100%;
  height: 400px;
}

/* 新增访问量模块样式 */
.visit-data-section {
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 20px;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-top: 30px;
}

.divider {
  height: 1px;
  background-color: #e0e0e0;
  margin: 20px 0;
}
</style>