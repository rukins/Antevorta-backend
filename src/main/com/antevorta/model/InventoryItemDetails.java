package com.antevorta.model;

import com.antevorta.onlinestore.AbstractOnlineStoreProduct;
import com.antevorta.utils.ProductJsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Entity
@Table(name = "inventory_items")
public class InventoryItemDetails {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String inventoryId;

    @Getter
    private String title;

    @JsonRawValue
    @Getter
    @Column(columnDefinition = "TEXT")
    private String inventoryItem;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
            @JoinColumn(name = "ONLINE_STORE_ORDINAL", referencedColumnName = "ORDINAL"),
            @JoinColumn(name = "USER_EMAIL", referencedColumnName = "USER_EMAIL")
    })
    private OnlineStoreDetails onlineStoreDetails;

    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "merged_inventory_items")
    private List<InventoryItemDetails> mergedInventoryItems;

    private boolean isLinker = false;

    public InventoryItemDetails(String product, OnlineStoreDetails onlineStoreDetails) {
        setOnlineStoreDetails(onlineStoreDetails);
        setInventoryItem(product);
    }

    public InventoryItemDetails(String inventoryItem, List<InventoryItemDetails> mergedInventoryItems, User user) {
        this.inventoryItem = inventoryItem;
        this.mergedInventoryItems = mergedInventoryItems;
        this.user = user;
        this.isLinker = true;
    }

    public InventoryItemDetails(Long id, String inventoryId, String title, String inventoryItem, OnlineStoreDetails onlineStoreDetails) {
        this.id = id;
        this.inventoryId = inventoryId;
        this.title = title;
        this.inventoryItem = inventoryItem;
        this.onlineStoreDetails = onlineStoreDetails;
    }

    public InventoryItemType getType() {
        if (this.isLinker) {
            return InventoryItemType.PRODUCT_LINKER;
        }

        return InventoryItemType.valueOf(this.onlineStoreDetails.getType().toString());
    }

    public String getArbitraryStoreName() {
        if (this.onlineStoreDetails == null) {
            return null;
        }

        return this.onlineStoreDetails.getArbitraryStoreName();
    }

    @JsonIgnore
    public boolean isLinker() {
        return isLinker;
    }

    public List<InventoryItemDetails> getMergedInventoryItems() {
        if (this.isLinker()) {
            return this.mergedInventoryItems;
        }

        return null;
    }

    public void setOnlineStoreDetails(OnlineStoreDetails onlineStoreDetails) {
        this.onlineStoreDetails = onlineStoreDetails;
        this.user = onlineStoreDetails.getUser();
    }

    public void setInventoryItem(String inventoryItem) {
        this.inventoryItem = inventoryItem;

        if (!this.isLinker) {
            setInventoryIdAndTitle(inventoryItem);
        }
    }

    private void setInventoryIdAndTitle(String product) {
        AbstractOnlineStoreProduct storeProduct = ProductJsonUtils
                .getAbstractProduct(product, OnlineStoreType.valueOf(this.getType().toString()));

        this.inventoryId = storeProduct.getId();
        this.title = storeProduct.getTitle();
    }
}
