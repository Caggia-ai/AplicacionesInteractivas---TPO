package com.uade.tpo.marketplace.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import jakarta.persistence.JoinColumn;

@Data
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_producto;
    
    @Column
    private String nombre;
    @Column
    private String descripcion;
    @Column
    private int precio;
    @Column
    private int stock;
    @Column
    private int porcentaje_descuento;
    @Column
    private boolean estado;

    // @OneToMany(mappedBy = "producto")
    // private List<DetalleCompra> detalleCompras;
    @OneToMany(mappedBy = "producto")
    private List<Imagen> imagenes;
    @OneToMany(mappedBy = "producto")
    private List<ProductoCarrito> productosCarrito;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    
}
