<template>
  <div class="modal-content">
    <div class="modal-header">
      <button class="back-btn" @click="goToHome">
        <i class="fa-solid fa-arrow-left"></i>
        <!-- Font Awesome 아이콘 -->
      </button>
      <h2>요리보고</h2>
      <!-- 페이지 번호 표시 -->
      <div class="page-indicator">
        <span>1</span>
        <span>2</span>
      </div>
    </div>
    <div class="modal-body">
      <div class="message-container">
        <!-- 1번: 인사말 -->
        <p class="first-text">어서와요!</p>
        <p class="second-text">이름과 이메일을 입력해주세요.</p>
      </div>

      <div class="input-container">
        <!-- 2번: 이름 입력 -->
        <input type="text" placeholder="이름 입력" v-model="localUserData.name" maxlength="6" />
        <span v-if="nameError" class="error-text">{{ nameError }}</span>

        <input
          type="email"
          placeholder="이메일 입력"
          v-model="localUserData.email"
          maxlength="24"
        />
        <!-- 이메일 유효성 검사 메시지 -->
        <span v-if="emailError" class="error-text">{{ emailError }}</span>
      </div>

      <div class="verification-container">
        <!-- 3번: 체크박스와 버튼 정렬 -->
        <div class="checkbox-container">
          <div class="email-checkbox" :class="{ verified: isVerified }"></div>
          <span class="checkbox-text">이메일 인증</span>
        </div>
        <button class="verify-btn" @click="handleSendVerification">
          {{ verificationButtonText }}
        </button>
      </div>
      <!-- 인증 에러 및 성공 메시지 표시 -->
      <span v-if="verificationMessage" class="message-text">{{ verificationMessage }}</span>
      <span v-if="verificationError" class="error-text">{{ verificationError }}</span>

      <div class="code-input-container">
        <!-- 4번: 인증코드 입력과 타이머 -->
        <div class="input-with-timer">
          <input
            type="text"
            placeholder="문자 6자리 입력"
            v-model="verificationCode"
            maxlength="6"
          />
          <span class="timer">{{ formattedTime }}</span>
        </div>
        <button class="confirm-btn" @click="confirmVerificationCode">확인</button>
      </div>
    </div>

    <!-- 오류 메시지 위치를 modal-footer 내부로 이동 -->
    <div class="modal-footer">
      <!-- 5번: 하단 버튼들 -->
      <YesNoButton type="cancel" label="회원가입 취소" @click="goToHome" />
      <YesNoButton type="next" label="다음" @click="goToNextStep" :disabled="!canProceed" />
    </div>
  </div>
</template>

<script setup>
import { ref, onUnmounted, computed, watch } from 'vue'
import YesNoButton from '@/components/common/YesNoButton.vue' // YesNoButton 컴포넌트 임포트
import router from '@/router/router'
// api/user.js에서 정의된 sendSignupVerificationEmail 함수 사용
import { sendSignupVerificationEmail, confirmVerificationCodeAPI } from '@/api/user.js'

// 외부에서 받아온 이벤트 정의
const emit = defineEmits(['close', 'goToStep2', 'update'])
const props = defineProps(['userData'])

// 로컬 상태로 사용자 데이터를 관리
const localUserData = ref({ ...props.userData })
const verificationCode = ref('')
const verificationButtonText = ref('인증번호 발송')
const timeRemaining = ref(0) // 초기 시간은 0으로 설정, 발송 버튼 클릭 시 300초로 설정
const verificationMessage = ref('') // 인증 관련 메시지
const verificationError = ref('') // 인증 에러 메시지
const nextStepError = ref('') // 다음 단계 오류 메시지
const nameError = ref('') // 이름 입력 에러 메시지
const emailError = ref('') // 이메일 입력 에러 메시지
const isVerified = ref(false) // 이메일 인증 성공 여부
let timerInterval = null

// 입력값 변경 시 부모에게 데이터 업데이트
watch(
  () => localUserData.value,
  (newData) => {
    emit('update', newData)
  },
  { deep: true }
)
// 이메일 유효성 검사 정규 표현식
const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

// 이메일 유효성 검사 결과 계산
const isEmailValid = computed(() => {
  return emailPattern.test(localUserData.value.email)
})

// 이메일 인증번호 발송 API 호출 함수
const sendVerificationCode = async () => {
  try {
    // api/user.js의 함수 사용하여 이메일 전송
    const response = await sendSignupVerificationEmail(localUserData.value.email)

    if (response.success) {
      // 성공 시 처리
      verificationMessage.value = '인증번호가 이메일로 발송되었습니다.' // 발송 성공 메시지
      verificationError.value = '' // 에러 메시지 초기화
    } else {
      // 실패 시 처리 (에러 메시지 표시)
      verificationError.value = response.error.message || '인증번호 발송에 실패했습니다.' // 서버에서 받은 에러 메시지 표시
      verificationMessage.value = '' // 성공 메시지 초기화
    }
  } catch (error) {
    // 오류 처리
    verificationError.value = error.response?.data?.message || '죄송합니다. 서버 내부 오류입니다.' // 서버 에러 메시지 표시
    verificationMessage.value = '' // 성공 메시지 초기화
    console.error('sendVerificationCode 에러:', error)
  }
}

