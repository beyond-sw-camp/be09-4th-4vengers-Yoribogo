package com.avengers.yoribogo.mainquestion;

import com.avengers.yoribogo.mainquestion.domain.MainQuestion;
import com.avengers.yoribogo.mainquestion.dto.MainQuestionDTO;
import com.avengers.yoribogo.mainquestion.service.MainQuestionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MainQuestionTests {

    @Autowired
    private MainQuestionService mainQuestionService;

    @DisplayName("회원의 질문(+포함된 선택지) 목록 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5})
    public void testGetMainQuestion(int userId) {
        List<MainQuestion> result = mainQuestionService.findQuestion(userId);
        Assertions.assertNotNull(result);
    }

    @DisplayName("질문 생성 확인 테스트")
    @Test
    public void testAddMainQuestion() {
        MainQuestion result = mainQuestionService.insertQuestion(
                new MainQuestionDTO("질문 생성 테스트", 3));
        Assertions.assertNotNull(result);
    }

    @DisplayName("질문 수정 확인 테스트")
    @Test
    public void testModifyMainQuestion() {
        MainQuestion result = mainQuestionService.updateQuestion(
                new MainQuestionDTO(11, "질문 수정 테스트", 3));
        Assertions.assertNotNull(result);
    }

    @DisplayName("질문 삭제 확인 테스트")
    @Test
    public void testDeleteMainQuestion() {
        boolean result = mainQuestionService.removeQuestion(11);
        Assertions.assertTrue(result);
    }
}
