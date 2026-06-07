package com.cooperative.cms.service;

import com.cooperative.cms.dto.TransactionDTO;
import com.cooperative.cms.entity.Transaction;
import com.cooperative.cms.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO dto) {
        Transaction transaction = Transaction.builder()
                .amount(dto.getAmount())
                .type(dto.getType())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .createdBy(dto.getCreatedBy())
                .build();

        transaction = transactionRepository.save(transaction);
        return toDTO(transaction);
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::toDTO).toList();
    }

    public List<TransactionDTO> getTransactionsBetween(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
        return transactionRepository
                .findByTransactionDateBetweenOrderByTransactionDateDesc(startDateTime, endDateTime)
                .stream().map(this::toDTO).toList();
    }

    public BigDecimal getTotalIncome(LocalDate start, LocalDate end) {
        return getTransactionsBetween(start, end).stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalExpenses(LocalDate start, LocalDate end) {
        return getTransactionsBetween(start, end).stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setCategory(transaction.getCategory());
        dto.setDescription(transaction.getDescription());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setCreatedBy(transaction.getCreatedBy());
        return dto;
    }
}
