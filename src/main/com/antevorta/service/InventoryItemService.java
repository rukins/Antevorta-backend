package com.antevorta.service;

import com.antevorta.exception.serverexception.EntityNotFoundException;
import com.antevorta.exception.serverexception.JsonSyntaxException;
import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.InventoryItemDetails;
import com.antevorta.model.OnlineStoreDetails;
import com.antevorta.model.User;
import com.antevorta.onlinestore.AbstractOnlineStore;
import com.antevorta.onlinestore.AbstractOnlineStoreProduct;
import com.antevorta.repository.OnlineStoreRepository;
import com.antevorta.repository.InventoryItemRepository;
import com.antevorta.security.encryptor.Encryptor;
import com.antevorta.utils.JsonUtils;
import com.antevorta.utils.ProductJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryItemService {
    private final InventoryItemRepository inventoryItemRepository;
    private final OnlineStoreRepository onlineStoreRepository;
    private final CurrentUserService currentUserService;
    private final Encryptor encryptor;

    @Autowired
    public InventoryItemService(InventoryItemRepository inventoryItemRepository, OnlineStoreRepository onlineStoreRepository,
                                CurrentUserService currentUserService, Encryptor encryptor) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.onlineStoreRepository = onlineStoreRepository;
        this.currentUserService = currentUserService;
        this.encryptor = encryptor;
    }

    public List<InventoryItemDetails> getAll() {
        User user = currentUserService.getAuthorizedUser();

        return inventoryItemRepository.findAllByUser(user);
    }

    public List<InventoryItemDetails> getAllByArbitraryStoreName(String arbitraryStoreName) {
        User user = currentUserService.getAuthorizedUser();

        return inventoryItemRepository.findAllByUserAndArbitraryStoreName(user, arbitraryStoreName);
    }

    public InventoryItemDetails getById(Long id) throws ServerException {
        User user = currentUserService.getAuthorizedUser();

        Optional<InventoryItemDetails> inventoryItemDetails = inventoryItemRepository.findByUserAndId(user, id);

        return inventoryItemDetails.orElseThrow(
                () -> new EntityNotFoundException(String.format("Inventory item with id '%o' not found", id))
        );
    }

    public InventoryItemDetails create(String productJson, String arbitraryStoreName) throws ServerException {
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

            return inventoryItemRepository.save(
                    new InventoryItemDetails(ProductJsonUtils.getString(abstractOnlineStoreProduct), onlineStoreDetails.get())
            );
        }

        throw new EntityNotFoundException(String.format("Online store with '%s' name not found", arbitraryStoreName));
    }

    public InventoryItemDetails update(String productJson, Long id) throws ServerException {
        if (!JsonUtils.isValid(productJson)) {
            throw new JsonSyntaxException("Json is invalid");
        }

        User user = currentUserService.getAuthorizedUser();

        Optional<InventoryItemDetails> inventoryItemDetails = inventoryItemRepository.findByUserAndId(user, id);

        if (inventoryItemDetails.isPresent()) {
            Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository
                    .findByUserAndArbitraryStoreName(user, inventoryItemDetails.get().getArbitraryStoreName());

            if (onlineStoreDetails.isPresent()) {
                AbstractOnlineStore onlineStore = AbstractOnlineStore.create(onlineStoreDetails.get().getType(),
                        onlineStoreDetails.get().getStoreName(), encryptor.decrypt(onlineStoreDetails.get().getAccessKey()));

                AbstractOnlineStoreProduct abstractOnlineStoreProduct;
                try {
                    abstractOnlineStoreProduct = onlineStore.put(
                            ProductJsonUtils.getAbstractProduct(productJson, onlineStore.getType()),
                            inventoryItemDetails.get().getInventoryId()
                    );
                } catch (Exception e) {
                    throw new ServerException(HttpStatus.BAD_REQUEST.toString());
                }

                inventoryItemDetails.get().setInventoryItem(ProductJsonUtils.getString(abstractOnlineStoreProduct));

                return inventoryItemRepository.save(inventoryItemDetails.get());
            }

            throw new EntityNotFoundException(String.format("Online store with '%s' name not found",
                    inventoryItemDetails.get().getArbitraryStoreName())
            );
        }

        throw new EntityNotFoundException(String.format("Inventory item with id '%o' not found", id));
    }

    public void delete(Long id) throws ServerException {
        User user = currentUserService.getAuthorizedUser();

        Optional<InventoryItemDetails> inventoryItemDetails = inventoryItemRepository.findByUserAndId(user, id);

        if (inventoryItemDetails.isPresent()) {
            Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository
                    .findByUserAndArbitraryStoreName(user, inventoryItemDetails.get().getArbitraryStoreName());

            if (onlineStoreDetails.isPresent()) {
                AbstractOnlineStore onlineStore = AbstractOnlineStore.create(onlineStoreDetails.get().getType(),
                        onlineStoreDetails.get().getStoreName(), encryptor.decrypt(onlineStoreDetails.get().getAccessKey()));

                onlineStore.delete(inventoryItemDetails.get().getInventoryId());
                inventoryItemRepository.deleteById(id);

                return;
            }

            throw new EntityNotFoundException(String.format("Online store with '%s' name not found",
                    inventoryItemDetails.get().getArbitraryStoreName())
            );
        }

        throw new EntityNotFoundException(String.format("Inventory item with id '%o' not found", id));
    }

    public InventoryItemDetails merge(String productJson, String inventoryIds) throws ServerException {
        if (!JsonUtils.isValid(productJson)) {
            throw new JsonSyntaxException("Json is invalid");
        }

        User user = currentUserService.getAuthorizedUser();

        List<Long> inventoryIdsList = Arrays.stream(inventoryIds.split(","))
                .map(id -> Long.parseLong(id.trim()))
                .toList();

        List<InventoryItemDetails> mergedInventoryItemDetailsList = new ArrayList<>();
        for (Long id : inventoryIdsList) {
            Optional<InventoryItemDetails> inventoryItemDetails = inventoryItemRepository.findByUserAndId(user, id);

            if (inventoryItemDetails.isPresent() && !inventoryItemDetails.get().isLinker()) {
                mergedInventoryItemDetailsList.add(inventoryItemDetails.get());
            }
        }

        return inventoryItemRepository.save(
                new InventoryItemDetails(productJson, mergedInventoryItemDetailsList, user)
        );
    }

    public void updateInventoryItemList() {
        User user = currentUserService.getAuthorizedUser();

        List<InventoryItemDetails> inventoryItemDetailsList = new ArrayList<>();

        List<OnlineStoreDetails> onlineStoreDetailsList = onlineStoreRepository.findAllByUser(user);

        for (OnlineStoreDetails onlineStoreDetails : onlineStoreDetailsList) {
            AbstractOnlineStore onlineStore = AbstractOnlineStore.create(onlineStoreDetails.getType(),
                    onlineStoreDetails.getStoreName(), encryptor.decrypt(onlineStoreDetails.getAccessKey()));

            Map<String, String> inventoryIdAndProductMap = onlineStore.getAll().stream()
                    .collect(Collectors.toMap(AbstractOnlineStoreProduct::getId, ProductJsonUtils::getString));
            List<String> inventoryIdsFromRepo = inventoryItemRepository
                    .findAllInventoryIdsByUserAndArbitraryStoreName(user, onlineStoreDetails.getArbitraryStoreName());

            // delete if product doesn't exist in online store
            inventoryIdsFromRepo.stream().filter(id -> !inventoryIdAndProductMap.containsKey(id)).forEach(
                    id -> inventoryItemRepository
                            .deleteByUserAndInventoryIdAndArbitraryStoreName(user, id, onlineStoreDetails.getArbitraryStoreName())
            );

            List<InventoryItemDetails> inventoryItemDetailsListToSave = new ArrayList<>();

            // update if product json is updated and add to productsToSave if it doesn't exist
            inventoryIdAndProductMap.keySet().forEach(
                    id -> {
                        Optional<InventoryItemDetails> product = inventoryItemRepository
                                .findByUserAndInventoryIdAndArbitraryStoreName(user, id, onlineStoreDetails.getArbitraryStoreName());

                        if (product.isPresent()) {
                            if (!product.get().getInventoryItem().equals(inventoryIdAndProductMap.get(id))) {
                                product.get().setInventoryItem(inventoryIdAndProductMap.get(id));

                                inventoryItemDetailsListToSave.add(product.get());
                            }
                        }

                        if (!inventoryItemRepository
                                .existsByUserAndInventoryIdAndArbitraryStoreName(user, id, onlineStoreDetails.getArbitraryStoreName())) {
                            inventoryItemDetailsListToSave
                                    .add(new InventoryItemDetails(inventoryIdAndProductMap.get(id), onlineStoreDetails));
                        }
                    }
            );

            inventoryItemDetailsList.addAll(inventoryItemDetailsListToSave);
        }

        inventoryItemRepository.saveAll(inventoryItemDetailsList);
    }
}
