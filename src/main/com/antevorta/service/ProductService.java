package com.antevorta.service;

import com.antevorta.exception.serverexception.EntityNotFoundException;
import com.antevorta.exception.serverexception.JsonSyntaxException;
import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.OnlineStoreDetails;
import com.antevorta.model.Product;
import com.antevorta.model.User;
import com.antevorta.onlinestore.AbstractOnlineStore;
import com.antevorta.onlinestore.AbstractOnlineStoreProduct;
import com.antevorta.repository.OnlineStoreRepository;
import com.antevorta.repository.ProductRepository;
import com.antevorta.security.encryptor.Encryptor;
import com.antevorta.utils.JsonUtils;
import com.antevorta.utils.ProductJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        if (!JsonUtils.isValid(productJson)) {
            throw new JsonSyntaxException("Json is invalid");
        }

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

            return productRepository.save(
                    new Product(ProductJsonUtils.getString(abstractOnlineStoreProduct), onlineStoreDetails.get())
            );
        }

        throw new EntityNotFoundException(String.format("Online store with '%s' name not found", arbitraryStoreName));
    }

    public Product update(String productJson, Long id) throws ServerException {
        if (!JsonUtils.isValid(productJson)) {
            throw new JsonSyntaxException("Json is invalid");
        }

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

        throw new EntityNotFoundException(String.format("Product with id '%o' not found", id));
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

                return;
            }

            throw new EntityNotFoundException(String.format("Online store with '%s' name not found",
                    product.get().getArbitraryStoreName())
            );
        }

        throw new EntityNotFoundException(String.format("Product with id '%o' not found", id));
    }

    public Product merge(String productJson, String productIds) throws ServerException {
        if (!JsonUtils.isValid(productJson)) {
            throw new JsonSyntaxException("Json is invalid");
        }

        User user = currentUserService.getAuthorizedUser();

        List<Long> productIdsList = Arrays.stream(productIds.split(","))
                .map(id -> Long.parseLong(id.trim()))
                .toList();

        List<Product> mergedProducts = new ArrayList<>();
        for (Long id : productIdsList) {
            Optional<Product> product = productRepository.findByUserAndId(user, id);

            if (product.isPresent() && !product.get().isLinker()) {
                mergedProducts.add(product.get());
            }
        }

        return productRepository.save(
                new Product(productJson, mergedProducts, user)
        );
    }

    public void updateProductList() {
        User user = currentUserService.getAuthorizedUser();

        List<Product> products = new ArrayList<>();

        List<OnlineStoreDetails> onlineStoreDetailsList = onlineStoreRepository.findAllByUser(user);

        for (OnlineStoreDetails onlineStoreDetails : onlineStoreDetailsList) {
            AbstractOnlineStore onlineStore = AbstractOnlineStore.create(onlineStoreDetails.getType(),
                    onlineStoreDetails.getStoreName(), encryptor.decrypt(onlineStoreDetails.getAccessKey()));

            Map<Long, String> productIdAndProductMap = onlineStore.getAll().stream()
                    .collect(Collectors.toMap(AbstractOnlineStoreProduct::getId, ProductJsonUtils::getString));
            List<Long> productIdsFromRepo = productRepository
                    .findAllProductIdsByUserAndArbitraryStoreName(user, onlineStoreDetails.getArbitraryStoreName());

            // delete if product doesn't exist in online store
            productIdsFromRepo.stream().filter(pId -> !productIdAndProductMap.containsKey(pId)).forEach(
                    pId -> productRepository
                            .deleteByUserAndProductIdAndArbitraryStoreName(user, pId, onlineStoreDetails.getArbitraryStoreName())
            );

            List<Product> productsToSave = new ArrayList<>();

            // update if product json is updated and add to productsToSave if it doesn't exist
            productIdAndProductMap.keySet().forEach(
                    id -> {
                        Optional<Product> product = productRepository
                                .findByUserAndProductIdAndArbitraryStoreName(user, id, onlineStoreDetails.getArbitraryStoreName());

                        if (product.isPresent()) {
                            if (!product.get().getProduct().equals(productIdAndProductMap.get(id))) {
                                product.get().setProduct(productIdAndProductMap.get(id));

                                productsToSave.add(product.get());
                            }
                        }

                        if (!productRepository
                                .existsByUserAndProductIdAndArbitraryStoreName(user, id, onlineStoreDetails.getArbitraryStoreName())) {
                            productsToSave.add(new Product(productIdAndProductMap.get(id), onlineStoreDetails));
                        }
                    }
            );

            products.addAll(productsToSave);
        }

        productRepository.saveAll(products);
    }
}
