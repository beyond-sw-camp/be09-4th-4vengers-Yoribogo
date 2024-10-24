package com.avengers.yoribogo.choice.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "choice")
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int choiceId;

    @Column(name = "choice_image")
    private String choiceImage;

    @Column(name = "choice_content")
    private String choiceContent;

    @Column(name = "main_question_id")
    private int mainQuestionId;

    public Choice(String choiceImage, String choiceContent, int mainQuestionId) {
        this.choiceImage = choiceImage;
        this.choiceContent = choiceContent;
        this.mainQuestionId = mainQuestionId;
    }
}
