<template>
  <div>
    <p v-if="loading">카카오 로그인 중입니다...</p>
    <p v-if="error">{{ error }}</p>

    <!-- 닉네임이 없을 경우 프로필 설정 모달 표시 -->
    <ProfileOAuthModal v-if="showProfileModal" @close="closeProfileModal" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { handleKakaoLogin, fetchUserByAuthId } from '@/api/user' // user.js에서 함수 임포트
import { useTokenStore } from '@/stores/tokenStore' // Pinia 스토어에서 토큰 관리
import ProfileOAuthModal from '@/components/user/sns/ProfileOAuthModal.vue'

const router = useRouter()
const tokenStore = useTokenStore() // Pinia 스토어 사용

const loading = ref(true)
const error = ref('')
const showProfileModal = ref(false) // 프로필 모달 표시 여부

// Kakao에서 전달받은 `code`를 서버로 전달하여 토큰 및 사용자 정보 받기
const handleKakaoCallback = async () => {
  const code = router.currentRoute.value.query.code

  if (code) {
    try {
      // 서버로 로그인 요청을 보내서 토큰을 받아옴
      const response = await handleKakaoLogin(code) // user.js에서 handleKakaoLogin 함수 사용

      if (response.success) {
        const tokenData = response.data

        // user_auth_id로 사용자 정보 조회, 여기에 액세스 토큰을 전달
        const userProfileResponse = await fetchUserByAuthId(
          tokenData.user_auth_id,
          tokenData.access_token
        )
        const userProfile = userProfileResponse?.data || {}

        // user_id 포함한 토큰과 사용자 정보를 Pinia 스토어에 저장
        await tokenStore.setTokenData({
          ...tokenData, // 기존의 토큰 데이터들 (access_token, refresh_token 등)
          user_id: userProfile.user_id // 조회한 user_id 추가
        })

        if (!userProfile.nickname || userProfile.nickname === '') {
          showProfileModal.value = true // 닉네임이 없는 경우 프로필 설정 모달 표시
        } else {
          router.push('/') // 닉네임이 있으면 홈으로 리다이렉트
        }
      } else {
        throw new Error(response.error || '카카오 로그인 실패')
      }
    } catch (err) {
      error.value = '로그인 처리 중 오류가 발생했습니다.'
      console.error('카카오 로그인 처리 중 오류:', err)
    } finally {
      loading.value = false
    }
  } else {
    error.value = '카카오 인증 코드가 없습니다.'
    loading.value = false
  }
}

// 모달 닫기 함수
const closeProfileModal = () => {
  showProfileModal.value = false
}

onMounted(() => {
  handleKakaoCallback()
})
</script>
