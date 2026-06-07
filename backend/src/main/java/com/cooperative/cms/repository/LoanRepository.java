package com.cooperative.cms.repository;

import com.cooperative.cms.entity.Loan;
import com.cooperative.cms.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByMemberIdOrderByAppliedDateDesc(Long memberId);
    List<Loan> findByStatus(LoanStatus status);
    long countByStatus(LoanStatus status);
}
