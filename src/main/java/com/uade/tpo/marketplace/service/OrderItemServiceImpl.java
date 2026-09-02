package com.uade.tpo.marketplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uade.tpo.marketplace.entity.OrderItem;
import com.uade.tpo.marketplace.repository.OrderItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItem> getItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public Optional<OrderItem> getItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId);
    }
}