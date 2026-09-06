package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.CartResponse;
import com.uade.tpo.marketplace.service.CartService;

@RestController
@RequestMapping("carts")
public class CartController {
    @Autowired private CartService cartService;

    @GetMapping // Ruta limpia: GET /carts
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User currentUser) {
        // Le pasamos su propio ID como objetivo, el servicio validará internamente
        Cart cart = cartService.getCartByUserId(currentUser.getId_user(), currentUser);
        return ResponseEntity.ok(CartResponse.fromEntity(cart));
    }

    @DeleteMapping("/clear") // Ruta limpia: DELETE /carts/clear
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User currentUser) {
        cartService.clearCart(currentUser.getId_user(), currentUser);
        return ResponseEntity.noContent().build();
    }
}