<template>
  <div class="mypage-view">
    <header>
      <ProfileButton class="profile-btn"></ProfileButton>
      <HomeButton class="home-btn"></HomeButton>
    </header>

    <!-- 사용자 프로필 -->
    <MainBoard :cur="'mypage'">
      <section class="user-profile-section">
        <!-- 다른 컴포넌트들 -->
        <UserProfile
          @openEditProfileModal="openEditProfileModal"
          @openAccountOptionsModal="openAccountOptionsModal"
        />
      </section>

      <!-- 탭 이름 및 탭 컴포넌트 추가 -->
      <div class="tabs-container">
        <!-- 탭 이름을 먼저 표시 -->
        <h2 class="tab-title">{{ currentTab }}</h2>

        <!-- 탭 버튼 섹션 -->
        <section class="tab-section">
          <router-link to="/mypage/satisfied" class="tab-button" exact-active-class="active">
            만족했던 레시피
          </router-link>
          <router-link to="/mypage/bookmarked" class="tab-button" exact-active-class="active">
            북마크한 레시피
          </router-link>
          <router-link to="/mypage/my-recipes" class="tab-button" exact-active-class="active">
            내가 작성한 레시피
          </router-link>
        </section>

        <!-- 선택한 탭에 따른 컴포넌트 표시 -->
        <div class="tab-content">
          <router-view :menuList="menuList" :isEmpty="isEmpty"></router-view>
        </div>
      </div>
    </MainBoard>
    <PaginationComponent :data="pageInfo" @changePage="handlePageChange"></PaginationComponent>

    <!-- 회원 관련 모달들 -->
    <ProfileEditModal v-if="isEditProfileModalOpen" @close="closeEditProfileModal" />
    <AccountOptionsModal
      v-if="isAccountOptionsModalOpen"
      @close="closeAccountOptionsModal"
      @openChangePasswordModal="openChangePasswordModal"
      @openAccountDeactivationModal="openAccountDeactivationModal"
    />
    <ChangePasswordModal v-if="isChangePasswordModalOpen" @close="closeChangePasswordModal" />
    <AccountDeactivationModal
      v-if="isAccountDeactivationModalOpen"
      @close="closeAccountDeactivationModal"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router' // vue-router 임포트
import { useTokenStore } from '@/stores/tokenStore' // Pinia store 사용
import axios from 'axios'
//component
import HomeButton from '@/components/common/HomeButton.vue'
import MainBoard from '@/components/common/MainBoard.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'

// 회원 프로필 관련 모달창
import UserProfile from '@/components/user/profile/UserProfile.vue'
import ProfileEditModal from '@/components/user/profile/ProfileEditModal.vue'
import ChangePasswordModal from '@/components/user/profile/ChangePasswordModal.vue'
import AccountDeactivationModal from '@/components/user/profile/AccountDeactivationModal.vue'
import AccountOptionsModal from '@/components/user/profile/AccountOptionsModal.vue'

// 탭에 보여줄 컴포넌트들 import
import PaginationComponent from '@/components/common/PaginationComponent.vue'

const currentTab = ref('북마크한 레시피')

/* 모달 상태관리 */
const isEditProfileModalOpen = ref(false)
const isAccountOptionsModalOpen = ref(false)
const isChangePasswordModalOpen = ref(false)
const isAccountDeactivationModalOpen = ref(false)

const openEditProfileModal = () => {
  isEditProfileModalOpen.value = true
}

const closeEditProfileModal = () => {
  isEditProfileModalOpen.value = false
}

const openAccountOptionsModal = () => {
  isAccountOptionsModalOpen.value = true
}

const closeAccountOptionsModal = () => {
  isAccountOptionsModalOpen.value = false
}

const openChangePasswordModal = () => {
  isChangePasswordModalOpen.value = true
  closeAccountOptionsModal()
}

const closeChangePasswordModal = () => {
  isChangePasswordModalOpen.value = false
}

const openAccountDeactivationModal = () => {
  isAccountDeactivationModalOpen.value = true
  closeAccountOptionsModal()
}

const closeAccountDeactivationModal = () => {
  isAccountDeactivationModalOpen.value = false
}

// 라우터 사용
const route = useRoute()
const router = useRouter()

const userId = ref(null)
const menuList = ref([])
const pageInfo = ref({})
const isEmpty = ref(true)
const isLogin = ref(false)

