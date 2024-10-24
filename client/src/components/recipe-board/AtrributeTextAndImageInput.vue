<template>
  <div class="attribute-input-container">
    <div class="attribute-name-wrapper">
      <span>{{ props.name }}</span>
      <button @click="handleAdd">추가 +</button>
    </div>

    <div class="attribute-input-wrapper">
      <div
        class="image-input"
        @dragover.prevent
        @dragleave="handleDragLeave"
        @drop.prevent="handleFileDrop"
        @dragenter="handleDragEnter"
      >
        <label for="file-upload-step" class="custom-file-upload">
          <i v-if="!uploadedFileName" class="fa-solid fa-image"></i>
          <div
            v-if="uploadedImageUrl"
            style="display: flex; justify-content: center; align-items: center"
          >
            <img :src="uploadedImageUrl" alt="미리보기 이미지" style="max-height: 15rem" />
          </div>
          <span v-if="!uploadedFileName">
            {{
              props.name === '조리 순서'
                ? '여기에 이미지를 드래그하거나 클릭하여 추가'
                : props.placeholder
            }}
          </span>
          <span v-else>{{ uploadedFileName }}</span>
        </label>
        <input id="file-upload-step" type="file" @change="handleFileChange" />
      </div>
      <div class="text-input">
        <input
          id="text-upload-input"
          type="text"
          :placeholder="props.placeholder"
          v-model="stepText"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const uploadedFileName = ref('') // 파일 이름을 저장할 변수
const uploadedImageUrl = ref('') // 이미지 URL 저장
const stepText = ref('') // 단계 텍스트 저장

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['add'])

const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file && file.type.startsWith('image/')) {
    uploadedFileName.value = file.name
    uploadedImageUrl.value = URL.createObjectURL(file)
  }
}

// "추가" 버튼 클릭 시 호출할 함수
const handleAdd = () => {
  // 단계 텍스트와 이미지를 담은 객체 생성
  const newStep = {
    content: stepText.value,
    image: uploadedImageUrl.value || null
  }

  if (newStep.content.trim() === '') {
    alert('조리 방법을 입력하세요.')
    return
  }

  // 상위 컴포넌트에 emit
  emit('add', newStep)

  // 입력 필드 초기화
  stepText.value = ''
  uploadedFileName.value = ''
  uploadedImageUrl.value = ''
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
  justify-content: space-between;
}

.attribute-name-wrapper span {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 9rem;
  height: 3rem;
  align-items: center;
  background-color: var(--navy-color);
  color: var(--white-color);
  border-radius: 0.8rem 0.8rem 0rem 0;
  font-size: 1.5rem;
  font-weight: 500;
}

.attribute-input-wrapper {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 30rem;
  background-color: var(--light-gray-color);
  border-radius: 0 0.8rem 0.8rem 0.8rem;
}

.text-input {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 70%;
  width: 90%;
  border-top: 1px solid #dcdcdc;
  position: relative;
}

.text-input input {
  width: 100%;
  background-color: transparent;
  border: none;
  outline: none;
  padding-left: 3rem;
  font-size: 1.5rem;
  text-align: center;
  margin-top: 2rem;
}

.image-input {
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.3s ease;
  position: relative;
  width: 100%;
  height: auto;
}

/* 이미지 아이콘 색 */
.fa-solid {
  color: gray;
}
.file-upload span {
  color: gray;
}

/* 드래그 중일 때 배경색 변경 */
.image-input.dragging {
  background-color: var(--primary-color-light);
  border: 2px dashed var(--primary-color);
}

.text-input input {
  width: 100%;
  background-color: transparent;
  border: none;
  outline: none;
  padding-left: 3rem;
  font-size: 1.5rem;
  text-align: center;
}

/* 추가 버튼 */
.attribute-name-wrapper button {
  margin-left: 1rem;
  padding: 0.5rem 1rem;
  background-color: var(--pink-color);
  color: white;
  border: none;
  border-radius: 0.5rem 0.5rem 0 0;
  cursor: pointer;
  top: 0px;
  right: 0px;
  font-size: 1.5rem;
}

.image-input i {
  font-size: 4rem;
  text-align: center;
}

.custom-file-upload {
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  padding: 0.5rem 1rem;
  gap: 1rem;
  cursor: pointer;
  font-size: 1.4rem;
  height: 20rem;
}
.custom-file-upload span {
  color: gray;
}

.image-input input[type='file'] {
  display: none;
}

@media screen and (max-width: 768px) {
  .text-input input {
    font-size: 1.4rem;
  }

  .custom-file-upload span {
    color: gray;
  }
}

@media screen and (max-width: 425px) {
  .text-input input {
    font-size: 1.25rem;
  }

  .custom-file-upload {
    font-size: 1.25rem;
  }
}

@media screen and (max-width: 375px) {
  .text-input input {
    font-size: 1.2rem;
  }

  .custom-file-upload {
    font-size: 1.2rem;
  }
}
</style>
