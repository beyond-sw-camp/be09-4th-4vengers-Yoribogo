<template>
  <main class="main-board">
    <section class="menu-container">
      <MenuTab
        class="bg-pink"
        :class="{ cur: activeTab === 'wiki' }"
        :iconClass="'fa-brands fa-wikipedia-w'"
        @click="goActiveTab('wiki')"
        >요리 위키</MenuTab
      >
      <MenuTab
        class="bg-red"
        :class="{ cur: activeTab === 'recipe-board' }"
        :iconClass="'fa-solid fa-book-open'"
        @click="goActiveTab('recipe-board')"
        >나만의 레시피</MenuTab
      >
      <MenuTab
        class="bg-pink"
        :class="{ cur: activeTab === 'mypage' }"
        :iconClass="'fa-solid fa-user'"
        @click="goActiveTab('mypage')"
        >마이페이지</MenuTab
      >
      <MenuTab
        class="bg-red"
        :class="{ cur: activeTab === 'qna' }"
        :iconClass="'fa-regular fa-circle-question'"
        @click="goActiveTab('qna')"
        >Q & A</MenuTab
      >
    </section>
    <section class="board">
      <slot></slot>
    </section>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import MenuTab from '@/components/common/MenuTab.vue'

const props = defineProps({
  cur: {
    type: String,
    required: true,
    default: 'wiki'
  }
})

const router = useRouter()

const activeTab = ref(props.cur) // 초기 활성 탭을 설정

const goActiveTab = (tab) => {
  router.push(`/${tab}`)
}
</script>

<style scoped>
.main-board {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 72%;
  min-width: 20rem;
  min-height: 65rem;
  margin-top: 9rem;
  margin-bottom: 4rem;
}

.menu-container {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-right: 8rem;
}

.board {
  position: relative;
  top: -3rem;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  min-height: 60rem;
  background-color: var(--white-color);
  border-radius: 1.6rem;
  z-index: 1;
}

@media screen and (max-width: 600px) {
  .menu-container {
    margin-right: 4rem;
  }
}
</style>
