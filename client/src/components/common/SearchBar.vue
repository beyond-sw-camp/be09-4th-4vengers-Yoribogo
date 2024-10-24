<template>
  <div class="search-bar">
    <form class="search-form" @submit.prevent="submitSearch">
      <input
        id="search-input"
        v-model="searchQuery"
        @focus="removePlaceholder"
        @blur="restorePlaceholder"
        @keydown.enter="submitSearch"
        :class="{ 'input-error': showError }"
      />
      <i class="fa-solid fa-magnifying-glass" @click="submitSearch"></i>
    </form>
    <p v-if="showError" class="error-msg">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const searchQuery = ref('레시피 검색')

const showError = ref(false)
const errorMessage = ref('')

const emit = defineEmits(['search'])

// 포커스 시 기존 문자열 제거
const removePlaceholder = () => {
  searchQuery.value = ''
}

// 포커스 해제 시 원래 placeholder 복원
const restorePlaceholder = () => {
  if (searchQuery.value === '') {
    searchQuery.value = '레시피 검색'
  }
}

// 검색 제출
const submitSearch = () => {
  if (!searchQuery.value || searchQuery.value === '레시피 검색') {
    // 검색어가 없으면 커스텀 에러 메시지 표시
    errorMessage.value = '검색어를 입력해 주세요.'
    showError.value = true
  } else {
    emit('search', searchQuery.value)
  }
}
</script>

<style scoped>
.search-bar {
  display: flex;
  flex-direction: column;
  height: 6rem;
  width: 50%;
  gap: 1rem;
}

.search-form {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 1rem 2rem;
  background-color: var(--light-pink-color);
  border-radius: 1.6rem;
  cursor: pointer;
  border: 2px solid transparent;
  transition: border 0.2s ease;
}

.search-form:focus-within {
  border: 2px solid var(--pink-color);
}

.search-bar input {
  width: 95%;
  text-align: center;
  font-size: 2.3rem;
  font-weight: 500;
  font-family: 'Noto Sans KR';
  color: #6c6c6c;
  line-height: 2.3rem;
  background-color: transparent;
  cursor: pointer;
  border: none;
}

.search-bar input:focus {
  outline: none;
}

.search-bar i {
  height: 2rem;
  width: 5%;
  font-size: 2rem;
  color: var(--red-color);
}

.error-msg {
  width: 100%;
  color: var(--red-color);
  font-size: 1.4rem;
  margin-top: 0.5rem;
  margin-left: 1rem;
}
</style>
