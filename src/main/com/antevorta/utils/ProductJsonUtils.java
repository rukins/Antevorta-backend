package com.antevorta.utils;

import com.antevorta.model.OnlineStoreType;
import com.antevorta.model.Product;
import com.antevorta.onlinestore.AbstractOnlineStoreProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.util.List;

public class ProductJsonUtils {
    public static String getString(List<Product> products) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();
        ArrayNode arrayNode = objectMapper.valueToTree(products);

        objectNode.putArray("products").addAll(arrayNode);

        return objectNode.toString();
    }

    @SneakyThrows
    public static String getString(AbstractOnlineStoreProduct product) {
        return new ObjectMapper().writeValueAsString(product);
    }

    @SneakyThrows
    public static AbstractOnlineStoreProduct getAbstractProduct(String json, OnlineStoreType type) {
        return new ObjectMapper().readValue(json, AbstractOnlineStoreProduct.getClassByType(type));
    }
}
