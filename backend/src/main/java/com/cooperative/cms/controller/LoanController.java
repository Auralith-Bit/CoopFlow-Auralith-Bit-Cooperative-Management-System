package com.cooperative.cms.controller;

import com.cooperative.cms.dto.LoanDTO;
import com.cooperative.cms.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoanDTO>> getLoansByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(loanService.getLoansByMember(memberId));
    }

    @PostMapping
    public ResponseEntity<LoanDTO> applyForLoan(@RequestBody LoanDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.applyForLoan(dto));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LoanDTO> approveLoan(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(loanService.approveLoan(id, request.get("approvedBy")));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LoanDTO> rejectLoan(@PathVariable Long id, @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(loanService.rejectLoan(id, request.get("notes")));
    }

    @PostMapping("/{id}/payment")
    public ResponseEntity<LoanDTO> makePayment(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        return ResponseEntity.ok(loanService.makePayment(id, request.get("amount")));
    }
}
