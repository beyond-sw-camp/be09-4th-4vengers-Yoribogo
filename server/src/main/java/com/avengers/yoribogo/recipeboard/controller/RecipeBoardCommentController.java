
package com.avengers.yoribogo.recipeboard.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardComment;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardCommentDTO;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardRecommentDTO;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardCommentServiceImpl;
import com.avengers.yoribogo.recipeboard.service.RecipeBoardRecommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-board/{recipeBoardId}/comments")
public class RecipeBoardCommentController {
    private final RecipeBoardCommentServiceImpl recipeBoardCommentService;
    private final RecipeBoardRecommentServiceImpl recipeBoardRecommentService;

    @Autowired
    public RecipeBoardCommentController(RecipeBoardCommentServiceImpl recipeBoardCommentService,
                                        RecipeBoardRecommentServiceImpl recipeBoardRecommentService) {
        this.recipeBoardCommentService = recipeBoardCommentService;
        this.recipeBoardRecommentService = recipeBoardRecommentService;
    }

    // 댓글 등록
    @PostMapping
    public ResponseDTO<RecipeBoardCommentDTO> createComment(
            @PathVariable Long recipeBoardId,
            @RequestBody RecipeBoardCommentDTO commentDTO) {

        commentDTO.setRecipeBoardId(recipeBoardId);

        RecipeBoardCommentDTO createdComment = recipeBoardCommentService.createComment(commentDTO);
        return ResponseDTO.ok(createdComment);  // 성공 응답
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseDTO<RecipeBoardCommentDTO> modifyComment(
            @PathVariable Long id,
            @RequestBody RecipeBoardCommentDTO commentDTO) {

        RecipeBoardCommentDTO updatedComment = recipeBoardCommentService.modifyComment(id, commentDTO);
        return ResponseDTO.ok(updatedComment);  // 성공 응답
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseDTO<Void> deleteComment(@PathVariable Long id) {
        recipeBoardCommentService.deleteComment(id);
        return new ResponseDTO<>(HttpStatus.NO_CONTENT, true, null, null);  // No Content 응답
    }

    // 댓글 게시글별 조회
    @GetMapping
    public ResponseDTO<List<RecipeBoardCommentDTO>> getCommentsByRecipeBoardId(
            @PathVariable Long recipeBoardId) {

        List<RecipeBoardCommentDTO> commentsByRecipeBoardId = recipeBoardCommentService.getCommentsByRecipeBoardId(recipeBoardId);
        return ResponseDTO.ok(commentsByRecipeBoardId);  // 성공 응답
    }

    // 대댓글 게시글별 조회
    @GetMapping("/recomments")
    public ResponseDTO<List<RecipeBoardRecommentDTO>> getRecommentsByRecipeBoardId(
            @PathVariable Long recipeBoardId) {

        List<RecipeBoardRecommentDTO> recomments = recipeBoardRecommentService.getRecommentsByRecipeId(recipeBoardId);
        return ResponseDTO.ok(recomments);  // 성공 응답
    }

    // 댓글 회원별 조회
    @GetMapping("/user/{userId}")
    public ResponseDTO<List<RecipeBoardCommentDTO>> getCommentsByUserId(
            @PathVariable Long userId) {

        List<RecipeBoardCommentDTO> commentsByUserId = recipeBoardCommentService.getCommentsByUserId(userId);
        return ResponseDTO.ok(commentsByUserId);  // 성공 응답
    }

    // 대댓글 회원별 조회
    @GetMapping("/recomments/user/{userId}")
    public ResponseDTO<List<RecipeBoardRecommentDTO>> getRecommentsByUserId(
            @PathVariable Long userId) {

        List<RecipeBoardRecommentDTO> recommentsByUserId = recipeBoardRecommentService.getRecommentsByUserId(userId);
        return ResponseDTO.ok(recommentsByUserId);  // 성공 응답
    }
}