package com.antevorta.repository;

import com.antevorta.model.Product;
import com.antevorta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByUserAndId(User user, Long id);
    List<Product> findAllByUser(User user);

    @Query("select new Product(p.id, p.productId, p.title, p.product, p.onlineStoreDetails) " +
            "from Product p where p.user = ?1 and p.onlineStoreDetails.arbitraryStoreName = ?2")
    List<Product> findAllByUserAndArbitraryStoreName(User user, String arbitraryStoreName);

    @Query("select new Product(p.id, p.productId, p.title, p.product, p.onlineStoreDetails) " +
            "from Product p where p.user = ?1 and p.productId = ?2 and p.onlineStoreDetails.arbitraryStoreName = ?3")
    Optional<Product> findByUserAndProductIdAndArbitraryStoreName(User user, Long productId, String arbitraryStoreName);

    @Query("select case when count(p) > 0 then true else false end from Product p " +
                "where p.user = ?1 and p.productId = ?2 and p.onlineStoreDetails.arbitraryStoreName = ?3"
    )
    boolean existsByUserAndProductIdAndArbitraryStoreName(User user, Long productId, String arbitraryStoreName);

    @Query("select p.productId from Product p where p.user = ?1 and p.onlineStoreDetails.arbitraryStoreName = ?2")
    List<Long> findAllProductIdsByUserAndArbitraryStoreName(User user, String arbitraryStoreName);

    @Transactional
    @Modifying
    @Query("delete from Product p where p in (" +
                "select p from Product p where p.user = ?1 and p.productId = ?2 and p.onlineStoreDetails.arbitraryStoreName = ?3" +
            ")"
    )
    void deleteByUserAndProductIdAndArbitraryStoreName(User user, Long productId, String arbitraryStoreName);
}
