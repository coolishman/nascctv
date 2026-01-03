<template>
  <header class="topbar">
    <div class="logo">
      <span class="logo-mark">NC</span>
      <div>
        <h3>NAS CCTV</h3>
        <p>安全 · 合规 · 高可用</p>
      </div>
    </div>
    <nav class="nav-links">
      <button class="link" @click="$emit('navigate', 'dashboard')">仪表盘</button>
      <button class="link" @click="$emit('navigate', 'cameras')">摄像头</button>
      <button class="link" @click="$emit('navigate', 'wall')">电视墙</button>
      <button class="link" @click="$emit('navigate', 'alerts')">告警中心</button>
      <button class="link" @click="$emit('navigate', 'settings')">系统设置</button>
    </nav>
    <div class="topbar-actions">
      <button class="secondary" :disabled="!isAuthenticated" @click="$emit('new-device')">
        新建设备
      </button>
      <button class="ghost" v-if="isAuthenticated" @click="$emit('logout')">
        退出登录
      </button>
    </div>
  </header>
</template>

<script setup>
defineProps({
  isAuthenticated: {
    type: Boolean,
    default: false
  }
});
defineEmits(['navigate', 'new-device', 'logout']);
</script>
