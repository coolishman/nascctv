<template>
  <section class="tv-wall">
    <header>
      <div>
        <p class="eyebrow">电视墙模式</p>
        <h2>{{ wall.name || '运营中心视频墙' }}</h2>
        <p class="subtitle">按权限展示摄像头通道，支持轮播播放</p>
      </div>
      <button class="primary" @click="togglePlay">
        {{ isPlaying ? '暂停轮播' : '开始轮播' }}
      </button>
    </header>

    <div class="wall-grid">
      <article
        v-for="tile in tiles"
        :key="tile.id"
        class="wall-tile"
      >
        <div class="wall-video">
          <span class="live">LIVE</span>
          <div class="wall-info">
            <h4>{{ currentCamera(tile)?.name }}</h4>
            <p>{{ currentCamera(tile)?.protocol }}</p>
            <p class="stream">{{ currentCamera(tile)?.streamUrl }}</p>
          </div>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue';

const props = defineProps({
  wall: {
    type: Object,
    default: () => ({})
  }
});

const tiles = computed(() => props.wall.tiles || []);
const rotationState = reactive({});
const isPlaying = ref(true);
let timerId = null;

const initRotation = () => {
  tiles.value.forEach((tile) => {
    rotationState[tile.id] = 0;
  });
};

const currentCamera = (tile) => {
  const playlist = tile.playlist || [];
  if (!playlist.length) {
    return null;
  }
  const index = rotationState[tile.id] || 0;
  return playlist[index % playlist.length];
};

const rotate = () => {
  tiles.value.forEach((tile) => {
    if (!tile.rotationSeconds || tile.rotationSeconds <= 0) {
      return;
    }
    const playlist = tile.playlist || [];
    if (playlist.length <= 1) {
      return;
    }
    rotationState[tile.id] = (rotationState[tile.id] + 1) % playlist.length;
  });
};

const startTimer = () => {
  clearInterval(timerId);
  timerId = setInterval(() => {
    if (isPlaying.value) {
      rotate();
    }
  }, 5000);
};

const togglePlay = () => {
  isPlaying.value = !isPlaying.value;
};


watch(tiles, () => {
  initRotation();
});

onMounted(() => {
  initRotation();
  startTimer();
});

onBeforeUnmount(() => {
  clearInterval(timerId);
});
</script>
