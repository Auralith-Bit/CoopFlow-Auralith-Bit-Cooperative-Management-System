package com.cooperative.cms.repository;

import com.cooperative.cms.entity.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface SavingsRepository extends JpaRepository<Savings, Long> {
    List<Savings> findByMemberIdOrderByTransactionDateDesc(Long memberId);

    @Query("SELECT COALESCE(SUM(CASE WHEN s.type = 'DEPOSIT' THEN s.amount ELSE -s.amount END), 0) FROM Savings s WHERE s.member.id = :memberId")
    BigDecimal getBalanceByMemberId(@Param("memberId") Long memberId);
}
