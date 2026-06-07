package com.cooperative.cms.controller;

import com.cooperative.cms.dto.TransactionDTO;
import com.cooperative.cms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/range")
    public ResponseEntity<List<TransactionDTO>> getTransactionsBetween(
            @RequestParam LocalDate start, @RequestParam LocalDate end) {
        return ResponseEntity.ok(transactionService.getTransactionsBetween(start, end));
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(dto));
    }
}
