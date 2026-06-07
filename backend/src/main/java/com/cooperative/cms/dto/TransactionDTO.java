package com.cooperative.cms.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String category;
    private String description;
    private LocalDateTime transactionDate;
    private String createdBy;
}
