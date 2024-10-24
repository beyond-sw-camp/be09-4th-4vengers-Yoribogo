<template>
  <div class="modal-overlay" v-if="showProfileModal">
    <div class="modal-content">
      <button class="close-btn" @click="closeModal">×</button>

      <div class="modal-header">
        <h2>프로필 정보 추가</h2>
      </div>

      <div class="modal-body">
        <div class="message-container">
          <p class="first-text">어서와요!</p>
          <p class="second-text">
            프로필 사진과 닉네임을 설정해주세요.<br />사용하실 닉네임 입력은 필수에요!
          </p>
        </div>

        <div class="profile-picture-container">
          <label class="profile-picture-label">
            <div class="profile-picture">
              <input type="file" @change="handleFileChange" accept="image/*" hidden />
              <span v-if="!profilePicture">+</span>
              <img v-if="profilePicture" :src="profilePicture" alt="Profile Picture" />
            </div>
          </label>

          <div class="nickname-container">
            <input type="text" placeholder="닉네임 입력" v-model="nickname" maxlength="30" />
            <button class="check-btn" @click="checkNicknameDuplication">중복 확인</button>
          </div>

          <span v-if="nicknameError" class="error-text">{{ nicknameError }}</span>
          <span
            v-if="nicknameDuplicationStatus"
            :class="{ 'success-text': !nicknameError, 'error-text': nicknameError }"
          >
            {{ nicknameDuplicationStatus }}
          </span>
        </div>
      </div>

      <div class="modal-footer">
        <YesNoButton type="cancel" label="로그아웃" @click="logout" />
        <YesNoButton
          type="complete"
          label="완료"
          @click="completeUpdateProfile"
          :disabled="!canComplete"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import YesNoButton from '@/components/common/YesNoButton.vue'
import { updateUserProfile, validateNickname } from '@/api/user' // 필요한 API 함수들 임포트
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/tokenStore' // Pinia 토큰 스토어 임포트

// 토큰 정보 주입
const tokenStore = useTokenStore()

// 모달 가시성을 제어하는 변수
const showProfileModal = ref(true) // true로 설정하여 모달을 처음에 표시함

// 닉네임과 프로필 사진을 위한 데이터
const nickname = ref('')
const profilePicture = ref('') // Base64로 변환된 이미지
const profilePictureFile = ref(null) // 실제 파일 객체를 저장할 변수
const nicknameError = ref('')
const isNicknameAvailable = ref(false)
const nicknameDuplicationStatus = ref('')

// 닉네임만 입력되어야 완료 버튼이 활성화됨
const canComplete = computed(() => nickname.value !== '' && isNicknameAvailable.value)

// 라우터 사용
const router = useRouter()

// 파일 선택 시 호출되는 함수
const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    profilePictureFile.value = file // 실제 파일을 저장
    const reader = new FileReader()
    reader.onload = (e) => {
      profilePicture.value = e.target.result // 미리보기를 위한 Base64 URL
    }
    reader.readAsDataURL(file)
  }
}

// 닉네임 중복 확인 함수
const checkNicknameDuplication = async () => {
  nicknameError.value = ''
  nicknameDuplicationStatus.value = ''
  isNicknameAvailable.value = false

  if (!nickname.value) {
    nicknameError.value = '닉네임을 입력해주세요.'
    return
  }

  try {
    const response = await validateNickname(nickname.value)
    if (response && response.success) {
      if (!response.data.exist) {
        nicknameDuplicationStatus.value = '사용 가능한 닉네임입니다.'
        isNicknameAvailable.value = true
      } else {
        nicknameError.value = '이미 사용 중인 닉네임입니다.'
      }
    } else {
      nicknameError.value = '닉네임 중복 확인 중 오류가 발생했습니다.'
    }
  } catch (error) {
    nicknameError.value = '닉네임 중복 확인 중 오류가 발생했습니다.'
    console.error('checkNicknameDuplication 에러:', error)
  }
}

