package com.cooperative.cms.service;

import com.cooperative.cms.dto.LoginRequest;
import com.cooperative.cms.dto.LoginResponse;
import com.cooperative.cms.dto.RegisterRequest;
import com.cooperative.cms.entity.Member;
import com.cooperative.cms.entity.MembershipStatus;
import com.cooperative.cms.entity.Role;
import com.cooperative.cms.entity.User;
import com.cooperative.cms.repository.MemberRepository;
import com.cooperative.cms.repository.UserRepository;
import com.cooperative.cms.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        String loginType = request.getLoginType() != null ? request.getLoginType() : "MEMBER";

        User user;

        if ("STAFF".equals(loginType)) {
            if (request.getUsername() == null || request.getUsername().isBlank()) {
                throw new BadCredentialsException("Staff ID is required");
            }
            user = userRepository.findByStaffId(request.getUsername().trim())
                    .orElseThrow(() -> new BadCredentialsException("Invalid staff ID or password"));
            if (user.getRole() == Role.MEMBER || user.getRole() == Role.ADMIN) {
                throw new BadCredentialsException("Invalid staff ID or password");
            }
        } else if ("ADMIN".equals(loginType)) {
            if (request.getUsername() == null || request.getUsername().isBlank()) {
                throw new BadCredentialsException("Admin ID is required");
            }
            user = userRepository.findByStaffId(request.getUsername().trim())
                    .or(() -> userRepository.findByUsername(request.getUsername().trim()))
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
            if (user.getRole() != Role.ADMIN) {
                throw new BadCredentialsException("Invalid credentials");
            }
        } else {
            if (request.getUsername() == null || request.getUsername().isBlank()) {
                throw new BadCredentialsException("Username is required");
            }
            user = userRepository.findByUsername(request.getUsername().trim())
                    .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
            if (user.getRole() != Role.MEMBER) {
                throw new BadCredentialsException("Invalid username or password");
            }
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("Account not yet approved. Please wait for admin approval.");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .staffId(user.getStaffId())
                .build();
    }

    public void register(RegisterRequest request) {
        String username = request.getUsername() != null && !request.getUsername().isBlank()
                ? request.getUsername() : request.getEmail();

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.MEMBER)
                .enabled(false)
                .build();

        user = userRepository.save(user);

        Member member = Member.builder()
                .memberId("MEM" + String.format("%04d", user.getId()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .user(user)
                .status(MembershipStatus.PENDING)
                .build();
        memberRepository.save(member);
    }
}
