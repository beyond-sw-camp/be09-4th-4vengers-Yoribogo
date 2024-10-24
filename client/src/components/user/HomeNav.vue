<template>
  <nav id="main-nav" class="main-nav">
    <div class="logo" @click="goHome">
      <img src="/src/assets/logo.svg" alt="Logo" />
      <span>요리보고</span>
    </div>

    <ul class="menu">
      <li class="search-recipe" @click="goWiki">
        <div class="search-bar">
          <span>레시피 검색</span>
          <i class="fa-solid fa-magnifying-glass"></i>
        </div>
      </li>

      <li v-if="isLoggedIn"><NotificationButton></NotificationButton></li>
      <li v-if="isLoggedIn"><ProfileButton></ProfileButton></li>
      <li v-if="!isLoggedIn" class="signup" @click="goSignup">회원가입</li>
      <li v-if="!isLoggedIn" class="login" @click="goLogin">로그인</li>
    </ul>
  </nav>
</template>

<script setup>
import ProfileButton from '@/components/common/ProfileButton.vue'
import NotificationButton from '@/components/common/NotificationButton.vue' // 알림 버튼 추가
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/tokenStore' // Pinia 스토어 임포트

const router = useRouter()
const tokenStore = useTokenStore() // Pinia 스토어 사용

const isLoggedIn = ref(false) // 로그인 상태

// 로그인 상태 확인
const checkLoginStatus = () => {
  isLoggedIn.value = !!tokenStore.token.accessToken // accessToken이 존재하면 로그인 상태
}

// 로그인 상태 변화 감시
watch(
  () => tokenStore.token.accessToken,
  () => {
    checkLoginStatus() // accessToken 변경 시 로그인 상태 확인
  },
  { immediate: true } // 초기에도 바로 실행
)

// 위키 페이지로 이동
const goWiki = () => {
  router.push('/wiki')
}

const goSignup = () => {
  router.push('/signup')
}
const goLogin = () => {
  router.push('/login-page')
}
// 로고 클릭 시 홈으로 이동
const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.main-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: calc(100% - 8rem);
  height: 100%;
  z-index: 9999;
}
.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo img {
  width: 6rem;
  height: 6rem;
}

.logo span {
  font-size: 2rem;
  font-weight: bold;
  color: var(--black-color);
}

/* .menu를 가운데 정렬시키고 각 요소가 한 줄에 나란히 있게 */
.menu {
  display: flex;
  align-items: center;
  gap: 4rem;
  font-size: 2rem;
  font-weight: 600;
  color: var(--black-color);
}

ul li {
  cursor: pointer;
}

/* 검색창, 알림, 프로필 버튼들이 자연스럽게 나란히 정렬되도록 설정 */
.search-recipe {
  display: flex;
  background-color: var(--blended-light-color);
  padding: 1rem 3.6rem;
  border: none;
  border-radius: 2rem;
}
.search-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
}

.search-bar span {
  line-height: 2rem;
  font-weight: 400;
}

.search-bar i {
  color: var(--yellow-color);
}

@media screen and (max-width: 425px) {
  .menu {
    font-size: 1.9rem;
    gap: 2.5rem;
  }
}
</style>
