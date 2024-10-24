<template>
  <div class="recipe-manual">
    <p class="manual-content">{{ props.manualContent }}</p>
    <div class="manual-img-wrapper">
      <img v-show="isImageLoading" :src="defaultImage" alt="ManualImage" />
      <img
        v-show="!isImageLoading"
        :src="manualImageSrc"
        alt="ManualImage"
        @load="handleImageLoad"
        @error="handleImageError"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'

const props = defineProps({
  manualContent: {
    type: String,
    required: true
  },
  manualImage: {
    type: String
  }
})

const isImageLoading = ref(true)
const isImageError = ref(false)

const defaultImage = ref(
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728804967802_a4720492-2dd2-4e59-8f31-79b55e6a169e_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.svg'
)

const manualImageSrc = ref('')

// 이미지 로딩 오류 처리 함수
const handleImageError = () => {
  manualImageSrc.value = defaultImage.value
  isImageLoading.value = false // 로딩 중지
  isImageError.value = true
}

// 이미지 로드 완료 처리 함수
const handleImageLoad = () => {
  isImageLoading.value = false // 로딩 중지
  isImageError.value = false
}

onMounted(() => {
  manualImageSrc.value = props.manualImage || defaultImage.value
})
</script>

<style scoped>
.recipe-manual {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 68%;
}

.manual-content {
  max-width: 44%;
  font-size: 2.4rem;
  font-weight: 500;
  line-height: 4rem;
  word-wrap: break-word;
}

.manual-img-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  max-width: 44%;
  height: 24rem;
  overflow: hidden;
  border-radius: 1.4rem;
}

.manual-img-wrapper img {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

@media screen and (max-width: 768px) {
  .manual-content {
    font-size: 2rem;
    line-height: 3.4rem;
  }
}
</style>
