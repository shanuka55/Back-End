package com.shopProject.shop.repository;

import com.shopProject.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Product (product_id,name,price,stock) VALUES (?,?,?,?)",nativeQuery = true)
    void addProductRecord(int productId,
                           String name,
                           double price,
                           int stock);

    boolean existsByProductId(long productId);

    int deleteByProductId(long productId);
}
