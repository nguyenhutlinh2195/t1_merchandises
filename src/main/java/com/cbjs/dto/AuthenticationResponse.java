package com.cbjs.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(hidden = true)
public class AuthenticationResponse {

    private String token;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone="Asia/Ho_Chi_Minh")
    private Date expiration;

    private User user;
}
