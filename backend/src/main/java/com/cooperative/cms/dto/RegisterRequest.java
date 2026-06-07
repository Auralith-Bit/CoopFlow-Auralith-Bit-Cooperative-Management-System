package com.cooperative.cms.dto;

import com.cooperative.cms.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    @NotBlank
    @Size(min = 6)
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Role role = Role.MEMBER;
}
