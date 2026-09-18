package com.company.travelplanner;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.company.travelplanner.dto.LoginRequest;
import com.company.travelplanner.dto.LoginResponse;
import com.company.travelplanner.service.AuthService;

@SpringBootTest
class AdminAuthIntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    void adminLoginShouldReturnToken() {
        LoginResponse response = authService.login(new LoginRequest("admin@company.com", "Admin@123"));

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("admin@company.com");
        assertThat(response.token()).isNotBlank();
        assertThat(response.role()).isEqualTo("ROLE_ADMIN");
    }
}
