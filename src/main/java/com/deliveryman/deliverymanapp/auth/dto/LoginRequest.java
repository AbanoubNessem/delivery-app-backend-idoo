package com.deliveryman.deliverymanapp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Login name is required")
    private String loginName;

    @NotBlank(message = "Password is required")
    private String password;
}
