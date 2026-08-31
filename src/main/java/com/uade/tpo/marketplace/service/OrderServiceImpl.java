package com.uade.tpo.marketplace.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Order createOrderFromCart(Long userId, String paymentMethod, String deliveryMethod) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("El usuario no tiene carrito activo"));

        if (cart.getProductosCarrito() == null || cart.getProductosCarrito().isEmpty()) {
            throw new RuntimeException("No se puede comprar con el carrito vacío");
        }

        // 1. Creamos la "Factura" (Order)
        Order order = new Order();
        order.setUser(user);
        order.setPayment_method(paymentMethod);
        order.setDelivery_method(deliveryMethod);
        order.setDate(LocalDate.now().toString()); // Fecha actual del sistema
        order.setTotal(cart.getTotal());
        order = orderRepository.save(order);

        // 2. Procesamos cada renglón del carrito
        for (CartItem cartItem : cart.getProductosCarrito()) {
            Product product = cartItem.getProduct();

            // Validamos que haya stock suficiente para el salto a producción
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Sin stock suficiente para el producto: " + product.getName());
            }

            // Descontamos el stock físico
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Creamos el renglón de la compra (Histórico inmutable)
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnit_price(cartItem.getUnit_price());
            orderItemRepository.save(orderItem);
        }

        // 3. Vaciamos el carrito para que el usuario pueda volver a comprar
        cartItemRepository.deleteAll(cart.getProductosCarrito());
        cart.setTotal(0);
        cartRepository.save(cart);

        return order;
    }
}