package com.avengers.yoribogo.recipeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeBoardManualDTO {

    @JsonProperty("manual_step")
    private int recipeBoardManualStep;

    @JsonProperty("manual_image")
    private MultipartFile recipeBoardManualImage;

    @JsonProperty("manual_content")
    private String recipeBoardManualContent;
}
