<template>
  <div class="modal-content">
    <div class="modal-header">
      <button class="back-btn" @click="goToPreviousStep">
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
        <!-- 인사말 -->
        <p class="first-text">만나서 반가워요!</p>
        <p class="second-text">아이디, 비밀번호, 닉네임을 입력해주세요.</p>
      </div>

      <div class="input-container">
        <!-- 아이디와 비밀번호 입력 -->
        <div class="userAuthId-container">
          <input
            type="text"
            placeholder="아이디 입력"
            v-model="localUserData.userAuthId"
            maxlength="20"
          />
          <button class="check-btn" @click="checkUserAuthIdDuplication">중복 확인</button>
        </div>
        <!-- 아이디 입력 에러 메시지 -->
        <span v-if="userAuthIdError" class="error-text">{{ userAuthIdError }}</span>
        <span v-if="userAuthIdDuplicationStatus" class="success-text">{{
          userAuthIdDuplicationStatus
        }}</span>

        <div class="password-input-container">
          <input
            :type="passwordVisible ? 'text' : 'password'"
            placeholder="비밀번호 입력"
            v-model="localUserData.password"
            maxlength="24"
          />
          <i class="eye-icon" @click="togglePasswordVisibility">
            <img :src="passwordVisible ? eyeOpenIcon : eyeClosedIcon" alt="eye icon" />
          </i>
        </div>
        <!-- 비밀번호 입력 에러 메시지 -->
        <span v-if="passwordError" class="error-text">{{ passwordError }}</span>

        <!-- 닉네임 입력 및 중복 확인 -->
        <div class="nickname-container">
          <input
            type="text"
            placeholder="닉네임 입력"
            v-model="localUserData.nickname"
            maxlength="10"
          />
          <button class="check-btn" @click="checkNicknameDuplication">중복 확인</button>
        </div>
        <!-- 닉네임 입력 에러 메시지 -->
        <span v-if="nicknameError" class="error-text">{{ nicknameError }}</span>
        <span v-if="nicknameDuplicationStatus" class="success-text">{{
          nicknameDuplicationStatus
        }}</span>
      </div>

      <div class="terms-container">
        <!-- 약관 동의 및 개인정보 처리방침 -->
        <div class="agreement-container">
          <!-- 약관 동의 토글 버튼 -->
          <div class="checkbox-container" @click="toggleAgreement">
            <div class="checkbox" :class="{ checked: isAgreed }">
              <!-- 체크된 경우 FontAwesome 체크 아이콘 -->
              <i v-if="isAgreed" class="fa-solid fa-check"></i>
            </div>
            <span class="checkbox-text">SGMA 이용약관 동의 및 개인정보처리방침</span>
          </div>
          <!-- 약관 보기 화살표 -->
          <div class="privacy-arrow-container" @click="openPrivacyPolicyModal">
            <i class="fa fa-chevron-right"></i>
            <!-- FontAwesome 화살표 아이콘 -->
          </div>
        </div>
        <!-- 약관 동의 에러 메시지 -->
        <span v-if="agreementError" class="error-text">{{ agreementError }}</span>
      </div>

      <div class="modal-footer">
        <!-- 하단 버튼들 -->
        <YesNoButton type="cancel" label="이전" @click="goToPreviousStep" />
        <YesNoButton
          type="complete"
          label="회원가입 하기"
          :disabled="!canProceed || isProcessing"
          @click="completeSignup"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import YesNoButton from '@/components/common/YesNoButton.vue' // YesNoButton 컴포넌트 임포트
import eyeOpenIcon from '@/assets/images/eye_open.png'
import eyeClosedIcon from '@/assets/images/eye_closed.png'
import { validateUserAuthId, validateNickname, signupUser } from '@/api/user' // user.js 파일에서 API 메서드 임포트
import { useRouter } from 'vue-router' // vue-router 임포트
// 외부에서 받아온 이벤트 정의
const emit = defineEmits(['close', 'goToStep1', 'openPrivacyPolicy', 'update'])
const props = defineProps({
  userData: {
    type: Object,
    required: true,
    default: () => ({
      name: '',
      email: ''
    })
  }
})

// 라우터 설정
const router = useRouter()

// 로컬 상태로 사용자 데이터를 관리
const localUserData = ref({
  userAuthId: '',
  password: '',
  nickname: '',
  name: props.userData.name, // Step 1에서 받은 이름 사용
  email: props.userData.email // Step 1에서 받은 이메일 사용
})

// 에러 메시지 상태
const userAuthIdError = ref('') // 아이디 입력 에러 메시지
const passwordError = ref('') // 비밀번호 입력 에러 메시지
const agreementError = ref('') // 약관 동의 에러 메시지
const nicknameError = ref('') // 닉네임 입력 에러 메시지
const userAuthIdDuplicationStatus = ref('') // 아이디 중복 검증 결과 메시지
const nicknameDuplicationStatus = ref('') // 닉네임 중복 검증 결과 메시지

