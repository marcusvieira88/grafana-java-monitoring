package com.github.marcusvieira.resolvers;

import com.github.marcusvieira.dtos.Message;
import com.github.marcusvieira.dtos.User;
import com.github.marcusvieira.filter.MessageFilter;
import com.github.marcusvieira.filter.UserFilter;
import com.github.marcusvieira.repositories.MessageRepository;
import com.github.marcusvieira.repositories.UserRepository;
import com.timgroup.statsd.StatsDClient;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;

import java.util.List;
public class QueryResolver {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final StatsDClient statsD;

    public QueryResolver(UserRepository userRepository, MessageRepository messageRepository, StatsDClient statsD) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.statsD = statsD;
    }

    @GraphQLQuery
    public User userById(String id) {
        User user = userRepository.findById(id);
        statsD.recordSetEvent("findUserById", user.getId()); //id message reports a number of unique occurrences of a metric, unique identifier
        return user;
    }

    @GraphQLQuery
    public Message messageById(String id) {
        Message message = messageRepository.findById(id);
        statsD.recordSetEvent("findMessageById", message.getId()); //id message reports a number of unique occurrences of a metric, unique identifier
        return message;
    }

    @GraphQLQuery
    public List<User> allUsers(UserFilter filter, @GraphQLArgument(name = "skip", defaultValue = "0") Number skip,
                               @GraphQLArgument(name = "first", defaultValue = "0") Number first) {
        return userRepository.getAllUsers(filter, skip.intValue(), first.intValue());
    }

    @GraphQLQuery
    public List<Message> allMessages(MessageFilter filter, @GraphQLArgument(name = "skip", defaultValue = "0") Number skip,
                                     @GraphQLArgument(name = "first", defaultValue = "0") Number first) {
        return messageRepository.getAllMessages(filter, skip.intValue(), first.intValue());
    }
}
