<template>
  <div class="app">
    <TopBar />
    <main class="content">
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
    </main>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import TopBar from './components/TopBar.vue';
import LoginPanel from './components/LoginPanel.vue';
import CameraList from './components/CameraList.vue';
import StatsCard from './components/StatsCard.vue';
import { fetchCameras } from './services/api';

const cameras = ref([]);
const metrics = reactive({
  online: 0,
  offline: 0,
  alerts: 0
});

const refreshCameras = async () => {
  cameras.value = await fetchCameras();
  metrics.online = cameras.value.filter((camera) => camera.status === 'online').length;
  metrics.offline = cameras.value.filter((camera) => camera.status === 'offline').length;
  metrics.alerts = Math.max(1, Math.floor(cameras.value.length / 3));
};

const handleLogin = () => {
  refreshCameras();
};

onMounted(() => {
  refreshCameras();
});
</script>
