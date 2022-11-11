package com.antevorta.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;

import java.io.IOException;

public class JsonUtils {
    private static final TypeAdapter<JsonElement> adapter = new Gson().getAdapter(JsonElement.class);

    public static boolean isValid(String json) {
        try {
            adapter.fromJson(json);
        } catch (JsonSyntaxException | IOException e) {
            return false;
        }
        return true;
    }
}
