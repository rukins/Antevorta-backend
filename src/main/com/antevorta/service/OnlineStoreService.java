package com.antevorta.service;

import com.antevorta.exception.serverexception.*;
import com.antevorta.model.OnlineStoreDetails;
import com.antevorta.model.OnlineStoreType;
import com.antevorta.model.User;
import com.antevorta.repository.OnlineStoreRepository;
import com.antevorta.security.exception.authexception.MultipleOnlineStoresByTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OnlineStoreService {
    private final OnlineStoreRepository onlineStoreRepository;
    private final CurrentUserService currentUserService;

    @Autowired
    public OnlineStoreService(OnlineStoreRepository onlineStoreRepository, CurrentUserService currentUserService) {
        this.onlineStoreRepository = onlineStoreRepository;
        this.currentUserService = currentUserService;
    }

    public List<OnlineStoreDetails> getAll() {
        return onlineStoreRepository.findAllByUser(currentUserService.getAuthorizedUser());
    }

    public List<OnlineStoreDetails> getAllByType(OnlineStoreType type) {
        return onlineStoreRepository.findAllByUserAndType(currentUserService.getAuthorizedUser(), type);
    }

    public OnlineStoreDetails addToUser(OnlineStoreDetails onlineStore) throws ServerException {
        User user = currentUserService.getAuthorizedUser();

        checkIfArbitraryStoreNameIsUnique(user, onlineStore.getArbitraryStoreName());

        onlineStore.setUser(user);
        onlineStore.generateId(onlineStoreRepository.countByUser(user));

        return onlineStoreRepository.save(onlineStore);
    }

    public OnlineStoreDetails updateArbitraryStoreName(String currentName, String newName) throws ServerException {
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

    private void checkIfArbitraryStoreNameIsUnique(User user, String arbitraryName) throws ArbitraryStoreNameNotUniqueException {
        List<String> arbitraryStoreNamesList = onlineStoreRepository.findAllByUser(user).stream()
                .map(OnlineStoreDetails::getArbitraryStoreName)
                .toList();

        if (arbitraryStoreNamesList.contains(arbitraryName)) {
            throw new ArbitraryStoreNameNotUniqueException(
                    String.format("Store with '%s' arbitrary name already exists", arbitraryName));
        }
    }

    public boolean hasUserOneOnlineStoreByType(OnlineStoreType type) throws MultipleOnlineStoresByTypeException {
        if (onlineStoreRepository.existsByUserAndType(currentUserService.getAuthorizedUser(), type)) {
            throw new MultipleOnlineStoresByTypeException(String.format("Store with '%s' type already exists", type));
        }

        return true;
    }
}