package com.cooperative.cms.dto;

import com.cooperative.cms.entity.LoanStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanDTO {
    private Long id;
    private Long memberId;
    private String memberName;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emiAmount;
    private BigDecimal totalPayable;
    private BigDecimal amountPaid;
    private LoanStatus status;
    private LocalDate appliedDate;
    private LocalDate approvedDate;
    private String approvedBy;
    private String notes;
}
