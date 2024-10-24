package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardComment;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardRecomment;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardRecommentStatus;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardRecommentDTO;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardCommentRepository;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRecommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeBoardRecommentServiceImpl implements RecipeBoardRecommentService {

    private final RecipeBoardCommentRepository recipeBoardCommentRepository;
    private final RecipeBoardRecommentRepository recommentRepository;
    private final RecipeBoardRecommentRepository recipeBoardRecommentRepository;


    public RecipeBoardRecommentServiceImpl(RecipeBoardRecommentRepository recommentRepository,
                                           RecipeBoardCommentRepository recipeBoardCommentRepository,
                                           RecipeBoardRecommentRepository recipeBoardRecommentRepository) {
        this.recipeBoardCommentRepository = recipeBoardCommentRepository;
        this.recommentRepository = recommentRepository;
        this.recipeBoardRecommentRepository = recipeBoardRecommentRepository;
    }

    // 대댓글 등록
    @Override
    @Transactional
    public RecipeBoardRecommentDTO createRecomment(Long commentId, RecipeBoardRecommentDTO recommentDTO) {
        // 댓글 객체를 가져오기
        RecipeBoardComment comment = recipeBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD_COMMENT));

        // 대댓글 엔티티 생성
        RecipeBoardRecomment recomment = new RecipeBoardRecomment();
        recomment.setRecipeBoardRecommentContent(recommentDTO.getRecipeBoardRecommentContent());
        recomment.setRecipeBoardRecommentStatus(RecipeBoardRecommentStatus.ACTIVE);
        recomment.setRecipeBoardRecommentCreatedAt(LocalDateTime.now());
        recomment.setRecipeBoardComment(comment); // 댓글 객체 설정 -> comment와 recomment 연결
        recomment.setUserId(recommentDTO.getUserId()); // 사용자 ID 설정

        // 대댓글 저장
        RecipeBoardRecomment createdRecomment = recommentRepository.save(recomment);

        // DTO로 변환하여 반환
        RecipeBoardRecommentDTO createdRecommentDTO = new RecipeBoardRecommentDTO();
        createdRecommentDTO.setRecipeBoardRecommentId(createdRecomment.getRecipeBoardRecommentId());
        createdRecommentDTO.setRecipeBoardRecommentContent(createdRecomment.getRecipeBoardRecommentContent());
        createdRecommentDTO.setRecipeBoardRecommentStatus(createdRecomment.getRecipeBoardRecommentStatus());
        createdRecommentDTO.setRecipeBoardRecommentCreatedAt(createdRecomment.getRecipeBoardRecommentCreatedAt());
        createdRecommentDTO.setRecipeBoardCommentId(commentId); // 댓글 ID 설정
        createdRecommentDTO.setUserId(createdRecomment.getUserId());

        return createdRecommentDTO;
    }

    // 대댓글 수정
    @Override
    @Transactional
    public RecipeBoardRecommentDTO updateRecomment(Long recommentId, RecipeBoardRecommentDTO recommentDTO){

        // 대댓글 조회
        RecipeBoardRecomment recomment = recommentRepository.findById(recommentId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD_RECOMMENT));



        // 수정할 DTO 가져오고 DB 수정
        recomment.setRecipeBoardRecommentContent(recommentDTO.getRecipeBoardRecommentContent());
        recomment.setRecipeBoardRecommentCreatedAt(LocalDateTime.now());

        RecipeBoardRecomment updatedRecomment = recommentRepository.save(recomment);

        RecipeBoardRecommentDTO updatedRecommentDTO = new RecipeBoardRecommentDTO();
        updatedRecommentDTO.setRecipeBoardRecommentId(updatedRecomment.getRecipeBoardRecommentId());
        updatedRecommentDTO.setRecipeBoardRecommentContent(updatedRecomment.getRecipeBoardRecommentContent());
        updatedRecommentDTO.setRecipeBoardRecommentStatus(updatedRecomment.getRecipeBoardRecommentStatus());
        updatedRecommentDTO.setRecipeBoardCommentId(updatedRecomment.getRecipeBoardComment().getRecipeBoardCommentId()); // 댓글 ID 가져오기
        updatedRecommentDTO.setRecipeBoardRecommentCreatedAt(updatedRecomment.getRecipeBoardRecommentCreatedAt());
        updatedRecommentDTO.setUserId(updatedRecomment.getUserId());

        return updatedRecommentDTO;


    }

    // 대댓글 삭제
    @Override
    @Transactional
    public void deleteRecomment(Long recommentId) {

        try {
            recommentRepository.deleteById(recommentId);
        } catch (EntityNotFoundException e) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD_RECOMMENT);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
        }



    }


    // 대댓글 게시글별 조회
    @Override
    @Transactional
    public List<RecipeBoardRecommentDTO> getRecommentsByRecipeId(Long recipeBoardId){

        // 1. 게시글에 해당하는 모든 댓글을 가져옴
        List<RecipeBoardComment> comments = recipeBoardCommentRepository.findAllByRecipeBoardId(recipeBoardId);

        // 2. 각 댓글에 달린 대댓글들을 모두 모아옴
        List<RecipeBoardRecomment> recomments = new ArrayList<>();

        for (RecipeBoardComment comment : comments) {
            // 해당 댓글에 달린 대댓글을 모두 가져옴
            List<RecipeBoardRecomment> commentRecomments = recipeBoardRecommentRepository.findByRecipeBoardComment_RecipeBoardCommentId(comment.getRecipeBoardCommentId());
            recomments.addAll(commentRecomments);
        }

        // 3. 대댓글 엔티티 리스트를 DTO 리스트로 변환
        List<RecipeBoardRecommentDTO> recommentDTOs = recomments.stream()
                .map(recomment -> {
                    RecipeBoardRecommentDTO dto = new RecipeBoardRecommentDTO();
                    dto.setRecipeBoardRecommentId(recomment.getRecipeBoardRecommentId());
                    dto.setRecipeBoardRecommentContent(recomment.getRecipeBoardRecommentContent());
                    dto.setRecipeBoardRecommentStatus(recomment.getRecipeBoardRecommentStatus());
                    dto.setRecipeBoardRecommentCreatedAt(recomment.getRecipeBoardRecommentCreatedAt());
                    dto.setUserId(recomment.getUserId());
                    dto.setRecipeBoardCommentId(recomment.getRecipeBoardComment().getRecipeBoardCommentId());
                    return dto;
                })
                .collect(Collectors.toList());

        return recommentDTOs;

    }

    // 대댓글 회원별 조회
    // 유저에 해당하는 대댓글 가져오기
    @Override
    @Transactional
    public List<RecipeBoardRecommentDTO> getRecommentsByUserId(Long userId){

        // 유저에 해당하는 대댓글 가져오기
        List<RecipeBoardRecomment> recomments = recipeBoardRecommentRepository.findAllByUserId(userId);
        if (recomments.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD_RECOMMENT);
        }

        // 3. 대댓글 엔티티 리스트를 DTO 리스트로 변환
        List<RecipeBoardRecommentDTO> recommentDTOs = recomments.stream()
                .map(recomment -> {
                    RecipeBoardRecommentDTO dto = new RecipeBoardRecommentDTO();
                    dto.setRecipeBoardRecommentId(recomment.getRecipeBoardRecommentId());
                    dto.setRecipeBoardRecommentContent(recomment.getRecipeBoardRecommentContent());
                    dto.setRecipeBoardRecommentStatus(recomment.getRecipeBoardRecommentStatus());
                    dto.setRecipeBoardRecommentCreatedAt(recomment.getRecipeBoardRecommentCreatedAt());
                    dto.setUserId(recomment.getUserId());
                    dto.setRecipeBoardCommentId(recomment.getRecipeBoardComment().getRecipeBoardCommentId());
                    return dto;
                })
                .collect(Collectors.toList());

        return recommentDTOs;
    }

}
