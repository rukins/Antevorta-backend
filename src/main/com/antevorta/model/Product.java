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

    @JsonRawValue
    @Getter
    @Column(columnDefinition = "TEXT")
    private String product;

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
    private List<Product> mergedProducts;

    private boolean isLinker = false;

    public Product(String product, OnlineStoreDetails onlineStoreDetails) {
        setOnlineStoreDetails(onlineStoreDetails);
        setProduct(product);
    }

    public Product(String product, List<Product> mergedProducts, User user) {
        this.product = product;
        this.mergedProducts = mergedProducts;
        this.user = user;
        this.isLinker = true;
    }

    public Product(Long id, Long productId, String title, String product, OnlineStoreDetails onlineStoreDetails) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.product = product;
        this.onlineStoreDetails = onlineStoreDetails;
    }

    public ProductType getType() {
        if (this.isLinker) {
            return ProductType.PRODUCT_LINKER;
        }

        return ProductType.valueOf(this.onlineStoreDetails.getType().toString());
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

    public List<Product> getMergedProducts() {
        if (this.isLinker()) {
            return this.mergedProducts;
        }

        return null;
    }

    public void setOnlineStoreDetails(OnlineStoreDetails onlineStoreDetails) {
        this.onlineStoreDetails = onlineStoreDetails;
        this.user = onlineStoreDetails.getUser();
    }

    public void setProduct(String product) {
        this.product = product;

        if (!this.isLinker) {
            setProductIdAndTitle(product);
        }
    }

    private void setProductIdAndTitle(String product) {
        AbstractOnlineStoreProduct storeProduct = ProductJsonUtils
                .getAbstractProduct(product, OnlineStoreType.valueOf(this.getType().toString()));

        this.productId = storeProduct.getId();
        this.title = storeProduct.getTitle();
    }
}
