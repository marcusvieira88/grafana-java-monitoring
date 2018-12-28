package com.github.marcusvieira.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageFilter {

    private String messageContains;

    @JsonProperty("message_contains")  //name used in the query
    public String getMessageContains() {
        return messageContains;
    }

    public void setMessageContains(String messageContains) {
        this.messageContains = messageContains;
    }

}
