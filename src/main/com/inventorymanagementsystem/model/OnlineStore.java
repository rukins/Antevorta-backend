package com.inventorymanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OnlineStore {
    private String id;
    private String arbitraryStoreName;
    private OnlineStoreType type;
}
