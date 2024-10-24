package com.avengers.yoribogo.user.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.security.JwtUtil;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.vo.ResponseUserVO;
import com.avengers.yoribogo.user.domain.vo.UserIdEmailVerificationVO;
import com.avengers.yoribogo.user.domain.vo.email.EmailVerificationSignupVO;
import com.avengers.yoribogo.user.domain.vo.email.EmailVerificationVO;
import com.avengers.yoribogo.user.domain.vo.email.ResponseEmailMessageVO;
import com.avengers.yoribogo.user.domain.vo.kakao.KakaoAuthorizationCode;
import com.avengers.yoribogo.user.domain.vo.login.AuthTokens;
import com.avengers.yoribogo.user.domain.vo.login.ResponseOAuthLoginVO;
import com.avengers.yoribogo.user.domain.vo.login.TokenRefreshRequest;
import com.avengers.yoribogo.user.domain.vo.naver.NaverAuthorizationCode;
import com.avengers.yoribogo.user.domain.vo.password.RequestUpdateLoggedInPasswordVO;
import com.avengers.yoribogo.user.domain.vo.password.RequestUpdatePasswordUserVO;
import com.avengers.yoribogo.user.domain.vo.signup.RequestResistAdminUserVO;
import com.avengers.yoribogo.user.domain.vo.signup.RequestResistEnterpriseUserVO;
import com.avengers.yoribogo.user.dto.UserDTO;
import com.avengers.yoribogo.user.dto.email.EmailVerificationUserIdRequestDTO;
import com.avengers.yoribogo.user.dto.email.EmailVerificationUserPasswordRequestDTO;
import com.avengers.yoribogo.user.dto.profile.RequestUpdateUserDTO;
import com.avengers.yoribogo.user.dto.profile.ResponseUserProfileDTO;
import com.avengers.yoribogo.user.dto.validate.BooleanResponseDTO;
import com.avengers.yoribogo.user.dto.validate.RequestNicknameDTO;
import com.avengers.yoribogo.user.dto.validate.RequestUserAuthIdentifierDTO;
import com.avengers.yoribogo.user.service.EmailVerificationService;
import com.avengers.yoribogo.user.service.OAuth2LoginService;
import com.avengers.yoribogo.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController("userCommandController")
@RequestMapping("/api/users")
public class UserController {
    private final Environment env;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final OAuth2LoginService oAuth2LoginService;
    private final EmailVerificationService emailVerificationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(Environment env, UserService userService, ModelMapper modelMapper,
                          OAuth2LoginService oAuth2LoginService, EmailVerificationService emailVerificationService,
                          JwtUtil jwtUtil) {
        this.env = env;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.oAuth2LoginService = oAuth2LoginService;
        this.emailVerificationService=emailVerificationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/health")
    public String status() {
        return "I'm Working in User Service on port "
                + env.getProperty("local.server.port");
    }

    // 설명. 1. 이메일 전송 API (회원가입시 실행)
    @PostMapping("/verification-email/signup")
    public ResponseDTO<?> sendVerificationEmail(@RequestBody @Validated EmailVerificationSignupVO request) {
        emailVerificationService.sendVerificationEmail(request.getEmail());

        ResponseEmailMessageVO responseEmailMessageVO = new ResponseEmailMessageVO();
        responseEmailMessageVO.setMessage("인증 코드가 이메일로 전송되었습니다.");
        return ResponseDTO.ok(responseEmailMessageVO);
    }

    // 설명. 1.2. 이메일 전송 API(아이디 찾기시 실행, 일반 회원만 아이디찾기 가능)
    @PostMapping("/verification-email/auth-id")
    public ResponseDTO<?> sendVerificationEmailForUserId(@RequestBody @Validated EmailVerificationUserIdRequestDTO request) {
        // 닉네임, 가입 구분, 이메일이 일치하는 사용자가 있는지 확인
        UserDTO user = userService.findUserByUserNicknameAndSignupPathAndEmail(request.getNickname(), SignupPath.NORMAL, request.getEmail());

        if (user == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        // 유효성 검사후 가능하면 이메일 전송
        emailVerificationService.sendVerificationEmail(request.getEmail());

        ResponseEmailMessageVO responseEmailMessageVO = new ResponseEmailMessageVO();
        responseEmailMessageVO.setMessage("아이디 찾기를 위한 인증 코드가 이메일로 전송되었습니다.");
        return ResponseDTO.ok(responseEmailMessageVO);
    }

    // 설명. 1.3. 이메일 전송 API (비밀번호 찾기시 실행)
    @PostMapping("/verification-email/user-password")
    public ResponseDTO<?> sendVerificationEmailForUserPassword(@RequestBody @Validated EmailVerificationUserPasswordRequestDTO request) {
        // NORMAL_{userAuthId}와 이메일이 일치하는 사용자가 있는지 확인
        UserDTO user = userService.findUserByUserAuthIdAndEmail(request.getUserAuthId(), request.getEmail());

        if (user == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        // 유효성 검사후 가능하면 이메일 전송
        emailVerificationService.sendVerificationEmail(request.getEmail());

        ResponseEmailMessageVO responseEmailMessageVO = new ResponseEmailMessageVO();
        responseEmailMessageVO.setMessage("비밀번호 찾기를 위한 인증 코드가 이메일로 전송되었습니다.");
        return ResponseDTO.ok(responseEmailMessageVO);
    }


    //설명. 2.1 이메일 인증번호 검증 API (회원가입,비밀번호 찾기 실행)
    @PostMapping("/verification-email/confirmation")
    public ResponseDTO<?> verifyEmail(@RequestBody @Validated EmailVerificationVO request) {
        boolean isVerified = emailVerificationService.verifyCode(request.getEmail(), request.getCode());

        ResponseEmailMessageVO responseEmailMessageVO =new ResponseEmailMessageVO();
        responseEmailMessageVO.setMessage("이메일 인증이 완료되었습니다.");
        if (isVerified) {
            return ResponseDTO.ok(responseEmailMessageVO);
        } else {
            return ResponseDTO.fail(new CommonException(ErrorCode.INVALID_VERIFICATION_CODE));
        }
    }

    //설명. 2.2 이메일 인증번호 검증 API (아이디 찾기 실행)
    @PostMapping("/nickname/verification-email")
    public ResponseDTO<?> verifyUserIdEmail(@RequestBody @Validated UserIdEmailVerificationVO request) {
        try {
            // 이메일과 인증 코드 검증
            UserEntity userEntity = emailVerificationService.verifyUserNicknameCode(request.getNickname(), request.getEmail(), request.getCode());
            return ResponseDTO.ok(userEntity);
        } catch (CommonException e) {
            // 검증 실패 시 예외 처리
            return ResponseDTO.fail(e);
        }
    }

    //설명. 3. 카카오 로그인, 네이버 로그인
    // 설명. 3.1 카카오 로그인
    @PostMapping("/oauth2/kakao")
    public ResponseDTO<ResponseOAuthLoginVO> loginWithKakao(@RequestBody KakaoAuthorizationCode code) {
        AuthTokens tokens = oAuth2LoginService.loginWithKakao(code.getCode());

        // 응답 본문에 필요한 정보 포함
        ResponseOAuthLoginVO loginResponseVO = new ResponseOAuthLoginVO(
                tokens.getAccessToken(),
                new Date(tokens.getAccessTokenExpiry()),
                tokens.getRefreshToken(),
                new Date(tokens.getRefreshTokenExpiry()),
                tokens.getUserAuthId()

        );

        return ResponseDTO.ok(loginResponseVO);
    }

    // 설명. 3.2 네이버 로그인
    @PostMapping("/oauth2/naver")
    public ResponseDTO<ResponseOAuthLoginVO> loginWithNaver(@RequestBody NaverAuthorizationCode code) {
        AuthTokens tokens = oAuth2LoginService.loginWithNaver(code);

        // 응답 본문에 필요한 정보 포함
        ResponseOAuthLoginVO loginResponseVO = new ResponseOAuthLoginVO(
                tokens.getAccessToken(),
                new Date(tokens.getAccessTokenExpiry()),
                tokens.getRefreshToken(),
                new Date(tokens.getRefreshTokenExpiry()),
                tokens.getUserAuthId()
        );

        return ResponseDTO.ok(loginResponseVO);
    }
    // 설명. 4 사용자 정보 조회

    // 설명. 4.1 회원 정보 조회 (user_identifier로 사용자 조회)
    @GetMapping("/identifier")
    public ResponseDTO<UserDTO> getUserByUserIdentifier(@RequestParam("user_identifier") String userIdentifier) {
        // userIdentifier로 사용자 조회
        UserEntity userEntity = userService.findByUserIdentifier(userIdentifier);

        // UserEntity를 UserDTO로 빌더 패턴을 사용하여 변환
        UserDTO userDTO = UserDTO.builder()
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .password(userEntity.getEncryptedPwd())
                .nickname(userEntity.getNickname())
                .email(userEntity.getEmail())
                .userAuthId(userEntity.getUserAuthId())
                .userStatus(userEntity.getUserStatus())
                .createdAt(userEntity.getCreatedAt())
                .withdrawnAt(userEntity.getWithdrawnAt())
                .profileImage(userEntity.getProfileImage())
                .acceptStatus(userEntity.getAcceptStatus())
                .signupPath(userEntity.getSignupPath())
                .userRole(userEntity.getUserRole())
                .userLikes(userEntity.getUserLikes())
                .tierId(userEntity.getTier() != null ? userEntity.getTier().getTierId() : null) // tier가 있으면 tier_id 설정
                .userIdentifier(userEntity.getUserIdentifier())
                .build();

        return ResponseDTO.ok(userDTO);
    }


    // 설명. 4.2 회원 정보 조회 (user_auth_id로 사용자 조회)
    @GetMapping("/userAuthId")
    public ResponseDTO<UserDTO> getUserByUserAuthId(@RequestParam("user_auth_id") String userAuthId) {
        // userAuthId로 사용자 조회
        UserEntity userEntity = userService.findByUserAuthId(userAuthId);

        // UserEntity를 UserDTO로 빌더 패턴을 사용하여 변환
        UserDTO userDTO = UserDTO.builder()
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .password(userEntity.getEncryptedPwd())
                .nickname(userEntity.getNickname())
                .email(userEntity.getEmail())
                .userAuthId(userEntity.getUserAuthId())
                .userStatus(userEntity.getUserStatus())
                .createdAt(userEntity.getCreatedAt())
                .withdrawnAt(userEntity.getWithdrawnAt())
                .profileImage(userEntity.getProfileImage())
                .acceptStatus(userEntity.getAcceptStatus())
                .signupPath(userEntity.getSignupPath())
                .userRole(userEntity.getUserRole())
                .userLikes(userEntity.getUserLikes())
                .tierId(userEntity.getTier() != null ? userEntity.getTier().getTierId() : null) // tier가 있으면 tier_id 설정
                .userIdentifier(userEntity.getUserIdentifier())
                .build();

        return ResponseDTO.ok(userDTO);
    }


    // 설명. 4.3 사용자 식별자(userId)로 조회한 후 UserDTO로 변환하여 반환
    @GetMapping("/{userId}")
    public ResponseDTO<UserDTO> getUserByUserId(@PathVariable("userId") Long userId) {
        UserEntity userEntity = userService.findByUserId(userId);

        // UserEntity를 UserDTO로 직접 빌더 패턴을 사용하여 변환
        UserDTO userDTO = UserDTO.builder()
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .password(userEntity.getEncryptedPwd()) // 암호화된 비밀번호를 설정
                .nickname(userEntity.getNickname())
                .email(userEntity.getEmail())
                .userAuthId(userEntity.getUserAuthId())
                .userStatus(userEntity.getUserStatus())
                .createdAt(userEntity.getCreatedAt())
                .withdrawnAt(userEntity.getWithdrawnAt())
                .profileImage(userEntity.getProfileImage())
                .acceptStatus(userEntity.getAcceptStatus())
                .signupPath(userEntity.getSignupPath())
                .userRole(userEntity.getUserRole())
                .userLikes(userEntity.getUserLikes())
                .tierId(userEntity.getTier() != null ? userEntity.getTier().getTierId() : null) // tier가 있으면 tier_id 설정
                .userIdentifier(userEntity.getUserIdentifier())
                .build();

        return ResponseDTO.ok(userDTO);
    }


    // 설명. 4.4 사용자 식별자(userId)로 조회 -> 닉네임, 프로필 사진, 티어 이름, 티어 이미지
    @GetMapping("/{userId}/profile")
    public ResponseDTO<ResponseUserProfileDTO> getUserProfileByUserId(@PathVariable("userId") Long userId) {
        UserEntity userEntity = userService.findByUserId(userId);

        // 새로운 DTO로 필요한 정보만 추출
        ResponseUserProfileDTO userProfileDTO = ResponseUserProfileDTO.builder()
                .nickname(userEntity.getNickname())
                .profileImage(userEntity.getProfileImage() != null ? userEntity.getProfileImage()
                        : "https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png") // 기본 이미지 처리
                .email(userEntity.getEmail())
                .userRole(userEntity.getUserRole().name())
                .signupPath(userEntity.getSignupPath().name())
                .tierName(userEntity.getTier() != null ? userEntity.getTier().getTierName() : "No Tier")
                .tierImage(userEntity.getTier() != null ? userEntity.getTier().getTierImage() : "https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_tier.png") // 기본 티어 이미지 처리
                .build();

        return ResponseDTO.ok(userProfileDTO);
    }



    // 설명. 5. 리프레시 토큰으로 액세스 토큰 재발급
    @PostMapping("/auth/refresh-token")
    public ResponseDTO<AuthTokens> refreshToken(@RequestBody TokenRefreshRequest request) {
        // 서비스 계층에 로직 위임
        AuthTokens authTokens = jwtUtil.refreshAccessToken(request.getRefreshToken());
        return ResponseDTO.ok(authTokens);
    }


    /* 설명. 6. 일반 회원 가입 기능 */
    @PostMapping("/signup/normal")
    public ResponseDTO<UserDTO> registNormalUser(@RequestBody RequestResistEnterpriseUserVO newUser) {
        // UserService 호출
        UserDTO savedUserDTO = userService.registUser(newUser); // 저장된 DTO 반환

        // ResponseUserVO로 변환하는 대신 UserDTO를 직접 응답으로 사용
        return ResponseDTO.ok(savedUserDTO);
    }


    /* 설명. 7. 관리자 회원 가입 기능 */
    @PostMapping("/signup/admin")
    public ResponseDTO<UserDTO> registAdminUser(@RequestBody RequestResistAdminUserVO newAdminUser) {
        // UserService 호출
        UserDTO savedUserDTO = userService.registAdminUser(newAdminUser); // 저장된 DTO 반환

        // ResponseUserVO로 변환하는 대신 UserDTO를 직접 응답으로 사용
        return ResponseDTO.ok(savedUserDTO);
    }


    /* 설명. 8. 닉네임 중복 검증  */
    @PostMapping("/nickname/validate")
    public ResponseDTO<BooleanResponseDTO> validateNickname(@RequestBody RequestNicknameDTO requestNicknameDTO) {
        BooleanResponseDTO booleanResponseDTO= userService. getUserByNicknameForDuplicate(requestNicknameDTO.getNickname());
        return ResponseDTO.ok(booleanResponseDTO);
    }

    /* 설명. 9. 아이디 중복 검증  */
    @PostMapping("/user-id/validate")
    public ResponseDTO<BooleanResponseDTO> getUserByUserIdentifier(@RequestBody RequestUserAuthIdentifierDTO requestUserIdentifierDTO) {
        BooleanResponseDTO booleanResponseDTO = userService.getUserByUserAuthId(requestUserIdentifierDTO.getUserAuthId());
        return ResponseDTO.ok(booleanResponseDTO);
    }

    //필기. 10. 회원 탈퇴
    @PatchMapping("/{userId}/deactivate")
    public ResponseDTO<?> deactivateUser(@PathVariable("userId") Long userId) {
        UserDTO userDTO = userService.deactivateUser(userId);
        return ResponseDTO.ok(userDTO);
    }
    //필기. 11. 사용자 재활성화
    @PostMapping("/activate")
    public ResponseDTO<?> activateUser(@RequestParam("userAuthId") String userAuthId ) {
        UserDTO userDTO = userService.activateUser(userAuthId);
        return ResponseDTO.ok(userDTO);
    }

    // 필기. 12. 로그인 전 사용자 비밀번호 재설정
    @PostMapping("/re-password")
    public ResponseDTO<UserDTO> updatePassword(@RequestBody RequestUpdatePasswordUserVO requestUpdatePasswordUserVO) {
        // 서비스 호출 및 결과 처리
        UserDTO userDTO = userService.updatePassword(requestUpdatePasswordUserVO.getUserAuthId(), requestUpdatePasswordUserVO.getPassword());
        return ResponseDTO.ok(userDTO);
    }


    // 필기. 13. 로그인한 사용자 비밀번호 재설정
    @PatchMapping("/{userId}/password")
    public ResponseDTO<UserDTO> updateLoginedPassword(@PathVariable("userId") Long userId, @RequestBody RequestUpdateLoggedInPasswordVO requestUpdatePasswordUserVO) {
        // 서비스 호출 및 결과 처리
        UserDTO userDTO = userService.updateLoggedInPassword(userId, requestUpdatePasswordUserVO.getPassword());
        return ResponseDTO.ok(userDTO);
    }

    // 필기. 14. 사용자 프로필 변경(닉네임, 사진)
    @PatchMapping("/{userId}/profile")
    public ResponseDTO<UserDTO> updateProfile(@PathVariable("userId") Long userId, @RequestParam("nickname") String nickname,
                                              @RequestParam(value = "profile_image", required = false) MultipartFile profileImage) {

        // DTO 객체 생성 및 값 설정
        RequestUpdateUserDTO userUpdateDTO = new RequestUpdateUserDTO();
        userUpdateDTO.setNickname(nickname);
        userUpdateDTO.setProfileImage(profileImage);

        // 서비스 호출 및 결과 처리
        UserDTO userDTO = userService.updateProfile(userId, userUpdateDTO);
        return ResponseDTO.ok(userDTO);
    }



}
