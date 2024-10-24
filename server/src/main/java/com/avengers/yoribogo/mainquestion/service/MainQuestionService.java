package com.avengers.yoribogo.mainquestion.service;

import com.avengers.yoribogo.mainquestion.domain.MainQuestion;
import com.avengers.yoribogo.mainquestion.dto.MainQuestionDTO;

import java.util.List;

public interface MainQuestionService {

    // 전체 질문 조회
    List<MainQuestion> findQuestion(int userId);

    // 질문 생성
    MainQuestion insertQuestion(MainQuestionDTO newQuestion);

    // 질문 수정
    MainQuestion updateQuestion(MainQuestionDTO modifyQuestion);

    // 질문 삭제
    boolean removeQuestion(int id);
}
