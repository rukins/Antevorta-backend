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

    @Override
    public String toString() {
        return String.format("{\"status\": %s, \"message\": \"%s\"}", status, message);
    }
}
