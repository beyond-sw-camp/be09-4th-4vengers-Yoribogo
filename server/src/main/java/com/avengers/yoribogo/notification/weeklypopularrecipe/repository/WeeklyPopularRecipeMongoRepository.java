package com.avengers.yoribogo.notification.weeklypopularrecipe.repository;

import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.WeeklyPopularRecipeEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyPopularRecipeMongoRepository extends MongoRepository<WeeklyPopularRecipeEntity, String> {
    // 컬렉션 내의 모든 데이터를 조회하는 메서드
    List<WeeklyPopularRecipeEntity> findAll();

    // likeId 필드를 기준으로 가장 높은 값을 가진 데이터를 하나 반환
    Optional<List<WeeklyPopularRecipeEntity>> findByCreatedAtAfter(LocalDateTime date);

    // 좋아요 삭제 레시피
    void deleteByUserIdAndMyRecipeId(String userId, String myRecipeId);

    // 일주일 이내의 좋아요 데이터를 가져와 게시글별로 그룹핑하고, 좋아요 수를 카운트한 후 상위 3개 게시글 선택
    @Aggregation(pipeline = {
            "{ $match: { 'created_at': { $gte: ?0 } } }",  // 일주일 이내의 데이터를 필터링
            "{ $group: { _id: '$my_recipe_id', count: { $sum: 1 }, like_id: { $first: '$like_id' }, my_recipe_id: { $first: '$my_recipe_id' }, user_id: { $first: '$user_id' }, created_at: { $first: '$created_at' } } }",  // 게시글별로 그룹핑하고 좋아요 수 카운트 및 필요한 필드 포함
            "{ $sort: { count: -1 } }",  // 좋아요 수 기준으로 내림차순 정렬
            "{ $limit: 3 }",  // 상위 3개 선택
            "{ $project: { _id: 0, likeId: '$like_id', myRecipeId: '$my_recipe_id', userId: '$user_id', createdAt: '$created_at', count: '$count' } }"  // 필요한 필드만 반환
    })
    List<WeeklyPopularRecipeEntity> findTop3LikedRecipesWithinLastWeek(LocalDateTime oneWeekAgo);
}