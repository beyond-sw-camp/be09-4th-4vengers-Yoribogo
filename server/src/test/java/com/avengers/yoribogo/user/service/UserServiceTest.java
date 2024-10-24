package com.avengers.yoribogo.user.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.dto.UserDTO;
import com.avengers.yoribogo.user.dto.profile.RequestUpdateUserDTO;
import com.avengers.yoribogo.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AmazonS3Client s3Client;

    @Test
    @DisplayName("사용자 프로필 업데이트 성공 테스트")
    void updateProfile_Success() throws IOException {
        // Given
        Long userId = 1L;
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("profileImage", "test.jpg",
                "image/jpeg", "test image content".getBytes());

        RequestUpdateUserDTO userUpdateDTO = new RequestUpdateUserDTO();
        userUpdateDTO.setNickname("새로운 닉네임");
        userUpdateDTO.setProfileImage(mockMultipartFile);

        // S3 파일 업로드 Mocking
        Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class)))
                .thenReturn(null);
        Mockito.when(s3Client.getUrl(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(new java.net.URL("https://s3.amazonaws.com/bucket/test.jpg"));

        // When
        UserDTO result = userService.updateProfile(userId, userUpdateDTO);

        // Then
        assertNotNull(result);
        assertEquals("새로운 닉네임", result.getNickname());
        assertEquals("https://s3.amazonaws.com/bucket/test.jpg", result.getProfileImage());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 프로필 업데이트 실패 테스트")
    void updateProfile_UserNotFound() {
        // Given
        Long userId = 999L;  // 존재하지 않는 사용자 ID로 테스트
        MockMultipartFile mockMultipartFile = new MockMultipartFile("profileImage", "test.jpg",
                "image/jpeg", "test image content".getBytes());

        RequestUpdateUserDTO userUpdateDTO = new RequestUpdateUserDTO();
        userUpdateDTO.setNickname("새로운 닉네임");
        userUpdateDTO.setProfileImage(mockMultipartFile);

        // When & Then
        CommonException exception = assertThrows(CommonException.class, () -> userService.updateProfile(userId, userUpdateDTO));
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    }

    @Test
    @DisplayName("프로필 이미지 업로드 실패 테스트")
    void updateProfile_FileUploadFailure() throws IOException {
        // Given
        Long userId = 1L;
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("profileImage", "test.jpg",
                "image/jpeg", "test image content".getBytes());

        RequestUpdateUserDTO userUpdateDTO = new RequestUpdateUserDTO();
        userUpdateDTO.setNickname("새로운 닉네임");
        userUpdateDTO.setProfileImage(mockMultipartFile);

        // S3 파일 업로드 실패 Mocking
        Mockito.doThrow(new AmazonClientException("S3 upload failed"))
                .when(s3Client).putObject(Mockito.any(PutObjectRequest.class));

        // When & Then
        CommonException exception = assertThrows(CommonException.class, () -> userService.updateProfile(userId, userUpdateDTO));
        assertEquals(ErrorCode.FILE_UPLOAD_ERROR, exception.getErrorCode());
    }
}