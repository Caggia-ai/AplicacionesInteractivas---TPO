package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.CartItem;

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
        List<CartItemResponse> itemsDto = new ArrayList<>();
        
        if (cart.getProductosCarrito() != null) {
            // Recorremos las ENTIDADES originales (CartItem), que tienen getProduct()
            for (CartItem item : cart.getProductosCarrito()) {
                
                // Filtramos: solo procesamos si el producto sigue activo
                if (item.getProduct().isState()) {
                    CartItemResponse dtoItem = CartItemResponse.fromEntity(item);
                    itemsDto.add(dtoItem);
                    totalCalculado += dtoItem.getSubtotal();
                }
            }
        }
        
        dto.setItems(itemsDto);
        dto.setTotal(totalCalculado); 
        return dto;
    }
}