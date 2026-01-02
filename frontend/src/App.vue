<template>
  <div class="app">
    <TopBar @navigate="handleNavigate" />
    <main class="content" v-if="view === 'dashboard'">
      <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>
      <section class="hero">
        <div>
          <p class="eyebrow">网络摄像头管理系统</p>
          <h1>统一管理多协议摄像头</h1>
          <p class="subtitle">
            支持 ONVIF、RTSP、GB/T 28181 等主流协议，集中监控、统一权限、实时告警。
          </p>
        </div>
        <LoginPanel @login-success="handleLogin" />
      </section>

      <section class="stats">
        <StatsCard title="在线摄像头" :value="metrics.online" trend="+12%" />
        <StatsCard title="离线摄像头" :value="metrics.offline" trend="-3%" />
        <StatsCard title="今日告警" :value="metrics.alerts" trend="+5" />
      </section>

      <section class="cameras">
        <div class="section-header">
          <h2>摄像头资产概览</h2>
          <button class="primary" @click="refreshCameras">刷新列表</button>
        </div>
        <CameraList :cameras="cameras" />
      </section>

      <section class="storage">
        <div class="section-header">
          <h2>存储与留存策略</h2>
          <button class="secondary">查看存储池</button>
        </div>
        <RecordingList :recordings="recordings" />
      </section>
    </main>
    <main class="content wall-content" v-else>
      <div v-if="errorMessage" class="error-banner dark">{{ errorMessage }}</div>
      <TVWall :wall="wall" />
    </main>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import TopBar from './components/TopBar.vue';
import LoginPanel from './components/LoginPanel.vue';
import CameraList from './components/CameraList.vue';
import RecordingList from './components/RecordingList.vue';
import StatsCard from './components/StatsCard.vue';
import TVWall from './components/TVWall.vue';
import { fetchCameras, fetchRecordings, fetchVideoWall } from './services/api';

const cameras = ref([]);
const recordings = ref([]);
const wall = ref({});
const view = ref('dashboard');
const metrics = reactive({
  online: 0,
  offline: 0,
  alerts: 0
});
const errorMessage = ref('');

const refreshCameras = async () => {
  errorMessage.value = '';
  try {
    cameras.value = await fetchCameras();
    metrics.online = cameras.value.filter((camera) => camera.status === 'online').length;
    metrics.offline = cameras.value.filter((camera) => camera.status === 'offline').length;
    metrics.alerts = Math.max(1, Math.floor(cameras.value.length / 3));
    recordings.value = await fetchRecordings(cameras.value);
    wall.value = await fetchVideoWall(1);
  } catch (error) {
    errorMessage.value = '无法连接后端服务，请检查数据库与接口配置。';
    cameras.value = [];
    recordings.value = [];
    wall.value = {};
  }
};

const handleLogin = () => {
  refreshCameras();
};

const handleNavigate = (target) => {
  view.value = target;
  if (target === 'wall' && !wall.value.tiles?.length) {
    fetchVideoWall(1)
      .then((data) => {
        wall.value = data;
      })
      .catch(() => {
        errorMessage.value = '无法加载电视墙数据，请检查后端服务。';
      });
  }
};

onMounted(() => {
  refreshCameras();
});
</script>
