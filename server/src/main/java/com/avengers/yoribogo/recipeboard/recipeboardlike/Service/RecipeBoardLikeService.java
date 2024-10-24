package com.avengers.yoribogo.recipeboard.recipeboardlike.Service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.DeleteLikeInMongoEvent;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.InsertLikeToMongoEvent;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoard;
import com.avengers.yoribogo.recipeboard.recipeboardlike.dto.RecipeBoardLikeEntity;
import com.avengers.yoribogo.recipeboard.recipeboardlike.Repository.RecipeBoardLikeRepository;
import com.avengers.yoribogo.recipeboard.recipeboardlike.dto.LikeRequestDTO;

import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import com.avengers.yoribogo.user.domain.Tier;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.repository.UserRepository;
import com.avengers.yoribogo.user.repository.TierRepository; // TierRepository 추가
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RecipeBoardLikeService {

    private final UserRepository userRepository;
    private final RecipeBoardRepository recipeBoardRepository;
    private final RecipeBoardLikeRepository recipeBoardLikeRepository;
    private final TierRepository tierRepository;  // TierRepository 추가
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public RecipeBoardLikeService(UserRepository userRepository, RecipeBoardRepository recipeBoardRepository, RecipeBoardLikeRepository recipeBoardLikeRepository, TierRepository tierRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.recipeBoardRepository = recipeBoardRepository;
        this.recipeBoardLikeRepository = recipeBoardLikeRepository;
        this.tierRepository = tierRepository;  // TierRepository 주입
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public boolean likePost(LikeRequestDTO likeRequestDTO) {
        Long userId = likeRequestDTO.getUserId();
        Long postId = likeRequestDTO.getPostId();

        // 1. 이미 해당 유저가 해당 게시글에 좋아요를 눌렀는지 확인
        RecipeBoardLikeEntity existingLike = recipeBoardLikeRepository.findByUserUserIdAndRecipeBoardRecipeBoardId(userId, postId).orElse(null);

        // 2. 유저와 게시글 정보 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        RecipeBoard recipeBoard = recipeBoardRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE));

        long postAuthorId = recipeBoard.getUserId();  // 게시글 작성자의 ID
        UserEntity postAuthor = userRepository.findById(postAuthorId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));  // 작성자 정보 조회

        boolean isLiked = false;  // 기본 상태는 좋아요가 취소되지 않은 상태로 설정

        if (existingLike != null) {
            // 3. 좋아요가 이미 눌러져 있는 경우 -> 좋아요 취소 로직
            recipeBoardLikeRepository.delete(existingLike);
            recipeBoard.setRecipeBoardLikes(recipeBoard.getRecipeBoardLikes() - 1);
            postAuthor.setUserLikes(postAuthor.getUserLikes() - 1);  // 작성자의 좋아요 수 -1
            log.info("좋아요 취소됨. postAuthor의 좋아요 수: {}", postAuthor.getUserLikes());
            isLiked = false;  // 좋아요가 취소됨
        } else {
            // 4. 좋아요가 눌러져 있지 않은 경우 -> 좋아요 추가 로직
            recipeBoard.setRecipeBoardLikes(recipeBoard.getRecipeBoardLikes() + 1);  // 게시글의 좋아요 수 +1

            postAuthor.setUserLikes(postAuthor.getUserLikes() + 1);  // 게시글 작성자의 좋아요 수 +1
            log.info("좋아요 추가됨. postAuthor의 좋아요 수: {}", postAuthor.getUserLikes());

            RecipeBoardLikeEntity newLike = new RecipeBoardLikeEntity();
            newLike.setUser(user);
            newLike.setRecipeBoard(recipeBoard);
            newLike.setLikeCreatedAt(LocalDateTime.now());
            recipeBoardLikeRepository.save(newLike);  // 좋아요 엔티티 저장
            isLiked = true;  // 좋아요가 추가됨
        }

        // 티어 업데이트 로직 추가
        updateTierBasedOnLikes(postAuthor);

        // 5. 변경된 게시글, 유저 정보 저장
        recipeBoardRepository.save(recipeBoard);
        userRepository.save(postAuthor);  // 게시글 작성자 정보 업데이트

        // 6. 좋아요 추가/취소 시 MongoDB 비동기 이벤트 처리
        if (isLiked) {
            applicationEventPublisher.publishEvent(new InsertLikeToMongoEvent(userId.toString(), postId.toString()));
        } else {
            applicationEventPublisher.publishEvent(new DeleteLikeInMongoEvent(userId.toString(), postId.toString()));
        }

        return isLiked;  // 좋아요 상태를 반환
    }

    // 티어 업데이트 로직
    private void updateTierBasedOnLikes(UserEntity postAuthor) {
        // 티어 기준을 likes 수에 따라 조회
        Tier newTier = tierRepository.findTopByTierCriteriaLessThanEqualOrderByTierCriteriaDesc(postAuthor.getUserLikes())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TIER));

        // 새로운 티어와 기존 티어가 다를 경우에만 업데이트
        if (!newTier.equals(postAuthor.getTier())) {
            postAuthor.setTier(newTier);  // 새로운 티어로 업데이트
            log.info("postAuthor의 티어가 {}로 변경되었습니다.", newTier.getTierName());
        }
    }

}
