package com.avengers.yoribogo.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestImagesDTO {
    private String prompt;
    private String size;
    private Integer n;
}
