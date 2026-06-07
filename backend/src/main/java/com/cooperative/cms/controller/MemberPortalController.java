package com.cooperative.cms.controller;

import com.cooperative.cms.dto.LoanDTO;
import com.cooperative.cms.dto.MemberDashboardDTO;
import com.cooperative.cms.dto.MemberDTO;
import com.cooperative.cms.dto.SavingsDTO;
import com.cooperative.cms.entity.LoanStatus;
import com.cooperative.cms.entity.Member;
import com.cooperative.cms.entity.User;
import com.cooperative.cms.repository.MemberRepository;
import com.cooperative.cms.security.SecurityUtil;
import com.cooperative.cms.service.LoanService;
import com.cooperative.cms.service.SavingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberPortalController {

    private final SecurityUtil securityUtil;
    private final MemberRepository memberRepository;
    private final SavingsService savingsService;
    private final LoanService loanService;

    @GetMapping("/me")
    public ResponseEntity<MemberDTO> getMyProfile() {
        Member member = getCurrentMember();
        return ResponseEntity.ok(toMemberDTO(member));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<MemberDashboardDTO> getDashboard() {
        Member member = getCurrentMember();

        BigDecimal balance = savingsService.getBalance(member.getId());
        List<SavingsDTO> recentTxns = savingsService.getSavingsHistory(member.getId());
        if (recentTxns.size() > 10) {
            recentTxns = recentTxns.subList(0, 10);
        }
        List<LoanDTO> allLoans = loanService.getLoansByMember(member.getId());
        List<LoanDTO> activeLoans = allLoans.stream()
                .filter(l -> l.getStatus() == LoanStatus.ACTIVE || l.getStatus() == LoanStatus.APPROVED)
                .toList();
        long pendingLoans = allLoans.stream()
                .filter(l -> l.getStatus() == LoanStatus.PENDING)
                .count();

        return ResponseEntity.ok(MemberDashboardDTO.builder()
                .member(toMemberDTO(member))
                .savingsBalance(balance)
                .recentTransactions(recentTxns)
                .activeLoans(activeLoans)
                .totalLoans(allLoans.size())
                .pendingLoans(pendingLoans)
                .build());
    }

    @GetMapping("/passbook")
    public ResponseEntity<Map<String, Object>> getPassbook() {
        Member member = getCurrentMember();
        BigDecimal balance = savingsService.getBalance(member.getId());
        List<SavingsDTO> transactions = savingsService.getSavingsHistory(member.getId());

        return ResponseEntity.ok(Map.of(
                "balance", balance,
                "transactions", transactions
        ));
    }

    @GetMapping("/loans")
    public ResponseEntity<List<LoanDTO>> getMyLoans() {
        Member member = getCurrentMember();
        return ResponseEntity.ok(loanService.getLoansByMember(member.getId()));
    }

    @PostMapping("/loans/apply")
    public ResponseEntity<LoanDTO> applyForLoan(@RequestBody LoanDTO dto) {
        Member member = getCurrentMember();
        dto.setMemberId(member.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.applyForLoan(dto));
    }

    @PostMapping("/savings/deposit")
    public ResponseEntity<SavingsDTO> deposit(@RequestBody SavingsDTO dto) {
        Member member = getCurrentMember();
        dto.setMemberId(member.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savingsService.recordDeposit(dto));
    }

    @PostMapping("/savings/withdraw")
    public ResponseEntity<SavingsDTO> withdraw(@RequestBody SavingsDTO dto) {
        Member member = getCurrentMember();
        dto.setMemberId(member.getId());
        return ResponseEntity.ok(savingsService.recordWithdrawal(dto));
    }

    private Member getCurrentMember() {
        User user = securityUtil.getCurrentUser();
        return memberRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("No member profile linked to this user"));
    }

    private MemberDTO toMemberDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setMemberId(member.getMemberId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setAddress(member.getAddress());
        dto.setCity(member.getCity());
        dto.setState(member.getState());
        dto.setZipCode(member.getZipCode());
        dto.setJoinDate(member.getJoinDate());
        dto.setStatus(member.getStatus());
        return dto;
    }
}
