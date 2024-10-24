<template>
  <div class="attribute-input-container">
    <div class="attribute-name-wrapper">
      <span>{{ name }}</span>
    </div>
    <div class="attribute-input-wrapper">
      <input id="menu-name-input" type="text" :placeholder="placeholder" v-model="inputValue" />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

// props 정의
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
  },
  modelValue: {
    type: String,
    default: ''
  }
})

// 상위 컴포넌트로 이벤트 전송
const emit = defineEmits(['update:modelValue'])

// `modelValue`를 `inputValue`로 설정
const inputValue = ref(props.modelValue)

// 새로운 값이 입력되면 상위 컴포넌트로 값 전송
watch(inputValue, (newValue) => {
  emit('update:modelValue', newValue) // 사용자가 입력한 값을 상위로 전송
})
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
