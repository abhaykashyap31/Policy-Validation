package com.company.travelplanner.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.company.travelplanner.service.AuthService;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    private final AuthService authService;

    public AdminDataInitializer(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(String... args) {
        authService.createDefaultAdminIfMissing();
    }
}
