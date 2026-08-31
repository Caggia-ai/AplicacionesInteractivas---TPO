package com.uade.tpo.marketplace.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.uade.tpo.marketplace.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id_cart = ?1 AND ci.product.id_product = ?2")
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    
}
