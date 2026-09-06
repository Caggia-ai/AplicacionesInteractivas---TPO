package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.CartItemRequest;
import com.uade.tpo.marketplace.service.CartItemService;

@RestController
@RequestMapping("cartItems")
public class CartItemController {
    @Autowired private CartItemService cartItemService;
    
    @PostMapping // Ruta limpia: POST /cartItems
    public ResponseEntity<Cart> addItem(
            @RequestBody CartItemRequest request,
            @AuthenticationPrincipal User currentUser) {
            
        return ResponseEntity.ok(cartItemService.addItemToCart(
            currentUser.getId_user(), 
            request.getProductId(), 
            request.getQuantity(), 
            currentUser
        ));
    }

    @DeleteMapping("/product/{productId}") // Ruta limpia: DELETE /cartItems/product/{productId}
    public ResponseEntity<Void> removeOneItem(
            @PathVariable Long productId,
            @AuthenticationPrincipal User currentUser) {
            
        cartItemService.removeItemFromCart(currentUser.getId_user(), productId, currentUser);
        return ResponseEntity.noContent().build();
    }
}