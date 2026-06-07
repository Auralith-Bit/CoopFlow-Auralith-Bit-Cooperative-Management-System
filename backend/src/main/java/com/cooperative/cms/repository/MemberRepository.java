package com.cooperative.cms.repository;

import com.cooperative.cms.entity.Member;
import com.cooperative.cms.entity.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByUserId(Long userId);
    boolean existsByMemberId(String memberId);
    boolean existsByEmail(String email);
    List<Member> findByStatus(MembershipStatus status);
}
