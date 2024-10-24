<template>
  <div class="login-page">
    <!-- 왼쪽 상단 로고 -->
    <div class="logo-wrapper" @click="goHome">
      <img src="/src/assets/logo.svg" alt="Yoribogo Logo" />
      <span>요리보고</span>
    </div>

    <!-- 오른쪽 요리사 이미지 및 텍스트 -->
    <div class="chef-wrapper">
      <img src="/src/assets/images/login_page_profile.png" alt="Chef" />
      <div class="speech-bubble">
        <span>지금 뭐 먹을지 고민하세요?</span>
      </div>
    </div>

  	<!-- 회원가입 단계별 모달 -->
    <SignupStep1
		v-if="isRegisterModalVisible && currentSignupStep === 1"
		@close="closeRegisterModal"
		@update="updateUserData"
		@goToStep2="goToStep2"
		:userData="userData"
	  />
	  <SignupStep2
		v-if="isRegisterModalVisible && currentSignupStep === 2"
		:userData="userData"
		@close="closeRegisterModal"
		@update="updateUserData"
		@goToStep1="openRegisterModal"
		@openPrivacyPolicy="openPrivacyPolicyModal"
	  />
	  <!-- 개인정보 처리방침 모달 -->
	  <PrivacyPolicyModal v-if="isPrivacyPolicyModalVisible" @close="closePrivacyPolicyModal" />

  </div>
</template>

<script setup>
import { ref, onMounted  } from 'vue';
import { useRouter } from 'vue-router';
import SignupStep1 from '@/components/user/signup/SignupStep1.vue';
import SignupStep2 from '@/components/user/signup/SignupStep2.vue';
import PrivacyPolicyModal from '@/components/user/signup/PrivacyPolicyModal.vue'; // PrivacyPolicyModal
 

// 라우터 선언
const router = useRouter();
// 상태 변수 선언
const isRegisterModalVisible = ref(false);
const currentSignupStep = ref(1); // 회원가입 단계를 1로 초기화
const isPrivacyPolicyModalVisible = ref(false); // 개인정보 처리방침 모달 상태
// Step1과 Step2에서 사용할 사용자 데이터
    const userData = ref({
	name: '', // 사용자 이름
	email: '', // 사용자 이메일
  });
  // Step1에서 전달받은 데이터 상태 업데이트
  const updateUserData = (newData) => {
	userData.value = { ...userData.value, ...newData };
  };
  
  // 모달 열고 닫기

  // 회원가입 모달 열기
    const openRegisterModal = () => {
	isRegisterModalVisible.value = true; // 회원가입 모달 열기
	currentSignupStep.value = 1; // 회원가입 1단계로 초기화
  };
  

  // 회원가입 1단계 모달에서 호출될 함수
  const goToStep2 = (payload) => {
	// SignupStep1에서 받은 이름과 이메일을 상태에 저장
	userData.value.name = payload.name;
	userData.value.email = payload.email;
	currentSignupStep.value = 2;
	console.log('goToStep2 received:', payload);
  };
  
  // 회원가입 모달 닫기
  const closeRegisterModal = () => {
	isRegisterModalVisible.value = false;
	currentSignupStep.value = 1; // 단계 초기화
  };
  
  // 개인정보 처리방침 모달 열기
  const openPrivacyPolicyModal = () => {
	isPrivacyPolicyModalVisible.value = true;
	isRegisterModalVisible.value = false;
  };
  
  // 개인정보 처리방침 모달 닫기
  const closePrivacyPolicyModal = () => {
	isPrivacyPolicyModalVisible.value = false;
	isRegisterModalVisible.value = true; // 다시 회원가입 모달로 돌아가기
  };
  
  // 회원가입 완료
  const completeSignup = () => {
	closeRegisterModal(); // 회원가입 모달 닫기
  };


// 페이지가 로드되면 자동으로 회원가입 모달 열기
onMounted(() => {
  openRegisterModal(); // 페이지가 로드되면 모달을 자동으로 열기
});
</script>

<style scoped>
/* 전체 페이지 스타일 */
.login-page {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100vh;
  background-color: #ffda14;
  position: relative;
}

/* 왼쪽 상단 로고 스타일 */
.logo-wrapper {
  display: flex;
  align-items: center;
  position: absolute;
  top: 2rem;
  left: 2rem;
  cursor: pointer;
}

.logo-wrapper img {
  width: 6rem;
  height: 6rem;
}

.logo-wrapper span {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
}

/* 요리사 이미지 및 텍스트 */
.chef-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: absolute;
  right: 8%;; /* 오른쪽에서 떨어지게 설정 */
  top: 50%; /* 화면의 세로 중앙에 배치 */
  transform: translateY(-50%); /* 세로 중앙 정렬을 위해 요소를 위로 50% 이동 */
}

.chef-wrapper img {
  width: 32rem;
  height: auto;
  position: relative;
}

/* 말풍선 스타일 */
.speech-bubble {
  position: absolute;
  bottom: 105%; /* 이미지 위에 나타나게 하기 위해 위치 조정 */
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  padding: 1rem 2rem;
  border-radius: 10px;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
  font-size: 1.6rem;
  font-weight: bold;
  color: #000;
  text-align: center;
  width: max-content;
  max-width: 60rem;
}

.speech-bubble::after {
  content: '';
  position: absolute;
  bottom: -50%; /* 이미지 위에 나타나게 하기 위해 위치 조정 */
  left: 50%;
  transform: translateX(-50%);
  border-width: 10px;
  border-style: solid;
  border-color: white transparent transparent transparent; /* 꼬리 부분을 삼각형으로 만듦 */
}
</style>
