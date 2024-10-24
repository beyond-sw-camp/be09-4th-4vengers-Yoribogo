<template>
  <div class="modal-content">
    <!-- 상단 왼쪽 뒤로가기 버튼 -->
    <div class="modal-header">
      <button class="back-btn" @click="goBack">
        <i class="fa-solid fa-arrow-left"></i>
        <!-- Font Awesome 아이콘 -->
      </button>
      <h2>요리보고</h2>
    </div>

    <div class="modal-body">
      <input type="text" placeholder="아이디" v-model="username" @keyup.enter="login" />
      <!-- 아이디 입력 에러 메시지 -->
      <span v-if="usernameError" class="error-message">{{ usernameError }}</span>

      <div class="password-input-container">
        <input
          :type="passwordVisible ? 'text' : 'password'"
          placeholder="비밀번호 입력"
          v-model="password"
          maxlength="24"
          @keyup.enter="login"
        />
        <i class="eye-icon" @click="togglePasswordVisibility">
          <img :src="passwordVisible ? eyeOpenIcon : eyeClosedIcon" alt="eye icon" />
        </i>
      </div>

      <!-- 비밀번호 입력 에러 메시지 -->
      <span v-if="passwordError" class="error-message">{{ passwordError }}</span>

      <button class="login-btn" @click="login">로그인</button>

      <div class="sns-login">
        <hr />
        <span>SNS LOGIN</span>
        <hr />
      </div>
      <div class="sns-buttons">
        <button class="sns-btn kakao" @click="navigateToKakaoLogin"></button>
        <button class="sns-btn naver" @click="navigateToNaverLogin"></button>
      </div>
      <div class="login-options">
        <a href="#" @click.prevent="openFindId">아이디 찾기</a>
        <span class="divider">|</span>
        <a href="#" @click.prevent="openPasswordReset">비밀번호 찾기</a>
        <span class="divider">|</span>
        <a href="#" @click.prevent="goSignup">회원가입</a>
      </div>
    </div>

    <!-- 계정 재활성화 모달 추가 -->
    <AccountReactivationModal
      v-if="isAccountReactivationModalVisible"
      @close="closeAccountReactivationModal"
      :userAuthId="username"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { fetchUserByAuthId } from '@/api/user' // user.js에서 함수 임포트
import { useTokenStore } from '@/stores/tokenStore' // Pinia 토큰 스토어 임포트
import axios from 'axios'
import eyeOpenIcon from '@/assets/images/eye_open.png'
import eyeClosedIcon from '@/assets/images/eye_closed.png'
import AccountReactivationModal from '@/components/user/login/AccountReactivationModal.vue'

// useRouter를 최상위 레벨에서 호출
const router = useRouter()

// 환경 변수로부터 카카오 로그인 관련 정보 불러오기
const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${import.meta.env.VITE_KAKAO_REST_API_KEY}&redirect_uri=${import.meta.env.VITE_KAKAO_REDIRECT_URI}&response_type=code`
const NAVER_AUTH_URL = `https://nid.naver.com/oauth2.0/authorize?client_id=${import.meta.env.VITE_NAVER_CLIENT_ID}&redirect_uri=${import.meta.env.VITE_NAVER_REDIRECT_URI}&response_type=code&state=STATE_STRING`

// 외부에서 받아온 이벤트 정의
const emit = defineEmits(['close', 'goToStep1', 'openPasswordReset', 'openFindId'])

const username = ref('')
const password = ref('')

// 에러 메시지 상태 변수 정의
const usernameError = ref('') // 아이디 에러 메시지 상태
const passwordError = ref('') // 비밀번호 에러 메시지 상태

// 비밀번호 표시 여부 상태 관리
const passwordVisible = ref(false)

// 계정 재활성화 모달 상태 관리
const isAccountReactivationModalVisible = ref(false)

// 계정 재활성화 모달 열기 함수
const openAccountReactivationModal = () => {
  isAccountReactivationModalVisible.value = true
}

// 계정 재활성화 모달 닫기 함수
const closeAccountReactivationModal = () => {
  isAccountReactivationModalVisible.value = false
}

// 모달 닫기 함수
const closeModal = () => {
  emit('close')
}

// 비밀번호 표시/숨기기 토글 함수
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value
}

// 로그인 처리 함수
const login = async () => {
  usernameError.value = ''
  passwordError.value = ''

  // 입력값 유효성 검사
  if (!username.value) {
    usernameError.value = '아이디를 입력하세요.'
    return
  }
  if (!password.value) {
    passwordError.value = '비밀번호를 입력하세요.'
    return
  }

  try {
    // 
    const response = await axios.post('/boot/login', {
      user_auth_id: username.value,
      password: password.value
    })

    // 응답 처리
    if (response.data.success) {
      const tokenStore = useTokenStore()

      // 토큰 데이터 임시 저장 (user_id 없이)
      const tempTokenData = {
        user_auth_id: response.data.data.user_auth_id,
        access_token: response.data.data.access_token,
        access_token_expiry: response.data.data.access_token_expiry,
        refresh_token: response.data.data.refresh_token,
        refresh_token_expiry: response.data.data.refresh_token_expiry
      }

      // user_auth_id로 사용자 정보 조회 (accessToken도 포함해서)
      await tokenStore.setTokenData(tempTokenData) // 먼저 accessToken을 설정해주고
      const userProfileResponse = await fetchUserByAuthId(
        tempTokenData.user_auth_id,
        tempTokenData.access_token
      ) // fetchUserId 호출 시 accessToken 사용
      const userProfile = userProfileResponse?.data || {}

      // 최종 토큰 데이터에 user_id 추가
      const tokenData = {
        ...tempTokenData,
        user_id: userProfile.user_id // 조회한 user_id 추가
      }

      // 토큰 정보 저장
      await tokenStore.setTokenData(tokenData)

      closeModal() // 모달 닫기
      // 홈화면으로 리다이렉트
      router.push('/')
    } else {
      passwordError.value = response.data.error?.message || '로그인에 실패했습니다.'
    }
  } catch (error) {
    if (error.response) {
      if (error.response.data?.error?.code === 40320) {
        openAccountReactivationModal() // 계정 재활성화 모달 열기
      } else {
        passwordError.value =
          error.response.data?.error?.message ||
          '로그인 요청이 거부되었습니다. 서버 상태를 확인해주세요.'
      }
    } else {
      passwordError.value = '로그인 요청이 실패했습니다. 서버 상태를 확인해주세요.'
    }
  }
}

