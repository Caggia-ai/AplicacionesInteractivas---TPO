package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.Cart;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class CartResponse {
    private Long id;
    private int total;
    private boolean state;
    private List<CartItemResponse> items;

    public static CartResponse fromEntity(Cart cart) {
        CartResponse dto = new CartResponse();
        dto.setId(cart.getId_cart());
        dto.setState(cart.isState());
        
        int totalCalculado = 0;
        
        if (cart.getProductosCarrito() != null) {
            List<CartItemResponse> itemsDto = cart.getProductosCarrito().stream()
                             .map(CartItemResponse::fromEntity)
                             .toList();
            dto.setItems(itemsDto);
            
            // Sumamos los subtotales de todos los items mapeados
            for(CartItemResponse i : itemsDto) {
                totalCalculado += i.getSubtotal();
            }
        } else {
            dto.setItems(new ArrayList<>()); 
        }
        
        dto.setTotal(totalCalculado); // Asignamos el total dinámico al JSON
        return dto;
    }
}