// 프로필 업데이트 함수
const completeUpdateProfile = async () => {
  if (!canComplete.value) return

  try {
    const profileImage = profilePictureFile.value || null // 파일 객체 사용

    // updateUserProfile 함수를 호출
    const response = await updateUserProfile(
      tokenStore.token.userId,
      nickname.value,
      profileImage,
      tokenStore.token.accessToken
    )

    if (response.success) {
      alert('프로필이 성공적으로 업데이트되었습니다!')

      // 페이지를 새로고침하는 대신, 특정 페이지로 이동
      window.location.href = '/mypage' // 확인 버튼 누르면 마이페이지로 이동
    } else {
      alert(`프로필 수정 중 오류가 발생했습니다: ${response.message}`)
    }
  } catch (error) {
    if (error.response && error.response.status === 403) {
      alert('접근 권한이 없습니다. 다시 로그인해주세요.')
    } else {
      alert('프로필 수정 중 오류가 발생했습니다. 다시 시도해주세요.')
    }
    console.error('completeUpdateProfile 에러:', error)
  }
}

// 로그아웃 처리
const logout = () => {
  tokenStore.logout() // Pinia 스토어의 로그아웃 함수 호출
  router.push('/') // 로그아웃 후 홈으로 이동
}

// 모달 닫기 함수 - 닉네임을 입력하지 않았을 경우 경고창을 표시
const closeModal = () => {
  if (!nickname.value || !isNicknameAvailable.value) {
    alert('닉네임을 설정하셔야 합니다!')
  } else {
    showProfileModal.value = false // 모달 창 닫기
  }
}

// 사용자가 페이지를 떠나려고 할 때 막음
const handlePopState = (event) => {
  if (showProfileModal.value && (!nickname.value || !isNicknameAvailable.value)) {
    const leave = confirm('닉네임 설정이 완료되지 않았습니다. 정말로 페이지를 떠나시겠습니까?')
    if (!leave) {
      event.preventDefault()
      window.history.pushState(null, null, window.location.href) // 뒤로가기를 막음
    } else {
      router.go(-1) // 정상적으로 페이지를 이동
    }
  }
}
onMounted(() => {
  window.addEventListener('popstate', handlePopState) // 브라우저의 뒤로가기를 감지
})

onUnmounted(() => {
  window.removeEventListener('popstate', handlePopState) // 이벤트 제거
})
</script>

<style scoped>
/* 모달 오버레이 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999; /* 화면 최상위에 오도록 */
}

.modal-content {
  background-color: white;
  border-radius: 10px;
  width: 400px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
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

/* 모달 닫기 버튼 */
.close-btn {
  position: absolute;
  top: 1rem;
  right: 1rem;
  font-size: 3.2rem;
  opacity: 50%;
  background: none;
  border: none;
  cursor: pointer;
}

/* 모달 헤더 */
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

/* 인사말 텍스트 */
/* 인사말 텍스트 */
.message-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 왼쪽 정렬 */
  padding-left: 1rem; /* 약간의 왼쪽 여백 추가 */
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
  text-align: left; /* 왼쪽 정렬 추가 */
}

/* 모달 바디 */
.modal-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  gap: 1.5rem;
  margin: 0.5rem;
  padding: 1rem;
}

/* 프로필 사진과 닉네임 입력 */
.profile-picture-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  margin-top: 2rem;
  padding: 1rem;
}

.profile-picture-label {
  cursor: pointer;
}

.profile-picture {
  width: 140px;
  height: 140px;
  background-color: #f5f5f5;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 2px dashed #53acff;
  position: relative;
  overflow: hidden;
  transition: border 0.3s ease-in-out;
}

.profile-picture:hover {
  border: 2px solid #2c3e50;
}

.profile-picture span {
  font-size: 4rem;
  color: #2c3e50;
}

.profile-picture img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  position: absolute;
  top: 0;
  left: 0;
}

/* 닉네임 입력 및 중복 확인 */
.nickname-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

input[type='text'] {
  width: 140px;
  height: 40px;
  padding: 0.8rem;
  border: 1.5px solid #c5ccd2;
  border-radius: 5px;
  font-size: 1.6rem;
  text-align: center;
  transition: border-color 0.3s ease-in-out;
}

input[type='text']:focus {
  border-color: #2c3e50;
  outline: none;
}

.check-btn {
  padding: 0.6rem 1rem;
  height: 40px;
  font-size: 1.6rem;
  border: none;
  border-radius: 5px;
  background-color: #53acff;
  color: white;
  cursor: pointer;
}

.check-btn:hover {
  background-color: #2c3e50;
}

/* 모달 푸터 */
.modal-footer {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 1rem 0;
}

/* 오류 텍스트 스타일 */
.error-text {
  color: red;
  font-size: 1.4rem;
  margin-top: 0.2rem;
}

/* 성공 텍스트 스타일 */
.success-text {
  color: green;
  font-size: 1.4rem;
  margin-top: 0.2rem;
}
</style>
