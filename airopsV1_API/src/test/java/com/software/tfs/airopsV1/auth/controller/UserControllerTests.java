package com.software.tfs.airopsV1.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.tfs.airopsV1.auth.dto.request.LoginRequest;
import com.software.tfs.airopsV1.auth.dto.request.RegistrationRequest;
import com.software.tfs.airopsV1.auth.dto.response.LoginResponse;
import com.software.tfs.airopsV1.auth.dto.response.RegistrationResponse;
import com.software.tfs.airopsV1.auth.repo.UserRepository;
import com.software.tfs.airopsV1.auth.service.JwtService;
import com.software.tfs.airopsV1.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationManager authenticationManager;


    // ===== xyz() Endpoint Tests =====

    @Test
    void xyz_NoExceptionThrown() throws Exception {
        mockMvc.perform(get("/ao/api/auth/xyz"))
                .andExpect(status().isOk());
    }

    // ===== register() Endpoint Tests =====

    @Test
    void register_WithValidRequest_ReturnsSuccess() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("newuser@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPasswordPlainText("Password123!");
        request.setCompanyName("TechCorp");

        UUID userId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        String token = "jwt-token-12345";
        RegistrationResponse response = new RegistrationResponse(
                "newuser@example.com",
                token,
                userId,
                companyId
        );

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.companyId").value(companyId.toString()));

        verify(userService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void register_CallsUserServiceOnce() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@example.com");
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setPasswordPlainText("SecurePass123!");
        request.setCompanyName("InnovateLabs");

        RegistrationResponse response = new RegistrationResponse(
                "test@example.com",
                "token",
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Assert
        verify(userService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void register_VerifiesRequestBodyPassed() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("verify@example.com");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPasswordPlainText("TestPass123!");
        request.setCompanyName("TestCompany");

        RegistrationResponse response = new RegistrationResponse(
                "verify@example.com",
                "token",
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).register(argThat(req ->
                req.getEmail().equals("verify@example.com") &&
                req.getFirstName().equals("Test") &&
                req.getLastName().equals("User") &&
                req.getPasswordPlainText().equals("TestPass123!") &&
                req.getCompanyName().equals("TestCompany")
        ));
    }

    @Test
    void register_WithMultipleInvocations_CallsServiceMultipleTimes() throws Exception {
        // Arrange
        RegistrationRequest request1 = new RegistrationRequest();
        request1.setEmail("user1@example.com");
        request1.setFirstName("User");
        request1.setLastName("One");
        request1.setPasswordPlainText("Pass123!");
        request1.setCompanyName("Company1");

        RegistrationRequest request2 = new RegistrationRequest();
        request2.setEmail("user2@example.com");
        request2.setFirstName("User");
        request2.setLastName("Two");
        request2.setPasswordPlainText("Pass123!");
        request2.setCompanyName("Company2");

        RegistrationResponse response1 = new RegistrationResponse("user1@example.com", "token1", UUID.randomUUID(), UUID.randomUUID());
        RegistrationResponse response2 = new RegistrationResponse("user2@example.com", "token2", UUID.randomUUID(), UUID.randomUUID());

        when(userService.register(any(RegistrationRequest.class)))
                .thenReturn(response1)
                .thenReturn(response2);

        // Act
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk());

        // Assert
        verify(userService, times(2)).register(any(RegistrationRequest.class));
    }

    @Test
    void register_ResponseContainsAllFields() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("complete@example.com");
        request.setFirstName("Complete");
        request.setLastName("Response");
        request.setPasswordPlainText("CompletePass123!");
        request.setCompanyName("CompleteCompany");

        UUID userId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        RegistrationResponse response = new RegistrationResponse("complete@example.com", "complete-token", userId, companyId);

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.companyId").exists());
    }

    // ===== login() Endpoint Tests =====

    @Test
    void login_WithValidCredentials_ReturnsSuccess() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("user@example.com", "Password123!");

        UUID userId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        String token = "jwt-login-token";
        LoginResponse response = new LoginResponse(userId, "user@example.com", companyId, token);

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.companyId").value(companyId.toString()));

        verify(userService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void login_CallsUserServiceOnce() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("login@example.com", "SecurePassword!");

        LoginResponse response = new LoginResponse(
                UUID.randomUUID(),
                "login@example.com",
                UUID.randomUUID(),
                "login-token"
        );

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        // Act
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Assert
        verify(userService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void login_VerifiesRequestBodyPassed() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("verify@example.com", "VerifyPass123!");

        LoginResponse response = new LoginResponse(
                UUID.randomUUID(),
                "verify@example.com",
                UUID.randomUUID(),
                "verify-token"
        );

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).login(argThat(req ->
                req.getEmail().equals("verify@example.com") &&
                req.getPassword().equals("VerifyPass123!")
        ));
    }

    @Test
    void login_WithMultipleInvocations_CallsServiceMultipleTimes() throws Exception {
        // Arrange
        LoginRequest request1 = new LoginRequest("user1@example.com", "Pass1!");
        LoginRequest request2 = new LoginRequest("user2@example.com", "Pass2!");

        LoginResponse response1 = new LoginResponse(UUID.randomUUID(), "user1@example.com", UUID.randomUUID(), "token1");
        LoginResponse response2 = new LoginResponse(UUID.randomUUID(), "user2@example.com", UUID.randomUUID(), "token2");

        when(userService.login(any(LoginRequest.class)))
                .thenReturn(response1)
                .thenReturn(response2);

        // Act
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk());

        // Assert
        verify(userService, times(2)).login(any(LoginRequest.class));
    }

    @Test
    void login_ResponseContainsAllFields() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("complete@example.com", "CompletePass!");

        UUID userId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        LoginResponse response = new LoginResponse(userId, "complete@example.com", companyId, "complete-token");

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.companyId").exists())
                .andExpect(jsonPath("$.token").exists());
    }

    // ===== Edge Cases and Error Scenarios =====

    @Test
    void register_WithNullRequest_HandledBySpring() throws Exception {
        // Spring will return 400 for null/invalid JSON
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());

        verify(userService, never()).register(any());
    }

    @Test
    void login_WithNullRequest_HandledBySpring() throws Exception {
        // Spring will return 400 for null/invalid JSON
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());

        verify(userService, never()).login(any());
    }

    @Test
    void register_WithEmptyEmail_RequestIsPassedToService() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPasswordPlainText("Pass123!");
        request.setCompanyName("TestCorp");

        RegistrationResponse response = new RegistrationResponse("", "token", UUID.randomUUID(), UUID.randomUUID());

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_WithEmptyEmail_RequestIsPassedToService() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("", "Password!");

        LoginResponse response = new LoginResponse(UUID.randomUUID(), "", UUID.randomUUID(), "token");

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    void register_WithSpecialCharactersInEmail_RequestIsPassedToService() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test+special@example.com");
        request.setFirstName("Special");
        request.setLastName("User");
        request.setPasswordPlainText("Pass123!");
        request.setCompanyName("SpecialCorp");

        RegistrationResponse response = new RegistrationResponse(
                "test+special@example.com",
                "token",
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test+special@example.com"));

        verify(userService).register(any(RegistrationRequest.class));
    }

    @Test
    void login_WithSpecialCharactersInEmail_RequestIsPassedToService() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("test+special@example.com", "Pass!");

        LoginResponse response = new LoginResponse(UUID.randomUUID(), "test+special@example.com", UUID.randomUUID(), "token");

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test+special@example.com"));

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    void register_WithLongEmail_RequestIsPassedToService() throws Exception {
        // Arrange
        String longEmail = "very.long.email.address.with.many.characters@subdomain.example.com";
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail(longEmail);
        request.setFirstName("Long");
        request.setLastName("Email");
        request.setPasswordPlainText("Pass123!");
        request.setCompanyName("LongEmailCorp");

        RegistrationResponse response = new RegistrationResponse(longEmail, "token", UUID.randomUUID(), UUID.randomUUID());

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).register(any(RegistrationRequest.class));
    }

    @Test
    void register_ResponseTokenNotNull() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("token@example.com");
        request.setFirstName("Token");
        request.setLastName("Test");
        request.setPasswordPlainText("Pass123!");
        request.setCompanyName("TokenCorp");

        RegistrationResponse response = new RegistrationResponse(
                "token@example.com",
                "non-null-token-value",
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("non-null-token-value"));

        verify(userService).register(any(RegistrationRequest.class));
    }

    @Test
    void login_ResponseTokenNotNull() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("token@example.com", "Pass!");

        LoginResponse response = new LoginResponse(
                UUID.randomUUID(),
                "token@example.com",
                UUID.randomUUID(),
                "non-null-token-value"
        );

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("non-null-token-value"));

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    void register_UserIdNotNull() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("userid@example.com");
        request.setFirstName("UserId");
        request.setLastName("Test");
        request.setPasswordPlainText("Pass123!");
        request.setCompanyName("UserIdCorp");

        UUID userId = UUID.randomUUID();
        RegistrationResponse response = new RegistrationResponse(
                "userid@example.com",
                "token",
                userId,
                UUID.randomUUID()
        );

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(userService).register(any(RegistrationRequest.class));
    }

    @Test
    void login_UserIdNotNull() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("userid@example.com", "Pass!");

        UUID userId = UUID.randomUUID();
        LoginResponse response = new LoginResponse(
                userId,
                "userid@example.com",
                UUID.randomUUID(),
                "token"
        );

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    void register_CompanyIdNotNull() throws Exception {
        // Arrange
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("companyid@example.com");
        request.setFirstName("Company");
        request.setLastName("Test");
        request.setPasswordPlainText("Pass123!");
        request.setCompanyName("CompanyIdCorp");

        UUID companyId = UUID.randomUUID();
        RegistrationResponse response = new RegistrationResponse(
                "companyid@example.com",
                "token",
                UUID.randomUUID(),
                companyId
        );

        when(userService.register(any(RegistrationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value(companyId.toString()));

        verify(userService).register(any(RegistrationRequest.class));
    }

    @Test
    void login_CompanyIdNotNull() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("companyid@example.com", "Pass!");

        UUID companyId = UUID.randomUUID();
        LoginResponse response = new LoginResponse(
                UUID.randomUUID(),
                "companyid@example.com",
                companyId,
                "token"
        );

        when(userService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/ao/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value(companyId.toString()));

        verify(userService).login(any(LoginRequest.class));
    }
}
