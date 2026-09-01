package com.uade.tpo.marketplace.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.JoinColumn;
import java.sql.Blob;

@Data
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_image;

    @Lob
    private Blob image;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product product;
}
