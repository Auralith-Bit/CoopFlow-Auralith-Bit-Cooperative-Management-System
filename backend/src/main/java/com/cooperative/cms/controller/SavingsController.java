package com.cooperative.cms.controller;

import com.cooperative.cms.dto.SavingsDTO;
import com.cooperative.cms.service.SavingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;

    @PostMapping("/deposit")
    public ResponseEntity<SavingsDTO> deposit(@RequestBody SavingsDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(savingsService.recordDeposit(dto));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<SavingsDTO> withdraw(@RequestBody SavingsDTO dto) {
        return ResponseEntity.ok(savingsService.recordWithdrawal(dto));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<SavingsDTO>> getSavingsHistory(@PathVariable Long memberId) {
        return ResponseEntity.ok(savingsService.getSavingsHistory(memberId));
    }

    @GetMapping("/member/{memberId}/balance")
    public ResponseEntity<Map<String, BigDecimal>> getBalance(@PathVariable Long memberId) {
        return ResponseEntity.ok(Map.of("balance", savingsService.getBalance(memberId)));
    }
}
