package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.entity.Cart;

public interface CartService {
    Cart getCartByUserId(Long userId);
    void clearCart(Long userId); 
}