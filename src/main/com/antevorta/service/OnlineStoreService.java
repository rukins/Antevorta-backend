package com.antevorta.service;

import com.antevorta.exception.serverexception.*;
import com.antevorta.model.OnlineStoreDetails;
import com.antevorta.model.OnlineStoreType;
import com.antevorta.model.User;
import com.antevorta.repository.OnlineStoreRepository;
import com.antevorta.security.encryptor.Encryptor;
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
        return onlineStoreRepository.findAllByUser(currentUserService.getAuthorizedUser());
    }

    public List<OnlineStoreDetails> getAllByType(OnlineStoreType type) {
        return onlineStoreRepository.findAllByUserAndType(currentUserService.getAuthorizedUser(), type);
    }

    public OnlineStoreDetails addToUser(OnlineStoreDetails onlineStore) throws ServerException {
        checkIfArbitraryStoreNameIsNotNull(onlineStore.getArbitraryStoreName());

        User user = currentUserService.getAuthorizedUser();

        checkIfUserHasOneOnlineStoreByType(user, onlineStore.getType());
        checkIfArbitraryStoreNameIsUnique(user, onlineStore.getArbitraryStoreName());

        onlineStore.setUser(user);
        onlineStore.setAccessKey(encryptor.encrypt(onlineStore.getAccessKey()));
        onlineStore.generateId(onlineStoreRepository.countByUser(user));

        return onlineStoreRepository.save(onlineStore);
    }

    public OnlineStoreDetails updateArbitraryStoreName(String currentName, String newName) throws ServerException {
        checkIfArbitraryStoreNameIsNotNull(newName);

        User user = currentUserService.getAuthorizedUser();

        checkIfArbitraryStoreNameIsUnique(user, newName);

        Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository.findByUserAndArbitraryStoreName(user, currentName);

        if (onlineStoreDetails.isPresent()) {
            onlineStoreDetails.get().setArbitraryStoreName(newName);

            return onlineStoreRepository.save(onlineStoreDetails.get());
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
        List<String> arbitraryStoreNamesList = onlineStoreRepository.findAllByUser(user).stream()
                .map(OnlineStoreDetails::getArbitraryStoreName)
                .toList();

        if (arbitraryStoreNamesList.contains(arbitraryName)) {
            throw new ArbitraryStoreNameNotUniqueException(
                    String.format("Store with '%s' arbitrary name already exists", arbitraryName));
        }
    }

    private void checkIfUserHasOneOnlineStoreByType(User user, OnlineStoreType type) throws MultipleOnlineStoresException {
        List<OnlineStoreDetails> onlineStoreProjectionList = onlineStoreRepository.findAllByUserAndType(user, type);

        if (onlineStoreProjectionList.size() == 1) {
            throw new MultipleOnlineStoresException(String.format("Store with '%s' type already exists", type));
        }
    }
}