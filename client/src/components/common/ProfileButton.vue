<template>
  <div class="profile-btn">
    <div class="profile-img-wrapper" @click="toggleMenu">
      <img :src="profileImage" alt="Profile" />
    </div>
    <ul class="menu-list" v-show="isMenuVisible">
      <!-- 로그인된 경우 마이페이지와 로그아웃을 동시에 표시 -->
      <li v-if="isLoggedIn" class="menu" @click="goMyPage">
        <i class="fa-solid fa-user"></i>
        <span>마이페이지</span>
      </li>
      <li v-if="isLoggedIn" class="menu" @click="logout">
        <i class="fa-solid fa-arrow-right-from-bracket"></i>
        <span>로그아웃</span>
      </li>

      <!-- 로그인되지 않은 경우 회원가입과 로그인 버튼을 동시에 표시 -->
      <li v-if="!isLoggedIn" class="menu" @click="goSignup">
        <i class="fa-solid fa-user-plus"></i>
        <span>회원가입</span>
      </li>
      <li v-if="!isLoggedIn" class="menu" @click="goLogin">
        <i class="fa-solid fa-arrow-right-to-bracket"></i>
        <span>로그인</span>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/tokenStore' // Pinia 스토어 임포트
import { fetchUserByAuthId } from '@/api/user' // 사용자 프로필 조회 API

const router = useRouter()
const tokenStore = useTokenStore() // Pinia 스토어 사용

// 로그인 상태 체크
const isLoggedIn = computed(() => !!tokenStore.token.accessToken)

// 기본 프로필 이미지 URL
const defaultProfileImage =
  'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png'
// 프로필 이미지 상태 관리
const profileImage = ref(defaultProfileImage)

// 로그인된 경우에만 프로필 이미지 가져오기
const loadUserProfileImage = async () => {
  const userAuthId = tokenStore.token.userAuthId // 정확한 경로로 변경
  const accessToken = tokenStore.token.accessToken

  if (isLoggedIn.value) {
    if (userAuthId && accessToken) {
      try {
        // 사용자 프로필 정보 가져오기
        const userProfile = await fetchUserByAuthId(userAuthId, accessToken)

        // 프로필 이미지가 있으면 설정, 없으면 기본 이미지 설정
        if (userProfile.success && userProfile.data.profile_image) {
          profileImage.value = userProfile.data.profile_image // 프로필 이미지 설정
        } else {
          profileImage.value = defaultProfileImage // 기본 이미지로 설정
        }
      } catch (error) {
        console.error('프로필 이미지 가져오기 중 에러 발생:', error)
        profileImage.value = defaultProfileImage // 에러 발생 시 기본 이미지 사용
      }
    } else {
      console.error('userAuthId 또는 accessToken이 없습니다.')
      profileImage.value = defaultProfileImage // 유효한 토큰이 없으면 기본 이미지 사용
    }
  } else {
    profileImage.value = defaultProfileImage // 로그인되지 않은 경우 기본 이미지 사용
  }
}

// 메뉴 보이기/숨기기
const isMenuVisible = ref(false)
const toggleMenu = () => {
  isMenuVisible.value = !isMenuVisible.value
}

// 마이페이지로 이동
const goMyPage = () => {
  router.push('/mypage')
}

// 로그아웃 처리
const logout = () => {
  tokenStore.logout() // Pinia 스토어의 로그아웃 함수 호출
  localStorage.removeItem('token')
  router.push('/') // 로그아웃 후 홈으로 이동
}

// 회원가입 페이지로 이동
const goSignup = () => {
  router.push('/signup')
}

// 회원가입 페이지로 이동
const goLogin = () => {
  router.push('/login-page')
}

// 마운트될 때 사용자 프로필 이미지 불러오기
onMounted(() => {
  loadUserProfileImage()
})
</script>

<style scoped>
.profile-btn {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 1rem;
  position: relative; /* 부모 요소는 relative */
}

.profile-img-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 6.3rem;
  height: 6.3rem;
  border: none;
  border-radius: 50%;
  overflow: hidden;
  background-color: var(--light-yellow-color);
  box-shadow: 0rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  cursor: pointer;
  z-index: 9999;
}

.profile-img-wrapper img {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

.profile-img-wrapper:hover {
  transition: 0.3s ease;
  box-shadow: 0rem 0.4rem 0.4rem 0.1rem rgba(60, 60, 60, 0.5);
}

.menu-list {
  display: flex;
  flex-direction: column;
  background-color: var(--white-color);
  width: 28rem;
  padding: 1rem;
  border-radius: 1.2rem;
  box-shadow: 0.1rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  z-index: 1000;
  position: absolute; /* 메뉴는 부모 안에서 절대 위치 */
  right: 0; /* 오른쪽 정렬 */
  top: 7rem; /* 아래로 약간 떨어뜨림 */
}

.menu {
  display: flex;
  align-items: center;
  width: 100%;
  height: 5rem;
  padding-left: 1rem;
  gap: 1rem;
  cursor: pointer;
  border: none;
  border-radius: 1rem;
  font-size: 1.6rem;
  font-weight: 500;
  color: var(--black-color);
}

.menu:hover {
  background-color: var(--light-pink-color);
}
</style>
