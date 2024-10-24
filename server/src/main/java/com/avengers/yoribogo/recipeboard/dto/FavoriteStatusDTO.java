package com.avengers.yoribogo.recipeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FavoriteStatusDTO {

    @JsonProperty("exists")
    private boolean isExists;

}
