package com.inventorymanagementsystem.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestUtils {
    public final static String AUTH_PATH = "/auth";
    public final static String LOGIN_PATH = "/login";
    public final static String LOGOUT_PATH = "/logout";
    public final static String SIGHUP_PATH = "/signup";
    public final static String ONLINE_STORES_PATH = "/onlinestores";
    public final static String PRODUCTS_PATH = "/products";

    public static String getHeadersString(HttpServletRequest request) {
        Map<String, String> headersList = new HashMap<>();

        Iterator<String> iterator = request.getHeaderNames().asIterator();
        while (iterator.hasNext()) {
            String header = iterator.next();
            headersList.put(header, request.getHeader(header));
        }

        return "Headers" + headersList;
    }
}
