<template>
  <div id="home-view" class="home-view">
    <img class="bg-circle" src="/src/assets/Intersect.png" alt="Background Circle" />
    <img class="chicken" src="/src/assets/chicken.png" alt="Chicken" />
    <header>
      <!-- 로그인 상태에 따라 메뉴 및 알림 아이콘 표시 -->
      <HomeNav @open-login-modal="openLoginModal" />
    </header>
    <main>
      <section id="start-section" class="start-section">
        <div class="catchphrase">
          <span class="emphasize">맛있는 선택의 시작.</span>
        </div>
        <div class="catchphrase">
          <span class="normal">여러분의 <span class="f-red">취향</span>에</span>
          <span class="indent normal">맞춘 <span class="f-red">음식 레시피.</span></span>
        </div>
        <button id="start-btn" class="start-btn" @click="goQuestion">시작하기</button>
      </section>
    </main>
    <LoginModal
      v-if="isLoginModalVisible"
      @close="closeLoginModal"
      @goToStep1="openRegisterModal"
      @openPasswordReset="openPasswordResetModal"
      @openFindId="openFindIdModal"
    />
  </div>
</template>

<script setup>
import LoginModal from '@/components/user/login/LoginModal.vue'
import HomeNav from '@/components/user/HomeNav.vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue'

// 로그인 상태 확인을 위한 변수
const isLoggedIn = ref(false)
const token = localStorage.getItem('token') // 로컬 스토리지에 저장된 토큰 확인

if (token) {
  isLoggedIn.value = true // 토큰이 있으면 로그인 상태로 설정
}

const router = useRouter()

const isLoginModalVisible = ref(false) // 로그인 모달 상태

// 로그인 모달 열기
const openLoginModal = () => {
  isLoginModalVisible.value = true
}

// 로그인 모달 닫기
const closeLoginModal = () => {
  isLoginModalVisible.value = false
}

const goQuestion = () => {
  router.push('/question/1')
}
</script>

<style scoped>
.home-view {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.bg-circle {
  position: absolute;
  top: 0;
  right: 0;
  width: 74rem;
  height: 66rem;
  z-index: 0;
}

.chicken {
  position: absolute;
  top: 30rem;
  right: 20rem;
  width: 80rem;
  height: 40rem;
}

.start-section {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-top: 18rem;
  margin-left: 20rem;
  margin-bottom: 10rem;
  gap: 9.6rem;
}

.catchphrase {
  display: flex;
  flex-direction: column;
  font-weight: 700;
  color: var(--black-color);
}

.emphasize {
  font-size: 8rem;
}

.normal {
  font-size: 6rem;
}

.f-red {
  color: var(--text-red-color);
}

.normal div span {
  color: var(--text-red-color);
}

.indent {
  margin-left: 12rem;
}

.start-btn {
  height: 10rem;
  width: 34rem;
  background-color: var(--white-color);
  border: none;
  border-radius: 3.5rem;
  box-shadow: 0.5rem 0.5rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  font-size: 3.5rem;
  font-weight: 700;
  color: var(--black-color);
  cursor: pointer;
  transition: 0.3s ease-out;
}

.start-btn:hover {
  transform: translateY(-0.3rem);
  box-shadow: 0.5rem 0.5rem 0.3rem 0.2rem rgba(60, 60, 60, 0.5);
}

/* 노트북 */
@media screen and (max-width: 1684px) {
  .start-section {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-top: 14rem;
    margin-left: 20rem;
    gap: 9.6rem;
  }
}

@media screen and (max-width: 1440px) {
  .chicken {
    top: 22rem;
    right: 15rem;
    width: 80rem;
    height: 40rem;
  }

  .bg-circle {
    width: 60rem;
    height: 54rem;
  }
}

@media screen and (max-width: 1280px) {
  .chicken {
    top: 22rem;
    right: 15rem;
    width: 60rem;
    height: 30rem;
  }

  .bg-circle {
    width: 50rem;
    height: 45rem;
  }
}

/* 태블릿 */
@media screen and (max-width: 1024px) {
  .chicken {
    top: 22rem;
    right: 15rem;
    width: 80rem;
    height: 40rem;
  }

  .bg-circle {
    width: 60rem;
    height: 54rem;
  }

  .start-section {
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    margin-top: 60rem;
    margin-left: 0px;
  }
}

/* 모바일 */
@media screen and (max-width: 768px) {
  .chicken {
    left: 50%;
    transform: translateX(-50%);
    width: 60rem;
    height: 30rem;
  }

  .bg-circle {
    width: 54rem;
    height: 48rem;
  }

  .emphasize {
    font-size: 6.4rem;
  }

  .normal {
    font-size: 4.8rem;
  }
}
</style>
