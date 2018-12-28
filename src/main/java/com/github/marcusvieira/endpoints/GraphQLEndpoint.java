package com.github.marcusvieira.endpoints;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.marcusvieira.dtos.User;
import com.github.marcusvieira.repositories.MessageRepository;
import com.github.marcusvieira.repositories.UserRepository;
import com.github.marcusvieira.resolvers.*;
import com.github.marcusvieira.utils.SanitizedError;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;
import io.leangen.graphql.GraphQLSchemaGenerator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static graphql.schema.GraphQLSchema.newSchema;

@WebServlet(urlPatterns = "/graphql-api")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private static final UserRepository userRepository;
    private static final MessageRepository messageRepository;
    private static final StatsDClient statsD;

    static {
        //Change to `new MongoClient("mongodb://<host>:<port>/graphql-java")`
        //if you don't have Mongo running locally on port 27017
        MongoDatabase mongo = new MongoClient().getDatabase("graphql-api");
        userRepository = new UserRepository(mongo.getCollection("users"));
        messageRepository = new MessageRepository(mongo.getCollection("messages"));
        //connect to StatsD server
        statsD = new NonBlockingStatsDClient("Graphql.java.example.api", "localhost", 8124);
    }

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        QueryResolver queryResolver = new QueryResolver(userRepository, messageRepository, statsD);
        MutationResolver mutationResolver = new MutationResolver(messageRepository, userRepository, statsD);
        MessageResolver messageResolver = new MessageResolver(userRepository);

        return new GraphQLSchemaGenerator()
                        .withOperationsFromSingletons(
                                queryResolver,
                                messageResolver,
                                mutationResolver)
                        .generate();
    }
}