package com.cooperative.cms.service;

import com.cooperative.cms.dto.MemberDTO;
import com.cooperative.cms.entity.Member;
import com.cooperative.cms.entity.MembershipStatus;
import com.cooperative.cms.entity.User;
import com.cooperative.cms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final SavingsRepository savingsRepository;
    private final LoanRepository loanRepository;

    @Transactional
    public MemberDTO createMember(MemberDTO dto) {
        if (memberRepository.existsByMemberId(dto.getMemberId())) {
            throw new RuntimeException("Member ID already exists");
        }

        Member member = new Member();
        member.setMemberId(dto.getMemberId());
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());
        member.setCity(dto.getCity());
        member.setState(dto.getState());
        member.setZipCode(dto.getZipCode());
        member.setDateOfBirth(dto.getDateOfBirth());
        member.setStatus(MembershipStatus.ACTIVE);

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            member.setUser(user);
        }

        member = memberRepository.save(member);
        return toDTO(member);
    }

    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll().stream().map(this::toDTO).toList();
    }

    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return toDTO(member);
    }

    public MemberDTO getMemberByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return toDTO(member);
    }

    @Transactional
    public MemberDTO updateMember(Long id, MemberDTO dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());
        member.setCity(dto.getCity());
        member.setState(dto.getState());
        member.setZipCode(dto.getZipCode());
        member.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getStatus() != null) {
            member.setStatus(dto.getStatus());
        }

        member = memberRepository.save(member);
        return toDTO(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        User user = member.getUser();

        savingsRepository.deleteAll(savingsRepository.findByMemberIdOrderByTransactionDateDesc(id));
        loanRepository.deleteAll(loanRepository.findByMemberIdOrderByAppliedDateDesc(id));

        memberRepository.delete(member);

        if (user != null) {
            userRepository.delete(user);
        }
    }

    public long getTotalMembers() {
        return memberRepository.count();
    }

    public long getActiveMembers() {
        return memberRepository.findAll().stream()
                .filter(m -> m.getStatus() == MembershipStatus.ACTIVE)
                .count();
    }

    private MemberDTO toDTO(Member member) {
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
        dto.setDateOfBirth(member.getDateOfBirth());
        dto.setJoinDate(member.getJoinDate());
        dto.setStatus(member.getStatus());
        if (member.getUser() != null) {
            dto.setUserId(member.getUser().getId());
        }
        return dto;
    }
}
