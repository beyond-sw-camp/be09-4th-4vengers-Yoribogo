<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <button class="close-btn" @click="closeModal">×</button>
      <div class="modal-header">
        <h2>회원 탈퇴</h2>
      </div>
      <div class="modal-body">
        <!-- 회원 탈퇴 안내문구 -->
        <div class="policy-content">
          <p>
            회원 탈퇴를 신청하신 경우, 다음 사항을 참고해주시기 바랍니다.<br />
            <br />
            1. 개인정보 삭제: 회원 탈퇴 시, 서비스 이용을 위해 제공하신 개인정보는 5년 동안 보관 후
            안전하게 파기됩니다. 다만, 관련 법령에 따라 일정 기간 동안 보관이 필요한 정보는 법령에서
            정한 기간 동안 보관 후 삭제됩니다.<br />
            <br />
            2. 이용 기록 삭제: 회원 탈퇴 시, 회원님의 모든 이용 기록 및 데이터는 복구할 수 없으며,
            이는 탈퇴 후에도 동일하게 적용됩니다.<br />
            <br />
            3. 법적 보관 의무: 「개인정보보호법」 및 기타 관련 법령에 의거하여, 일부 거래 정보 및
            법적으로 필요한 데이터는 일정 기간 동안 보관될 수 있습니다.<br />
            <br />
            4. 재가입 제한: 회원탈퇴 후 동일한 계정 정보로 재가입하실 수 있으며, 계정에 따라 일정
            기간 동안 재가입이 제한될 수 있습니다.<br />
            <br />
            요리보고를 이용해주셔서 감사합니다.
          </p>
        </div>
      </div>
      <div class="modal-footer">
        <YesNoButton type="cancel" label="아니요" @click="closeModal" />
        <YesNoButton type="confirm" label="탈퇴하기" @click="deactivateAccount" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router' // Vue Router 임포트
import { deactivateUser } from '@/api/user' // 회원 탈퇴 API 호출 함수
import YesNoButton from '@/components/common/YesNoButton.vue' // YesNoButton 컴포넌트 임포트

import { useTokenStore } from '@/stores/tokenStore' // Pinia 토큰 스토어 임포트

const tokenStore = useTokenStore()
const emit = defineEmits(['close']) // 부모에게 close 이벤트 방출
const router = useRouter() // Vue Router 사용 설정

// 모달 닫기 함수
const closeModal = () => {
  emit('close')
}

// 로그아웃 및 토큰 삭제 함수
const logout = () => {
  tokenStore.logout() // Pinia 스토어의 로그아웃 함수 호출
  router.push('/') // 로그아웃 후 홈으로 이동
}

// 회원 탈퇴 요청 함수
const deactivateAccount = async () => {
  if (confirm('정말로 회원 탈퇴를 진행하시겠습니까?')) {
    try {
      const response = await deactivateUser(tokenStore.token.userId, tokenStore.token.accessToken)
      if (response.success) {
        alert('회원 탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.')
        logout() // 로그아웃 함수 호출
        closeModal() // 모달 닫기
      } else {
        alert(`회원 탈퇴 중 오류가 발생했습니다: ${response.message}`)
      }
    } catch (error) {
      alert('회원 탈퇴 중 오류가 발생했습니다. 다시 시도해주세요.')
      console.error('회원 탈퇴 오류:', error)
    }
  }
}
</script>

<style scoped>
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

.modal-content {
  background-color: white;
  border-radius: 10px;
  width: 400px;
  height: 480px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  text-align: left;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

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

.modal-header {
  text-align: center;
}

.modal-header h2 {
  margin-bottom: 1.5rem;
  font-size: 2.2rem;
  font-weight: 900;
  color: #f35757;
}

.modal-body {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  overflow-y: auto;
  margin-bottom: 1rem;
}

.policy-content {
  border: 1px solid #ccc;
  padding: 1.5rem;
  border-radius: 5px;
  background-color: #f9f9f9;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
}

.policy-content p {
  font-size: 1.4rem;
  color: #525150;
  line-height: 1.8;
  margin: 0;
}

.modal-footer {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
</style>
