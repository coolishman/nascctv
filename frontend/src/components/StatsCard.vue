<template>
  <div class="stats-card">
    <div>
      <p class="label">{{ title }}</p>
      <h3>{{ value }}</h3>
    </div>
    <span class="trend">{{ trend }}</span>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  value: Number,
  trend: String
});
</script>
