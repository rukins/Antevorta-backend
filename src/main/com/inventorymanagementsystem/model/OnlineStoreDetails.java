package com.inventorymanagementsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "online_stores")
public class OnlineStoreDetails {
    @Id
    private String id;
    private String arbitraryStoreName;
    private OnlineStoreType type;
    private String storeName;
    private String accessKey;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public OnlineStoreDetails(String arbitraryStoreName, OnlineStoreType type, String storeName, String accessKey) {
        this.arbitraryStoreName = arbitraryStoreName;
        this.type = type;
        this.storeName = storeName;
        this.accessKey = accessKey;
    }

    public void generateId(String userEmail) {
        this.id = String.format("%s:%s", userEmail, this.arbitraryStoreName);
    }
}
