package com.avengers.yoribogo.user.repository;

import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT COALESCE(MAX(u.userId), 0) FROM UserEntity u")
    Long findMaxUserId();

    // userIdentifier로 사용자 조회 (페치 조인으로 tier 함께 조회)
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.tier WHERE u.userIdentifier = :userIdentifier")
    Optional<UserEntity> findByUserIdentifier(@Param("userIdentifier") String userIdentifier);

    // 닉네임으로 사용자 조회 (페치 조인으로 tier 함께 조회)
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.tier WHERE u.nickname = :nickname")
    Optional<UserEntity> findByNickname(@Param("nickname") String nickname);

    // 이름, 가입 경로, 이메일로 사용자 찾기 (페치 조인으로 tier 함께 조회)
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.tier WHERE u.nickname = :nickname AND u.signupPath = :signupPath AND u.email = :email")
    Optional<UserEntity> findByNicknameAndSignupPathAndEmail(@Param("nickname") String nickname, @Param("signupPath") SignupPath signupPath, @Param("email") String email);

    // userAuthId와 이메일로 사용자 찾기 (페치 조인으로 tier 함께 조회)
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.tier WHERE u.userAuthId = :userAuthId AND u.email = :email")
    Optional<UserEntity> findByUserAuthIdAndEmail(@Param("userAuthId") String userAuthId, @Param("email") String email);

    // userAuthId로 사용자 조회 (페치 조인으로 tier 함께 조회)
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.tier WHERE u.userAuthId = :userAuthId")
    Optional<UserEntity> findByUserAuthId(@Param("userAuthId") String userAuthId);

    // userId로 사용자 조회 (페치 조인으로 tier 함께 조회)
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.tier WHERE u.userId = :userId")
    Optional<UserEntity> findByUserIdWithTier(@Param("userId") Long userId);

    // 알림 기능 위해 추가
    Optional<UserEntity> findById(Long id);  // 특정 회원을 Optional로 조회
}
