package com.uade.tpo.marketplace.service;


import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.CartItem;

public interface CartItemService {
    Cart addItemToCart(Long userId, Long productId, int quantity);
    java.util.Optional<CartItem> removeItemFromCart(Long userId, Long productId);
}