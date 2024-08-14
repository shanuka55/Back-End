package com.shopProject.shop.repository;

import com.shopProject.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Order, Long> {

    boolean existsByOrderId(long orderId);

    int deleteByOrderId(long orderId);
}
