<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <button class="close-btn" @click="closeModal">×</button>
      <div class="modal-header">
        <h2>요리보고</h2>
      </div>
      <div class="modal-body">
        <div class="message-box">
          <!-- 닉네임과 아이디 표시 -->
          <p>{{ props.nickname }}님의 아이디는</p>
          <p class="user-id">
            <strong>{{ props.userAuthId }}</strong> 입니다.
          </p>
        </div>
        <button class="login-btn" @click="goToLogin">로그인 하러 가기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
const emit = defineEmits(['close', 'openLogin'])

// Props로 nickname과 userAuthId를 각각 받음
const props = defineProps({
  nickname: {
    type: String,
    required: true
  },
  userAuthId: {
    type: String,
    required: true
  }
})

// 모달 닫기 함수
const closeModal = () => {
  emit('close')
}

// 로그인 페이지로 이동
const goToLogin = () => {
  emit('close')
  emit('openLogin') // 로그인 모달을 여는 이벤트 호출
}
</script>

<style scoped>
/* 모달 오버레이 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

/* 모달 콘텐츠 */
.modal-content {
  background-color: white;
  border-radius: 10px;
  width: 380px;
  height: 280px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  text-align: center;
  animation: slide-up 0.3s ease-out;
}

/* 슬라이드 애니메이션 */
@keyframes slide-up {
  from {
    transform: translateY(50px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

/* 닫기 버튼 */
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
  text-align: center;
}

.modal-header h2 {
  margin-bottom: 1rem;
  font-size: 4rem;
  color: #000000;
}

/* 메시지 박스 */
.message-box {
  font-size: 2.2rem;
  color: #525150;
  margin-top: 4rem;
  margin-bottom: 2rem;
}

.user-id {
  font-size: 2.6rem;
  font-weight: 700;
  color: #f08c3a;
}

/* 로그인 버튼 */
.login-btn {
  width: 240px;
  height: 40px;
  background-color: #2c3e60;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 2rem;
  font-weight: 600;
  margin-top: 1rem;
}
</style>
