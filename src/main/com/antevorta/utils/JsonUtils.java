package com.antevorta.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    public static boolean isValid(String json) {
        try {
            new ObjectMapper().readTree(json);
        } catch (JacksonException e) {
            return false;
        }
        return true;
    }
}
