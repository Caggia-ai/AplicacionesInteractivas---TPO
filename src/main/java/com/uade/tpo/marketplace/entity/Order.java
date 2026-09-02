package com.uade.tpo.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_order;
    
    @Column
    private String payment_method;
    @Column
    private String date;
    @Column
    private int total;
    @Column
    private String delivery_method;
    @Column
    private String shipping_time;
    @Column
    private String origin;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
}
