package com.github.marcusvieira.repositories;

import com.github.marcusvieira.dtos.Message;
import com.github.marcusvieira.filter.MessageFilter;
import com.github.marcusvieira.utils.Scalars;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class MessageRepository {

    private final MongoCollection<Document> messages;

    public MessageRepository(MongoCollection<Document> messages) {
        this.messages = messages;
    }

    public Message findById(String id) {
        Document doc = messages.find(eq("_id", new ObjectId(id))).first();
        return message(doc);
    }

    public List<Message> getAllMessages(MessageFilter filter, int skip, int first) {
        Optional<Bson> mongoFilter = Optional.ofNullable(filter).map(this::buildFilter);

        List<Message> allMessages = new ArrayList<>();
        FindIterable<Document> documents = mongoFilter.map(messages::find).orElseGet(messages::find);
        for (Document doc : documents.skip(skip).limit(first)) {
            allMessages.add(message(doc));
        }
        return allMessages;
    }

    //builds a Bson from a MessageFilter
    private Bson buildFilter(MessageFilter filter) {
        String messagePattern = filter.getMessageContains();
        Bson messageCondition = null;
        Bson urlCondition = null;
        if (messagePattern != null && !messagePattern.isEmpty()) {
            messageCondition = regex("message", ".*" + messagePattern + ".*", "i");
        }
        return messageCondition;
    }

    public Message saveMessage(Message message) {
        Document doc = new Document();
        doc.append("userId", message.getUserId());
        doc.append("createdAt", Scalars.dateTime.getCoercing().serialize(message.getCreatedAt()));
        doc.append("message", message.getMessage());
        messages.insertOne(doc);
        return new Message(
                doc.get("_id").toString(),
                message.getCreatedAt(),
                message.getUserId(),
                message.getMessage());
    }

    private Message message(Document doc) {
        return new Message(
                doc.get("_id").toString(),
                ZonedDateTime.parse(doc.getString("createdAt")),
                doc.getString("userId"),
                doc.getString("message")
        );
    }
}
