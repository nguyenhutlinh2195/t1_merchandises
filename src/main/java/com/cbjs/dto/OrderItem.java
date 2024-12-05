package com.cbjs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(hidden = true)
public class OrderItem {
    private Long id;

    private String merchName;

    private Long merchId;

    private String merchImage;

    private String address;

    private Double total;

    private Long count;
}
