package com.avengers.yoribogo.answer.service;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.answer.dto.AnswerDTO;

import java.util.List;

public interface AnswerService {
    List<Answer> findAnswer(int inquiryId);

    // 답변 추가 시 답변 작성자(ADMIN/ENTERPRISE)가 문의 상태(문의중/답변완료) 결정
    Answer insertAnswer(AnswerDTO newAnswer);

    // 답변 수정 불가X
//    Answer updateAnswer(AnswerDTO modifyAnswer);

    // 답변 삭제 시 문의의 마지막 답변 작성자(ADMIN/ENTERPRISE)가 문의 상태(문의중/답변완료) 결정
    boolean removeAnswer(int id);
}
