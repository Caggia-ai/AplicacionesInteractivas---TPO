package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entity.OrderItem;
import com.uade.tpo.marketplace.entity.dto.OrderItemResponse;
import com.uade.tpo.marketplace.service.OrderItemService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orderItems")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    // Sirve para mostrar el detalle de una compra en el "Historial de Órdenes"
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponse>> getItemsByOrderId(@PathVariable Long orderId) { // Pasamos orderId a Long
        List<OrderItem> items = orderItemService.getItemsByOrderId(orderId);
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        List<OrderItemResponse> responseList = items.stream()
                                                    .map(OrderItemResponse::fromEntity)
                                                    .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<OrderItemResponse> getItemById(@PathVariable Long itemId) { // Pasamos itemId a Long
        Optional<OrderItem> item = orderItemService.getItemById(itemId);
        return item.map(orderItem -> ResponseEntity.ok(OrderItemResponse.fromEntity(orderItem)))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
}