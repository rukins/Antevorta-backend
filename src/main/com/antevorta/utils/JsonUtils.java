package com.antevorta.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonUtils {
    public static boolean isValid(String json) {
        try {
            new ObjectMapper().readTree(json);
        } catch (JacksonException e) {
            return false;
        }
        return true;
    }

    @SneakyThrows
    public static String convertToString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T convertToObject(String json, Class<T> objectClass) {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(json, objectClass);
    }
}
