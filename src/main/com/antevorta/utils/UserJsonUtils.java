package com.antevorta.utils;

import com.antevorta.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserJsonUtils {
    public static String getString(User user) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.putIfAbsent("user", objectMapper.valueToTree(user));

        return objectNode.toString();
    }
}
