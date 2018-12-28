package com.github.marcusvieira.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserFilter {

    private String nameContains;

    @JsonProperty("name_contains") //name used in the query
    public String getNameContains() {
        return nameContains;
    }

    public void getNameContains(String messageContains) {
        this.nameContains = nameContains;
    }

}
