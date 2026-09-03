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
        dto.setTotal(cart.getTotal());
        dto.setState(cart.isState());
        
        // Transformamos la lista de CartItem a CartItemResponse
        if (cart.getProductosCarrito() != null) {
            dto.setItems(cart.getProductosCarrito().stream()
                             .map(CartItemResponse::fromEntity)
                             .toList());
        } else {
            dto.setItems(new ArrayList<>()); // Evita que devuelva null si está vacío
        }
        return dto;
    }
}