<template>
  <router-view />
  <!-- 여기에 라우터가 활성화된 컴포넌트를 렌더링 -->
</template>

<script setup>
import { onMounted, watch, computed } from 'vue'
import { useTokenStore } from '@/stores/tokenStore' // Pinia 스토어 임포트
import { connectSSE } from '@/api/SSERequest' // SSE 연결 함수 임포트

// Pinia 스토어 사용
const tokenStore = useTokenStore()

// 토큰 상태를 computed로 설정하여 반응형 참조 보장
const accessToken = computed(() => tokenStore.token.accessToken)

// SSE 연결 시도 함수
const ConnectSSE = () => {
  if (accessToken.value) {
    connectSSE() // 토큰이 존재할 경우 SSE 연결 시도
  }
}

// 토큰 상태를 감시하여, 토큰이 설정되면 SSE 연결 시도
watch(
  accessToken, // 토큰 상태를 감시
  (newToken) => {
    if (newToken) {
      ConnectSSE() // 토큰이 존재할 경우 SSE 연결 시도
    }
  },
  { immediate: true } // 컴포넌트가 마운트될 때 즉시 실행
)

// 컴포넌트가 마운트될 때 로그인 상태 초기화
onMounted(() => {
  tokenStore.initializeState() // 로컬 스토리지에서 상태 복원 시도
  ConnectSSE() // 페이지가 로드될 때 SSE 연결 시도
})
</script>
