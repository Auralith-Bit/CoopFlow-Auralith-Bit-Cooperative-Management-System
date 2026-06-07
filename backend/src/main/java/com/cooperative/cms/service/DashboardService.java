package com.cooperative.cms.service;

import com.cooperative.cms.dto.DashboardDTO;
import com.cooperative.cms.entity.MembershipStatus;
import com.cooperative.cms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final SavingsRepository savingsRepository;
    private final TransactionRepository transactionRepository;

    public DashboardDTO getDashboardData() {
        long totalMembers = memberRepository.count();
        long activeMembers = memberRepository.findAll().stream()
                .filter(m -> m.getStatus() == MembershipStatus.ACTIVE)
                .count();
        long pendingLoans = loanRepository.countByStatus(com.cooperative.cms.entity.LoanStatus.PENDING);
        long activeLoans = loanRepository.countByStatus(com.cooperative.cms.entity.LoanStatus.ACTIVE);

        BigDecimal totalSavings = savingsRepository.findAll().stream()
                .filter(s -> s.getType() == com.cooperative.cms.entity.TransactionType.DEPOSIT)
                .map(com.cooperative.cms.entity.Savings::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(
                        savingsRepository.findAll().stream()
                                .filter(s -> s.getType() == com.cooperative.cms.entity.TransactionType.WITHDRAWAL)
                                .map(com.cooperative.cms.entity.Savings::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );

        BigDecimal totalLoanAmount = loanRepository.findAll().stream()
                .map(com.cooperative.cms.entity.Loan::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate now = LocalDate.now();
        BigDecimal totalIncome = transactionRepository
                .findByTransactionDateBetweenOrderByTransactionDateDesc(
                        startOfMonth.atStartOfDay(), now.atTime(23, 59, 59))
                .stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .map(com.cooperative.cms.entity.Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactionRepository
                .findByTransactionDateBetweenOrderByTransactionDateDesc(
                        startOfMonth.atStartOfDay(), now.atTime(23, 59, 59))
                .stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .map(com.cooperative.cms.entity.Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> statusDistribution = memberRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        m -> m.getStatus().name(),
                        Collectors.counting()
                ));

        return DashboardDTO.builder()
                .totalMembers(totalMembers)
                .activeMembers(activeMembers)
                .pendingLoans(pendingLoans)
                .activeLoans(activeLoans)
                .totalSavings(totalSavings)
                .totalLoanAmount(totalLoanAmount)
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .memberStatusDistribution(statusDistribution)
                .build();
    }
}
