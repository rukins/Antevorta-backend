package com.inventorymanagementsystem.service;

import com.inventorymanagementsystem.exception.globalexception.ArbitraryStoreNameNotUniqueException;
import com.inventorymanagementsystem.exception.globalexception.EmptyArbitraryStoreNameException;
import com.inventorymanagementsystem.exception.globalexception.MultipleOnlineStoresException;
import com.inventorymanagementsystem.exception.globalexception.UserNotFoundException;
import com.inventorymanagementsystem.model.*;
import com.inventorymanagementsystem.repository.OnlineStoreRepository;
import com.inventorymanagementsystem.repository.UserRepository;
import com.inventorymanagementsystem.utils.RequestPaths;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OnlineStoreService {
    private final OnlineStoreRepository onlineStoreRepository;
    private final UserRepository userRepository;

    public OnlineStoreService(OnlineStoreRepository onlineStoreRepository, UserRepository userRepository) {
        this.onlineStoreRepository = onlineStoreRepository;
        this.userRepository = userRepository;
    }

    // TODO: add encrypting of store accessKey
    @SneakyThrows
    public void addOnlineStoreToUser(OnlineStoreDetails onlineStore) {
        if (onlineStore.getArbitraryStoreName() == null || onlineStore.getArbitraryStoreName().isEmpty()) {
            throw new EmptyArbitraryStoreNameException("Arbitrary store name shouldn't be empty", RequestPaths.ONLINE_STORES);
        }

        User user = getCurrentUser();

        checkIfUserHasOneOnlineStoreByType(user, onlineStore.getType());
        checkIfArbitraryStoreNameIsUnique(user, onlineStore.getArbitraryStoreName());

        onlineStore.setUser(user);
        onlineStore.generateId(user.getEmail());

        onlineStoreRepository.save(onlineStore);
    }

    public List<OnlineStore> getAllOnlineStoresOfCurrentUser() {
        return onlineStoreRepository.findAllByUser(getCurrentUser());
    }

    public List<OnlineStore> getAllOnlineStoresOfCurrentUserByType(OnlineStoreType type) {
        return onlineStoreRepository.findAllByUserAndType(getCurrentUser(), type);
    }

    @SneakyThrows
    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> user = userRepository.findByEmail(email);

        return user.orElseThrow(() -> new UserNotFoundException("User not found", "/"));
    }

    @SneakyThrows
    private void checkIfArbitraryStoreNameIsUnique(User user, String arbitraryName) {
        List<String> arbitraryStoreNamesList = onlineStoreRepository.findAllByUser(user).stream()
                .map(OnlineStore::getArbitraryStoreName)
                .toList();

        if (arbitraryStoreNamesList.contains(arbitraryName)) {
            throw new ArbitraryStoreNameNotUniqueException(
                    String.format("Store with %s arbitrary name already exists", arbitraryName), RequestPaths.ONLINE_STORES);
        }
    }

    @SneakyThrows
    private void checkIfUserHasOneOnlineStoreByType(User user, OnlineStoreType type) {
        List<OnlineStore> onlineStoreProjectionList = onlineStoreRepository.findAllByUserAndType(user, type);

        if (onlineStoreProjectionList.size() == 1) {
            throw new MultipleOnlineStoresException(
                    String.format("Store with %s type already exists", type), RequestPaths.ONLINE_STORES);
        }
    }
}