package com.cbjs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class RegisterRequest {
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password is required")
    private String password;
}
