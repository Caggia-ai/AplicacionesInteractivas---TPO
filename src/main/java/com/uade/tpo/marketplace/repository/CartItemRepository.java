package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.marketplace.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {}
