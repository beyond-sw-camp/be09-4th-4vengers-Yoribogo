<template>
  <div class="profile-container" v-if="userProfile">
    <!-- 프로필 이미지 클릭 시 닉네임 및 사진 수정 모달 열기 -->
    <div class="avatar" @click="$emit('openEditProfileModal')">
      <img :src="userProfile.profileImage" alt="프로필 사진" />
    </div>

    <!-- 사용자 정보 -->
    <div class="user-info">
      <div class="name-tier-container">
        <h2>{{ userProfile.nickname }} 님</h2>
        <img :src="userProfile.tierImage" alt="티어 이미지" class="tier-image" />
      </div>

      <!-- 회원정보 수정 버튼 클릭 시 비밀번호 변경/회원탈퇴 모달 열기 -->
      <button @click="$emit('openAccountOptionsModal')">계정정보 수정</button>
    </div>
  </div>

  <div v-else>
    <p>프로필 정보를 불러오는 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useTokenStore } from '@/stores/tokenStore' // Pinia store 사용
import { fetchUserDetailProfile } from '@/api/user' // API 호출 함수

const userProfile = ref(null) // 사용자 프로필 데이터

// Pinia store에서 토큰 정보 가져오기
const tokenStore = useTokenStore()

// API를 호출해 사용자 프로필 정보 가져오기
const loadUserProfile = async () => {
  try {
    const userId = tokenStore.token.userId
    const accessToken = tokenStore.token.accessToken

    if (userId && accessToken) {
      const profileData = await fetchUserDetailProfile(userId, accessToken)
      if (profileData.success) {
        userProfile.value = {
          profileImage: profileData.data.profileImage,
          nickname: profileData.data.nickname || '닉네임 없음',
          tierImage: profileData.data.tierImage
        }
      } else {
        console.error('프로필 정보를 불러오는 데 실패했습니다.')
      }
    } else {
      console.error('userId 또는 accessToken이 없습니다.')
    }
  } catch (error) {
    console.error('프로필 정보를 가져오는 중 오류 발생:', error)
  }
}

// 컴포넌트가 마운트될 때 프로필 정보 불러오기
onMounted(() => {
  loadUserProfile()
})
</script>

<style scoped>
.profile-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
  background-color: transparent;
  padding: 2rem;
  border-radius: 8rem 80rem 1rem 1rem;
  max-width: 30rem;
  margin: auto;
}

.avatar {
  position: relative; /* 부모 요소를 기준으로 자식 요소 위치 지정 */
  cursor: pointer;
}

.avatar img {
  width: 15rem;
  height: 15rem;
  border-radius: 50%;
  object-fit: cover;
  border: 0.5rem solid white;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease; /* 부드러운 전환 효과 */
}

.avatar:hover img {
  transform: scale(1.05); /* 이미지 확대 */
  box-shadow: 0rem 0.6rem 1.2rem rgba(0, 0, 0, 0.2); /* 그림자 효과 추가 */
}

.avatar::after {
  content: '프로필 수정하기'; /* hover 상태에서 표시할 텍스트 */
  position: absolute;
  bottom: -1rem; /* 텍스트 위치를 이미지 아래로 */
  left: 50%;
  transform: translateX(-50%);
  color: white;
  background-color: #2c3e50;
  padding: 0.4rem 0.8rem;
  border-radius: 0.5rem;
  font-size: 1.2rem;
  width: max-content;
  opacity: 0; /* 기본 상태에서 숨김 */
  transition: opacity 0.3s ease; /* 부드러운 전환 */
  pointer-events: none;
}

.avatar:hover::after {
  opacity: 1; /* hover 시 텍스트가 보이도록 */
}
.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.name-tier-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.3rem;
  margin-bottom: 1rem;
}

.name-tier-container h2 {
  margin: 0;
  font-size: 2rem;
  font-weight: bold;
  color: #333;
  line-height: 1;
}

.tier-image {
  width: 2.4rem;
  height: 2.4rem;
  object-fit: contain;
  margin-top: 0.5rem;
}

button {
  background-color: #ececec;
  color: black;
  border: none;
  padding: 0.7rem 1.5rem;
  border-radius: 0.5rem;
  cursor: pointer;
  margin-top: 1rem;
  font-family: 'Noto Sans KR', 'Noto Sans', sans-serif;
  font-size: 1.4rem;
  font-weight: 500;
  box-shadow: 0rem 0.3rem 0.6rem rgba(0, 0, 0, 0.15);
  transition: box-shadow 0.2s ease;
}

button:hover {
  box-shadow: 0rem 0.6rem 0.8rem rgba(0, 0, 0, 0.2);
}

@media screen and (max-width: 600px) {
  .profile-container {
    gap: 1rem;
  }

  .avatar img {
    width: 12rem;
    height: 12rem;
  }

  .name-tier-container h2 {
    font-size: 1.6rem;
  }

  .tier-image {
    width: 2rem;
    height: 2rem;
  }

  button {
    padding: 0.4rem 1rem;
    font-size: 1.2rem;
  }
}
</style>
