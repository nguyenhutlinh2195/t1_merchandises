package com.cbjs.dto;

import com.cbjs.entity.RoleEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(hidden = true)
public class User {
    private Long id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name is required")
    private String name;

    private String image;

    @Email(message = "Invalid email address")
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email address is required")
    private String email;

    @NotNull(message = "Balance cannot be null")
    @NotBlank(message = "Balance is required")
    private Double balance;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
