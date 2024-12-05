package com.cbjs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@Schema(hidden = true)
public class AddOrderItemRequest {

    @NotNull(message = "Merchandise cannot be null")
    private Long merchId;

    @NotNull(message = "Quantity cannot be null")
    private Long count;
}
