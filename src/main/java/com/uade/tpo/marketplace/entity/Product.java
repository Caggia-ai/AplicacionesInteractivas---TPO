package com.uade.tpo.marketplace.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import jakarta.persistence.JoinColumn;

@Data
@Entity
public class Product {

    public Product(String name, String description, int price, int stock, int discount_percentage){
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.discount_percentage = discount_percentage;
        this.state = true;
    }
    public Product(){}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_product;
    
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private int price;
    @Column
    private int stock;
    @Column
    private int discount_percentage;
    @Column
    private boolean state;

    // @OneToMany(mappedBy = "producto")
    // private List<DetalleCompra> detalleCompras;
    
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Image> images;
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    
}
