package com.github.marcusvieira.dtos;

import java.time.ZonedDateTime;

public class Message {
    private final String id;
    private final ZonedDateTime createdAt;
    private final String userId;
    private final String message;

    public Message(ZonedDateTime createdAt, String userId, String message) {
        this(null, createdAt, userId, message);
    }

    public Message(String id, ZonedDateTime createdAt, String userId, String message) {
        this.id = id;
        this.createdAt = createdAt;
        this.userId = userId;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}