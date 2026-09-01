package com.uade.tpo.marketplace.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponse {
  private Long id;
  private String file;
}
