package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;
import com.uade.tpo.marketplace.entity.Order;

public interface OrderService {
    Order createOrderFromCart(Long userId, String paymentMethod, String deliveryMethod);
    List<Order> getOrdersByUserId(Long userId);
    Optional<Order> getOrderById(Long orderId);
}