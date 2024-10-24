package com.avengers.yoribogo.user.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.config.OAuthProperties;
import com.avengers.yoribogo.security.JwtUtil;
import com.avengers.yoribogo.user.domain.Tier;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.AcceptStatus;
import com.avengers.yoribogo.user.domain.enums.ActiveStatus;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.avengers.yoribogo.user.domain.vo.kakao.KakaoUser;
import com.avengers.yoribogo.user.domain.vo.login.AuthTokens;
import com.avengers.yoribogo.user.domain.vo.naver.NaverAuthorizationCode;
import com.avengers.yoribogo.user.domain.vo.naver.NaverUser;
import com.avengers.yoribogo.user.repository.TierRepository;
import com.avengers.yoribogo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OAuth2LoginService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TierRepository tierRepository;
    private final RestTemplate restTemplate;
    private final OAuthProperties oAuthProperties;
    private final long accessExpirationTime;
    private final long refreshExpirationTime;

    @Autowired
    public OAuth2LoginService(JwtUtil jwtUtil,
                              UserRepository userRepository,
                              TierRepository tierRepository,
                              @Qualifier("securityRestTemplate") RestTemplate restTemplate,
                              OAuthProperties oAuthProperties,
                              @Value("${token.access-expiration-time}") long accessExpirationTime,
                              @Value("${token.refresh-expiration-time}") long refreshExpirationTime) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.tierRepository=tierRepository;
        this.restTemplate = restTemplate;
        this.oAuthProperties = oAuthProperties;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public AuthTokens loginWithKakao(String authorizationCode) {
        OAuthProperties.OAuthClient kakaoClient = oAuthProperties.getRegistration().get("kakao");

        // 카카오 액세스 토큰 요청
        String accessToken = getKakaoAccessToken(authorizationCode, kakaoClient.getRedirectUri());

        // 카카오 사용자 정보 요청
        KakaoUser kakaoUser = getKakaoUserInfo(accessToken);

        // 사용자 식별자 생성
        String userIdentifier = createUserIdentifier(SignupPath.KAKAO, kakaoUser.getId());

        // 사용자 정보 확인 또는 신규 회원 가입
        UserEntity user = getOrCreateMember(userIdentifier, kakaoUser.getEmail(), kakaoUser.getRealName(), SignupPath.KAKAO);

        // JWT 토큰 생성
        return createAuthTokens(user);
    }

    public AuthTokens loginWithNaver(NaverAuthorizationCode code) {
        OAuthProperties.OAuthClient naverClient = oAuthProperties.getRegistration().get("naver");

        // 네이버 액세스 토큰 요청
        String accessToken = getNaverAccessToken(code.getCode(), code.getState(), naverClient.getRedirectUri());

        // 네이버 사용자 정보 요청
        NaverUser naverUser = getNaverUserInfo(accessToken);

        // 사용자 식별자 생성
        String userIdentifier = createUserIdentifier(SignupPath.NAVER, naverUser.getId());

        // 사용자 정보 확인 또는 신규 회원 가입
        UserEntity user = getOrCreateMember(userIdentifier, naverUser.getEmail(), naverUser.getRealName(), SignupPath.NAVER);

        // JWT 토큰 생성
        return createAuthTokens(user);
    }

    private AuthTokens createAuthTokens(UserEntity user) {
        // 액세스 토큰 생성
        String accessToken = jwtUtil.generateToken(user, List.of("ROLE_ENTERPRISE"));
        // 리프레시 토큰 생성
        String refreshToken = jwtUtil.generateRefreshToken(user, List.of("ROLE_ENTERPRISE"));

        // 만료 시간 설정
        Date accessTokenExpiry = new Date(System.currentTimeMillis() + accessExpirationTime);
        Date refreshTokenExpiry = new Date(System.currentTimeMillis() + refreshExpirationTime);


        // AuthTokens에 사용자 식별자 추가
        return new AuthTokens(
                accessToken,
                refreshToken,
                "Bearer",
                accessTokenExpiry.getTime(),
                refreshTokenExpiry.getTime(),
                user.getUserAuthId()
        );
    }

    private UserEntity getOrCreateMember(String userIdentifier, String email, String realName, SignupPath provider) {
        // user_identifier를 기반으로 사용자 조회
        Optional<UserEntity> existingUserOpt = userRepository.findByUserIdentifier(userIdentifier);

        if (existingUserOpt.isPresent()) {
            UserEntity existingUser = existingUserOpt.get();

            // 사용자가 이미 존재하는 경우, 역할이 ENTERPRISE 인지 확인
            if (existingUser.getUserRole() != UserRole.ENTERPRISE) {
                throw new CommonException(ErrorCode.INVALID_ENTERPRISE_ROLE);
            }

            // 존재하는 사용자를 반환
            return existingUser;
        }

        // 사용자 정보가 없으면 신규 회원 생성
        UserEntity newUser = new UserEntity();
        newUser.setUserIdentifier(userIdentifier);

        // provider에 따른 userAuthId 설정
        String userAuthId = userIdentifier.split("_")[1]; // KAKAO_id or NAVER_id 형식
        newUser.setUserAuthId(userAuthId);

        newUser.setEmail(email);
        newUser.setUserName(realName);  // 실명
        newUser.setSignupPath(provider); // 가입 경로
        newUser.setUserStatus(ActiveStatus.ACTIVE);
        newUser.setAcceptStatus(AcceptStatus.Y);
        newUser.setCreatedAt(LocalDateTime.now().withNano(0));

        // provider에 따라 역할 부여
        if (provider == SignupPath.KAKAO || provider == SignupPath.NAVER) {
            newUser.setUserRole(UserRole.ENTERPRISE); // 일반 회원
        } else {
            newUser.setUserRole(UserRole.ADMIN); // 관리자일 경우
        }

        // 일반 회원일 경우에만 tier와 userLikes 설정
        if (newUser.getUserRole() == UserRole.ENTERPRISE) {
            // 티어 조회 대신 기본 티어 ID 설정
            Tier bronzeTier = new Tier();
            bronzeTier.setTierId(1L); // 기본 티어를 브론즈(1)로 설정
            newUser.setTier(bronzeTier); // 기본 티어 설정

            newUser.setUserLikes(0L); // 기본 좋아요 값 설정
        }

        // 새 사용자를 저장하고 반환
        return userRepository.save(newUser);
    }


    private String getKakaoAccessToken(String authorizationCode, String redirectUri) {
        // 카카오 API를 사용하여 액세스 토큰 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", oAuthProperties.getRegistration().get("kakao").getClientId());
        params.add("client_secret", oAuthProperties.getRegistration().get("kakao").getClientSecret());
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map<String, String>> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                (Class<Map<String, String>>) (Class<?>) Map.class);

        return response.getBody().get("access_token");
    }

    private KakaoUser getKakaoUserInfo(String accessToken) {
        // 카카오 API를 사용하여 사용자 정보 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                (Class<Map<String, Object>>) (Class<?>) Map.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("카카오 액세스 토큰을 받아오지 못했습니다. 상태 코드: " + response.getStatusCode() + ", 응답 본문: " + response.getBody());
        }

        Map<String, Object> userInfo = response.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");

        String email = (String) kakaoAccount.get("email");
        String name = (String) kakaoAccount.get("name"); // 실명 확인

        return new KakaoUser(
                userInfo.get("id").toString(),
                email,
                name
        );
    }

    private String getNaverAccessToken(String authorizationCode, String state, String redirectUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", oAuthProperties.getRegistration().get("naver").getClientId());
        params.add("client_secret", oAuthProperties.getRegistration().get("naver").getClientSecret());
        params.add("code", authorizationCode);
        params.add("state", state);
        params.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map<String, String>> response = restTemplate.postForEntity(
                "https://nid.naver.com/oauth2.0/token",
                request,
                (Class<Map<String, String>>) (Class<?>) Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("access_token");
        } else {
            throw new RuntimeException("네이버 액세스 토큰을 받아올 수 없습니다.");
        }
    }

    private NaverUser getNaverUserInfo(String accessToken) {
        // 네이버 API를 사용하여 사용자 정보 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                request,
                (Class<Map<String, Object>>) (Class<?>) Map.class);

        Map<String, Object> userInfo = (Map<String, Object>) response.getBody().get("response");
        return new NaverUser(
                userInfo.get("id").toString(),
                (String) userInfo.get("email"),
                (String) userInfo.get("name")  // 네이버에서는 실명이 name 속성에 담김
        );
    }

    private String createUserIdentifier(SignupPath provider, String userAuthId) {
        // user_identifier 생성: SIGNUP_PATH_{ID}
        return provider.name() + "_" + userAuthId;
    }
}
