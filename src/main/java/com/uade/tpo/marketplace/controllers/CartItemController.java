package com.uade.tpo.marketplace.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.dto.CartItemRequest;
import com.uade.tpo.marketplace.service.CartItemService;

@RestController
@RequestMapping("cartItems")
public class CartItemController {
    @Autowired private CartItemService cartItemService;
    
    @PostMapping("/user/{userId}")
    public ResponseEntity<Cart> addItem(@PathVariable Long userId, @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartItemService.addItemToCart(userId, request.getProductId(), request.getQuantity()));
    }

    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<Void> removeOneItem(@PathVariable Long userId, @PathVariable Long productId) {
        cartItemService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
