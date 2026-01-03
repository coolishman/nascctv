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
            <template v-if="!isTileActive(tile)">
              <h4>通道已暂停</h4>
              <p>点击播放恢复</p>
            </template>
            <template v-else-if="currentCamera(tile)">
              <h4>{{ currentCamera(tile)?.name }}</h4>
              <p>{{ currentCamera(tile)?.protocol }}</p>
              <p class="stream">{{ currentCamera(tile)?.streamUrl }}</p>
            </template>
            <template v-else>
              <h4>暂无授权通道</h4>
              <p>请联系管理员配置权限</p>
            </template>
          </div>
          <button class="ghost" @click="toggleTile(tile)">
            {{ isTileActive(tile) ? '暂停' : '播放' }}
          </button>
        </div>
      </article>
    </div>
    <p v-if="!tiles.length" class="empty-state">暂无电视墙布局数据</p>
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
const rotationState = reactive({
  indices: {},
  elapsed: {}
});
const pausedTiles = reactive({});
const isPlaying = ref(true);
let timerId = null;

const initRotation = () => {
  tiles.value.forEach((tile) => {
    rotationState.indices[tile.id] = 0;
    rotationState.elapsed[tile.id] = 0;
    pausedTiles[tile.id] = tile.enabled === false;
  });
};

const isTileActive = (tile) => !pausedTiles[tile.id];

const currentCamera = (tile) => {
  const playlist = tile.playlist || [];
  if (!playlist.length || !isTileActive(tile)) {
    return null;
  }
  const index = rotationState.indices[tile.id] || 0;
  return playlist[index % playlist.length];
};

const rotate = () => {
  tiles.value.forEach((tile) => {
    if (!isTileActive(tile)) {
      return;
    }
    if (!tile.rotationSeconds || tile.rotationSeconds <= 0) {
      return;
    }
    const playlist = tile.playlist || [];
    if (playlist.length <= 1) {
      return;
    }
    rotationState.elapsed[tile.id] = (rotationState.elapsed[tile.id] || 0) + 1;
    if (rotationState.elapsed[tile.id] >= tile.rotationSeconds) {
      rotationState.indices[tile.id] = (rotationState.indices[tile.id] + 1) % playlist.length;
      rotationState.elapsed[tile.id] = 0;
    }
  });
};

const startTimer = () => {
  clearInterval(timerId);
  timerId = setInterval(() => {
    if (isPlaying.value) {
      rotate();
    }
  }, 1000);
};

const togglePlay = () => {
  isPlaying.value = !isPlaying.value;
};

const toggleTile = (tile) => {
  pausedTiles[tile.id] = !isTileActive(tile);
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
