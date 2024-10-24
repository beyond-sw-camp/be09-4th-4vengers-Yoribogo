package com.avengers.yoribogo.choice.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChoiceDTO {
    private int choiceId;
    private String choiceImage;
    private String choiceContent;
    private int mainQuestionId;

    public ChoiceDTO(String choiceImage, String choiceContent, int mainQuestionId) {
        this.choiceImage = choiceImage;
        this.choiceContent = choiceContent;
        this.mainQuestionId = mainQuestionId;
    }
}
