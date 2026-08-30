package com.uade.tpo.marketplace.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.uade.tpo.marketplace.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT c FROM Cart c WHERE c.user.id_user = ?1")
    Optional<Cart> findByUserId(Long userId);
}