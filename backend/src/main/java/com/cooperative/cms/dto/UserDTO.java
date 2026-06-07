package com.cooperative.cms.dto;

import com.cooperative.cms.entity.Role;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String staffId;
    private String email;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdAt;
}
