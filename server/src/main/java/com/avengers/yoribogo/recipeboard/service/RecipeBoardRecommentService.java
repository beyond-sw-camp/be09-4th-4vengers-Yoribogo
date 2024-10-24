package com.avengers.yoribogo.recipeboard.service;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardRecommentDTO;

import java.util.List;

public interface RecipeBoardRecommentService {

    // 대댓글 등록
    RecipeBoardRecommentDTO createRecomment(Long commentId, RecipeBoardRecommentDTO recipeBoardRecommentDTO);


    // 대댓글 수정
    RecipeBoardRecommentDTO updateRecomment(Long recommentId, RecipeBoardRecommentDTO recommentDTO);

    // 대댓글 삭제
    void deleteRecomment(Long id);


    // 대댓글 게시글별 조회
    List<RecipeBoardRecommentDTO> getRecommentsByRecipeId(Long recipeBoardId);


    // 대댓글 게시글별 조회
    List<RecipeBoardRecommentDTO> getRecommentsByUserId(Long userId);
}
