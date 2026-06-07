package com.cooperative.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MemberDashboardDTO {
    private MemberDTO member;
    private BigDecimal savingsBalance;
    private List<SavingsDTO> recentTransactions;
    private List<LoanDTO> activeLoans;
    private long totalLoans;
    private long pendingLoans;
}
