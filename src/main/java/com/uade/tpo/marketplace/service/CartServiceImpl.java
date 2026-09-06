package com.uade.tpo.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.repository.CartItemRepository;
import com.uade.tpo.marketplace.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;

    
    public Cart getCartByUserId(Long targetUserId, User currentUser) {
        if (!targetUserId.equals(currentUser.getId_user()) && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("No tienes acceso a este carrito");
        }
        
        return cartRepository.findByUserId(targetUserId)
            .orElseThrow(() -> new RuntimeException("El usuario no tiene carrito"));
    }

   
    @Transactional 
    public void clearCart(Long targetUserId, User currentUser) {
        // Reutilizamos el método de arriba que ya incluye la validación de seguridad
        Cart cart = getCartByUserId(targetUserId, currentUser);
        
        cartItemRepository.deleteAll(cart.getProductosCarrito());
        cartRepository.save(cart);
    }
}