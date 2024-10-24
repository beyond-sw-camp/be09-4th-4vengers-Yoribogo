package com.avengers.yoribogo.recipeboard.recipeboardlike.controller;

import com.avengers.yoribogo.recipeboard.recipeboardlike.Service.RecipeBoardLikeService;
import com.avengers.yoribogo.recipeboard.recipeboardlike.dto.LikeRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@Slf4j
public class RecipeLikeController {

    private final RecipeBoardLikeService recipeBoardLikeService;

    public RecipeLikeController(RecipeBoardLikeService recipeBoardLikeService) {
        this.recipeBoardLikeService = recipeBoardLikeService;
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> likePost(@RequestBody LikeRequestDTO likeRequestDTO) {
        log.info("Received like request: {}", likeRequestDTO);
        boolean isLiked = recipeBoardLikeService.likePost(likeRequestDTO);  // 좋아요 상태 반환

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("isLiked", isLiked);  // 좋아요 상태 반환
        response.put("message", isLiked ? "좋아요가 추가되었습니다." : "좋아요가 취소되었습니다.");

        return ResponseEntity.ok(response);
    }


}
