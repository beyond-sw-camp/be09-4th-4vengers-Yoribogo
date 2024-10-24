package com.avengers.yoribogo.openai.dto;

import com.avengers.yoribogo.openai.aggregate.ImageData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseImagesDTO {
    private List<ImageData> data;
}
