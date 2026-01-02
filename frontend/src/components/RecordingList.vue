<template>
  <div class="recording-list">
    <header>
      <h3>录像存储概览</h3>
      <p>集中存储与留存策略</p>
    </header>
    <div class="recording-grid">
      <article v-for="recording in recordings" :key="recording.id" class="recording-card">
        <div>
          <h4>{{ recording.cameraName }}</h4>
          <p>{{ recording.window }}</p>
        </div>
        <div class="recording-meta">
          <span>{{ recording.format }}</span>
          <span>{{ recording.size }}</span>
          <span>留存 {{ recording.retentionDays }} 天</span>
        </div>
      </article>
    </div>
  </div>
</template>

<script setup>
defineProps({
  recordings: {
    type: Array,
    default: () => []
  }
});
</script>
