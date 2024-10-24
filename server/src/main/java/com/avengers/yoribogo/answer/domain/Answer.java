package com.avengers.yoribogo.answer.domain;

import com.avengers.yoribogo.common.Role;
import com.avengers.yoribogo.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int answerId;

    @Column(name = "answer_content")
    private String answerContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "writer_type")
    private Role writerType;

    @Column(name = "answer_created_at")
    private LocalDateTime answerCreatedAt;

    @Column(name = "inquiry_id")
    private int inquiryId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;

    public Answer(String answerContent, Role writerType,int inquiryId, UserEntity user) {
        this.answerContent = answerContent;
        this.writerType = writerType;
        this.inquiryId = inquiryId;
        this.user = user;
    }
}