// 아이디 찾기 모달 열기
const openFindId = () => {
  emit('openFindId')
}

// 비밀번호 찾기 모달 열기
const openPasswordReset = () => {
  emit('openPasswordReset')
}

// 카카오 로그인 페이지로 리다이렉트
const navigateToKakaoLogin = () => {
  window.location.href = KAKAO_AUTH_URL
}

// 네이버 로그인 페이지로 리다이렉트
const navigateToNaverLogin = () => {
  window.location.href = NAVER_AUTH_URL
}

// 홈화면으로 이동
const goBack = () => {
  router.push('/')
}

// 회원가입 창으로 이동
const goSignup = () => {
  router.push('/signup')
}
</script>

<style scoped>
/* 모달 콘텐츠 */
.modal-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
  border-radius: 10px;
  width: 400px;
  height: 500px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  animation: slide-up 0.3s ease-out;

  /* 화면 중앙에 배치 */
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

/* 슬라이드 애니메이션 */
@keyframes slide-up {
  from {
    transform: translate(-50%, -40%);
    opacity: 0;
  }
  to {
    transform: translate(-50%, -50%);
    opacity: 1;
  }
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.2rem 1rem; /* 상단 여백을 줄여서 위로 밀기 */
  position: relative;
  width: 100%;
  margin-top: 6rem;
  margin-bottom: 10rem; /* 모달 내용과의 간격 줄이기 */
}

.modal-header h2 {
  font-size: 3.6rem; /* 조금 더 작게 */
  color: #000000;
  margin: 0; /* 텍스트 기본 여백 제거 */
  position: absolute; /* 텍스트를 중앙에 고정 */
  top: 50%; /* 부모 높이의 중앙 */
  left: 50%; /* 부모 너비의 중앙 */
  transform: translate(-50%, -50%); /* 중앙 정렬 */
}

.back-btn {
  position: absolute;
  left: 1rem; /* 좌측 여백 */
  top: 50%; /* 중앙에 맞춤 */
  transform: translateY(-50%); /* 세로 정렬 */
  background-color: black;
  border-radius: 50%;
  width: 3.5rem; /* 크기 살짝 줄임 */
  height: 3.5rem;
  display: flex;
  justify-content: center;
  align-items: center;
  border: none;
  cursor: pointer;
}

.back-btn i {
  color: white;
  font-size: 1.4rem; /* 아이콘도 조금 작게 */
}

.modal-body input {
  width: 360px;
  height: 40px;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1.6rem;
  margin: 0.5rem;
}

/* 비밀번호 입력 필드 컨테이너 */
.password-input-container {
  position: relative;
  display: flex;
  align-items: center;
}

/* 눈 아이콘 스타일링 */
.eye-icon {
  position: absolute;
  right: 20px; /* 아이콘을 오른쪽 끝에 배치 */
  cursor: pointer;
}

.eye-icon img {
  width: 16px; /* 아이콘 너비 조정 */
  height: 16px; /* 아이콘 높이 조정 */
  opacity: 0.5;
}

.login-btn {
  width: 360px;
  height: 40px;
  border: none;
  background-color: #000000;
  color: #ffffff;
  text-align: center;
  font-size: 1.8rem;
  font-weight: 200;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 6rem;
  margin-bottom: 2rem;
}

.sns-login {
  font-size: 1.5rem;
  display: flex;
  width: auto;
  align-items: center;
  margin: 1rem 0;
}

.sns-login hr {
  flex: 1;
  border: none;
  border-top: 1px solid #ddd;
}

.sns-login span {
  margin: 0 1.6rem;
  color: #888;
}

.sns-buttons {
  display: flex;
  justify-content: center;
  gap: 8rem;
  margin-bottom: 4rem;
}

.sns-btn {
  width: 5rem;
  height: 5rem;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.kakao {
  background-image: url('@/assets/images/btn_kakao.png');
}

.naver {
  background-image: url('@/assets/images/btn_naver.png');
}

.login-options {
  margin-top: 0rem;
  font-size: 1.4rem;
  color: #888;
  display: flex;
  justify-content: center;
  gap: 1.6rem;
}

.divider {
  color: #888;
}

.login-options a {
  color: #888888;
  text-decoration: none;
}

/* 컴포넌트 내에만 적용되는 에러 메시지 스타일 */
.error-message {
  color: #e1523a;
  font-size: 1.4rem;
  margin-top: 0.5rem; /* 위 여백 */
  margin-left: 1.5rem; /* 위 여백 */
  display: block; /* 블록 요소로 설정 */
  text-align: left;
}
</style>
