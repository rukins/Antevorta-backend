package com.antevorta.utils;

import com.antevorta.model.InventoryItemDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class InventoryItemJsonUtils {
    public static String getString(List<InventoryItemDetails> inventoryItemDetailsList) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.valueToTree(inventoryItemDetailsList);

        objectNode.putArray("inventoryItems").addAll(arrayNode);

        return objectNode.toString();
    }
}
