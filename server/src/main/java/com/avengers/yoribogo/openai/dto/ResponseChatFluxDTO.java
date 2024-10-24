package com.avengers.yoribogo.openai.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Getter
public class ResponseChatFluxDTO implements Serializable {
    private String id;
    private List<Choices> choices;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Choices{
        private Delta delta;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Delta{
            private String content;
        }
    }
}
