package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.marketplace.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
