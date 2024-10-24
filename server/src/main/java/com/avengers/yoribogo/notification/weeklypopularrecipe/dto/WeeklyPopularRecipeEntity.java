package com.avengers.yoribogo.notification.weeklypopularrecipe.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Document(collection = "weeklyrecipe")  // MongoDB 컬렉션 이름 매핑
@Data
public class WeeklyPopularRecipeEntity {
    @Id
    private String id;  // MongoDB의 _id 필드와 매핑

    @Field("like_id")
    private String likeId;        // like_id 필드

    @Field("my_recipe_id")
    private String myRecipeId;    // my_recipe_id 필드

    @Field("user_id")
    private String userId;        // user_id 필드

    @Field("created_at")
    private ZonedDateTime createdAt;  // created_at 필드

}
