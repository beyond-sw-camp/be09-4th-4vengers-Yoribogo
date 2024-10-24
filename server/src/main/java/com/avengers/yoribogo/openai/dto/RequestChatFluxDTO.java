package com.avengers.yoribogo.openai.dto;

import com.avengers.yoribogo.openai.aggregate.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestChatFluxDTO implements Serializable {
    private String model;
    private List<Message> messages;
    private Boolean stream;

    public RequestChatFluxDTO(String model, String prompt, Boolean stream) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new Message("user", prompt));
        this.stream = stream;
    }
}
