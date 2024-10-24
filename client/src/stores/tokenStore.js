import { defineStore } from 'pinia'
import { reactive } from 'vue'
import { fetchUserByAuthId } from '@/api/user' // user.js에 정의한 fetchUserByAuthId 함수 임포트
import axios from 'axios'

export const useTokenStore = defineStore('token', () => {
  const token = reactive({
    userId: null, // 사용자 ID
    userAuthId: null, // user_auth_id로 사용자 식별
    accessToken: null, // 액세스 토큰
    accessTokenExpiry: null, // 액세스 토큰 만료 시간
    refreshToken: null, // 리프레시 토큰
    refreshTokenExpiry: null // 리프레시 토큰 만료 시간
  })

  // 토큰 데이터 설정 메서드 (로그인 성공 시 호출)
  const setTokenData = async (tokenData) => {
    // API 응답에서 받은 tokenData를 상태에 저장
    token.userId = tokenData?.user_id || null // user_id 설정
    token.userAuthId = tokenData?.user_auth_id || null // user_identifier -> user_auth_id로 변경
    token.accessToken = tokenData?.access_token || null // 액세스 토큰 설정
    token.accessTokenExpiry = tokenData?.access_token_expiry || null // 액세스 토큰 만료 시간 설정
    token.refreshToken = tokenData?.refresh_token || null // 리프레시 토큰 설정
    token.refreshTokenExpiry = tokenData?.refresh_token_expiry || null // 리프레시 토큰 만료 시간 설정

    // 로컬 스토리지에 저장
    localStorage.setItem('token', JSON.stringify(token))
  }

  // 사용자 정보 조회 함수 (user_auth_id로 조회)
  const fetchUserId = async (userAuthId) => {
    try {
      const response = await fetchUserByAuthId(userAuthId, token.accessToken) // accessToken 추가
      if (response.success) {
        token.userId = response.data.user_id // user_id 저장
      } else {
        console.error('사용자 정보를 가져오지 못했습니다.')
      }
    } catch (error) {
      console.error('사용자 정보 조회 오류:', error)
    }
  }

  // 새 액세스 토큰 발급 함수 (리프레시 토큰 사용)
  const refreshAccessToken = async () => {
    try {
      const response = await axios.post('/boot/api/users/auth/refresh-token', {
        refresh_token: token.refreshToken // 리프레시 토큰 전송
      })

      if (response.data.success) {
        const newTokenData = response.data.data
        await setTokenData(newTokenData) // 새로운 토큰 저장
        return newTokenData.access_token // 새 액세스 토큰 반환
      } else {
        console.error('리프레시 토큰으로 토큰 갱신 실패:', response.data.error)
        return null
      }
    } catch (error) {
      console.error('리프레시 토큰 오류:', error)
      return null
    }
  }

  // 애플리케이션 초기화 시 로컬 스토리지에서 데이터 복원
  const initializeState = () => {
    const storedToken = localStorage.getItem('token')

    if (storedToken) {
      const parsedToken = JSON.parse(storedToken)

      // 상태 복원
      token.userId = parsedToken.userId || null
      token.userAuthId = parsedToken.userAuthId || null
      token.accessToken = parsedToken.accessToken || null
      token.accessTokenExpiry = parsedToken.accessTokenExpiry || null
      token.refreshToken = parsedToken.refreshToken || null
      token.refreshTokenExpiry = parsedToken.refreshTokenExpiry || null

      // user_auth_id가 String 타입인지 확인
      if (token.userAuthId && typeof token.userAuthId === 'string') {
        try {
          fetchUserId(token.userAuthId) // 유효한 user_auth_id로 사용자 정보 복원
        } catch (error) {
          console.error('fetchUserId 호출 중 에러 발생:', error)
        }
      } else {
        console.warn('유효하지 않은 user_auth_id:', token.userAuthId)
      }
    }
  }

  // 로그아웃 처리
  const logout = () => {
    // 모든 토큰 및 사용자 데이터 초기화
    token.userId = null
    token.userAuthId = null
    token.accessToken = null
    token.accessTokenExpiry = null
    token.refreshToken = null
    token.refreshTokenExpiry = null

    // 로컬 스토리지에서도 제거
    localStorage.removeItem('token')
    // 로컬 스토리지 전체 제거
    localStorage.clear()
  }

  return {
    token,
    setTokenData,
    refreshAccessToken,
    fetchUserId,
    initializeState,
    logout
  }
})
