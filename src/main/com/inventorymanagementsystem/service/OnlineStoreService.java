package com.inventorymanagementsystem.service;

import com.inventorymanagementsystem.exception.serverexception.*;
import com.inventorymanagementsystem.model.OnlineStoreDetails;
import com.inventorymanagementsystem.model.OnlineStoreType;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.OnlineStoreRepository;
import com.inventorymanagementsystem.security.encryptor.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OnlineStoreService {
    private final OnlineStoreRepository onlineStoreRepository;
    private final CurrentUserService currentUserService;
    private final Encryptor encryptor;

    @Autowired
    public OnlineStoreService(OnlineStoreRepository onlineStoreRepository, CurrentUserService currentUserService, Encryptor encryptor) {
        this.onlineStoreRepository = onlineStoreRepository;
        this.currentUserService = currentUserService;
        this.encryptor = encryptor;
    }

    public List<OnlineStoreDetails> getAll() {
        return onlineStoreRepository.findAllByUserWithoutCredentials(currentUserService.getAuthorizedUser());
    }

    public List<OnlineStoreDetails> getAllByType(OnlineStoreType type) {
        return onlineStoreRepository.findAllByUserAndTypeWithoutCredentials(currentUserService.getAuthorizedUser(), type);
    }

    public void addToUser(OnlineStoreDetails onlineStore) throws ServerException {
        checkIfArbitraryStoreNameIsNotNull(onlineStore.getArbitraryStoreName());

        User user = currentUserService.getAuthorizedUser();

        checkIfUserHasOneOnlineStoreByType(user, onlineStore.getType());
        checkIfArbitraryStoreNameIsUnique(user, onlineStore.getArbitraryStoreName());

        onlineStore.setUser(user);
        onlineStore.setAccessKey(encryptor.encrypt(onlineStore.getAccessKey()));
        onlineStore.generateId(onlineStoreRepository.countByUser(user));

        onlineStoreRepository.save(onlineStore);
    }

    public void updateArbitraryStoreName(String currentName, String newName) throws ServerException {
        checkIfArbitraryStoreNameIsNotNull(newName);

        User user = currentUserService.getAuthorizedUser();

        checkIfArbitraryStoreNameIsUnique(user, newName);

        Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository.findByUserAndArbitraryStoreName(user, currentName);

        if (onlineStoreDetails.isPresent()) {
            onlineStoreDetails.get().setArbitraryStoreName(newName);

            onlineStoreRepository.save(onlineStoreDetails.get());

            return;
        }

        throw new EntityNotFoundException(String.format("Online store with '%s' name not found", currentName));
    }

    public void deleteByArbitraryStoreName(String name) throws ServerException {
        User user = currentUserService.getAuthorizedUser();

        Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository.findByUserAndArbitraryStoreName(user, name);

        if (onlineStoreDetails.isPresent()) {
            onlineStoreRepository.delete(onlineStoreDetails.get());

            return;
        }

        throw new EntityNotFoundException(String.format("Online store with '%s' name not found", name));
    }

    private void checkIfArbitraryStoreNameIsNotNull(String arbitraryStoreName) throws EmptyArbitraryStoreNameException {
        if (arbitraryStoreName == null || arbitraryStoreName.isEmpty()) {
            throw new EmptyArbitraryStoreNameException("Arbitrary store name shouldn't be empty");
        }
    }

    private void checkIfArbitraryStoreNameIsUnique(User user, String arbitraryName) throws ArbitraryStoreNameNotUniqueException {
        List<String> arbitraryStoreNamesList = onlineStoreRepository.findAllByUserWithoutCredentials(user).stream()
                .map(OnlineStoreDetails::getArbitraryStoreName)
                .toList();

        if (arbitraryStoreNamesList.contains(arbitraryName)) {
            throw new ArbitraryStoreNameNotUniqueException(
                    String.format("Store with '%s' arbitrary name already exists", arbitraryName));
        }
    }

    private void checkIfUserHasOneOnlineStoreByType(User user, OnlineStoreType type) throws MultipleOnlineStoresException {
        List<OnlineStoreDetails> onlineStoreProjectionList = onlineStoreRepository.findAllByUserAndTypeWithoutCredentials(user, type);

        if (onlineStoreProjectionList.size() == 1) {
            throw new MultipleOnlineStoresException(String.format("Store with '%s' type already exists", type));
        }
    }
}