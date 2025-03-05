package com.dofuspulse.api.auth.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.LoginRequest;
import com.dofuspulse.api.auth.RegisterRequest;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import jakarta.transaction.Transactional;
import java.nio.CharBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.SessionRepository;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest extends PostgresIntegrationTestContainer {

  static String testEmail = "test@test.com";
  static String testPassword = "StrongPassword123@";

  @Autowired
  MockMvcTester mvcTester;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  SessionRepository sessionRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    UserPrincipal testUser = new UserPrincipal();
    testUser.setEmail(testEmail);
    testUser.setPassword(passwordEncoder.encode(CharBuffer.wrap(testPassword)).toCharArray());
    testUser.setRole(Role.USER);

    userRepository.save(testUser);
  }

  @Test
  void shouldLoginSuccessfullyAndSessionCreatedAndPersisted() throws Exception {

    LoginRequest loginRequest = LoginRequest.builder()
        .email(testEmail)
        .password(testPassword)
        .build();

    mvcTester.post().uri("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(loginRequest))
        .assertThat()
        .hasStatus(HttpStatus.OK)
        .containsHeader("Set-Cookie")
        .cookies()
        .hasCookieSatisfying("SESSION", sessionCookie ->
            assertThat(sessionCookie.getValue())
                .isNotNull()
                .asBase64Decoded()
                .satisfies(decodedSessionCookie ->
                    assertThat(sessionRepository.findById(new String(decodedSessionCookie)))
                        .isNotNull()));
  }

  @Test
  @DisplayName("Should not login and not create session with invalid credentials")
  void shouldNotLoginAndNotCreateSession() throws Exception {

    LoginRequest loginRequest = LoginRequest.builder()
        .email(testEmail)
        .password("wrongpassword")
        .build();

    mvcTester.post().uri("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(loginRequest))
        .assertThat()
        .hasStatus(HttpStatus.UNAUTHORIZED)
        .cookies()
        .doesNotContainCookie("SESSION");

  }

  @Test
  @DisplayName("Should register user successfully")
  void shouldRegisterUserSuccessfully() throws Exception {
    String newEmail = "newuser@mail.com";
    RegisterRequest registerRequest = RegisterRequest.builder()
        .email(newEmail)
        .password(testPassword.toCharArray())
        .build();

    mvcTester.post().uri("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(registerRequest))
        .assertThat()
        .hasStatus(HttpStatus.OK);

    assertThat(userRepository.findByEmail(newEmail))
        .isPresent()
        .get()
        .satisfies(user -> {
          assertThat(user.getEmail())
              .isEqualTo(newEmail);

          String registerPassword = new String(registerRequest.getPassword());

          assertThat(passwordEncoder.matches(registerPassword, user.getPassword()))
              .isTrue();
        });

  }

  @Test
  @DisplayName("Should return 409 when registering with existing email")
  void shouldReturn409WhenRegisteringWithExistingEmail() throws Exception {
    RegisterRequest registerRequest = RegisterRequest.builder()
        .email(testEmail)
        .password(testPassword.toCharArray())
        .build();

    mvcTester.post().uri("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(registerRequest))
        .assertThat().hasStatus(HttpStatus.CONFLICT);
  }

}
