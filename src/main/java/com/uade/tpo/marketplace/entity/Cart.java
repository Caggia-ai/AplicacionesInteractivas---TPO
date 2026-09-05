package com.uade.tpo.marketplace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cart;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    //@Column
    //private int total;
    @Column
    private String expiration;
    @Column
    private boolean state;

    @JsonIgnore
    @OneToMany(mappedBy = "cart")
    private java.util.List<CartItem> productosCarrito;
}
