package com.antevorta.utils;

import com.google.gson.*;
import com.antevorta.model.OnlineStoreType;
import com.antevorta.model.Product;
import com.antevorta.onlinestore.AbstractOnlineStoreProduct;

import java.util.List;

public class ProductJsonUtils {
    private final static Gson gson = new Gson();

    public static String getString(Product product) {
        return getJsonObject(product).toString();
    }

    public static String getString(List<Product> products) {
        JsonObject jsonObject = new JsonObject();

        JsonArray productJsonArray = new JsonArray();
        for (Product product : products) {
            productJsonArray.add(getJsonObject(product));
        }

        jsonObject.add("products", productJsonArray);

        return jsonObject.toString();
    }

    public static String getString(AbstractOnlineStoreProduct product) {
        return gson.toJson(product);
    }

    public static AbstractOnlineStoreProduct getAbstractProduct(String json, OnlineStoreType type) {
        return gson.fromJson(json, AbstractOnlineStoreProduct.getClassByType(type));
    }

    private static JsonObject getJsonObject(Product product) {
        JsonObject productJson = new JsonObject();

        productJson.addProperty("id", product.getId());
        productJson.addProperty("productId", product.getProductId());
        productJson.addProperty("title", product.getTitle());
        productJson.add("product", JsonParser.parseString(product.getProduct()));

        if (product.isLinker()) {
            JsonArray mergedProductJsonArray = new JsonArray();
            for (Product mergedProduct : product.getMergedProducts()) {
                mergedProductJsonArray.add(getJsonObject(mergedProduct));
            }
            productJson.add("mergedProducts", mergedProductJsonArray);
        }

        productJson.addProperty("type", product.getType().toString());

        if (!product.isLinker()) {
            productJson.addProperty("arbitraryStoreName", product.getArbitraryStoreName());
        }

        return productJson;
    }
}
