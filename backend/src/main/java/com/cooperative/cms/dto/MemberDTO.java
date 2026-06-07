package com.cooperative.cms.dto;

import com.cooperative.cms.entity.MembershipStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberDTO {
    private Long id;
    private String memberId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private LocalDate dateOfBirth;
    private LocalDate joinDate;
    private MembershipStatus status;
    private Long userId;
}
