package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.dto.RecipeBoardCommentDTO;

import java.util.List;

public interface RecipeBoardCommentService {
    RecipeBoardCommentDTO createComment(RecipeBoardCommentDTO commentDTO);
    RecipeBoardCommentDTO modifyComment(Long id, RecipeBoardCommentDTO commentDTO);
    void deleteComment(Long id);
    List<RecipeBoardCommentDTO> getCommentsByRecipeBoardId(Long recipeId);
    List<RecipeBoardCommentDTO> getCommentsByUserId(Long userId);
}
