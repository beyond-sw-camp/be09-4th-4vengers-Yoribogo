<template>
  <div class="loading-container">
    <div class="loading" :style="{ borderColor: 'transparent ' + props.loadingColor }"></div>
    <div class="loading-text" :style="{ color: props.textColor }">loading..</div>
  </div>
</template>

<script setup>
const props = defineProps({
  loadingColor: {
    type: String,
    default: '#fff' // 기본 색상을 흰색으로 설정
  },
  textColor: {
    type: String,
    default: '#fff' // 기본 텍스트 색상을 흰색으로 설정
  }
})
</script>

<style scoped>
@keyframes rotate-loading {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes loading-text-opacity {
  0%,
  20%,
  100% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}

.loading-container {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 8rem auto;
}

.loading {
  width: 20rem;
  height: 20rem;
  border: 0.8rem solid transparent;
  border-radius: 50%;
  animation: rotate-loading 1.5s linear infinite;
}

.loading-container:hover .loading {
  transition: border-color 0.5s ease-in-out;
}

.loading-text {
  position: absolute;
  font-family: 'Noto Sans';
  font-size: 2rem;
  font-weight: bold;
  text-transform: uppercase;
  animation: loading-text-opacity 2s linear infinite;
}
</style>
