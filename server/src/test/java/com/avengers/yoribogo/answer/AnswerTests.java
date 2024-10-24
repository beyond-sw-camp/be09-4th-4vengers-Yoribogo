package com.avengers.yoribogo.answer;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.answer.dto.AnswerDTO;
import com.avengers.yoribogo.answer.service.AnswerService;
import com.avengers.yoribogo.common.Role;
import com.avengers.yoribogo.common.Status;
import com.avengers.yoribogo.inquiry.service.InquiryService;
import com.avengers.yoribogo.user.domain.enums.AcceptStatus;
import com.avengers.yoribogo.user.domain.enums.ActiveStatus;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.avengers.yoribogo.user.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class AnswerTests {

    private AnswerService answerService;
    private InquiryService inquiryService;
    private ModelMapper modelMapper;

    @Autowired
    public AnswerTests(AnswerService answerService, InquiryService inquiryService, ModelMapper modelMapper) {
        this.answerService = answerService;
        this.inquiryService = inquiryService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);
    }

    @DisplayName("문의의 답변 목록 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void testGetAnswer(int inquiryId) {
        List<Answer> result = answerService.findAnswer(inquiryId);
        Assertions.assertNotNull(result);
    }

    @DisplayName("답변 생성 확인 테스트")
    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"ADMIN", "ENTERPRISE"})
    public void testAddAnswer(Role writer) {
        UserDTO user = new UserDTO(4L,
                "조국찬",
                "password456",
                "조국찬",
                "changuk0308@naver.com",
                "abc123xyz",
                ActiveStatus.ACTIVE,
                LocalDateTime.parse("2023-08-03T12:00:00"),
                null,
                null,
                AcceptStatus.Y,
                SignupPath.NORMAL,
                UserRole.ENTERPRISE,
                15L,
                4L,
                "NORMAL_abc123xyz");
        UserDTO admin = new UserDTO(1L,
                "조찬국",
                "password456",
                "조찬국",
                "alice@example.com",
                "changuk1234",
                ActiveStatus.ACTIVE,
                LocalDateTime.parse("2023-08-01T10:30:00"),
                null,
                null,
                AcceptStatus.Y,
                SignupPath.ADMIN,
                UserRole.ADMIN,
                null,
                null,
                "ADMIN_changuk1234");

        Answer result = new Answer();

        if (writer == Role.ADMIN) result = answerService.insertAnswer(new AnswerDTO("답변 생성 테스트 내용", Role.ADMIN, 1, admin));
        else result = answerService.insertAnswer(new AnswerDTO("답변 생성 테스트 내용", Role.ENTERPRISE, 1, user));

        Status status = inquiryService.findInquiryById(1).getAnswerStatus();

        if (writer == Role.ADMIN) Assertions.assertTrue(status == Status.ANSWERED);
        else Assertions.assertTrue(status == Status.PENDING);
    }

    @DisplayName("답변 삭제 확인 테스트")
    @Test
    public void testDeleteAnswer() {
        boolean result = answerService.removeAnswer(3);
        Assertions.assertTrue(result);
    }
}
