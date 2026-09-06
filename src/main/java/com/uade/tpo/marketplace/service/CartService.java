package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.User;

public interface CartService {
    public Cart getCartByUserId(Long targetUserId, User currentUser);
    public void clearCart(Long targetUserId, User currentUser);
}