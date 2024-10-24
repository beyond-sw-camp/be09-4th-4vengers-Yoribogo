<template>
  <div class="menu-item">
    <div class="menu-img-wrapper" v-show="isLoading"></div>
    <div class="menu-img-wrapper" v-show="!isLoading">
      <img :src="menuImageSrc" alt="MenuImage" @load="handleImageLoad" @error="handleImageError" />
    </div>
    <div class="menu-name-wrapper">
      <span class="menu-name">{{ formattedMenuName }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
  menuImage: {
    type: String,
    required: true,
    default: ''
  },
  menuName: {
    type: String,
    required: true
  }
})

const defaultImage = ref(
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728804967802_a4720492-2dd2-4e59-8f31-79b55e6a169e_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.svg'
)

const isLoading = ref(true)
const imageError = ref(false)

const menuImageSrc = ref('')

// 메뉴 이름 처리
const formattedMenuName = computed(() => {
  return props.menuName.trim() || 'Unknown Menu'
})

// 이미지 로딩 오류 처리 함수
const handleImageError = () => {
  menuImageSrc.value = defaultImage.value
  isLoading.value = false // 로딩 중지
  imageError.value = true
}

// 이미지 로드 완료 처리 함수
const handleImageLoad = () => {
  isLoading.value = false // 로딩 중지
  imageError.value = false
}

onMounted(() => {
  menuImageSrc.value = props.menuImage || defaultImage.value
})
</script>

<style scoped>
.menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 32rem;
  background-color: var(--white-color);
  padding: 1rem;
  border-radius: 0.5rem;
  box-shadow: 0 0.4rem 1rem rgba(0, 0, 0, 0.1);
  transition: 0.3s ease;
  overflow: hidden;
  cursor: pointer;
}

.menu-item:hover {
  box-shadow: 0 1rem 2rem rgba(0, 0, 0, 0.1);
}

.menu-img-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 80%;
  background-color: #aaaaaa;
  overflow: hidden;
  border-radius: 1.6rem;
}

.menu-img-wrapper img {
  height: 150%;
  width: 150%;
  object-fit: cover;
}

.menu-name-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 20%;
  width: 100%;
  overflow: hidden;
}

.menu-name {
  font-size: 2rem;
  font-weight: 500;
  line-height: 2rem;
  text-overflow: ellipsis;
  color: var(--black-color);
}

@media screen and (max-width: 1280px) {
  .menu-item {
    height: 30rem;
  }

  .menu-name {
    font-size: 1.8rem;
  }
}

@media screen and (max-width: 1024px) {
  .menu-item {
    height: 28rem;
  }

  .menu-name {
    font-size: 1.6rem;
  }
}

@media screen and (max-width: 425px) {
  .menu-item {
    height: 22rem;
  }

  .menu-name {
    font-size: 1.3rem;
  }
}

@media screen and (max-width: 320px) {
  .menu-item {
    height: 20rem;
  }

  .menu-name {
    font-size: 1.1rem;
  }
}
</style>
