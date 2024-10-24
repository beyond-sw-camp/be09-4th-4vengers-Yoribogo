package com.avengers.yoribogo.mainquestion.domain;

import com.avengers.yoribogo.choice.domain.Choice;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "main_question")
public class MainQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mainQuestionId;

    @Column(name = "main_question_content")
    private String mainQuestionContent;

    @Column(name = "user_id")
    private int userId;

    @OneToMany(mappedBy = "mainQuestionId")
    private List<Choice> choice;

    public MainQuestion(String mainQuestionContent, int userId) {
        this.mainQuestionContent = mainQuestionContent;
        this.userId = userId;
    }

    public MainQuestion(int mainQuestionId, String mainQuestionContent, int userId) {
        this.mainQuestionId = mainQuestionId;
        this.mainQuestionContent = mainQuestionContent;
        this.userId = userId;
    }
}
