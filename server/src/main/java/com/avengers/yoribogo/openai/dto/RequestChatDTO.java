package com.avengers.yoribogo.openai.dto;

import com.avengers.yoribogo.openai.aggregate.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestChatDTO {
    private String model;
    private List<Message> messages;

    public RequestChatDTO(String model, String prompt) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
