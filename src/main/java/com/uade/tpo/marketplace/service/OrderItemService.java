package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;
import com.uade.tpo.marketplace.entity.OrderItem;

public interface OrderItemService {
    List<OrderItem> getItemsByOrderId(Long orderId);
    Optional<OrderItem> getItemById(Long orderItemId);
}