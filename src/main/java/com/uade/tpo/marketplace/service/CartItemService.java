package com.uade.tpo.marketplace.service;


import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.CartItem;
import java.util.Optional;

public interface CartItemService {
    Cart addItemToCart(Long userId, Long productId, int quantity);
    Optional<CartItem> removeItemFromCart(Long userId, Long productId);
}