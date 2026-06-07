package com.cooperative.cms.dto;

import com.cooperative.cms.entity.TransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SavingsDTO {
    private Long id;
    private Long memberId;
    private String memberName;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDateTime transactionDate;
    private BigDecimal balanceAfter;
}
