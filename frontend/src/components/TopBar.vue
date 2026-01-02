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
      <button class="link" @click="$emit('navigate', 'wall')">电视墙</button>
      <button class="link">告警中心</button>
      <button class="link">系统设置</button>
    </nav>
    <button class="secondary">新建设备</button>
  </header>
</template>
