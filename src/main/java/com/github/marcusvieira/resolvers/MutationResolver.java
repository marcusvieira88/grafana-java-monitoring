package com.github.marcusvieira.resolvers;

import com.github.marcusvieira.dtos.*;
import com.github.marcusvieira.repositories.MessageRepository;
import com.github.marcusvieira.repositories.UserRepository;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import io.leangen.graphql.annotations.GraphQLMutation;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class MutationResolver {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final StatsDClient statsD;

    public MutationResolver(MessageRepository messageRepository, UserRepository userRepository, StatsDClient statsD) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.statsD = statsD;
    }

    @GraphQLMutation
    public User createUser(String name, String email, String password) {
        User newUser = new User(name, email, password);
        User userSaved = userRepository.saveUser(newUser);
        statsD.incrementCounter("new_users"); //shows the numbers of occurrences a event
        return userSaved;
    }

    @GraphQLMutation
    public Message createMessage(String userId, String message) {
        ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);
        Message messageSaved = messageRepository.saveMessage(new Message(now, userId, message));
        statsD.incrementCounter("new_messages"); //shows the numbers of occurrences a event
        return messageSaved;
    }
}