package com.uade.tpo.marketplace.service;


import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.CartItem;
import com.uade.tpo.marketplace.entity.User;
import java.util.Optional;

public interface CartItemService {
    Cart addItemToCart(Long targetUserId, Long productId, int quantityToAdd, User currentUser);
    Optional<CartItem> removeItemFromCart(Long targetUserId, Long productId, User currentUser);
}