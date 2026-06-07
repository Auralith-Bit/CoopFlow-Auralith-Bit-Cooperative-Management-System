package com.cooperative.cms.dto;

import com.cooperative.cms.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String email;
    private Role role;
    private String staffId;
}
