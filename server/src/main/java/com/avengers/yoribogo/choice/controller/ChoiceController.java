package com.avengers.yoribogo.choice.controller;

import com.avengers.yoribogo.choice.service.ChoiceService;
import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.choice.domain.Choice;
import com.avengers.yoribogo.choice.dto.ChoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/choice")
public class ChoiceController {

    private final ChoiceService choiceService;

    @Autowired
    public ChoiceController(ChoiceService choiceService) {
        this.choiceService = choiceService;
    }

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }

    // '/get?id=2' 질문(question_id=2) 선택지 조회
    @GetMapping("/get")
    public ResponseDTO getChoice(@RequestParam Integer id) {
        List<Choice> result = choiceService.findChoice(id);
        return ResponseDTO.ok(result);
    }

    // 선택지 생성
    @PostMapping("/add")
    public ResponseDTO addQuestion(@RequestBody ChoiceDTO newChoice) {
        Choice result = choiceService.insertChoice(newChoice);
        return result != null ? ResponseDTO.ok(result) : ResponseDTO.ok("선택지 생성 실패");
    }

    // 선택지 수정
    @PostMapping("/modify")
    public ResponseDTO moidifyChoice(@RequestBody ChoiceDTO modifyChoice) {
        Choice result = choiceService.updateChoice(modifyChoice);
        return result != null ? ResponseDTO.ok(result) : ResponseDTO.ok("선택지 수정 실패");
    }

    // 선택지 삭제
    @DeleteMapping("/delete")
    public ResponseDTO deleteChoice(@RequestParam int id) {
        boolean result = choiceService.removeChoice(id);
        return result ? ResponseDTO.ok("선택지 삭제 성공") : ResponseDTO.ok("선택지 삭제 실패");
    }
}
