package com.avengers.yoribogo.choice;

import com.avengers.yoribogo.choice.domain.Choice;
import com.avengers.yoribogo.choice.dto.ChoiceDTO;
import com.avengers.yoribogo.choice.service.ChoiceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChoiceTests {

    @Autowired
    private ChoiceService choiceService;

    @DisplayName("질문에 포함된 선택지 목록 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void testGetChoice(int mainQuestionId) {
        List<Choice> result = choiceService.findChoice(mainQuestionId);
        Assertions.assertNotNull(result);
    }

    @DisplayName("선택지 생성 확인 테스트")
    @Test
    public void testAddChoice() {
        Choice result = choiceService.insertChoice(
                new ChoiceDTO("testImage.jpg", "선택지 생성 테스트", 1));
        Assertions.assertNotNull(result);
    }

    @DisplayName("선택지 수정 확인 테스트")
    @Test
    public void testModifyChoice() {
        Choice result = choiceService.updateChoice(
                new ChoiceDTO(11, "testImage.jpg", "선택지 수정 테스트", 1));
        Assertions.assertNotNull(result);
    }

    @DisplayName("선택지 삭제 확인 테스트")
    @Test
    public void testDeleteChoice() {
        boolean result = choiceService.removeChoice(11);
        Assertions.assertTrue(result);
    }

}
