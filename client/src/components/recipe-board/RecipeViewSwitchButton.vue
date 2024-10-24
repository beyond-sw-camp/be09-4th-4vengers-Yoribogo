<template>
  <div class="toggle-container">
    <div :class="['toggle-button', isWeekly ? 'active' : '']" @click="toggleToWeekly">
      주간 인기 레시피
    </div>
    <div :class="['toggle-button', !isWeekly ? 'active' : '']" @click="toggleToAll">
      전체 레시피
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const isWeekly = ref(true) // 기본값을 주간 인기 레시피로 설정
const emit = defineEmits(['toggle'])

// 주간 인기 레시피로 토글
const toggleToWeekly = () => {
  isWeekly.value = true
  emit('toggle', true) // true 값을 부모 컴포넌트로 전달
}

// 전체 레시피로 토글
const toggleToAll = () => {
  isWeekly.value = false
  emit('toggle', false) // false 값을 부모 컴포넌트로 전달
}
</script>

<style scoped>
.toggle-container {
  display: flex;
  justify-content: space-between;
  max-width: 40%;
  margin: 0 auto;
  border-radius: 50px;
  background-color: #e0e0e0;
  padding: 0.5rem;
  margin-top: 2rem;
  width: 50rem;
}

.toggle-button {
  flex: 1;
  text-align: center;
  padding: 1rem 0;
  border-radius: 50px;
  cursor: pointer;
  font-size: 2.3rem;
  color: white;
  transition: background-color 0.3s ease;
  text-wrap: nowrap;
}

.toggle-button.active {
  background-color: #374151;
}

.toggle-button:not(.active) {
  background-color: transparent;
  color: #858585;
  width: 30rem;
  padding: 1rem;
}
</style>
