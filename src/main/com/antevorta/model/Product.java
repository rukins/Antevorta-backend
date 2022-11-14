package com.antevorta.model;

import com.antevorta.onlinestore.AbstractOnlineStoreProduct;
import com.antevorta.utils.ProductJsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

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

    @Getter
    @ManyToMany
    private List<Product> mergedProducts = null;

    @Getter
    @Setter
    private boolean isLinker = false;

    public Product(String product, OnlineStoreDetails onlineStoreDetails) {
        this.product = product;
        this.onlineStoreDetails = onlineStoreDetails;

        setProductIdAndTitle(product);
    }

    public Product(Long id, Long productId, String title, String product, OnlineStoreDetails onlineStoreDetails) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.product = product;
        this.onlineStoreDetails = onlineStoreDetails;
    }

    public Product(String product, List<Product> mergedProducts) {
        this.product = product;
        this.mergedProducts = mergedProducts;
        this.isLinker = true;
    }

    public ProductType getType() {
        if (this.isLinker) {
            return ProductType.PRODUCT_LINKER;
        }

        return ProductType.valueOf(this.onlineStoreDetails.getType().toString());
    }

    public String getArbitraryStoreName() {
        return this.onlineStoreDetails.getArbitraryStoreName();
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
