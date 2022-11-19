package com.antevorta.repository;

import com.antevorta.model.OnlineStoreDetails;
import com.antevorta.model.OnlineStoreType;
import com.antevorta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OnlineStoreRepository extends JpaRepository<OnlineStoreDetails, String> {
    Long countByUser(User user);
    List<OnlineStoreDetails> findAllByUser(User user);
    List<OnlineStoreDetails> findAllByUserAndType(User user, OnlineStoreType type);
    Optional<OnlineStoreDetails> findByUserAndArbitraryStoreName(User user, String arbitraryStoreName);
}
