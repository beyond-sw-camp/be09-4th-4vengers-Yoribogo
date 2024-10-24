package com.avengers.yoribogo.recipe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RequestRecipeManualDTO {

    @JsonProperty("manual")
    private List<Map<String,String>> manual;

}
