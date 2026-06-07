package com.cooperative.cms.controller;

import com.cooperative.cms.dto.CreateUserRequest;
import com.cooperative.cms.dto.MemberDTO;
import com.cooperative.cms.dto.UserDTO;
import com.cooperative.cms.entity.Member;
import com.cooperative.cms.entity.MembershipStatus;
import com.cooperative.cms.entity.User;
import com.cooperative.cms.repository.MemberRepository;
import com.cooperative.cms.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userRepository.findAll().stream()
                .map(this::toUserDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().build();
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        String staffId = generateStaffId();

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole())
                .staffId(staffId)
                .enabled(true)
                .build();

        user = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toUserDTO(user));
    }

    @GetMapping("/members/pending")
    public ResponseEntity<List<MemberDTO>> getPendingMembers() {
        List<MemberDTO> members = memberRepository.findByStatus(MembershipStatus.PENDING)
                .stream().map(this::toMemberDTO)
                .toList();
        return ResponseEntity.ok(members);
    }

    @PutMapping("/members/{id}/approve")
    public ResponseEntity<Void> approveMember(@PathVariable Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (member.getStatus() != MembershipStatus.PENDING) {
            return ResponseEntity.badRequest().build();
        }

        User user = member.getUser();
        if (user != null) {
            user.setEnabled(true);
            userRepository.save(user);
        }

        member.setStatus(MembershipStatus.ACTIVE);
        memberRepository.save(member);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}/reject")
    public ResponseEntity<Void> rejectMember(@PathVariable Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (member.getStatus() != MembershipStatus.PENDING) {
            return ResponseEntity.badRequest().build();
        }

        User user = member.getUser();
        memberRepository.delete(member);
        if (user != null) {
            userRepository.delete(user);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private String generateStaffId() {
        long count = userRepository.count();
        return "STF" + String.format("%04d", count + 1);
    }

    private UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setStaffId(user.getStaffId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
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
        if (member.getUser() != null) {
            dto.setUserId(member.getUser().getId());
        }
        return dto;
    }
}
