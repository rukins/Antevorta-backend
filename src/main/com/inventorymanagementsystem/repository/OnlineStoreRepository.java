package com.inventorymanagementsystem.repository;

import com.inventorymanagementsystem.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OnlineStoreRepository extends JpaRepository<OnlineStoreDetails, String> {
    Long countByUser(User user);
    List<OnlineStoreDetails> findAllByUser(User user);
    @Query("select new OnlineStoreDetails(u.id, u.arbitraryStoreName, u.type) " +
            "from OnlineStoreDetails u where u.user = ?1")
    List<OnlineStoreDetails> findAllByUserWithoutCredentials(User user);

    @Query("select new OnlineStoreDetails(u.id, u.arbitraryStoreName, u.type) " +
            "from OnlineStoreDetails u where u.user = ?1 and u.type = ?2")
    List<OnlineStoreDetails> findAllByUserAndTypeWithoutCredentials(User user, OnlineStoreType type);

    Optional<OnlineStoreDetails> findByUserAndArbitraryStoreName(User user, String arbitraryStoreName);
}
