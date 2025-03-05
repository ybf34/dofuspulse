package com.dofuspulse.api.auth.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.auth.AuthService;
import com.dofuspulse.api.auth.LoginRequest;
import com.dofuspulse.api.auth.RegisterRequest;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserRepository;
import com.dofuspulse.api.auth.UserService;
import com.dofuspulse.api.exception.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import java.nio.CharBuffer;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Transactional
@DisplayName("Auth service integration test")
public class AuthServiceIntegrationTest extends PostgresIntegrationTestContainer {

  static String testEmail = "test@test.com";
  static String testPassword = "StrongPassword123@";

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  @Autowired
  AuthService authService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    UserPrincipal user = new UserPrincipal();
    user.setEmail(testEmail);
    user.setPassword(passwordEncoder.encode(CharBuffer.wrap(testPassword)).toCharArray());
    user.setRole(Role.USER);

    userRepository.save(user);
  }

  @Test
  @DisplayName("Should authenticate successfully and create session")
  void shouldAuthenticateSuccessfullyAndCreateSession() {
    LoginRequest loginRequest = LoginRequest.builder()
        .email(testEmail)
        .password(testPassword)
        .build();

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    authService.loginAttempt(loginRequest, request, response);

    assertThat(SecurityContextHolder.getContext().getAuthentication())
        .isNotNull()
        .satisfies(auth -> {
          assertThat(auth.isAuthenticated())
              .isTrue();
          assertThat(auth.getPrincipal())
              .isNotNull()
              .isInstanceOf(UserPrincipal.class)
              .asInstanceOf(InstanceOfAssertFactories.type(UserPrincipal.class))
              .extracting(UserPrincipal::getEmail, UserPrincipal::getRole)
              .containsExactly(testEmail, Role.USER);

          assertThat(auth.getAuthorities()).anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        });

    MockHttpSession session = (MockHttpSession) request.getSession(false);
    assertThat(session).isNotNull();
  }

  @Test
  @DisplayName("Authenticate with wrong password should throw BadCredential and not create session")
  void shouldNotAuthenticateWithWrongPasswordAndNotCreateSession() {
    LoginRequest loginRequest = LoginRequest.builder()
        .email(testEmail)
        .password("wrongpassword")
        .build();

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    assertThrows(BadCredentialsException.class, () ->
        authService.loginAttempt(loginRequest, request, response));

    MockHttpSession session = (MockHttpSession) request.getSession(false);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(session).isNull();
  }

  @Test
  @DisplayName("Should not authenticate when wrong Email provided")
  void shouldNotAuthenticateWithWrongEmailAndNotCreateSession() {
    LoginRequest loginRequest = LoginRequest.builder()
        .email("wrongemail@test.com")
        .password(testPassword)
        .build();

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    assertThatThrownBy(
        () -> authService.loginAttempt(loginRequest, request, response))
        .isInstanceOf(BadCredentialsException.class);

    MockHttpSession session = (MockHttpSession) request.getSession(false);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(session).isNull();
  }

  @Test
  @DisplayName("Should register user with non existent email")
  void shouldRegisterUserWithNonExistentEmailWithUserRole() {
    String newEmail = "newuser@test.com";

    RegisterRequest registerRequest = RegisterRequest.builder()
        .email(newEmail)
        .password(testPassword.toCharArray())
        .build();

    authService.register(registerRequest);

    assertThat(userService.findByEmail(newEmail))
        .isPresent()
        .get()
        .satisfies(user -> {
          assertThat(user.getEmail()).isEqualTo(newEmail);
          assertThat(user.getRole()).isEqualTo(Role.USER);
          assertThat(passwordEncoder.matches(new String(registerRequest.getPassword()),
              user.getPassword())).isTrue();
        });

  }

  @Test
  @DisplayName("Should not register user with existent email")
  void shouldNotRegisterUserWithExistentEmail() {

    RegisterRequest existingUserRequest = RegisterRequest.builder()
        .email(testEmail)
        .password("somepassword".toCharArray())
        .build();

    assertThatThrownBy(() -> authService.register(existingUserRequest)).isInstanceOf(
        UserAlreadyExistsException.class);
  }

}

