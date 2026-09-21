package com.uade.tpo.marketplace.controllers;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
 
import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.CartItem;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.CartItemRequest;
import com.uade.tpo.marketplace.entity.dto.CartResponse;
import com.uade.tpo.marketplace.entity.dto.MessageResponse;
import com.uade.tpo.marketplace.service.CartItemService;
 
import java.util.Optional;
 
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
    public ResponseEntity<MessageResponse> removeOneItem(
            @PathVariable Long productId,
            @AuthenticationPrincipal User currentUser) {
            
        Optional<CartItem> result = cartItemService.removeItemFromCart(currentUser.getId_user(), productId, currentUser);
 
        // Mensaje distinto según si quedó con unidades restantes o se eliminó del todo,
        // en vez de devolver 204/un número sin contexto.
        String message = result
                .map(item -> "Se descontó una unidad. Quedan " + item.getQuantity() + " en el carrito.")
                .orElse("Se eliminó el producto del carrito (no quedaban más unidades).");
 
        return ResponseEntity.ok(new MessageResponse(message));
    }
 
    // Eliminar producto del carrito por completo
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<MessageResponse> removeProductEntirely(
            @PathVariable Long productId,
            @AuthenticationPrincipal User currentUser) {
            
        cartItemService.removeProductEntirely(currentUser.getId_user(), productId, currentUser);
        return ResponseEntity.ok(new MessageResponse("Producto eliminado del carrito."));
    }
 
    // Setear cantidad exacta de un producto en el carrito
    @PutMapping("/product/{productId}")
    public ResponseEntity<MessageResponse> setItemQuantity(
            @PathVariable Long productId,
            @RequestParam int quantity,
            @AuthenticationPrincipal User currentUser) {
            
        cartItemService.setItemQuantity(currentUser.getId_user(), productId, quantity, currentUser);
        return ResponseEntity.ok(new MessageResponse("Cantidad del producto actualizada a " + quantity + "."));
    }
}
