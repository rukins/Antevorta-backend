package com.antevorta.repository;

import com.antevorta.model.InventoryItemDetails;
import com.antevorta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItemDetails, Long> {
    Optional<InventoryItemDetails> findByUserAndId(User user, Long id);
    List<InventoryItemDetails> findAllByUser(User user);

    @Query("select new InventoryItemDetails(p.id, p.inventoryId, p.title, p.inventoryItem, p.onlineStoreDetails) " +
            "from InventoryItemDetails p where p.user = ?1 and p.onlineStoreDetails.arbitraryStoreName = ?2")
    List<InventoryItemDetails> findAllByUserAndArbitraryStoreName(User user, String arbitraryStoreName);

    @Query("select new InventoryItemDetails(p.id, p.inventoryId, p.title, p.inventoryItem, p.onlineStoreDetails) " +
            "from InventoryItemDetails p where p.user = ?1 and p.inventoryId = ?2 and p.onlineStoreDetails.arbitraryStoreName = ?3")
    Optional<InventoryItemDetails> findByUserAndInventoryIdAndArbitraryStoreName(User user, Long inventoryId, String arbitraryStoreName);

    @Query("select case when count(p) > 0 then true else false end from InventoryItemDetails p " +
            "where p.user = ?1 and p.inventoryId = ?2 and p.onlineStoreDetails.arbitraryStoreName = ?3"
    )
    boolean existsByUserAndInventoryIdAndArbitraryStoreName(User user, Long inventoryId, String arbitraryStoreName);

    @Query("select p.inventoryId from InventoryItemDetails p where p.user = ?1 and p.onlineStoreDetails.arbitraryStoreName = ?2")
    List<Long> findAllInventoryIdsByUserAndArbitraryStoreName(User user, String arbitraryStoreName);

    @Transactional
    @Modifying
    @Query("delete from InventoryItemDetails p where p in (" +
            "select p from InventoryItemDetails p where p.user = ?1 and p.inventoryId = ?2 and p.onlineStoreDetails.arbitraryStoreName = ?3" +
            ")"
    )
    void deleteByUserAndInventoryIdAndArbitraryStoreName(User user, Long inventoryId, String arbitraryStoreName);
}
