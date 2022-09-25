package com.inventorymanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseBody {
    private Integer status;
    private String message;
    private String path;

    @Override
    public String toString() {
        return String.format("{\"status\": %s, \"message\": \"%s\", \"path\": \"%s\"}", status, message, path);
    }
}
