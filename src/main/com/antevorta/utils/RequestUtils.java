package com.antevorta.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestUtils {
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
