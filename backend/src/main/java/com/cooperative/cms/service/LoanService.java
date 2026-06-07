package com.cooperative.cms.service;

import com.cooperative.cms.dto.LoanDTO;
import com.cooperative.cms.entity.*;
import com.cooperative.cms.repository.LoanRepository;
import com.cooperative.cms.repository.MemberRepository;
import com.cooperative.cms.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public LoanDTO applyForLoan(LoanDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        BigDecimal monthlyRate = dto.getInterestRate()
                .divide(BigDecimal.valueOf(100 * 12), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal pow = onePlusR.pow(dto.getTenureMonths());
        BigDecimal emi = dto.getAmount()
                .multiply(monthlyRate)
                .multiply(pow)
                .divide(pow.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        BigDecimal totalPayable = emi.multiply(BigDecimal.valueOf(dto.getTenureMonths()));

        Loan loan = Loan.builder()
                .member(member)
                .amount(dto.getAmount())
                .interestRate(dto.getInterestRate())
                .tenureMonths(dto.getTenureMonths())
                .emiAmount(emi)
                .totalPayable(totalPayable)
                .status(LoanStatus.PENDING)
                .build();

        loan = loanRepository.save(loan);
        return toDTO(loan);
    }

    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll().stream().map(this::toDTO).toList();
    }

    public List<LoanDTO> getLoansByMember(Long memberId) {
        return loanRepository.findByMemberIdOrderByAppliedDateDesc(memberId)
                .stream().map(this::toDTO).toList();
    }

    public LoanDTO getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        return toDTO(loan);
    }

    @Transactional
    public LoanDTO approveLoan(Long id, String approvedBy) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Loan is not in pending status");
        }

        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovedDate(LocalDate.now());
        loan.setApprovedBy(approvedBy);
        loan = loanRepository.save(loan);
        return toDTO(loan);
    }

    @Transactional
    public LoanDTO rejectLoan(Long id, String notes) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Loan is not in pending status");
        }

        loan.setStatus(LoanStatus.REJECTED);
        loan.setNotes(notes);
        loan = loanRepository.save(loan);
        return toDTO(loan);
    }

    @Transactional
    public LoanDTO makePayment(Long loanId, BigDecimal paymentAmount) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.APPROVED && loan.getStatus() != LoanStatus.ACTIVE) {
            throw new RuntimeException("Loan is not active");
        }

        BigDecimal remaining = loan.getTotalPayable().subtract(loan.getAmountPaid());
        if (paymentAmount.compareTo(remaining) > 0) {
            paymentAmount = remaining;
        }

        BigDecimal interestPortion = loan.getTotalPayable()
                .subtract(loan.getAmount())
                .divide(BigDecimal.valueOf(loan.getTenureMonths()), 2, RoundingMode.HALF_UP);
        BigDecimal principalPortion = paymentAmount.subtract(interestPortion);
        if (principalPortion.compareTo(BigDecimal.ZERO) < 0) {
            principalPortion = BigDecimal.ZERO;
        }

        Payment payment = Payment.builder()
                .loan(loan)
                .amount(paymentAmount)
                .principalComponent(principalPortion)
                .interestComponent(interestPortion)
                .build();
        paymentRepository.save(payment);

        loan.setAmountPaid(loan.getAmountPaid().add(paymentAmount));
        if (loan.getAmountPaid().compareTo(loan.getTotalPayable()) >= 0) {
            loan.setStatus(LoanStatus.CLOSED);
        } else if (loan.getStatus() == LoanStatus.APPROVED) {
            loan.setStatus(LoanStatus.ACTIVE);
        }
        loan = loanRepository.save(loan);

        return toDTO(loan);
    }

    public long getPendingLoanCount() {
        return loanRepository.countByStatus(LoanStatus.PENDING);
    }

    public long getActiveLoanCount() {
        return loanRepository.countByStatus(LoanStatus.ACTIVE);
    }

    public BigDecimal getTotalLoanAmount() {
        return loanRepository.findAll().stream()
                .map(Loan::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private LoanDTO toDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setMemberId(loan.getMember().getId());
        dto.setMemberName(loan.getMember().getFirstName() + " " + loan.getMember().getLastName());
        dto.setAmount(loan.getAmount());
        dto.setInterestRate(loan.getInterestRate());
        dto.setTenureMonths(loan.getTenureMonths());
        dto.setEmiAmount(loan.getEmiAmount());
        dto.setTotalPayable(loan.getTotalPayable());
        dto.setAmountPaid(loan.getAmountPaid());
        dto.setStatus(loan.getStatus());
        dto.setAppliedDate(loan.getAppliedDate());
        dto.setApprovedDate(loan.getApprovedDate());
        dto.setApprovedBy(loan.getApprovedBy());
        dto.setNotes(loan.getNotes());
        return dto;
    }
}
