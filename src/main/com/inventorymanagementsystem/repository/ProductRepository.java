package com.inventorymanagementsystem.repository;

import com.inventorymanagementsystem.model.Product;
import com.inventorymanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    @Query("select new Product(p.id, p.productId, p.title, p.product, p.onlineStoreDetails) " +
//            "from Product p where p.onlineStoreDetails.user = ?1 and p.productId = ?2")
//    Optional<Product> findByUserAndProductId(User user, Long productId);

    @Query("select new Product(p.id,p.productId, p.title, p.product, p.onlineStoreDetails) " +
            "from Product p where p.onlineStoreDetails.user = ?1 and p.id = ?2")
    Optional<Product> findByUserAndId(User user, Long id);

    @Query("select new Product(p.id, p.productId, p.title, p.product, p.onlineStoreDetails) " +
            "from Product p where p.onlineStoreDetails.user = ?1")
    List<Product> findAllByUser(User user);

    @Query("select new Product(p.id, p.productId, p.title, p.product, p.onlineStoreDetails) " +
            "from Product p where p.onlineStoreDetails.user = ?1 and p.onlineStoreDetails.arbitraryStoreName = ?2")
    List<Product> findAllByUserAndArbitraryStoreName(User user, String arbitraryStoreName);

    @Query("select case when count(p) > 0 then true else false end from Product p " +
            "where p.productId = ?1 and p.onlineStoreDetails.arbitraryStoreName = ?2")
    boolean existsByProductIdAndArbitraryStoreName(Long productId, String arbitraryStoreName);
}
