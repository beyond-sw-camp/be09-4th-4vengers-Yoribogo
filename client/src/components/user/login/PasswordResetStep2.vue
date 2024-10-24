<template>
  <div class="modal-content">
    <div class="modal-header">
      <button class="back-btn" @click="goToStep1">
        <i class="fa-solid fa-arrow-left"></i>
        <!-- Font Awesome 아이콘 -->
      </button>
      <h2>요리보고</h2>
      <!-- 페이지 번호 표시 -->
      <div class="page-indicator">
        <span>2</span>
        <span>2</span>
      </div>
    </div>
    <div class="modal-body">
      <div class="message-container">
        <p class="first-text">{{ passwordResetData.user_auth_id }}님</p>
        <p class="second-text">비밀번호를 변경해주세요.</p>
      </div>

      <!-- 새 비밀번호 입력 -->
      <div class="input-container">
        <input
          :type="newPasswordVisible ? 'text' : 'password'"
          placeholder="새 비밀번호 입력"
          v-model="newPassword"
          maxlength="24"
        />
        <!-- 눈 아이콘 -->
        <i class="eye-icon" @click="toggleNewPasswordVisibility">
          <img :src="newPasswordVisible ? eyeOpenIcon : eyeClosedIcon" alt="eye icon" />
        </i>
      </div>
      <!-- 비밀번호 길이 에러 -->
      <span v-if="passwordError" class="error-text">{{ passwordError }}</span>

      <!-- 새 비밀번호 확인 -->
      <div class="input-container">
        <input
          :type="confirmPasswordVisible ? 'text' : 'password'"
          placeholder="새 비밀번호 확인"
          v-model="confirmPassword"
          maxlength="24"
        />
        <!-- 눈 아이콘 -->
        <i class="eye-icon" @click="toggleConfirmPasswordVisibility">
          <img :src="confirmPasswordVisible ? eyeOpenIcon : eyeClosedIcon" alt="eye icon" />
        </i>
      </div>
      <!-- 비밀번호 일치 여부 -->
      <span v-if="passwordMatchMessage" :class="passwordMatchClass">{{
        passwordMatchMessage
      }}</span>
    </div>

    <!-- 하단 버튼들 -->
    <div class="modal-footer">
      <YesNoButton type="cancel" label="취소" @click="goToStep1" />
      <YesNoButton
        type="next"
        label="비밀번호 변경"
        @click="resetPassword"
        :disabled="!canSubmit"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { resetPasswordAPI } from '@/api/user' // user.js에서 함수 임포트
import YesNoButton from '@/components/common/YesNoButton.vue'
import eyeOpenIcon from '@/assets/images/eye_open.png'
import eyeClosedIcon from '@/assets/images/eye_closed.png'

const emit = defineEmits(['close', 'openPasswordReset', 'openLogin'])

// LoginView.vue에서 전달받은 passwordResetData
const props = defineProps({
  passwordResetData: {
    type: Object,
    required: true,
    default: () => ({
      user_auth_id: '' // 기본값 설정
    })
  }
})

// 새 비밀번호, 비밀번호 확인 관련 변수들
const newPassword = ref('')
const confirmPassword = ref('')
const passwordError = ref('') // 비밀번호 길이 에러 메시지
const passwordMatchMessage = ref('') // 비밀번호 일치 여부 메시지
const passwordMatchClass = ref('') // 에러 혹은 성공 스타일 클래스
const newPasswordVisible = ref(false) // 새 비밀번호 표시 여부
const confirmPasswordVisible = ref(false) // 비밀번호 확인 표시 여부

// 눈 아이콘 토글 함수
const toggleNewPasswordVisibility = () => {
  newPasswordVisible.value = !newPasswordVisible.value
}

const toggleConfirmPasswordVisibility = () => {
  confirmPasswordVisible.value = !confirmPasswordVisible.value
}

// 비밀번호 유효성 검사 함수
const isPasswordValid = computed(() => {
  return newPassword.value.length >= 6
})

// 제출 가능 여부 (비밀번호 확인이 일치하는지 체크)
const canSubmit = computed(() => {
  return (
    newPassword.value.length > 0 &&
    newPassword.value === confirmPassword.value &&
    isPasswordValid.value
  )
  // return true;
})

