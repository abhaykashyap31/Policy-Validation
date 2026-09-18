package com.company.travelplanner.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.travelplanner.dto.LoginRequest;
import com.company.travelplanner.dto.LoginResponse;
import com.company.travelplanner.entity.AdminUser;
import com.company.travelplanner.repository.AdminUserRepository;
import com.company.travelplanner.security.JwtUtil;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AdminUserRepository adminUserRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       AdminUserRepository adminUserRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.adminUserRepository = adminUserRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        if (!authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        AdminUser adminUser = adminUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        String role = adminUser.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        String token = jwtUtil.generateToken(adminUser.getEmail(), role);
        return new LoginResponse(token, adminUser.getEmail(), role);
    }

    public void createDefaultAdminIfMissing() {
        if (adminUserRepository.existsByEmail("admin@company.com")) {
            return;
        }

        AdminUser adminUser = new AdminUser();
        adminUser.setEmail("admin@company.com");
        adminUser.setPassword(passwordEncoder.encode("Admin@123"));
        adminUser.setRole("ADMIN");
        adminUser.setEnabled(true);
        adminUserRepository.save(adminUser);
    }
}
