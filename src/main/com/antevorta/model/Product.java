package com.antevorta.model;

import com.antevorta.onlinestore.AbstractOnlineStoreProduct;
import com.antevorta.utils.ProductJsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
            @JoinColumn(name = "ONLINE_STORE_ORDINAL", referencedColumnName = "ORDINAL"),
            @JoinColumn(name = "USER_EMAIL", referencedColumnName = "USER_EMAIL")
    })
    private OnlineStoreDetails onlineStoreDetails;

    public Product(String product, OnlineStoreDetails onlineStoreDetails) {
        this.product = product;
        this.onlineStoreDetails = onlineStoreDetails;

        setProductIdAndTitle(product);
    }

    public OnlineStoreType getType() {
        return this.onlineStoreDetails.getType();
    }

    public String getArbitraryStoreName() {
        return this.onlineStoreDetails.getArbitraryStoreName();
    }

    public void setProduct(String product) {
        this.product = product;

        setProductIdAndTitle(product);
    }

    private void setProductIdAndTitle(String product) {
        AbstractOnlineStoreProduct storeProduct = ProductJsonUtils.getAbstractProduct(product, getType());

        this.productId = storeProduct.getId();
        this.title = storeProduct.getTitle();
    }
}
