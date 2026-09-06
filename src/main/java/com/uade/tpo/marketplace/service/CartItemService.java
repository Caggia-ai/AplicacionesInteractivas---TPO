package com.uade.tpo.marketplace.service;


import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.CartItem;
import com.uade.tpo.marketplace.entity.User;
import java.util.Optional;

public interface CartItemService {
    public Cart addItemToCart(Long targetUserId, Long productId, int quantityToAdd, User currentUser);
    public Optional<CartItem> removeItemFromCart(Long targetUserId, Long productId, User currentUser);
    public void removeProductEntirely(Long targetUserId, Long productId, User currentUser);
    public CartItem setItemQuantity(Long targetUserId, Long productId, int exactQuantity, User currentUser);
}