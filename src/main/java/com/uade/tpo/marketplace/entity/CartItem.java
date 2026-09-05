package com.uade.tpo.marketplace.entity;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cart_item;

    //@Column
    //private int unit_price;
    @Column
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "id_cart", nullable = false)
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;
}
