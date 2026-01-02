<template>
  <div class="app">
    <TopBar @navigate="handleNavigate" @new-device="handleNewDevice" />
    <main class="content" v-if="view === 'dashboard'">
      <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>
      <div v-if="actionMessage" class="toast">{{ actionMessage }}</div>
      <section class="hero">
        <div>
          <p class="eyebrow">网络摄像头管理系统</p>
          <h1>统一管理多协议摄像头</h1>
          <p class="subtitle">
            支持 ONVIF、RTSP、GB/T 28181 等主流协议，集中监控、统一权限、实时告警。
          </p>
        </div>
        <LoginPanel v-if="!isAuthenticated" @login-success="handleLogin" />
        <div v-else class="login-panel">
          <h3>已登录</h3>
          <p>当前账号已通过认证，可查看系统数据。</p>
        </div>
      </section>

      <template v-if="isAuthenticated">
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
          <CameraList
            :cameras="cameras"
            @preview="handlePreview"
            @configure="handleConfigure"
          />
        </section>

        <section class="storage">
          <div class="section-header">
            <h2>存储与留存策略</h2>
            <button class="secondary">查看存储池</button>
          </div>
          <RecordingList :recordings="recordings" />
        </section>
      </template>
      <section v-else class="section-card">
        <h2>请先登录</h2>
        <p>登录后可查看摄像头资产、电视墙和告警信息。</p>
      </section>
    </main>
    <main class="content wall-content" v-else-if="view === 'wall'">
      <div v-if="errorMessage" class="error-banner dark">{{ errorMessage }}</div>
      <div v-if="actionMessage" class="toast dark">{{ actionMessage }}</div>
      <template v-if="isAuthenticated">
        <TVWall :wall="wall" />
      </template>
      <section v-else class="section-card">
        <h2>请先登录</h2>
        <p>登录后可查看电视墙内容。</p>
      </section>
    </main>
    <main class="content" v-else-if="view === 'alerts'">
      <section class="section-card">
        <h2>告警中心</h2>
        <p v-if="isAuthenticated">暂无告警数据，请检查摄像头状态或网络连接。</p>
        <p v-else>登录后可查看告警信息。</p>
        <button class="secondary" @click="handleRefreshAlerts" :disabled="!isAuthenticated">
          刷新告警
        </button>
      </section>
    </main>
    <main class="content" v-else>
      <section class="section-card">
        <h2>系统设置</h2>
        <p v-if="isAuthenticated">在这里管理存储策略、用户权限与系统参数。</p>
        <p v-else>登录后可查看系统设置。</p>
        <button class="secondary" @click="handleOpenSettings" :disabled="!isAuthenticated">
          打开设置
        </button>
      </section>
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
const isAuthenticated = ref(Boolean(localStorage.getItem('nascctv_token')));
const metrics = reactive({
  online: 0,
  offline: 0,
  alerts: 0
});
const errorMessage = ref('');
const actionMessage = ref('');

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

const handleLogin = (payload) => {
  isAuthenticated.value = true;
  actionMessage.value = payload?.lastLoginIp
    ? `登录成功，上次登录 IP：${payload.lastLoginIp}`
    : '登录成功，这是首次登录记录。';
  refreshCameras();
};

const handleNavigate = (target) => {
  view.value = target;
  actionMessage.value = '';
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

const handlePreview = (camera) => {
  actionMessage.value = `正在预览：${camera.name}`;
};

const handleConfigure = (camera) => {
  actionMessage.value = `正在配置：${camera.name}`;
};

const handleNewDevice = () => {
  actionMessage.value = '进入新建设备流程';
};

const handleRefreshAlerts = () => {
  actionMessage.value = '告警已刷新（示例）';
};

const handleOpenSettings = () => {
  actionMessage.value = '系统设置已打开（示例）';
};

onMounted(() => {
  if (isAuthenticated.value) {
    refreshCameras();
  }
});
</script>
