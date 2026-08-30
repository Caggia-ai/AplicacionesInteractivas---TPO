package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.entity.Order;

public interface OrderService {
    Order createOrderFromCart(Long userId, String paymentMethod, String deliveryMethod);
}