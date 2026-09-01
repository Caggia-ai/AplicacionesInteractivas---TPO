package com.uade.tpo.marketplace.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uade.tpo.marketplace.entity.Cart;
import com.uade.tpo.marketplace.entity.CartItem;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.repository.CartItemRepository;
import com.uade.tpo.marketplace.repository.CartRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;


@Service
public class CartItemServiceImpl implements CartItemService {
@Autowired private CartItemRepository cartItemRepository;
@Autowired private CartRepository cartRepository;
@Autowired private ProductRepository productRepository;

public Cart addItemToCart(Long userId, Long productId, int quantityToAdd) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    if (!product.isState()) {
        throw new RuntimeException("El producto ya no está disponible.");
    }
    Cart cart = cartRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("El usuario no tiene carrito"));

    CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId_cart(), productId)
        .orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(0); 
            newItem.setUnit_price(product.getPrice());
            return newItem;
        });

    
    item.setQuantity(item.getQuantity() + quantityToAdd);
    cartItemRepository.save(item);

    cart.setTotal(cart.getTotal() + (item.getUnit_price() * quantityToAdd));
    return cartRepository.save(cart);
}

public Optional<CartItem> removeItemFromCart(Long userId, Long productId) {
    
    Cart cart = cartRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Error crítico: el usuario no tiene carrito asignado"));
        
    CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId_cart(), productId)
        .orElseThrow(() -> new RuntimeException("Error crítico: el producto no está en el carrito"));
        
    cart.setTotal(cart.getTotal() - item.getUnit_price());
    cartRepository.save(cart);

    item.setQuantity(item.getQuantity() - 1); 

    if (item.getQuantity() <= 0) {
        cartItemRepository.delete(item); 
        return Optional.empty(); 
    } 
    
    cartItemRepository.save(item);
    return Optional.of(item);
}
    
}

