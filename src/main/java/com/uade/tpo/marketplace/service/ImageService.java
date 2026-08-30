package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.entity.Image;

public interface ImageService {
    Image addImageToProduct(Long productId, String url);
}