package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.dto.CartItemRequest;
import com.uade.tpo.marketplace.service.CartService;

@RestController
@RequestMapping("carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/user/{userId}/items")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long userId, @RequestBody CartItemRequest request) {
        Cart updatedCart = cartService.addProductToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }
}