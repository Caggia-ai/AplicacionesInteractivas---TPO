package com.uade.tpo.marketplace.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.uade.tpo.marketplace.entity.*;
import com.uade.tpo.marketplace.repository.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    @Override
    @Transactional
    public Order createOrderFromCart(Long userId, String paymentMethod, String deliveryMethod) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("El usuario no tiene carrito activo"));

        if (cart.getProductosCarrito() == null || cart.getProductosCarrito().isEmpty()) {
            throw new RuntimeException("No se puede comprar con el carrito vacío");
        }

        // Creamos la orden primero para tener su ID
        Order order = new Order();
        order.setUser(user);
        order.setPayment_method(paymentMethod);
        order.setDelivery_method(deliveryMethod);
        order.setDate(LocalDate.now());
        order = orderRepository.save(order);

        int totalDeLaOrden = 0; // Vamos a ir acumulando el total

        for (CartItem cartItem : cart.getProductosCarrito()) {
            Product product = cartItem.getProduct();
            if (!product.isState()) {
                throw new RuntimeException("El producto " + product.getName() + " ya no está disponible para la compra.");
            }

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Sin stock suficiente para el producto: " + product.getName());
            }

            // Descontamos el stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Calculamos el precio congelado en este instante
            int descuento = (product.getPrice() * product.getDiscount_percentage()) / 100;
            int precioCongelado = product.getPrice() - descuento;

            // Guardamos el historial del item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnit_price(precioCongelado);
            orderItemRepository.save(orderItem);
            
            totalDeLaOrden += (precioCongelado * cartItem.getQuantity());
        }

        // Guardamos el total final congelado en la Orden
        order.setTotal(totalDeLaOrden);
        orderRepository.save(order);

        // Limpiamos el carrito
        cartItemRepository.deleteAll(cart.getProductosCarrito());

        return order;
    }
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
