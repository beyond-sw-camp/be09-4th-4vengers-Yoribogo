package com.avengers.yoribogo.answer.controller;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.answer.dto.AnswerDTO;
import com.avengers.yoribogo.answer.service.AnswerService;
import com.avengers.yoribogo.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }

    // '/get?id=2' 문의(inquiry_id=2)의 답변 조회
    @GetMapping("/get")
    public ResponseDTO getAnswer(@RequestParam("id") int id) {
        List<Answer> result = answerService.findAnswer(id);
        return ResponseDTO.ok(result);
    }

    // 답변 생성
    @PostMapping("/add")
    public ResponseDTO addAnswer(@RequestBody AnswerDTO newAnswer) {
        Answer result = answerService.insertAnswer(newAnswer);
        return result != null ? ResponseDTO.ok(result) : ResponseDTO.ok("답변 생성 실패");
    }

//    // 답변 수정
//    @PostMapping("/modify")
//    public ResponseDTO modifyAnswer(@RequestBody AnswerDTO modifyAnswer) {
//        Answer result = answerService.updateAnswer(modifyAnswer);
//        return result != null ? ResponseDTO.ok(result) : ResponseDTO.ok("답변 수정 실패");
//    }

    // 답변 삭제
    @DeleteMapping("/delete")
    public ResponseDTO deleteAnswer(@RequestParam("id") int id) {
        boolean result = answerService.removeAnswer(id);
        return result ? ResponseDTO.ok("답변 삭제 성공") : ResponseDTO.ok("답변 삭제 실패");
    }
}
