package com.antevorta.utils;

import com.antevorta.model.OnlineStoreDetails;
import com.antevorta.model.OnlineStoreType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class OnlineStoreJsonUtils {
    public static String getString(OnlineStoreDetails onlineStore) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.putIfAbsent("onlineStore", objectMapper.valueToTree(onlineStore));

        return objectNode.toString();
    }

    public static String getString(List<OnlineStoreDetails> onlineStores) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.valueToTree(onlineStores);

        objectNode.putArray("onlineStores").addAll(arrayNode);

        return objectNode.toString();
    }

    public static String getString(List<OnlineStoreDetails> onlineStores, OnlineStoreType type) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.valueToTree(onlineStores);

        objectNode.putArray("onlineStores").addAll(arrayNode);
        objectNode.put("type", type.toString());

        return objectNode.toString();
    }
}
