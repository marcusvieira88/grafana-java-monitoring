package com.github.marcusvieira.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.github.marcusvieira.dtos.Message;
import com.github.marcusvieira.dtos.User;
import com.github.marcusvieira.repositories.UserRepository;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;

public class MessageResolver implements GraphQLResolver<Message> {

    private final UserRepository userRepository;

    public MessageResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GraphQLQuery
    public User user(@GraphQLContext Message message) {
        if (message.getUserId() == null) {
            return null;
        }
        return userRepository.findById(message.getUserId());
    }
}
