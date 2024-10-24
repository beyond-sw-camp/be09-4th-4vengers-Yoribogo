import apiClient from '@/api/axios';  // Axios 설정이 적용된 apiClient 사용

// 설명. 1. 이메일 전송 API (회원가입 시 실행)
export const sendSignupVerificationEmail = async (email) => {
  try {
    const response = await apiClient.post('/users/verification-email/signup', { email });
    return response.data;
  } catch (error) {
    console.error('sendSignupVerificationEmail 에러:', error);
    throw error;
  }
};

// 설명. 1.2 이메일 전송 API (아이디 찾기 시 실행)
export const sendAuthIdVerificationEmail = async (nickname, email) => {
  try {
    const response = await apiClient.post('/users/verification-email/auth-id', { nickname, email });
    return response.data;
  } catch (error) {
    if (error.code === 'ECONNABORTED') {
      console.error('요청 타임아웃:', error);
    } else {
      console.error('sendAuthIdVerificationEmail 에러:', error);
    }
    throw error;
  }
};

// 설명. 1.3 이메일 전송 API (비밀번호 찾기 시 실행)
export const sendPasswordResetVerificationEmail = async (userAuthId, email) => {
  try {
    const response = await apiClient.post('/users/verification-email/user-password', { user_auth_id: userAuthId, email });
    return response.data;
  } catch (error) {
    console.error('sendPasswordResetVerificationEmail 에러:', error);
    throw error;
  }
};

// 설명. 2.1 이메일 인증번호 검증 API (회원가입, 아이디, 비밀번호 찾기 실행)
export const confirmVerificationCodeAPI = async (email, code) => {
  try {
    const response = await apiClient.post('/users/verification-email/confirmation', { email, code });
    return response.data;
  } catch (error) {
    console.error('confirmVerificationCode 에러:', error);
    throw error;
  }
};

// 설명. 2.2 닉네임과 이메일로 사용자 정보 조회 및 인증 API (아이디 찾기 실행)
export const verifyUserNicknameCode = async (nickname, email, code) => {
  try {
    const response = await apiClient.post('/users/nickname/verification-email', {
      nickname,
      email,
      code,
    });
    return response.data; // 성공 시 사용자 데이터 반환
  } catch (error) {
    console.error('verifyUserNicknameCode 에러:', error);
    throw error; // 오류 발생 시 예외 던지기
  }
};

// 설명. 3.1 카카오 로그인 처리 API
export const handleKakaoLogin = async (code) => {
  try {
    const response = await apiClient.post('/users/oauth2/kakao', { code });
    return response.data;
  } catch (error) {
    console.error('카카오 로그인 처리 중 오류:', error);
    throw error;
  }
};

// 설명. 3.2 네이버 로그인 처리 API
export const handleNaverLogin = async (code, state) => {
  try {
    const response = await apiClient.post('/users/oauth2/naver', { code, state });
    return response.data;
  } catch (error) {
    console.error('네이버 로그인 처리 중 오류:', error);
    throw error;
  }
};

// 설명. 4.1 사용자 정보 조회 (user_identifier로 사용자 조회) - GET 요청 (액세스 토큰 필요)
export const fetchUserProfile = async (userIdentifier, accessToken) => {
  try {
    const response = await apiClient.get(`/users/identifier`, {
      params: { user_identifier: userIdentifier },
      headers: { Authorization: `Bearer ${accessToken}` }, // 토큰 추가
    });
    return response.data;
  } catch (error) {
    console.error('사용자 프로필 조회 중 오류:', error);
    throw error;
  }
};

// 설명. 4.2 user_auth_id로 사용자 정보 조회 - GET 요청 (액세스 토큰 필요)
export const fetchUserByAuthId = async (userAuthId, accessToken) => {
  try {
    const response = await apiClient.get(`/users/userAuthId`, {
      params: { user_auth_id: userAuthId },
      headers: { Authorization: `Bearer ${accessToken}` }, // 토큰 추가
    });
    return response.data;
  } catch (error) {
    console.error('user_auth_id로 사용자 정보 조회 중 오류:', error);
    throw error;
  }
};

// 설명. 4.3 user_id로 사용자 정보 조회 - GET 요청 (액세스 토큰 필요)
export const fetchUserById = async (userId, accessToken) => {
  try {
    const response = await apiClient.get(`/users/${userId}`, {
      headers: { Authorization: `Bearer ${accessToken}` }, // 토큰 추가
    });
    return response.data;  // 반환된 사용자 정보
  } catch (error) {
    console.error('user_id로 사용자 정보 조회 중 오류:', error);
    throw error;
  }
};

