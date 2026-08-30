package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.marketplace.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {}