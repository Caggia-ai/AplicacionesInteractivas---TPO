package com.uade.tpo.marketplace.entity.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddFileRequest {
  private String name;
  private MultipartFile file;
  private Long productId;
}
