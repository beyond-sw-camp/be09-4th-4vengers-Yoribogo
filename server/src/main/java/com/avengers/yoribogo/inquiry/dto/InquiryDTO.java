package com.avengers.yoribogo.inquiry.dto;

import com.avengers.yoribogo.common.Status;
import com.avengers.yoribogo.common.Visibility;
import com.avengers.yoribogo.inquiry.domain.Inquiry;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InquiryDTO {
    private Integer inquiryId;
    private String inquiryTitle;
    private String inquiryContent;
    private Status inquiryStatus;
    private Visibility inquiryVisibility;
    private LocalDateTime inquiryCreatedAt;
    private Integer answers;
    private Status answerStatus;
    private List<Inquiry> answer;
    private UserDTO user;

    public InquiryDTO(String inquiryTitle, String inquiryContent, UserDTO user) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.user = user;
    }

    public InquiryDTO(String inquiryTitle, String inquiryContent, Visibility inquiryVisibility, UserDTO user) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryVisibility = inquiryVisibility;
        this.user = user;
    }

    public InquiryDTO(Integer inquiryId,
                      String inquiryTitle,
                      String inquiryContent,
                      Status inquiryStatus,
                      Visibility inquiryVisibility,
                      LocalDateTime inquiryCreatedAt,
                      int answers,
                      Status answerStatus,
                      UserDTO user) {
        this.inquiryId = inquiryId;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryStatus = inquiryStatus;
        this.inquiryVisibility = inquiryVisibility;
        this.inquiryCreatedAt = inquiryCreatedAt;
        this.answers = answers;
        this.answerStatus = answerStatus;
        this.user = user;
    }
}
