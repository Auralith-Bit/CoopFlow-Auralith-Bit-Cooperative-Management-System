package com.cooperative.cms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    @NotBlank
    private String password;
    private String loginType = "MEMBER";
}
