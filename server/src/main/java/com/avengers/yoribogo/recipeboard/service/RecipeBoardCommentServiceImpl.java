package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardComment;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardCommentStatus;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardCommentDTO;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardCommentRepository;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeBoardCommentServiceImpl implements RecipeBoardCommentService {
    private final RecipeBoardCommentRepository commentRepository;
    private final RecipeBoardRepository recipeBoardRepository;
    private final ModelMapper modelMapper;

    public RecipeBoardCommentServiceImpl(RecipeBoardCommentRepository commentRepository,
                                         RecipeBoardRepository recipeBoardRepository,
                                         ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.recipeBoardRepository = recipeBoardRepository;
        this.modelMapper = modelMapper;
    }

    /* 댓글 등록 */
    @Override
    @Transactional
    public RecipeBoardCommentDTO createComment(RecipeBoardCommentDTO commentDTO) {
        RecipeBoardComment comment = modelMapper.map(commentDTO, RecipeBoardComment.class);

        // 기본 status -> ACTIVE 설정
        comment.setRecipeBoardCommentStatus(RecipeBoardCommentStatus.ACTIVE);
        // 시간 설정
        comment.setRecipeBoardCommentCreatedAt(LocalDateTime.now().withNano(0));


        if (comment.getRecipeBoardCommentContent() == null || comment.getRecipeBoardCommentContent().trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.MISSING_REQUIRED_FIELD.getMessage());
        }

        try {
            RecipeBoardComment createdComment = commentRepository.save(comment);
            return modelMapper.map(createdComment, RecipeBoardCommentDTO.class);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        }
    }



    /* 댓글 수정 */
    @Override
    @Transactional
    public RecipeBoardCommentDTO modifyComment(Long id, RecipeBoardCommentDTO commentDTO) {
        // 수정할 내용이 비어 있는지 체크
        if (commentDTO.getRecipeBoardCommentContent() == null || commentDTO.getRecipeBoardCommentContent().trim().isEmpty()) {
            throw new CommonException(ErrorCode.MISSING_REQUIRED_FIELD);
        }

        // 기존 댓글을 ID로 찾음
        RecipeBoardComment existingComment = commentRepository.findById(id).orElseThrow(
                () -> new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD_COMMENT));

        // 엔티티의 ID를 변경하지 않고, 나머지 필드만 수동으로 매핑
        existingComment.setRecipeBoardCommentContent(commentDTO.getRecipeBoardCommentContent());

        // 댓글 수정 및 저장
        try {
            RecipeBoardComment updatedComment = commentRepository.save(existingComment);
            // 수정된 엔티티를 DTO로 변환하여 반환
            return modelMapper.map(updatedComment, RecipeBoardCommentDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), e);
        }
    }



    /* 댓글 삭제 */
    @Override
    @Transactional
    public void deleteComment(Long id) {


        try {
            commentRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(ErrorCode.NOT_FOUND_RECIPE_BOARD_COMMENT.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), e);
        }
    }

    /* 댓글 게시글별 조회 */
    @Override
    public List<RecipeBoardCommentDTO> getCommentsByRecipeBoardId(Long recipeId) {
        if (!recipeBoardRepository.existsById(recipeId)) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD);
        }

        List<RecipeBoardComment> comments = commentRepository.findAllByRecipeBoardId(recipeId);
        if (comments.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD_COMMENT);
        }

        // 엔티티 리스트를 DTO 리스트로 변환
        List<RecipeBoardCommentDTO> commentDTO = comments.stream()
                .map(comment -> modelMapper.map(comment, RecipeBoardCommentDTO.class))
                .collect(Collectors.toList());

        return commentDTO;
    }


    /* 댓글 회원별 조회 */
    @Override
    public List<RecipeBoardCommentDTO> getCommentsByUserId(Long userId) {
        if (userId == null) {
            throw new CommonException(ErrorCode.MISSING_REQUIRED_FIELD);        }

        List<RecipeBoardComment> comments = commentRepository.findAllByUserId(userId);
        if (comments.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE_BOARD_COMMENT);
        }

        List<RecipeBoardCommentDTO> commentDTO = comments.stream()
                .map(comment -> modelMapper.map(comment, RecipeBoardCommentDTO.class))
                .collect(Collectors.toList());

        return commentDTO;
    }
}
