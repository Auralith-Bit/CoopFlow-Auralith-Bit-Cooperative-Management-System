package com.cooperative.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class DashboardDTO {
    private long totalMembers;
    private long activeMembers;
    private long pendingLoans;
    private long activeLoans;
    private BigDecimal totalSavings;
    private BigDecimal totalLoanAmount;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private Map<String, Long> memberStatusDistribution;
}
