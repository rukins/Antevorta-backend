package com.inventorymanagementsystem.service;

import com.inventorymanagementsystem.exception.globalexception.ArbitraryStoreNameNotUniqueException;
import com.inventorymanagementsystem.exception.globalexception.EmptyArbitraryStoreNameException;
import com.inventorymanagementsystem.exception.globalexception.MultipleOnlineStoresException;
import com.inventorymanagementsystem.exception.globalexception.EntityNotFoundException;
import com.inventorymanagementsystem.model.OnlineStoreDetails;
import com.inventorymanagementsystem.model.OnlineStoreType;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.OnlineStoreRepository;
import com.inventorymanagementsystem.repository.UserRepository;
import com.inventorymanagementsystem.security.encryptor.Encryptor;
import com.inventorymanagementsystem.utils.RequestUtils;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OnlineStoreService {
    private final OnlineStoreRepository onlineStoreRepository;
    private final UserRepository userRepository;
    private final Encryptor encryptor;

    public OnlineStoreService(OnlineStoreRepository onlineStoreRepository, UserRepository userRepository, Encryptor encryptor) {
        this.onlineStoreRepository = onlineStoreRepository;
        this.userRepository = userRepository;
        this.encryptor = encryptor;
    }

    public List<OnlineStoreDetails> getAllOnlineStoresOfCurrentUser() {
        return onlineStoreRepository.findAllByUser(getCurrentUser());
    }

    public List<OnlineStoreDetails> getAllOnlineStoresOfCurrentUserByType(OnlineStoreType type) {
        return onlineStoreRepository.findAllByUserAndType(getCurrentUser(), type);
    }

    @SneakyThrows
    public void addOnlineStoreToUser(OnlineStoreDetails onlineStore) {
        checkIfArbitraryStoreNameIsNotNull(onlineStore.getArbitraryStoreName(), RequestUtils.ONLINE_STORES_PATH);

        User user = getCurrentUser();

        checkIfUserHasOneOnlineStoreByType(user, onlineStore.getType());
        checkIfArbitraryStoreNameIsUnique(user, onlineStore.getArbitraryStoreName(), RequestUtils.ONLINE_STORES_PATH);

        onlineStore.setUser(user);
        onlineStore.setAccessKey(encryptor.encrypt(onlineStore.getAccessKey()));
        onlineStore.generateId(user.getEmail());

        onlineStoreRepository.save(onlineStore);
    }

    @SneakyThrows
    public void updateOnlineStoreName(String currentName, String newName) {
        checkIfArbitraryStoreNameIsNotNull(newName, RequestUtils.ONLINE_STORES_PATH + "/" + currentName + "?newName=" + newName);

        User user = getCurrentUser();

        checkIfArbitraryStoreNameIsUnique(user, newName, RequestUtils.ONLINE_STORES_PATH + "/" + currentName + "?newName=" + newName);

        Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository.findByArbitraryStoreNameAndUser(currentName, user);

        if (onlineStoreDetails.isPresent()) {
            onlineStoreDetails.get().setArbitraryStoreName(newName);

            onlineStoreRepository.save(onlineStoreDetails.get());

            return;
        }

        throw new EntityNotFoundException(String.format("Online store with '%s' name not found", currentName),
                RequestUtils.ONLINE_STORES_PATH + "/" + currentName + "?newName=" + newName);
    }

    @SneakyThrows
    public void deleteOnlineStoreByName(String name) {
        User user = getCurrentUser();

        Optional<OnlineStoreDetails> onlineStoreDetails = onlineStoreRepository.findByArbitraryStoreNameAndUser(name, user);

        if (onlineStoreDetails.isPresent()) {
            onlineStoreRepository.delete(onlineStoreDetails.get());

            return;
        }

        throw new EntityNotFoundException(String.format("Online store with '%s' name not found", name),
                RequestUtils.ONLINE_STORES_PATH + "/" + name);
    }

    @SneakyThrows
    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> user = userRepository.findByEmail(email);

        return user.orElseThrow(() -> new EntityNotFoundException("User not found", "/"));
    }

    @SneakyThrows
    private void checkIfArbitraryStoreNameIsNotNull(String arbitraryStoreName, String path) {
        if (arbitraryStoreName == null || arbitraryStoreName.isEmpty()) {
            throw new EmptyArbitraryStoreNameException("Arbitrary store name shouldn't be empty", path);
        }
    }

    @SneakyThrows
    private void checkIfArbitraryStoreNameIsUnique(User user, String arbitraryName, String path) {
        List<String> arbitraryStoreNamesList = onlineStoreRepository.findAllByUser(user).stream()
                .map(OnlineStoreDetails::getArbitraryStoreName)
                .toList();

        if (arbitraryStoreNamesList.contains(arbitraryName)) {
            throw new ArbitraryStoreNameNotUniqueException(
                    String.format("Store with '%s' arbitrary name already exists", arbitraryName), path);
        }
    }

    @SneakyThrows
    private void checkIfUserHasOneOnlineStoreByType(User user, OnlineStoreType type) {
        List<OnlineStoreDetails> onlineStoreProjectionList = onlineStoreRepository.findAllByUserAndType(user, type);

        if (onlineStoreProjectionList.size() == 1) {
            throw new MultipleOnlineStoresException(
                    String.format("Store with '%s' type already exists", type), RequestUtils.ONLINE_STORES_PATH);
        }
    }
}