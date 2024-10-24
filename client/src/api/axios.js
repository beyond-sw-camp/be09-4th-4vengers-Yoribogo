import axios from 'axios';
import { useTokenStore } from '@/stores/tokenStore';  // Pinia 스토어 임포트

const apiClient = axios.create({
  // baseURL: '/api',  // API 기본 경로
  baseURL: '/boot/api',  // API 기본 경로
  timeout: 500000,  // 요청 타임아웃 설정 (50초)
});

// 요청 인터셉터: 모든 요청에 Authorization 헤더 추가
apiClient.interceptors.request.use(
  (config) => {
    const tokenStore = useTokenStore();  // Pinia 스토어에서 토큰 가져오기
    const accessToken = tokenStore.token.accessToken;
    
    // 토큰이 있는 경우 Authorization 헤더에 추가
    if (accessToken) {
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터: 401 오류 시 리프레시 토큰으로 재발급 처리
apiClient.interceptors.response.use(
  (response) => response,  // 응답이 성공적인 경우 그대로 반환
  async (error) => {
    const tokenStore = useTokenStore();
    const originalRequest = error.config;

    // 401 Unauthorized 에러 시 리프레시 토큰으로 새 액세스 토큰 요청
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;  // 무한 루프 방지

      // 리프레시 토큰으로 액세스 토큰 갱신 시도
      const newAccessToken = await tokenStore.refreshAccessToken();
      
      if (newAccessToken) {
        // 새로운 토큰을 헤더에 추가하고 다시 요청
        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
        return apiClient(originalRequest);  // 재요청
      }
    }

    return Promise.reject(error);  // 에러 반환
  }
);

export default apiClient;
