<template>
  <div class="ingredient-step-container">
    <!-- 재료 영역 -->
    <div class="section">
      <div class="section-header">재료</div>
      <div class="section-content" style="width: 100%">
        <span v-if="ingredients.length === 0">재료 항목이 비어있습니다.</span>
        <div v-else v-for="(ingredient, index) in ingredients" :key="index" class="ingredient-item">
          <div style="display: flex; justify-content: space-evenly; align-items: center; gap: 4px">
            <span
              style="
                max-width: 16rem;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
              "
            >
              &nbsp;
              {{ ingredient }}
            </span>
            <button @click="removeItem(index)">✕</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 조리 순서 영역 -->
    <div class="section">
      <div class="section-header">조리 순서</div>
      <div class="section-content scroll-container">
        <span v-if="steps.length === 0">요리 방법 항목이 비어있습니다.</span>
        <div v-else v-for="(step, index) in steps" :key="index">
          <WriteStepInput
            :index="index"
            :initialStep="step"
            @add-step="addStep"
            @remove-step="removeStep"
            @update-step="updateStep(index, $event)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import WriteStepInput from '@/components/recipe-board/WriteStepInput.vue'

// 재료와 조리 순서 배열 선언 (초기값이 없으면 비어있는 메시지 출력)
const menuIngredients = ref([])
const steps = ref([])

// 상위 컴포넌트에서 전달받는 props
const props = defineProps({
  ingredients: {
    type: Array,
    required: true
  },
  manual_step: {
    type: Array,
    required: true
  }
})

// 상위 컴포넌트로 이벤트를 전달하기 위한 emit
const emit = defineEmits(['remove-item', 'add-step', 'remove-step'])

// 재료 삭제 함수
const removeItem = (index) => {
  emit('remove-item', index)
}

// props.ingredients가 변경될 때 ingredients 배열을 업데이트
watch(
  () => props.ingredients,
  (newIngredients) => {
    menuIngredients.value = [...newIngredients] // 깊은 복사로 배열을 새로 할당
  }
)

// props.manual_step이 변경될 때 steps 배열을 업데이트
watch(
  () => props.manual_step,
  (newSteps) => {
    steps.value = [...newSteps] // 깊은 복사로 배열을 새로 할당
  }
)

/* 조리 방법 추가, 삭제 메소드 */
const addStep = (index) => {
  emit('add-step', index)
}

// 조리 방법 삭제
const removeStep = (index) => {
  emit('remove-step', index) // 이벤트 발생
}

// 조리 방법 수정
const updateStep = (index, updatedStep) => {
  emit('update-step', index, updatedStep)
}
</script>

<style scoped>
.ingredient-step-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5rem;
}

.section {
  width: 100%;
  max-width: 600px;
  display: flex;
  align-items: center;
  flex-direction: column;
}

.section-header {
  background-color: #2d3e50;
  color: white;
  padding: 1rem 1.3rem;
  font-size: 1.5rem;
  font-weight: 600;
  border-radius: 0.4rem;
  text-align: center;
  width: 80px;
}

.section-content {
  justify-content: center;
  align-items: center;
  padding: 0.7rem 1rem;
  border-radius: 50px;
  font-size: 1.2rem;
  color: #666;
  text-align: center;
  margin-top: 2rem;
}

/* 부모 div 크기를 넘지 않도록 */
.section-content {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem; /* 항목 간의 간격 */
}

/* 재료 담는 박스 */
.ingredient-item {
  display: flex;
  align-items: center;
  padding: 0.4rem;
  border: 1px solid #1c2e3e; /* 테두리 색상 */
  border-radius: 2rem; /* pill 모양을 위해 radius를 높게 설정 */
  background-color: white;
  font-size: 1.6rem;
  color: #000;
  gap: 1rem; /* 텍스트와 버튼 사이의 간격 */
}

.ingredient-item button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 2.5rem;
  height: 2.5rem;
  background-color: #1c2e3e; /* X 버튼의 배경색 */
  color: white;
  border: none;
  border-radius: 50%; /* 원형 버튼 */
  cursor: pointer;
  font-size: 1.5rem;
  line-height: 1;
}

.section-content span {
  font-size: 1.7rem;
}

.scroll-container {
  max-height: 500px;
  overflow-y: auto;
  padding: 10px;
  border-radius: 0px;
}
</style>
