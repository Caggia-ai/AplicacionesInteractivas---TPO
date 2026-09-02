package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.uade.tpo.marketplace.entity.OrderItem;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    // Consulta para traer todos los items pertenecientes a una orden específica
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id_order = ?1")
    List<OrderItem> findByOrderId(Long orderId);
}