// 발송 버튼 클릭 시 타이머 시작const handleSendVerification = () => {
const handleSendVerification = () => {
  // 초기화
  nameError.value = ''
  emailError.value = ''
  verificationError.value = ''
  verificationMessage.value = '' // 기존 메시지 초기화

  // 입력값 유효성 체크
  if (localUserData.value.name === '') {
    nameError.value = '이름을 입력해주세요.'
    return
  }
  if (!isEmailValid.value) {
    emailError.value = '유효한 이메일을 입력해주세요.'
    return
  }

  verificationMessage.value = '인증번호를 전송 중입니다...' // 전송 중 메시지 표시

  // 타이머가 작동 중이든 아니든 타이머를 재설정
  timeRemaining.value = 300 // 5분 설정
  sendVerificationCode() // 인증번호 발송 함수 호출

  if (!timerInterval) {
    startTimer() // 타이머 시작
  }
}

// 타이머 시작 함수
const startTimer = () => {
  clearTimer() // 기존 타이머 초기화
  timerInterval = setInterval(() => {
    if (timeRemaining.value > 0) {
      timeRemaining.value--
    } else {
      clearTimer() // 타이머 종료 시 타이머 초기화
    }
  }, 1000)
}

// 타이머 정리 함수
const clearTimer = () => {
  if (timerInterval) {
    clearInterval(timerInterval)
    timerInterval = null
  }
}

// 인증번호 확인 함수
const confirmVerificationCode = async () => {
  try {
    // api/user.js의 함수 사용하여 인증번호 검증
    const response = await confirmVerificationCodeAPI(
      localUserData.value.email,
      verificationCode.value
    )

    if (response.success) {
      // 성공 시 처리
      verificationMessage.value = '인증되었습니다.' // 인증 성공 메시지
      verificationError.value = '' // 에러 메시지 초기화
      isVerified.value = true // 인증 성공 토글 활성화
    } else {
      // 실패 시 처리 (서버에서 받은 에러 메시지 표시)
      verificationError.value = response.message || '인증번호가 올바르지 않습니다.'
      verificationMessage.value = '' // 성공 메시지 초기화
    }
  } catch (error) {
    // 에러 처리
    verificationError.value =
      error.response?.data?.message || '인증에 실패했습니다. 다시 시도해주세요.'
    verificationMessage.value = '' // 성공 메시지 초기화
    console.error('confirmVerificationCode 에러:', error)
  }
}

// 다음 버튼 활성화 여부 계산
const canProceed = computed(() => {
  return localUserData.value.name !== '' && isEmailValid.value && isVerified.value // 이름, 유효한 이메일, 인증 성공 여부 확인
  // return true;
})

// 홈으로 이동
const goToHome = () => {
  clearTimer() // 타이머 정리
  router.push('/')
  emit('close') // 회원가입 모달 닫기
}

// 다음 단계로 이동하는 함수
const goToNextStep = () => {
  nextStepError.value = '' // 기존 에러 메시지 초기화

  if (canProceed.value) {
    nextStepError.value = '' // 오류 메시지 초기화
    emit('goToStep2', { name: localUserData.value.name, email: localUserData.value.email }) // name과 email 키로 데이터 전달
  } else {
    // 필수 입력값이 부족한 경우 경고 메시지
    if (!localUserData.value.name) {
      nextStepError.value = '이름을 입력해주세요.'
    } else if (!isEmailValid.value) {
      nextStepError.value = '유효한 이메일을 입력해주세요.'
    } else if (!isVerified.value) {
      nextStepError.value = '이메일 인증을 완료해 주세요.'
    } else {
      nextStepError.value = '모든 필드를 입력하고 이메일 인증을 완료해 주세요.'
    }
    console.error('nextStepError:', nextStepError.value) // 에러 메시지 확인
  }
}

// 타이머 형식 변경 함수
const formattedTime = computed(() => {
  const minutes = String(Math.floor(timeRemaining.value / 60)).padStart(2, '0')
  const seconds = String(timeRemaining.value % 60).padStart(2, '0')
  return `${minutes}:${seconds}`
})

onUnmounted(() => {
  clearTimer() // 컴포넌트가 파괴될 때 타이머 정리
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
.message-text {
  color: #02b853;
  font-size: 1.4rem;
  margin-top: 0.5rem;
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
}

.page-indicator span:nth-child(1) {
  font-weight: 700;
}

/* 모달 바디 */
.modal-body {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
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

/* 입력 필드 */
.input-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  margin-top: 2rem;
}

.modal-body input {
  width: 360px;
  height: 40px;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1.6rem;
}

/* 인증번호 발송 */
.verification-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.checkbox-container {
  display: flex;
  align-items: center;
}

/* 인증 전 원형 스타일 */
.email-checkbox {
  width: 1.4rem;
  height: 1.4rem;
  border-radius: 50%;
  margin-right: 10px;
  cursor: pointer;
  border: 1px solid #ff7d7d;
  background-color: #ffffff;
}

/* 인증 후 체크박스 스타일 */
.email-checkbox.verified {
  background-color: transparent;
  border: 1px solid #02b853;
}

.checkbox-text {
  font-size: 1.8rem;
  font-weight: 600;
  color: #525150;
}

/* 인증번호 발송 버튼 */
.verify-btn {
  width: 100px;
  height: 40px;
  border: 0.8px solid #ff7d7d;
  border-radius: 5px;
  background-color: #fff;
  font-size: 1.6rem;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  color: #525150;
}

/* 인증코드 입력 필드 및 타이머 */
.code-input-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  margin-top: 2rem;
}

.input-with-timer {
  position: relative;
  width: 70%;
}

.input-with-timer input {
  width: 100%;
  padding-right: 60px;
}

.input-with-timer .timer {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #e99a24;
  font-size: 1.4rem;
}

/* 확인 버튼 */
.confirm-btn {
  width: 100px;
  height: 40px;
  background-color: #ff7d7d;
  color: white;
  display: flex;
  border-radius: 5px;
  border: none;
  justify-content: center;
  align-items: center;
}

/* 모달 푸터 */
.modal-footer {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 1rem 0;
}
</style>
