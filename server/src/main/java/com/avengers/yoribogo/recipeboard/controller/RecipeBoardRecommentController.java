

package com.avengers.yoribogo.recipeboard.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardRecommentDTO;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardRecommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe-board/{recipeBoardId}/comments/{commentId}")
public class RecipeBoardRecommentController {

    private final RecipeBoardRecommentServiceImpl recipeBoardRecommentService;

    @Autowired
    public RecipeBoardRecommentController(RecipeBoardRecommentServiceImpl recipeBoardRecommentService) {
        this.recipeBoardRecommentService = recipeBoardRecommentService;
    }

    // 대댓글 등록
    @PostMapping
    public ResponseDTO<RecipeBoardRecommentDTO> createRecomment(
            @PathVariable Long commentId,
            @RequestBody RecipeBoardRecommentDTO recommentDTO) {

        recommentDTO.setRecipeBoardCommentId(commentId);

        RecipeBoardRecommentDTO createdRecomment = recipeBoardRecommentService.createRecomment(commentId, recommentDTO);
        return ResponseDTO.ok(createdRecomment); // 성공 응답
    }

    // 대댓글 수정
    @PutMapping("/recomments/{recommentId}")
    public ResponseDTO<RecipeBoardRecommentDTO> updateRecomment(
            @PathVariable Long recommentId,
            @RequestBody RecipeBoardRecommentDTO recommentDTO) {

        recommentDTO.setRecipeBoardCommentId(recommentId);

        RecipeBoardRecommentDTO updatedRecomment = recipeBoardRecommentService.updateRecomment(recommentId, recommentDTO);
        return ResponseDTO.ok(updatedRecomment); // 성공 응답
    }

    // 대댓글 삭제
    @DeleteMapping("/recomments/{recommentId}")
    public ResponseDTO<Void> deleteRecomment(@PathVariable Long recommentId) {
        recipeBoardRecommentService.deleteRecomment(recommentId);
        return new ResponseDTO<>(HttpStatus.NO_CONTENT, true, null, null); // No Content 응답
    }
}