// 약관 동의 여부 상태
const isAgreed = ref(false)

// 비밀번호 표시 여부 상태
const passwordVisible = ref(false)

// 아이디와 닉네임 사용 가능 여부
const isUserAuthIdAvailable = ref(false)
const isNicknameAvailable = ref(false)

// 약관 동의 체크박스 토글 함수
const toggleAgreement = () => {
  isAgreed.value = !isAgreed.value
}

// 비밀번호 표시/숨기기 토글 함수
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value
}

/**
 * 사용자 아이디 중복 확인 함수
 */
const checkUserAuthIdDuplication = async () => {
  // 초기화
  userAuthIdError.value = ''
  userAuthIdDuplicationStatus.value = ''
  isUserAuthIdAvailable.value = false

  if (!localUserData.value.userAuthId) {
    userAuthIdError.value = '아이디를 입력해주세요.'
    return
  }

  try {
    const response = await validateUserAuthId(localUserData.value.userAuthId)

    if (response && response.success) {
      const isExist = response.data.exist

      if (!isExist) {
        userAuthIdDuplicationStatus.value = '사용 가능한 아이디입니다.'
        isUserAuthIdAvailable.value = true
      } else {
        userAuthIdError.value = '이미 사용 중인 아이디입니다.'
        isUserAuthIdAvailable.value = false
      }
    } else {
      userAuthIdError.value = response.message || '아이디 중복 확인 중 문제가 발생했습니다.'
    }
  } catch (error) {
    userAuthIdError.value = '아이디 중복 확인 중 오류가 발생했습니다.'
    console.error('checkUserAuthIdDuplication 에러:', error)
  }
}

/**
 * 닉네임 중복 확인 함수
 */
const checkNicknameDuplication = async () => {
  // 초기화
  nicknameError.value = ''
  nicknameDuplicationStatus.value = ''
  isNicknameAvailable.value = false

  if (!localUserData.value.nickname) {
    nicknameError.value = '닉네임을 입력해주세요.'
    return
  }

  try {
    const response = await validateNickname(localUserData.value.nickname)

    if (response && response.success) {
      const isExist = response.data.exist

      if (!isExist) {
        nicknameDuplicationStatus.value = '사용 가능한 닉네임입니다.'
        isNicknameAvailable.value = true
      } else {
        nicknameError.value = '이미 사용 중인 닉네임입니다.'
        isNicknameAvailable.value = false
      }
    } else {
      nicknameError.value = response.message || '닉네임 중복 확인 중 문제가 발생했습니다.'
    }
  } catch (error) {
    nicknameError.value = '닉네임 중복 확인 중 오류가 발생했습니다.'
    console.error('checkNicknameDuplication 에러:', error)
  }
}

// 다음 버튼 활성화 여부 계산
const canProceed = computed(() => {
  return (
    localUserData.value.userAuthId !== '' &&
    localUserData.value.password !== '' &&
    localUserData.value.nickname !== '' &&
    isAgreed.value &&
    isUserAuthIdAvailable.value &&
    isNicknameAvailable.value
  ) // 사용 가능 여부도 추가
})

// 모달 닫기 함수
const closeModal = () => {
  emit('close')
}

// 이전 단계로 이동하는 함수
const goToPreviousStep = () => {
  emit('goToStep1') // 이전 단계로 이동 이벤트 발생
}

// 회원가입 처리 중 상태 플래그
const isProcessing = ref(false) // 비동기 요청 중 상태 관리

// 회원가입 완료 함수
const completeSignup = async () => {
  // 이미 처리 중이면 중복 호출 방지
  if (isProcessing.value) return

  // 처리 중 상태 설정
  isProcessing.value = true

  // 필수 필드가 모두 채워졌는지 체크 (간단한 유효성 검사)
  if (
    !localUserData.value.userAuthId ||
    !localUserData.value.password ||
    !localUserData.value.name ||
    !localUserData.value.nickname ||
    !localUserData.value.email
  ) {
    alert('모든 필드를 채워주세요.')
    isProcessing.value = false
    return
  }

  try {
    // 회원가입 API 호출
    const response = await signupUser({
      user_auth_id: localUserData.value.userAuthId,
      password: localUserData.value.password,
      user_name: localUserData.value.name, // Step 1에서 받은 이름 사용
      nickname: localUserData.value.nickname,
      email: localUserData.value.email, // Step 1에서 받은 이메일 사용
      signup_path: 'NORMAL' // 회원가입 경로 지정
    })

    // 응답에서 success 값 확인
    if (response.success) {
      // 가입 성공 처리
      emit('update', localUserData.value) // 부모에게 현재 데이터를 전달
      alert('회원가입이 완료되었습니다! 홈 화면으로 이동합니다.')
      closeModal() // 모달 닫기
      router.push('/') // 홈 화면으로 이동
    } else {
      // 실패 시 오류 메시지 처리
      const errorMessage = response.error?.message || '회원가입에 실패했습니다. 다시 시도해주세요.'
      alert(`sgma: ${errorMessage}`)
    }
  } catch (error) {
    // 네트워크 또는 서버 에러 처리
    alert('sgma: 회원가입에 실패했습니다. 다시 시도해주세요.')
    console.error('completeSignup 에러:', error)
  } finally {
    // 처리 완료 후 상태 해제
    isProcessing.value = false
  }
}

