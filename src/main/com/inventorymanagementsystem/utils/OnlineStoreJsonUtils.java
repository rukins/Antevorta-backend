package com.inventorymanagementsystem.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inventorymanagementsystem.model.OnlineStoreType;

public class OnlineStoreJsonUtils {
    private final static Gson gson = new Gson();

    public static String getString(Object onlineStoreJson) {
        JsonObject jsonObject = new JsonObject();

        JsonElement jsonElement = JsonParser.parseString(gson.toJson(onlineStoreJson));

        jsonObject.add("onlineStores", jsonElement);

        return jsonObject.toString();
    }

    public static String getString(Object onlineStoreJson, OnlineStoreType type) {
        JsonObject jsonObject = new JsonObject();

        JsonElement jsonElement = JsonParser.parseString(gson.toJson(onlineStoreJson));

        jsonObject.add("onlineStores", jsonElement);
        jsonObject.addProperty("type", type.toString());

        return jsonObject.toString();
    }
}
