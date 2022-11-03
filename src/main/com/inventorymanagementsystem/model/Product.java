package com.inventorymanagementsystem.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Getter
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    private Long productId;

    @Getter
    private String title;

    @Getter
    @Column(columnDefinition = "TEXT")
    private String product;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ONLINE_STORE_ORDINAL", referencedColumnName = "ORDINAL"),
            @JoinColumn(name = "USER_EMAIL", referencedColumnName = "USER_EMAIL")
    })
    private OnlineStoreDetails onlineStoreDetails;

    public OnlineStoreType getType() {
        return this.onlineStoreDetails.getType();
    }

    public String getArbitraryStoreName() {
        return this.onlineStoreDetails.getArbitraryStoreName();
    }
}
