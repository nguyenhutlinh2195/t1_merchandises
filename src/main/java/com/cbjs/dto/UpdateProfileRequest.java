package com.cbjs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(hidden = true)
public class UpdateProfileRequest {
    private String name;
}
