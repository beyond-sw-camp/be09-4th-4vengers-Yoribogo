<template>
  <div class="step-input-container">
    <div class="step-content">
      <div class="step-title">{{ index + 1 }}단계</div>
      <div class="step-image">
        <!-- 이미지가 있는 경우 보여줌 -->
        <img v-if="stepImage" :src="stepImage" alt="조리 이미지" />
        <input v-else type="file" @change="onImageChange" />
      </div>
      <div class="step-description">
        <input
          type="text"
          v-model="stepText"
          placeholder="조리 방법을 입력하세요"
          @input="emitStepUpdate"
        />
      </div>
      <button class="step-add-button" @click="addStepToNextIndex">조리 순서 추가</button>
    </div>

    <button class="step-remove-button" @click="removeStep">✕</button>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onBeforeUnmount } from 'vue'

// Props: 현재 단계 인덱스와 초기 데이터
const props = defineProps({
  index: {
    type: Number,
    required: true
  },
  initialStep: {
    type: Object,
    required: true
  }
})

// Emits: 상위 컴포넌트에 변경 알림을 보낼 이벤트
const emit = defineEmits(['add-step', 'remove-step', 'update-step'])

// 데이터 바인딩
const stepText = ref(props.initialStep.step)
const stepImage = ref(props.initialStep.image)

onMounted(() => {
  const storedImage = localStorage.getItem(`step_image_${props.index}`)
  if (storedImage) {
    stepImage.value = storedImage
  }
})

const onImageChange = (e) => {
  const file = e.target.files[0]
  if (file) {
    const reader = new FileReader()

    // 파일을 Base64로 인코딩
    reader.readAsDataURL(file)

    reader.onload = () => {
      const base64Image = reader.result // Base64 인코딩된 파일 데이터
      stepImage.value = base64Image // 이미지 미리보기용으로 Base64 사용

      // 로컬 스토리지에 Base64로 저장
      const manualStep = JSON.parse(localStorage.getItem('manual_step')) || []
      manualStep[props.index] = { step: stepText.value, image: base64Image }
      localStorage.setItem('manual_step', JSON.stringify(manualStep))

      emit('update-step', { step: stepText.value, image: base64Image }) // 상위 컴포넌트에 변경 내용 전달
    }

    reader.onerror = (error) => {
      console.error('파일을 읽는 중 오류 발생:', error)
    }
  }
}

// 컴포넌트 언마운트 시 Blob URL 정리
onBeforeUnmount(() => {
  if (stepImage.value) {
    URL.revokeObjectURL(stepImage.value)
  }
})

// 단계 정보를 업데이트하는 메소드
const updateLocalStorage = () => {
  const manualSteps = JSON.parse(localStorage.getItem('manual_step')) || []
  manualSteps[props.index] = { step: stepText.value, image: stepImage.value }
  localStorage.setItem('manual_step', JSON.stringify(manualSteps))
}

// 텍스트와 이미지의 변화를 감시
watch([stepText, stepImage], () => {
  updateLocalStorage() // 값이 변경될 때마다 로컬 스토리지에 업데이트
})

// 단계 삭제
const removeStep = () => {
  emit('remove-step', props.index) // 올바르게 index를 전달
}

// 단계 추가
const addStepToNextIndex = () => {
  emit('add-step', { step: '', image: '' })
}
</script>

<style scoped>
.step-input-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2rem;
  border: 1px solid #dcdcdc;
  border-radius: 0.8rem;
  background-color: #fff;
  margin-bottom: 1rem;
  position: relative;
  width: 58rem;
}

.step-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  width: 100%;
}

.step-title {
  font-weight: bold;
  font-size: 1.6rem;
  text-align: center;
  color: #333;
}

.step-image {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 12rem;
  background-color: #f5f5f5;
  border-radius: 0.8rem;
  overflow: hidden;
}

.step-image img {
  max-width: 100%;
  max-height: 12rem;
  object-fit: cover;
}

.step-description {
  width: 100%;
  display: flex;
  justify-content: center;
}

.step-description input {
  width: 100%;
  padding: 1rem;
  font-size: 1.4rem;
  border: 1px solid #dcdcdc;
  border-radius: 0.8rem;
  text-align: center;
}

.step-add-button {
  width: 100%;
  padding: 1rem;
  font-size: 1.4rem;
  background-color: #243b55;
  color: #fff;
  border: none;
  border-radius: 0.8rem;
  cursor: pointer;
  text-align: center;
}

.step-add-button:hover {
  background-color: #1e2f44;
}

.step-remove-button {
  position: absolute;
  top: 1rem;
  right: 1rem;
  background-color: #ff4c4c;
  border: none;
  border-radius: 50%;
  width: 2.5rem;
  height: 2.5rem;
  color: white;
  cursor: pointer;
}

.step-remove-button:hover {
  background-color: #e84343;
}
</style>
