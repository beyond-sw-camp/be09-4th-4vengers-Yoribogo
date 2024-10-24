<template>
  <div class="attribute-input-container">
    <div class="attribute-name-wrapper">
      <span>{{ props.name }}</span>
      <button @click="addItem">추가 +</button>
    </div>
    <div class="attribute-input-wrapper">
      <input
        id="attribute-input"
        type="text"
        :placeholder="props.placeholder"
        v-model="inputValue"
      />
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  name: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    required: true
  },
  showButton: {
    type: Boolean,
    default: false
  }
})

import { ref } from 'vue'

// 이벤트 상위 컴포넌트로 전송
const emit = defineEmits(['add-item'])
// 입력값 담기
const inputValue = ref('')

// addItem 메서드 정의
// addItem 버튼을 클릭하면 배열에 재료가 추가되고, 입력 필드는 다시 초기화되어 입력을 추가로 받을 수 있다.
const addItem = () => {
  if (inputValue.value) {
    emit('add-item', inputValue.value)
    inputValue.value = '' // 입력 필드 초기화
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

.attribute-name-wrapper {
  display: flex;
  justify-content: space-between;
}

.attribute-input-wrapper {
  display: flex;
  width: 100%;
  min-height: 9rem;
  background-color: var(--light-gray-color);
  border-radius: 0 0.8rem 0.8rem 0.8rem;
  position: relative;
}

.attribute-input-wrapper input {
  width: 100%;
  background-color: transparent;
  border: none;
  outline: none;
  padding-left: 3rem;
  font-size: 1.5rem;
}

.attribute-name-wrapper button {
  margin-left: 1rem;
  padding: 0.5rem 1rem;
  background-color: var(--pink-color);
  color: white;
  border: none;
  border-radius: 0.5rem 0.5rem 0 0;
  cursor: pointer;
  font-size: 1.5rem;
}
</style>
