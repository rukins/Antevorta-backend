package com.inventorymanagementsystem.service;

import com.inventorymanagementsystem.exception.serverexception.EntityNotFoundException;
import com.inventorymanagementsystem.exception.serverexception.ServerException;
import com.inventorymanagementsystem.model.OnlineStoreDetails;
import com.inventorymanagementsystem.model.Product;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.onlinestore.AbstractOnlineStore;
import com.inventorymanagementsystem.onlinestore.AbstractOnlineStoreProduct;
import com.inventorymanagementsystem.repository.OnlineStoreRepository;
import com.inventorymanagementsystem.repository.ProductRepository;
import com.inventorymanagementsystem.security.encryptor.Encryptor;
import com.inventorymanagementsystem.utils.ProductJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OnlineStoreRepository onlineStoreRepository;
    private final CurrentUserService currentUserService;
    private final Encryptor encryptor;

    @Autowired
    public ProductService(ProductRepository productRepository, OnlineStoreRepository onlineStoreRepository,
                          CurrentUserService currentUserService, Encryptor encryptor) {
        this.productRepository = productRepository;
        this.onlineStoreRepository = onlineStoreRepository;
        this.currentUserService = currentUserService;
        this.encryptor = encryptor;
    }

    public List<Product> getAll() {
        User user = currentUserService.getAuthorizedUser();

        return productRepository.findAllByUser(user);
    }

    public List<Product> getAllByArbitraryStoreName(String arbitraryStoreName) {
        User user = currentUserService.getAuthorizedUser();

        return productRepository.findAllByUserAndArbitraryStoreName(user, arbitraryStoreName);
    }

    public Product getById(Long id) throws ServerException {
        User user = currentUserService.getAuthorizedUser();

        Optional<Product> product = productRepository.findByUserAndId(user, id);

        return product.orElseThrow(() -> new EntityNotFoundException(String.format("Product with id '%o' not found", id)));
    }

    public Product create(String productJson, String arbitraryStoreName) throws ServerException {
        User user = currentUserService.getAuthorizedUser();

        Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository
                .findByUserAndArbitraryStoreName(user, arbitraryStoreName);

        if (onlineStoreDetails.isPresent()) {
            AbstractOnlineStore onlineStore = AbstractOnlineStore.create(onlineStoreDetails.get().getType(),
                    onlineStoreDetails.get().getStoreName(), encryptor.decrypt(onlineStoreDetails.get().getAccessKey()));

            AbstractOnlineStoreProduct abstractOnlineStoreProduct;
            try {
                abstractOnlineStoreProduct = onlineStore.post(
                        ProductJsonUtils.getAbstractProduct(productJson, onlineStore.getType())
                );
            } catch (Exception e) {
                throw new ServerException(HttpStatus.BAD_REQUEST.toString());
            }

            return productRepository.save(Product.builder()
                                                    .product(ProductJsonUtils.getString(abstractOnlineStoreProduct))
                                                    .title(abstractOnlineStoreProduct.getTitle())
                                                    .productId(abstractOnlineStoreProduct.getId())
                                                    .onlineStoreDetails(onlineStoreDetails.get())
                                                .build()
            );
        }

        throw new EntityNotFoundException(String.format("Online store with '%s' name not found", arbitraryStoreName));
    }

    public Product update(String productJson, Long id) throws ServerException {
        User user = currentUserService.getAuthorizedUser();

        Optional<Product> product = productRepository.findByUserAndId(user, id);

        if (product.isPresent()) {
            Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository
                    .findByUserAndArbitraryStoreName(user, product.get().getArbitraryStoreName());

            if (onlineStoreDetails.isPresent()) {
                AbstractOnlineStore onlineStore = AbstractOnlineStore.create(onlineStoreDetails.get().getType(),
                        onlineStoreDetails.get().getStoreName(), encryptor.decrypt(onlineStoreDetails.get().getAccessKey()));

                AbstractOnlineStoreProduct abstractOnlineStoreProduct;
                try {
                    abstractOnlineStoreProduct = onlineStore.put(
                            ProductJsonUtils.getAbstractProduct(productJson, onlineStore.getType()),
                            product.get().getProductId()
                    );
                } catch (Exception e) {
                    throw new ServerException(HttpStatus.BAD_REQUEST.toString());
                }

                product.get().setProduct(ProductJsonUtils.getString(abstractOnlineStoreProduct));

                return productRepository.save(product.get());
            }

            throw new EntityNotFoundException(String.format("Online store with '%s' name not found",
                    product.get().getArbitraryStoreName())
            );
        }

        throw new EntityNotFoundException(String.format("Online store with id '%o' not found", id));
    }

    public void delete(Long id) throws ServerException {
        User user = currentUserService.getAuthorizedUser();

        Optional<Product> product = productRepository.findByUserAndId(user, id);

        if (product.isPresent()) {
            Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository
                    .findByUserAndArbitraryStoreName(user, product.get().getArbitraryStoreName());

            if (onlineStoreDetails.isPresent()) {
                AbstractOnlineStore onlineStore = AbstractOnlineStore.create(onlineStoreDetails.get().getType(),
                        onlineStoreDetails.get().getStoreName(), encryptor.decrypt(onlineStoreDetails.get().getAccessKey()));

                onlineStore.delete(product.get().getProductId());
                productRepository.deleteById(id);
            }

            throw new EntityNotFoundException(String.format("Online store with '%s' name not found",
                    product.get().getArbitraryStoreName())
            );
        }

        throw new EntityNotFoundException(String.format("Product with id '%o' not found", id));
    }

    public void updateProductList() {
        User user = currentUserService.getAuthorizedUser();

        List<Product> products = new ArrayList<>();

        List<OnlineStoreDetails> onlineStoreDetailsList = onlineStoreRepository.findAllByUser(user);

        for (OnlineStoreDetails onlineStoreDetails : onlineStoreDetailsList) {
            AbstractOnlineStore onlineStore = AbstractOnlineStore.create(onlineStoreDetails.getType(),
                    onlineStoreDetails.getStoreName(), encryptor.decrypt(onlineStoreDetails.getAccessKey()));

            List<AbstractOnlineStoreProduct> storeProducts = onlineStore.getAll();

            products.addAll(
                    storeProducts.stream()
                            .map(p -> Product.builder()
                                        .product(ProductJsonUtils.getString(p))
                                        .title(p.getTitle())
                                        .productId(p.getId())
                                        .onlineStoreDetails(onlineStoreDetails)
                                    .build()
                            )
                            .filter(p -> !productRepository
                                    .existsByProductIdAndArbitraryStoreName(p.getProductId(), p.getArbitraryStoreName()))
                            .toList()
            );
        }

        productRepository.saveAll(products);
    }
}