// 설명. userId로 프로필 정보 조회 - GET 요청 (액세스 토큰 필요)
export const fetchUserDetailProfile = async (userId, accessToken) => {
  try {
    const response = await apiClient.get(`/users/${userId}/profile`, {
      headers: { Authorization: `Bearer ${accessToken}` }, // 토큰 추가
    });
    return response.data;  // 반환된 사용자 프로필 정보
  } catch (error) {
    console.error('프로필 정보 조회 중 오류:', error);
    throw error;
  }
};

// 설명. 4.4 사용자 프로필 변경 (닉네임, 사진) - PATCH 요청 (액세스 토큰 필요)
export const updateUserProfile = async (userId, nickname, profileImage, accessToken) => {
  try {
    const formData = new FormData();
    formData.append('nickname', nickname);
    if (profileImage) formData.append('profile_image', profileImage);

    const response = await apiClient.patch(`/users/${userId}/profile`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${accessToken}`, // 토큰 추가
      },
    });
    return response.data;
  } catch (error) {
    console.error('updateUserProfile 에러:', error);
    throw error;
  }
};

// 설명. 5. 리프레시 토큰으로 액세스 토큰 재발급 - POST 요청 (토큰 필요 없음)
export const refreshAccessToken = async (refreshToken) => {
  try {
    const response = await apiClient.post('/users/auth/refresh-token', { refresh_token: refreshToken });
    return response.data;
  } catch (error) {
    console.error('refreshAccessToken 에러:', error);
    throw error;
  }
};

// 설명. 6. 일반 회원가입 API - POST 요청 (토큰 필요 없음)
export const signupUser = async (userData) => {
  try {
    const response = await apiClient.post('/users/signup/normal', userData);
    return response.data;
  } catch (error) {
    console.error('signupUser 에러:', error);
    throw error;
  }
};

// 설명. 7. 관리자 회원가입 API - POST 요청 (토큰 필요 없음)
export const signupAdminUser = async (adminData) => {
  try {
    const response = await apiClient.post('/users/signup/admin', adminData);
    return response.data;
  } catch (error) {
    console.error('signupAdminUser 에러:', error);
    throw error;
  }
};

// 설명. 8. 닉네임 중복 검증 API - POST 요청 (토큰 필요 없음)
export const validateNickname = async (nickname) => {
  try {
    const response = await apiClient.post('/users/nickname/validate', { nickname });
    return response.data;
  } catch (error) {
    console.error('validateNickname 에러:', error);
    throw error;
  }
};

// 설명. 9. 아이디 중복 검증 API - POST 요청 (토큰 필요 없음)
export const validateUserAuthId = async (userAuthId) => {
  try {
    const response = await apiClient.post('/users/user-id/validate', { user_auth_id: userAuthId });
    return response.data;
  } catch (error) {
    console.error('validateUserAuthId 에러:', error);
    throw error;
  }
};

// 설명. 10. 회원 탈퇴 API - PATCH 요청 (액세스 토큰 필요)
export const deactivateUser = async (userId, accessToken) => {
  try {
    const response = await apiClient.patch(`/users/${userId}/deactivate`, null, {
      headers: { Authorization: `Bearer ${accessToken}` }, // 토큰 추가
    });
    return response.data;
  } catch (error) {
    console.error('deactivateUser 에러:', error);
    throw error;
  }
};

// 설명. 11. 사용자 재활성화 API - POST 요청 (토큰 필요 없음)
export const reactivateUserByAuthId = async (userAuthId) => {
  try {
    const response = await apiClient.post('/users/activate', null, { // POST 요청의 body는 null로 설정
      params: { userAuthId: userAuthId } // RequestParam으로 userAuthId 전달
    });
    return response.data;
  } catch (error) {
    console.error('reactivateUserByAuthId 에러:', error);
    throw error;
  }
};

// 설명. 12. 로그인 전 비밀번호 재설정 API - POST 요청 (토큰 필요 없음)
export const resetPasswordAPI = async (userAuthId, password) => {
  try {
    const response = await apiClient.post('/users/re-password', {
      user_auth_id: userAuthId, // 스네이크 케이스로 수정
      password: password
    });
    return response.data;
  } catch (error) {
    console.error('resetPassword 에러:', error);
    throw error;
  }
};

// 설명. 13. 로그인 후 비밀번호 재설정 API - PATCH 요청 (액세스 토큰 필요)
export const updateLoginedPassword = async (userId, password, accessToken) => {
  try {
    const response = await apiClient.patch(`/users/${userId}/password`, { password }, {
      headers: { Authorization: `Bearer ${accessToken}` }, // 토큰 추가
    });
    return response.data;
  } catch (error) {
    console.error('updateLoginedPassword 에러:', error);
    throw error;
  }
};
