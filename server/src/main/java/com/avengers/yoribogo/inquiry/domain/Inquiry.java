package com.avengers.yoribogo.inquiry.domain;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.common.Status;
import com.avengers.yoribogo.common.Visibility;
import com.avengers.yoribogo.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "inquiry")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inquiryId;

    @Column(name = "inquiry_title")
    private String inquiryTitle;

    @Column(name = "inquiry_content")
    private String inquiryContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_status")
    private Status inquiryStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_visibility")
    private Visibility inquiryVisibility;

    @Column(name = "inquiry_created_at")
    private LocalDateTime inquiryCreatedAt;

    @Column(name = "answers")
    private Integer answers;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_status")
    private Status answerStatus;

    @OneToMany(mappedBy = "inquiryId", fetch = FetchType.LAZY)
    private List<Answer> answer;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public Inquiry(String inquiryTitle, String inquiryContent, UserEntity user) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.user = user;
    }


}
