<template>
  <div class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <button class="close-btn" @click="closeModal">×</button>
      <div class="modal-header">
        <h2>계정정보 수정</h2>
      </div>
      <div class="modal-body">
        <button class="modal-btn" @click="openChangePasswordModal">비밀번호 변경</button>
        <button class="modal-btn" @click="openAccountDeactivationModal">회원탈퇴</button>
      </div>
    </div>
  </div>
</template>

<script setup>
const emit = defineEmits(['close', 'openChangePasswordModal', 'openAccountDeactivationModal']);

const closeModal = () => {
  emit('close');
};

const openChangePasswordModal = () => {
  emit('openChangePasswordModal');
};

const openAccountDeactivationModal = () => {
  emit('openAccountDeactivationModal');
};
</script>

<style scoped>
/* 모달 전체 오버레이 */
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
  z-index: 9999; /* 최상단으로 위치 */
}

/* 모달 콘텐츠 */
.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 10px;
  width: 400px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 부드러운 그림자 효과 */
  text-align: center;
  position: relative;
}

/* 모달 닫기 버튼 */
.close-btn {
  position: absolute;
  top: 1rem;
  right: 1rem;
  font-size: 3rem;
  background: none;
  border: none;
  cursor: pointer;
}

/* 모달 제목 스타일 */
.modal-header h2 {
  margin: 0;
  font-size: 3rem;
  color: #333;
  margin-bottom: 4rem;
}

/* 모달 본문 버튼 스타일 */
.modal-body {
  margin-top: 2rem;
  display: flex;
  justify-content: space-between; /* 버튼들을 가로로 나란히 배치 */
  gap: 2rem; /* 버튼 사이의 간격을 줌 */
}

.modal-btn {
  flex: 1; /* 버튼들이 같은 크기를 차지하도록 설정 */
  padding: 1rem 0.8rem;
  background-color: #ff7d7d;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 2.4rem;
  font-weight: 600;
  font-family: 'Noto Sans';
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.modal-btn:hover {
  background-color: #ff5959; /* 호버 시 살짝 어두운 색으로 변경 */
}
</style>
