<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <div class="modal-header">
        <h2>휴면 계정 안내</h2>
      </div>
      <div class="modal-body">
        <div class="message-container">
          <p class="first-text">오랜만이에요.</p>
          <p class="second-text">계정을 재활성화해주세요!</p>
        </div>
        <div class="modal-footer">
          <YesNoButton type="cancel" label="취소" @click="closeModal" />
          <YesNoButton type="confirm" label="확인" @click="reactivateUser" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactivateUserByAuthId } from '@/api/user' // user.js에서 함수 임포트
import YesNoButton from '@/components/common/YesNoButton.vue'
const emit = defineEmits(['close'])

const props = defineProps({
  userAuthId: String
})

// 재활성화 함수
const reactivateUser = async () => {
  try {
    // reactivateUserByAuthId 함수 호출
    const response = await reactivateUserByAuthId(props.userAuthId)

    if (response.success) {
      alert('계정이 재활성화되었습니다. 다시 로그인 해주세요.')
      closeModal()
    } else {
      alert('재활성화에 실패했습니다.')
    }
  } catch (error) {
    alert('서버 오류가 발생했습니다. 다시 시도해주세요.')
    console.error('reactivateUser 에러:', error.response ? error.response.data : error.message) // 오류 로그 출력
  }
}

// 모달 닫기 함수
const closeModal = () => {
  emit('close')
}
</script>

<style scoped>
/* 모달 헤더 */
.modal-header {
  text-align: center;
}

.modal-header h2 {
  margin: 2rem;
  font-size: 3rem;
  color: #2c3e60;
}

/* 모달 오버레이 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 모달 콘텐츠 */
.modal-content {
  background-color: white;
  border-radius: 10px;
  width: 360px;
  height: 260px;
  padding: 20px;
  text-align: center;
}

/* 모달 바디 */
.modal-body {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 1rem;
  margin: 0.5rem;
  text-align: center;
}

/* 인사말 텍스트 */
.message-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
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

/* 모달 푸터 */
.modal-footer {
  position: relative;
  width: 320px;
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
}
</style>
