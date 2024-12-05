package com.cbjs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(hidden = true)
public class Merch {
    private Long id;

    @NotBlank(message = "Name is required")
    @NotNull(message = "Name cannot be null")
    private String name;

    @DecimalMax(value = "9999.99", message = "Price must be less than $99.99")
    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be between 0 and 9999.99")
    private Double price;

    @NotNull(message = "Description cannot be null")
    private String description;

    @NotNull(message = "Image path cannot be null")
    private String imagePath;
}
