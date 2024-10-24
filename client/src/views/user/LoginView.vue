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

    <!-- Login Modal -->
    <LoginModal v-if="isLoginModalVisible" 
        @close="closeLoginModal" 
        @openPasswordReset="openPasswordResetModal"
		    @openFindId="openFindIdModal"     
    />

    
	  <!-- 회원 재활성화 모달 -->
	  <AccountReactivationModal
      v-if="isAccountReactivationModalVisible"
      :userAuthId="userAuthId"
      @close="closeAccountReactivationModal"
	  />

     <!-- 비밀번호 찾기 Step1 모달 -->
      <PasswordResetStep1
      v-if="isPasswordResetModalVisible && currentPasswordResetStep === 1"
      @close="closePasswordResetModal"
      @goToPasswordResetStep2="goToPasswordResetStep2"
      @update="updatePasswordResetData" 
      @openLogin="openLoginModal"
      :passwordResetData="passwordResetData" />
      <!-- 비밀번호 찾기 Step2 모달 -->
      <PasswordResetStep2
      v-if="isPasswordResetModalVisible && currentPasswordResetStep === 2"
      @close="closePasswordResetModal"
      @update="updatePasswordResetData" 
      @openPasswordReset="openPasswordResetModal"
      @openLogin="openLoginModal"
      :passwordResetData="passwordResetData"/>

       <!-- 아이디 찾기 Step1 모달 -->
      <FindIdStep1
        v-if="isFindIdModalVisible && currentFindIdStep === 1"
        @close="closeFindIdModal"
        @goToFindIdStep2="goToFindIdStep2"
      />
      <!-- 아이디 찾기 Step2 모달 -->
      <FindIdStep2
        v-if="isFindIdModalVisible && currentFindIdStep === 2" 
        @close="closeFindIdModal"
        :nickname="foundNickname"
        :userAuthId="foundUserAuthId"/>
  </div>
</template>

<script setup>
import { ref, onMounted  } from 'vue';
import { useRouter } from 'vue-router';
import LoginModal from '@/components/user/login/LoginModal.vue'; // 모달 컴포넌트 임포트

import AccountReactivationModal from '@/components/user/login/AccountReactivationModal.vue'; // 계정활 성화 모달창

import PasswordResetStep1 from '@/components/user/login/PasswordResetStep1.vue'; // 비밀번호 찾기 Step1 모달창
import PasswordResetStep2 from '@/components/user/login/PasswordResetStep2.vue'; // 비밀번호 찾기 Step2 모달창

import FindIdStep1 from '@/components/user/login/FindIdStep1.vue'; // 아이디 찾기 Step1 모달창
import FindIdStep2 from '@/components/user/login/FindIdStep2.vue'; // 아이디 찾기 Step2 모달창

// 라우터 선언
const router = useRouter();
// 상태 변수 선언

const isLoginModalVisible = ref(false);// 로그인 모달창
const isAccountReactivationModalVisible = ref(false);// 계정활성화 모달 상태
const isPasswordResetModalVisible = ref(false);// 비밀번호 찾기 모달 상태
const currentPasswordResetStep = ref(1); // 비밀번호 찾기 단계 초기화

const isFindIdModalVisible = ref(false); // 아이디 찾기 모달 상태
const currentFindIdStep = ref(1); // 현재 아이디 찾기 단계초기화

const userAuthId = ref(''); // 사용자ID 초기화
const foundNickname = ref(''); // 찾은 유저 닉네임
const foundUserAuthId = ref('');   // 찾은 유저 아이디


 // 비밀번호 찾기 상태 데이터 관리
 const passwordResetData = ref({
  user_auth_id: '', // 아이디
  email: '', // 이메일
});

// 로그인 모달 열기
const openLoginModal = () => {
  isLoginModalVisible.value = true;
};

// 로그인 모달 닫기
const closeLoginModal = () => {
  isLoginModalVisible.value = false;
};


// 계정 재활성화 모달 열기
const openAccountReactivationModal = (authId) => {
  userAuthId.value = authId;
  isAccountReactivationModalVisible.value = true;
};

// 계정 재활성화 모달 닫기
const closeAccountReactivationModal = () => {
  isAccountReactivationModalVisible.value = false;
};


// 비밀번호 찾기 데이터 업데이트
const updatePasswordResetData = (newData) => {
  passwordResetData.value = { ...passwordResetData.value, ...newData };
};

// 비밀번호 찾기 모달 열기
const openPasswordResetModal = () => {
 isLoginModalVisible.value = false; // 로그인 모달 닫기
  isPasswordResetModalVisible.value = true;
  currentPasswordResetStep.value = 1;
};

// 비밀번호 찾기 모달 닫기
const closePasswordResetModal = () => {
  isPasswordResetModalVisible.value = false;
  currentPasswordResetStep.value = 1;  // 비밀번호 찾기 단계를 1로 초기화
};

// Step2로 이동하는 함수
const goToPasswordResetStep2 = (payload) => {
  if (payload) {
    // payload가 있을 경우 해당 데이터를 저장
    passwordResetData.value = {
      user_auth_id: payload.user_auth_id,
      email: payload.email,
    };
  }
  // Step2로 이동
  currentPasswordResetStep.value = 2;
};


// 아이디 찾기 모달 열기
const openFindIdModal = () => {
console.log('아이디 찾기 모달 열기 함수 호출됨(내비게이션에서)')
  isFindIdModalVisible.value = true;
  currentFindIdStep.value = 1; // Step1로 초기화
};

// 아이디 찾기 모달 닫기
const closeFindIdModal = () => {
  isFindIdModalVisible.value = false;
  currentFindIdStep.value = 1; // 단계 초기화
};  

// Step2로 이동
const goToFindIdStep2 = (nickname, userAuthId) => {
  foundNickname.value = nickname; // 전달받은 아이디 설정
  foundUserAuthId.value = userAuthId;     // 유저 아이디도 함께 전달
  currentFindIdStep.value = 2; // Step2로 이동
};

// 홈으로 돌아가기
const goHome = () => {
  router.push('/');
};

// 페이지가 로드되면 자동으로 모달 열기
onMounted(() => {
  openLoginModal(); // 페이지가 로드되면 모달을 자동으로 열기
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
