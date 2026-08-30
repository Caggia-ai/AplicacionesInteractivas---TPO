package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.marketplace.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {}