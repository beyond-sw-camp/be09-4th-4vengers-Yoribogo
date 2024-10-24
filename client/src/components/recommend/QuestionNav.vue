<template>
  <div class="question-nav">
    <div class="rounded-line"></div>
    <div class="menu" v-for="(question, index) in props.questions" :key="index">
      <div class="circle">
        <div class="mini-circle" v-if="currentQid === question.qid"></div>
      </div>
      <span>질문{{ question.qid }}</span>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { ref, watch } from 'vue'

// 현재 경로에서 qid를 추출하고 숫자로 변환
const route = useRoute()

// 현재 qid를 추출하고 이를 추적하는 상태로 만듭니다.
let currentQid = ref(parseInt(route.params.qid))

// route.params.qid가 변경될 때마다 currentQid 값을 업데이트
watch(
  () => route.params.qid,
  (newQid) => {
    currentQid.value = parseInt(newQid)
  }
)

const props = defineProps({
  questions: {
    type: Array,
    required: true
  }
})
</script>

<style scoped>
.question-nav {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  width: calc(100% - 24rem);
  height: 100%;
  position: relative;
  z-index: 9999;
}

.rounded-line {
  position: absolute;
  width: 100%;
  height: 4px;
  background-color: var(--white-color);
  border-radius: 1rem;
  z-index: 0;
}

.menu {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1;
  gap: 1rem;
  margin-top: 3.7rem;
  font-size: 2rem;
  color: var(--sub-yellow-color);
}

.circle {
  display: flex;
  height: 3.5rem;
  width: 3.5rem;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  background-color: var(--white-color);
}

.mini-circle {
  display: flex;
  height: 2.1rem;
  width: 2.1rem;
  border-radius: 50%;
  background-color: var(--yellow-color);
}
</style>
