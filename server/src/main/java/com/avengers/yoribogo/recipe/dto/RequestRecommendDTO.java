package com.avengers.yoribogo.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RequestRecommendDTO {
    private String first;
    private String second;
    private String third;
    private String fourth;
    private String fifth;
}