const fetchRecipes = async (userId, page) => {
  try {
    let response
    if (currentTab.value === '만족했던 레시피') {
      response = (await axios.get(`/boot/api/recommended-menus?user=${userId}&page=${page}`)).data
    } else if (currentTab.value === '북마크한 레시피') {
      response = (await axios.get(`/boot/api/recipe-board/favorites/users/${userId}?pageNo=${page}`))
        .data
    } else if (currentTab.value === '내가 작성한 레시피') {
      response = (await axios.get(`/boot/api/recipe-board/users/${userId}/boards?pageNo=${page}`)).data
    }

    if (response.success) {
      menuList.value = response.data.content
      pageInfo.value = response.data.page
      isEmpty.value = menuList.value.length === 0
    } else {
      menuList.value = []
      isEmpty.value = true
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

// 페이지 변경 핸들러
const handlePageChange = (newPage) => {
  router.push({ query: { ...route.query, page: newPage } })
}

// 페이지 변경을 감지하고 fetch 요청
watch(
  () => [route.query.page, route.path],
  async ([newPage, newPath]) => {
    const page = parseInt(newPage || '1', 10)

    if (newPath.includes('satisfied')) {
      currentTab.value = '만족했던 레시피'
    } else if (newPath.includes('bookmarked')) {
      currentTab.value = '북마크한 레시피'
    } else if (newPath.includes('my-recipes')) {
      currentTab.value = '내가 작성한 레시피'
    }

    await fetchRecipes(userId.value, page)
  }
)

onMounted(() => {
  const tokenStore = useTokenStore()
  if (!tokenStore.token.accessToken) {
    alert('마이페이지를 보시려면 로그인이 필요합니다!');
    router.push('/login-page');
    return // 종료
  } else {
    userId.value = JSON.parse(localStorage.getItem('token')).userId
    isLogin.value = true

    if (route.path.includes('satisfied')) {
      currentTab.value = '만족했던 레시피'
    } else if (route.path.includes('bookmarked')) {
      currentTab.value = '북마크한 레시피'
    } else if (route.path.includes('my-recipes')) {
      currentTab.value = '내가 작성한 레시피'
    }

    fetchRecipes(userId.value, 1)
  }
})
</script>

<style scoped>
.mypage-view {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

/* 프로필 사진 고정된 위치 (탭과 독립) */
.user-profile-section {
  position: absolute;
  top: -10rem;
  left: 12rem; /* 고정된 위치 */
  transform: translateX(-50%);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10; /* 다른 요소 위로 위치 */
}

/* 탭 컨테이너 */
.tabs-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  margin-top: 8rem; /* 상단으로부터 여유 공간 추가 */
}

/* 탭 이름 스타일 */
.tab-title {
  font-size: 2.8rem;
  font-weight: bold;
  margin-top: 0; /* 여백이 위쪽으로 밀리지 않도록 */
  text-align: center;
}

/* 탭 버튼 섹션 */
.tab-section {
  display: flex;
  justify-content: center;
  position: relative;
  margin-top: 2rem; /* 탭 제목과의 간격 */
}

/* 탭 버튼 스타일 */
.tab-button {
  background-color: #dadada;
  border: none;
  padding: 1rem 4.4rem;
  margin: 0 -1.75rem; /* 버튼들이 겹치게 하는 마진 */
  border-radius: 3rem;
  cursor: pointer;
  font-size: 1.6rem;
  color: white;
  z-index: 1; /* 기본 z-index 값 */
  position: relative;
}

.tab-button.active {
  background-color: #2c3e50;
  color: white;
  z-index: 2; /* 활성화된 버튼을 다른 버튼 위로 */
}

/* 탭 콘텐츠 영역 */
.tab-content {
  display: flex;
  justify-content: center;
  width: 100%;
}

/* 상단 버튼들 위치 */
.notification-btn {
  position: absolute;
  top: 7rem;
  left: 12rem;
}

.profile-btn {
  position: absolute;
  top: 7rem;
  right: 12rem;
}

.home-btn {
  position: absolute;
  top: 7rem;
  right: 20rem;
}

@media screen and (max-width: 1024px) {
  .tab-button {
    padding: 1.1rem 4rem;
    margin: 0 -1.6rem;
    font-size: 1.4rem;
  }
}

@media screen and (max-width: 960px) {
  .tab-button {
    padding: 1.1rem 4rem;
    margin: 0 -1.4rem;
    font-size: 1.35rem;
  }
}

@media screen and (max-width: 900px) {
  .tab-title {
    margin-top: 10rem;
  }

  .tab-section {
    flex-direction: column;
    position: absolute;
    right: -8rem;
    gap: 1rem;
  }

  .tab-button {
    padding: 1rem 3rem;
    font-size: 1.4rem;
  }
}

@media screen and (max-width: 600px) {
  .tab-section {
    right: -6.5rem;
    gap: 1rem;
  }

  .tab-button {
    padding: 1rem 2.5rem;
    font-size: 1.3rem;
  }

  .user-profile-section {
    top: -9rem;
    left: 9rem;
  }
}

@media screen and (max-width: 425px) {
  .user-profile-section {
    top: -9rem;
    left: 8.5rem;
  }
}

@media screen and (max-width: 370px) {
  .user-profile-section {
    top: -9rem;
    left: 8rem;
  }
}
</style>
