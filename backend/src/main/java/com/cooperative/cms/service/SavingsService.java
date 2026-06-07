package com.cooperative.cms.service;

import com.cooperative.cms.dto.SavingsDTO;
import com.cooperative.cms.entity.Member;
import com.cooperative.cms.entity.Savings;
import com.cooperative.cms.entity.TransactionType;
import com.cooperative.cms.repository.MemberRepository;
import com.cooperative.cms.repository.SavingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingsService {

    private final SavingsRepository savingsRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SavingsDTO recordDeposit(SavingsDTO dto) {
        return recordTransaction(dto, TransactionType.DEPOSIT);
    }

    @Transactional
    public SavingsDTO recordWithdrawal(SavingsDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        BigDecimal currentBalance = getBalance(member.getId());
        if (currentBalance.compareTo(dto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        return recordTransaction(dto, TransactionType.WITHDRAWAL);
    }

    private SavingsDTO recordTransaction(SavingsDTO dto, TransactionType type) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        BigDecimal balanceBefore = getBalance(member.getId());
        BigDecimal balanceAfter = type == TransactionType.DEPOSIT
                ? balanceBefore.add(dto.getAmount())
                : balanceBefore.subtract(dto.getAmount());

        Savings savings = Savings.builder()
                .member(member)
                .amount(dto.getAmount())
                .type(type)
                .description(dto.getDescription())
                .balanceAfter(balanceAfter)
                .build();

        savings = savingsRepository.save(savings);
        return toDTO(savings);
    }

    public List<SavingsDTO> getSavingsHistory(Long memberId) {
        return savingsRepository.findByMemberIdOrderByTransactionDateDesc(memberId)
                .stream().map(this::toDTO).toList();
    }

    public BigDecimal getBalance(Long memberId) {
        return savingsRepository.getBalanceByMemberId(memberId);
    }

    public BigDecimal getTotalSavings() {
        List<Savings> all = savingsRepository.findAll();
        return all.stream()
                .filter(s -> s.getType() == TransactionType.DEPOSIT)
                .map(Savings::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(
                        all.stream()
                                .filter(s -> s.getType() == TransactionType.WITHDRAWAL)
                                .map(Savings::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );
    }

    private SavingsDTO toDTO(Savings savings) {
        SavingsDTO dto = new SavingsDTO();
        dto.setId(savings.getId());
        dto.setMemberId(savings.getMember().getId());
        dto.setMemberName(savings.getMember().getFirstName() + " " + savings.getMember().getLastName());
        dto.setAmount(savings.getAmount());
        dto.setType(savings.getType());
        dto.setDescription(savings.getDescription());
        dto.setTransactionDate(savings.getTransactionDate());
        dto.setBalanceAfter(savings.getBalanceAfter());
        return dto;
    }
}