// 개인정보 처리방침 모달 열기 함수
const openPrivacyPolicyModal = () => {
  emit('openPrivacyPolicy') // 부모 컴포넌트에게 개인정보 처리방침 모달을 열라는 이벤트 전달
}
</script>

<style scoped>
/* 오류 텍스트 스타일 */
.error-text {
  color: #e1523a;
  font-size: 1.4rem;
  margin-top: 0.1rem;
}

/* 성공 텍스트 스타일 */
.success-text {
  color: #02b853;
  font-size: 1.4rem;
  margin-top: 0.1rem;
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
  gap: 1rem;
  margin-top: 1.5rem;
}

.userAuthId-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.check-btn {
  width: 80px;
  height: 40px;
  font-size: 1.4rem;
  border: none;
  border-radius: 5px;
  background-color: #ff7d7d;
  color: white;
  cursor: pointer;
}

.check-btn:hover {
  background-color: #ff7d7d;
}

/* 아이디 입력 필드 */
.userAuthId-container input {
  width: 274px; /* 아이디 입력 필드 너비 */
}

/* 비밀번호 입력 필드 컨테이너 */
.password-input-container {
  position: relative;
  display: flex;
  align-items: center;
}

.password-input-container input {
  width: 360px; /* 비밀번호 입력 필드 너비 */
}

/* 닉네임 입력 필드 컨테이너 */
.nickname-container {
  display: flex;
  align-items: center;
  gap: 1rem; /* 닉네임 필드와 버튼 간의 간격 */
}

.nickname-container input {
  width: 274px; /* 닉네임 입력 필드 너비 */
}

/* 눈 아이콘 스타일링 */
.eye-icon {
  position: absolute;
  right: 10px; /* 비밀번호 필드 안쪽에 배치 */
  cursor: pointer;
}

.eye-icon img {
  width: 16px; /* 아이콘 너비 조정 */
  height: 16px; /* 아이콘 높이 조정 */
  opacity: 0.5;
}

/* 입력 필드 스타일 */
.modal-body input {
  height: 40px;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1.6rem;
}

/* 약관 동의 및 개인정보 처리방침 */
.terms-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  position: absolute;
  bottom: 80px; /* 모달 콘텐츠 하단에 배치 */
  width: 360px; /* 전체 너비 */
}

.agreement-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border: 1px solid #ccc;
  padding: 1rem;
  border-radius: 5px;
  height: 40px;
  width: 360px;
}

.checkbox-container {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.checkbox {
  width: 22px;
  height: 22px;
  border: 3px solid #525150; /* 체크 전에는 볼드 테두리 */
  border-radius: 4px;
  background-color: white;
  margin-right: 10px;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.checkbox.checked {
  background-color: #2c3e60;
  border-color: #2c3e60; /* 체크 후에는 배경 및 테두리가 초록색 */
}

.checkbox.checked i {
  color: white; /* 체크된 경우 체크 아이콘 색상 */
  font-size: 1.6rem; /* 체크 아이콘 크기 */
}

.checkbox-text {
  font-size: 1.8rem;
  font-weight: 600;
  color: #525150;
}

.privacy-arrow-container {
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  margin-right: 1rem;
}

.privacy-arrow-container i.fa-chevron-right {
  font-size: 2.4rem;
  color: #2c3e60; /* 화살표 색상 */
}

/* 모달 푸터 */
.modal-footer {
  position: absolute; /* 모달 콘텐츠 내에서 절대 위치 지정 */
  bottom: 10px; /* 모달 콘텐츠의 하단에 배치 */
  width: 360px; /* 푸터의 너비 */
  justify-content: space-between; /* 요소들 간의 공간을 균등 분배 */
  display: flex; /* 플렉스 박스 사용 */
  background-color: white; /* 배경색이 바디와 같게 */
  margin: 0 auto; /* 좌우 가운데 정렬 */
  left: 0; /* 부모 요소 기준 왼쪽 기준점 */
  right: 0; /* 부모 요소 기준 오른쪽 기준점 */
}
</style>
