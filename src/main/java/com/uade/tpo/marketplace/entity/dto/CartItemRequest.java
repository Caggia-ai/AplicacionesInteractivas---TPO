package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private int quantity;
}
