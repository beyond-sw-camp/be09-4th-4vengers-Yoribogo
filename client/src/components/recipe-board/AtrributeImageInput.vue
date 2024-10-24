<template>
  <div class="attribute-input-container">
    <div class="attribute-name-wrapper">
      <span>{{ props.name }}</span>
    </div>
    <div class="attribute-input-wrapper" @dragover.prevent @drop.prevent="handleFileDrop">
      <label for="file-upload" class="custom-file-upload">
        <i v-if="!uploadedImageUrl" class="fa-solid fa-image"></i>
        <!-- 이미지 미리보기 -->
        <div
          v-if="uploadedImageUrl"
          style="display: flex; justify-content: center; align-items: center"
        >
          <img
            :src="uploadedImageUrl"
            alt="미리보기 이미지"
            style="max-width: 50rem; max-height: 30rem"
          />
        </div>
        <span v-if="!uploadedImageUrl">{{ props.placeholder }}</span>
      </label>
      <input id="file-upload" type="file" @change="handleFileChange" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const uploadedImageUrl = ref('') // 이미지 URL 저장

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    required: true
  },
  modelValue: {
    type: [String, Object], // 파일 또는 문자열을 허용
    default: ''
  }
})

const emit = defineEmits(['update:modelValue']) // 상위 컴포넌트로 이벤트 전송

// 파일 선택 시 호출
const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file && file.type.startsWith('image/')) {
    uploadedImageUrl.value = URL.createObjectURL(file) // 이미지 미리보기 URL 생성
    emit('update:modelValue', file) // 파일 자체를 상위 컴포넌트로 전달
  }
}

// 드래그 앤 드롭 시 호출
const handleFileDrop = (event) => {
  const file = event.dataTransfer.files[0]
  if (file && file.type.startsWith('image/')) {
    uploadedImageUrl.value = URL.createObjectURL(file) // 이미지 미리보기 URL 생성
    emit('update:modelValue', file) // 파일 자체를 상위 컴포넌트로 전달
  }
}
</script>

<style scoped>
.attribute-input-container {
  display: flex;
  flex-direction: column;
  width: 90%;
  height: fit-content;
}

.attribute-name-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 9rem;
  height: 3rem;
  background-color: var(--navy-color);
  color: var(--white-color);
  border-radius: 0.8rem 0.8rem 0rem 0;
  font-size: 1.5rem;
  font-weight: 500;
}

.attribute-input-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 30rem;
  background-color: var(--light-gray-color);
  border-radius: 0 0.8rem 0.8rem 0.8rem;
  border: 2px dashed var(--gray-color);
}

.attribute-input-wrapper i {
  font-size: 4rem;
  text-align: center;
}

.custom-file-upload {
  display: flex;
  flex-direction: column;
  padding: 0.5rem 1rem;
  gap: 1rem;
  cursor: pointer;
  font-size: 1.5rem;
}

.attribute-input-wrapper input[type='file'] {
  display: none;
}

.custom-file-upload span {
  color: gray;
}

.fa-solid {
  color: gray;
}
</style>
