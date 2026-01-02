<template>
  <div class="camera-grid">
    <article v-for="camera in cameras" :key="camera.id" class="camera-card">
      <div class="camera-header">
        <div>
          <h4>{{ camera.name }}</h4>
          <p>{{ camera.location || '未设置位置' }}</p>
        </div>
        <span :class="['status', camera.status]">{{ camera.status }}</span>
      </div>
      <div class="camera-meta">
        <div>
          <span class="label">协议</span>
          <strong>{{ camera.protocol }}</strong>
        </div>
        <div>
          <span class="label">厂商 / 型号</span>
          <strong>{{ camera.vendor || '通用' }} · {{ camera.model || '标准型号' }}</strong>
        </div>
        <div>
          <span class="label">认证方式</span>
          <strong>{{ camera.authType || 'BASIC' }}</strong>
        </div>
        <div>
          <span class="label">流地址</span>
          <strong class="truncate">{{ camera.streamUrl }}</strong>
        </div>
      </div>
      <div class="camera-actions">
        <button class="secondary">预览</button>
        <button class="ghost">配置</button>
      </div>
    </article>
  </div>
</template>

<script setup>
defineProps({
  cameras: {
    type: Array,
    default: () => []
  }
});
</script>
