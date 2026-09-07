package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.CartItemRequest;
import com.uade.tpo.marketplace.entity.dto.CartResponse;
import com.uade.tpo.marketplace.service.CartItemService;

@RestController
@RequestMapping("cartItems")
public class CartItemController {
    @Autowired private CartItemService cartItemService;
    
    @PostMapping
    public ResponseEntity<CartResponse> addItem( // 1. Cambiamos Cart por CartResponse
            @RequestBody CartItemRequest request,
            @AuthenticationPrincipal User currentUser) {
            
        Cart updatedCart = cartItemService.addItemToCart(
            currentUser.getId_user(), 
            request.getProductId(), 
            request.getQuantity(), 
            currentUser
        );
        
        // 2. Mapeamos la entidad al DTO antes de devolverla
        return ResponseEntity.ok(CartResponse.fromEntity(updatedCart)); 
}

    @DeleteMapping("/product/one/{productId}") // Ruta limpia: DELETE /cartItems/product/one/{productId}
    public ResponseEntity<Void> removeOneItem(
            @PathVariable Long productId,
            @AuthenticationPrincipal User currentUser) {
            
        cartItemService.removeItemFromCart(currentUser.getId_user(), productId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // Eliminar producto del carrito por completo
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> removeProductEntirely(
            @PathVariable Long productId,
            @AuthenticationPrincipal User currentUser) {
            
        cartItemService.removeProductEntirely(currentUser.getId_user(), productId, currentUser);
        return ResponseEntity.noContent().build();
    }

    // Setear cantidad exacta de un producto en el carrito
    @PutMapping("/product/{productId}")
    public ResponseEntity<Void> setItemQuantity(
            @PathVariable Long productId,
            @RequestParam int quantity,
            @AuthenticationPrincipal User currentUser) {
            
        cartItemService.setItemQuantity(currentUser.getId_user(), productId, quantity, currentUser);
        return ResponseEntity.ok().build();
    }
}