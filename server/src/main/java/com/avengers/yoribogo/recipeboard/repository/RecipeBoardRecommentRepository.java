package com.avengers.yoribogo.recipeboard.repository;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardComment;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardRecomment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeBoardRecommentRepository extends JpaRepository<RecipeBoardRecomment, Long> {


    List<RecipeBoardRecomment> findByRecipeBoardComment_RecipeBoardCommentId(Long commentId);



    // 회원 id로 조회
    List<RecipeBoardRecomment> findAllByUserId(Long userId);



}