// 비밀번호 재설정 API 호출 함수
const resetPassword = async () => {
  // 오류 메시지 초기화
  passwordError.value = ''
  passwordMatchMessage.value = ''

  // 유효성 검사
  if (!isPasswordValid.value) {
    passwordError.value = '비밀번호는 6자 이상이어야 합니다.'
    return
  }

  if (newPassword.value !== confirmPassword.value) {
    passwordMatchMessage.value = '비밀번호를 다시 확인해주세요.'
    passwordMatchClass.value = 'error-text' // 빨간색 스타일
    return
  }

  // 비밀번호 재설정 API 호출 (POST)
  try {
    const response = await resetPasswordAPI(props.passwordResetData.user_auth_id, newPassword.value)

    if (response.success) {
      alert('비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요!')
      goToLogin() //로그인창으로 이동
    } else {
      alert('비밀번호 변경에 실패했습니다.')
    }
  } catch (error) {
    alert('서버 오류가 발생했습니다.')
    console.error('비밀번호 재설정 에러:', error)
  }
}

// Step1로 돌아가기
const goToStep1 = () => {
  emit('openPasswordReset')
}

// 모달 닫기 함수
const goToLogin = () => {
  emit('close')
  emit('openLogin') // 로그인 모달을 여는 이벤트 호출
}

// 비밀번호 일치 여부 및 길이 검사
watch([newPassword, confirmPassword], () => {
  if (newPassword.value.length > 0 && confirmPassword.value.length > 0) {
    if (newPassword.value === confirmPassword.value) {
      passwordMatchMessage.value = '비밀번호가 일치합니다!'
      passwordMatchClass.value = 'success-text' // 초록색 스타일
    } else {
      passwordMatchMessage.value = '비밀번호를 다시 확인해주세요.'
      passwordMatchClass.value = 'error-text' // 빨간색 스타일
    }
  } else {
    passwordMatchMessage.value = ''
  }

  if (newPassword.value.length > 0 && !isPasswordValid.value) {
    passwordError.value = '비밀번호는 6자 이상이어야 합니다.'
  } else {
    passwordError.value = ''
  }
})
</script>

<style scoped>
/* 오류 텍스트 스타일 */
.error-text {
  color: #e1523a;
  font-size: 1.4rem;
  margin-top: 0.2rem;
}

/* 성공 텍스트 스타일 */
.success-text {
  color: #02b853;
  font-size: 1.4rem;
  margin-top: 0.2rem;
}

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
  /* 애니메이션 적용 */
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
  margin-bottom: 4rem; /* 모달 내용과의 간격 줄이기 */
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

.page-indicator {
  position: absolute;
  right: 1rem; /* 우측 여백 */
  top: 50%;
  transform: translateY(-50%); /* 세로 중앙 맞춤 */
  display: flex;
  align-items: center;
  background-color: #a6a6a6;
  padding: 5px 10px;
  border-radius: 15px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.page-indicator span {
  font-size: 1.4rem;
  color: #fff;
  margin: 0 5px;
  font-weight: 700;
}

.page-indicator span:nth-child(1) {
  color: #525150;
}
.page-indicator span:nth-child(2) {
  font-weight: 700;
}

/* 모달 바디 */
.modal-body {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start; /* 수정: 요소들이 위쪽에 붙게 설정 */
  align-items: flex-start;
  gap: 1rem;
  margin: 0.5rem;
}

/* 인사말 텍스트 */
.message-container {
  display: flex;
  flex-direction: column;
}

.first-text {
  font-size: 2.2rem;
  color: #525150;
}

.second-text {
  font-size: 2.2rem;
  font-weight: 700;
  color: #525150;
  margin-bottom: 1rem;
}

/* 입력 필드 스타일 */
.input-container {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-top: 2rem;
}

.input-container input {
  width: 360px;
  height: 40px;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1.6rem;
}

/* 수정된 눈 아이콘 스타일링 */
.eye-icon {
  position: absolute;
  right: 10px;
  top: 50%; /* top을 50%로 변경 */
  transform: translateY(-50%); /* 수직 중앙 정렬 */
  cursor: pointer;
}

.eye-icon img {
  width: 16px;
  height: 16px;
  opacity: 0.5;
}

/* 모달 푸터 */
.modal-footer {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 1rem 0;
}
</style>
