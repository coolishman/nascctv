<template>
  <div class="app">
    <TopBar
      :is-authenticated="isAuthenticated"
      @navigate="handleNavigate"
      @new-device="handleNewDevice"
      @logout="handleLogout"
    />
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
            @delete="handleDelete"
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
    <main class="content" v-else-if="view === 'cameras'">
      <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>
      <div v-if="actionMessage" class="toast">{{ actionMessage }}</div>
      <section class="section-card">
        <div class="section-header">
          <h2>摄像头管理</h2>
          <button class="primary" @click="refreshCameras" :disabled="!isAuthenticated">
            刷新列表
          </button>
        </div>
        <CameraList
          v-if="isAuthenticated"
          :cameras="cameras"
          @preview="handlePreview"
          @configure="handleConfigure"
          @delete="handleDelete"
        />
      </section>
    </main>
    <main class="content wall-content" v-else-if="view === 'wall'">
      <div v-if="errorMessage" class="error-banner dark">{{ errorMessage }}</div>
      <div v-if="actionMessage" class="toast dark">{{ actionMessage }}</div>
      <template v-if="isAuthenticated">
        <div class="section-header">
          <h2>电视墙</h2>
          <button class="secondary" @click="handleOpenWallConfig" :disabled="!isAuthenticated">
            配置电视墙
          </button>
        </div>
        <TVWall :wall="wall" />
      </template>
    </main>
    <main class="content" v-else-if="view === 'alerts'">
      <section class="section-card">
        <h2>告警中心</h2>
        <p v-if="isAuthenticated">暂无告警数据，请检查摄像头状态或网络连接。</p>
        <button class="secondary" @click="handleRefreshAlerts" :disabled="!isAuthenticated">
          刷新告警
        </button>
        <ul v-if="isAuthenticated && alertItems.length" class="alert-list">
          <li v-for="alert in alertItems" :key="alert.id">
            <strong>{{ alert.message }}</strong>
            <span>{{ alert.location }}</span>
          </li>
        </ul>
      </section>
    </main>
    <main class="content" v-else>
      <section class="section-card">
        <h2>系统设置</h2>
        <p v-if="isAuthenticated">在这里管理存储策略、用户权限与系统参数。</p>
        <button class="secondary" @click="handleOpenSettings" :disabled="!isAuthenticated">
          打开设置
        </button>
      </section>
    </main>

    <div v-if="showDeviceModal" class="modal">
      <div class="modal-content">
        <header>
          <h3>{{ selectedCamera ? '配置摄像头' : '新建设备' }}</h3>
          <button class="ghost" @click="handleCloseModal">关闭</button>
        </header>
        <div class="modal-grid">
          <label>
            名称
            <input v-model="deviceForm.name" placeholder="摄像头名称" />
          </label>
          <label>
            协议
            <select v-model="deviceForm.protocol">
              <option>ONVIF</option>
              <option>RTSP</option>
              <option>GB/T 28181</option>
            </select>
          </label>
          <label>
            厂商
            <input v-model="deviceForm.vendor" placeholder="厂商" />
          </label>
          <label>
            型号
            <input v-model="deviceForm.model" placeholder="型号" />
          </label>
          <label>
            认证方式
            <select v-model="deviceForm.authType">
              <option>BASIC</option>
              <option>DIGEST</option>
              <option>TOKEN</option>
            </select>
          </label>
          <label>
            状态
            <select v-model="deviceForm.status">
              <option>online</option>
              <option>offline</option>
            </select>
          </label>
          <label class="full">
            流地址
            <input v-model="deviceForm.streamUrl" placeholder="rtsp://..." />
          </label>
          <label class="full">
            位置
            <input v-model="deviceForm.location" placeholder="位置" />
          </label>
        </div>
        <footer>
          <button class="secondary" @click="handleCloseModal">取消</button>
          <button class="primary" @click="handleSaveDevice" :disabled="!isAuthenticated">
            保存
          </button>
        </footer>
      </div>
    </div>

    <div v-if="showPreviewModal" class="modal">
      <div class="modal-content">
        <header>
          <h3>视频预览</h3>
          <button class="ghost" @click="handleCloseModal">关闭</button>
        </header>
        <div class="preview-body">
          <p>{{ selectedCamera?.name }}</p>
          <p class="stream">{{ selectedCamera?.streamUrl }}</p>
          <div class="preview-placeholder">预览窗口占位</div>
        </div>
      </div>
    </div>

    <div v-if="showSettingsModal" class="modal">
      <div class="modal-content">
        <header>
          <h3>系统设置</h3>
          <button class="ghost" @click="handleCloseModal">关闭</button>
        </header>
        <div class="modal-grid">
          <label>
            录像留存天数
            <input type="number" v-model.number="settings.retentionDays" />
          </label>
          <label>
            告警提示音
            <select v-model="settings.alertSound">
              <option :value="true">开启</option>
              <option :value="false">关闭</option>
            </select>
          </label>
          <label>
            电视墙自动轮播
            <select v-model="settings.autoRotate">
              <option :value="true">开启</option>
              <option :value="false">关闭</option>
            </select>
          </label>
        </div>
        <footer>
          <button class="secondary" @click="handleCloseModal">关闭</button>
          <button class="primary" @click="handleSaveSettings" :disabled="!isAuthenticated">
            保存
          </button>
        </footer>
      </div>
    </div>
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
import {
  createCamera,
  deleteCamera,
  fetchCameras,
  fetchRecordings,
  fetchSettings,
  fetchVideoWall,
  updateCamera,
  updateSettings
} from './services/api';

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
const showDeviceModal = ref(false);
const showPreviewModal = ref(false);
const showSettingsModal = ref(false);
const showWallConfigModal = ref(false);
const selectedCamera = ref(null);
const wallForm = reactive({
  name: '',
  description: ''
});
const wallTiles = ref([]);
const deviceForm = reactive({
  name: '',
  protocol: 'RTSP',
  vendor: '',
  model: '',
  authType: 'BASIC',
  streamUrl: '',
  status: 'online',
  location: ''
});
const alertItems = ref([]);
const settings = reactive({
  retentionDays: 30,
  alertSound: true,
  autoRotate: true
});

