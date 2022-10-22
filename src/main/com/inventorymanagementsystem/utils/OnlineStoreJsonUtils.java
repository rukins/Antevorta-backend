package com.inventorymanagementsystem.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inventorymanagementsystem.model.OnlineStoreDetails;
import com.inventorymanagementsystem.model.OnlineStoreType;

import java.util.List;

public class OnlineStoreJsonUtils {
    private final static Gson gson = new Gson();

    public static String getString(OnlineStoreDetails onlineStore) {
        JsonObject jsonObject = new JsonObject();

        JsonElement jsonElement = JsonParser.parseString(gson.toJson(onlineStore));

        jsonObject.add("onlineStore", jsonElement);

        return jsonObject.toString();
    }

    public static String getString(List<OnlineStoreDetails> onlineStores) {
        JsonObject jsonObject = new JsonObject();

        JsonElement jsonElement = JsonParser.parseString(gson.toJson(onlineStores));

        jsonObject.add("onlineStores", jsonElement);

        return jsonObject.toString();
    }

    public static String getString(List<OnlineStoreDetails> onlineStoreJson, OnlineStoreType type) {
        JsonObject jsonObject = new JsonObject();

        JsonElement jsonElement = JsonParser.parseString(gson.toJson(onlineStoreJson));

        jsonObject.add("onlineStores", jsonElement);
        jsonObject.addProperty("type", type.toString());

        return jsonObject.toString();
    }
}
