package com.cbjs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "merch")
public class MerchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(unique = true)
    @NotNull(message = "Name cannot be null")
    private String name;

    @DecimalMax(value = "9999.99", message = "Price must be less than $99.99")
    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be between 0 and 9999.99")
    private Double price;

    private String description;

    private String imagePath;

}