const refreshCameras = async () => {
  errorMessage.value = '';
  try {
    cameras.value = await fetchCameras();
    metrics.online = cameras.value.filter((camera) => camera.status === 'online').length;
    metrics.offline = cameras.value.filter((camera) => camera.status === 'offline').length;
    metrics.alerts = Math.max(1, Math.floor(cameras.value.length / 3));
    recordings.value = await fetchRecordings(cameras.value);
    wall.value = await fetchVideoWall(1);
    alertItems.value = cameras.value
      .filter((camera) => camera.status === 'offline')
      .map((camera) => ({
        id: camera.id,
        message: `${camera.name} 当前离线，请检查网络或供电。`,
        location: camera.location || '未配置位置'
      }));
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

const handleLogout = () => {
  localStorage.removeItem('nascctv_token');
  isAuthenticated.value = false;
  view.value = 'dashboard';
  cameras.value = [];
  recordings.value = [];
  wall.value = {};
  alertItems.value = [];
  actionMessage.value = '已退出登录';
};

const handleNavigate = (target) => {
  if (!isAuthenticated.value && target !== 'dashboard') {
    view.value = 'dashboard';
    return;
  }
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
  selectedCamera.value = camera;
  showPreviewModal.value = true;
};

const handleConfigure = (camera) => {
  actionMessage.value = `正在配置：${camera.name}`;
  selectedCamera.value = camera;
  Object.assign(deviceForm, {
    name: camera.name,
    protocol: camera.protocol,
    vendor: camera.vendor || '',
    model: camera.model || '',
    authType: camera.authType || 'BASIC',
    streamUrl: camera.streamUrl,
    status: camera.status,
    location: camera.location || ''
  });
  showDeviceModal.value = true;
};

const handleDelete = async (camera) => {
  if (!window.confirm(`确认删除 ${camera.name} 吗？`)) {
    return;
  }
  try {
    await deleteCamera(camera.id);
    actionMessage.value = '设备已删除';
    refreshCameras();
  } catch (error) {
    actionMessage.value = '删除失败，请检查权限或网络连接。';
  }
};

const handleNewDevice = () => {
  actionMessage.value = '进入新建设备流程';
  selectedCamera.value = null;
  Object.assign(deviceForm, {
    name: '',
    protocol: 'RTSP',
    vendor: '',
    model: '',
    authType: 'BASIC',
    streamUrl: '',
    status: 'online',
    location: ''
  });
  showDeviceModal.value = true;
};

const handleRefreshAlerts = () => {
  actionMessage.value = '告警已刷新';
  alertItems.value = cameras.value
    .filter((camera) => camera.status === 'offline')
    .map((camera) => ({
      id: camera.id,
      message: `${camera.name} 当前离线，请检查网络或供电。`,
      location: camera.location || '未配置位置'
    }));
};

const handleOpenSettings = () => {
  actionMessage.value = '系统设置已打开';
  showSettingsModal.value = true;
  if (!isAuthenticated.value) {
    return;
  }
  fetchSettings()
    .then((data) => {
      settings.retentionDays = data.retentionDays ?? settings.retentionDays;
      settings.alertSound = data.alertSound ?? settings.alertSound;
      settings.autoRotate = data.autoRotate ?? settings.autoRotate;
    })
    .catch(() => {
      actionMessage.value = '加载设置失败，请检查权限或网络连接。';
    });
};

const handleSaveSettings = async () => {
  try {
    await updateSettings({
      retentionDays: settings.retentionDays,
      alertSound: settings.alertSound,
      autoRotate: settings.autoRotate
    });
    actionMessage.value = '系统设置已保存';
    showSettingsModal.value = false;
  } catch (error) {
    actionMessage.value = '保存设置失败，请检查权限或网络连接。';
  }
};

const handleSaveDevice = async () => {
  try {
    if (selectedCamera.value?.id) {
      await updateCamera(selectedCamera.value.id, { ...deviceForm });
      actionMessage.value = '设备已更新';
    } else {
      await createCamera({ ...deviceForm });
      actionMessage.value = '设备已保存';
    }
    showDeviceModal.value = false;
    refreshCameras();
  } catch (error) {
    actionMessage.value = '保存失败，请检查权限或网络连接。';
  }
};

const handleCloseModal = () => {
  showDeviceModal.value = false;
  showPreviewModal.value = false;
  showSettingsModal.value = false;
  actionMessage.value = '';
};

onMounted(() => {
  if (isAuthenticated.value) {
    refreshCameras();
    fetchSettings()
      .then((data) => {
        settings.retentionDays = data.retentionDays ?? settings.retentionDays;
        settings.alertSound = data.alertSound ?? settings.alertSound;
        settings.autoRotate = data.autoRotate ?? settings.autoRotate;
      })
      .catch(() => {
        actionMessage.value = '加载设置失败，请检查权限或网络连接。';
      });
  }
});
</script>
