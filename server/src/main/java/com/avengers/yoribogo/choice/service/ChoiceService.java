package com.avengers.yoribogo.choice.service;

import com.avengers.yoribogo.choice.domain.Choice;
import com.avengers.yoribogo.choice.dto.ChoiceDTO;

import java.util.List;

public interface ChoiceService {

    // 질문의 선택지 조회
    List<Choice> findChoice(int mainQuestionId);

    // 선택지 생성
    Choice insertChoice(ChoiceDTO newChoice);

    // 선택지 수정
    Choice updateChoice(ChoiceDTO modifyChoice);

    // 선택지 삭제
    boolean removeChoice(int id);
}
