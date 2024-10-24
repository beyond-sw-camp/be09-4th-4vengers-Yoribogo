package com.avengers.yoribogo.mainquestion.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.mainquestion.domain.MainQuestion;
import com.avengers.yoribogo.mainquestion.dto.MainQuestionDTO;
import com.avengers.yoribogo.mainquestion.service.MainQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/main-question")
public class MainQuestionController {

    private final MainQuestionService mainQuestionService;

    @Autowired
    public MainQuestionController(MainQuestionService mainQuestionService) {
        this.mainQuestionService = mainQuestionService;
    }

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }

    // '/get?id=3' 회원(user_id=3)의 전체 질문
    @GetMapping("/get")
    public ResponseDTO getQuestion(@RequestParam int id) {
        List<MainQuestion> result = mainQuestionService.findQuestion(id);
        return ResponseDTO.ok(result);
    }

    // 질문 생성
    @PostMapping("/add")
    public ResponseDTO addQuestion(@RequestBody MainQuestionDTO newQuestion) {
        MainQuestion result = mainQuestionService.insertQuestion(newQuestion);
        return result != null ? ResponseDTO.ok(result): ResponseDTO.ok("질문 생성 실패");
    }

    // 질문 수정
    @PostMapping("/modify")
    public ResponseDTO modifytQuestion(@RequestBody MainQuestionDTO modifyQuestion) {
        MainQuestion result = mainQuestionService.updateQuestion(modifyQuestion);
        return result != null ? ResponseDTO.ok(result): ResponseDTO.ok("질문 수정 실패");
    }

    // 질문 삭제
    @DeleteMapping("/delete")
    public ResponseDTO deleteQuestion(@RequestParam int id) {
        boolean result = mainQuestionService.removeQuestion(id);
        return result ? ResponseDTO.ok("질문 삭제 성공") : ResponseDTO.ok("질문 삭제 실패");
    }
}
