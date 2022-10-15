package com.inventorymanagementsystem.repository;

import com.inventorymanagementsystem.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnlineStoreRepository extends JpaRepository<OnlineStoreDetails, String> {
    @Query("select new com.inventorymanagementsystem.model.OnlineStoreDetails(u.id, u.arbitraryStoreName, u.type) " +
            "from OnlineStoreDetails u where u.user = ?1")
    List<OnlineStoreDetails> findAllByUser(User user);

    @Query("select new com.inventorymanagementsystem.model.OnlineStoreDetails(u.id, u.arbitraryStoreName, u.type) " +
            "from OnlineStoreDetails u where u.user = ?1 and u.type = ?2")
    List<OnlineStoreDetails> findAllByUserAndType(User user, OnlineStoreType type);
}
