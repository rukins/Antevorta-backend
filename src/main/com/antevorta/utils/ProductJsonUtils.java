package com.antevorta.utils;

import com.antevorta.model.OnlineStoreType;
import com.antevorta.onlinestore.AbstractOnlineStoreProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class ProductJsonUtils {
    @SneakyThrows
    public static String getString(AbstractOnlineStoreProduct product) {
        return new ObjectMapper().writeValueAsString(product);
    }

    @SneakyThrows
    public static AbstractOnlineStoreProduct getAbstractProduct(String json, OnlineStoreType type) {
        return new ObjectMapper().readValue(json, AbstractOnlineStoreProduct.getClassByType(type));
    }
}
