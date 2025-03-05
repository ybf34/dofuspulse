package com.dofuspulse.api.auth.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.AuthService;
import com.dofuspulse.api.auth.LoginRequest;
import com.dofuspulse.api.auth.RegisterRequest;
import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.auth.UserService;
import com.dofuspulse.api.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.CharBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserService userService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private SecurityContextRepository scr;

  @InjectMocks
  private AuthService authService;

  @Test
  @DisplayName("Login attempt should return success and save security context")
  void shouldLoginSuccessfullyAndSaveSession() {

    LoginRequest loginRequest = LoginRequest.builder()
        .email("test@example.com")
        .password("password")
        .build();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    Authentication mockAuthentication = mock(Authentication.class);

    when(authenticationManager.authenticate(
        any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);

    authService.loginAttempt(loginRequest, request, response);

    var capturedCredentials = ArgumentCaptor.forClass(
        UsernamePasswordAuthenticationToken.class);

    verify(authenticationManager).authenticate(capturedCredentials.capture());

    UsernamePasswordAuthenticationToken capturedToken = capturedCredentials.getValue();

    assertThat(capturedToken)
        .extracting(UsernamePasswordAuthenticationToken::getPrincipal,
            UsernamePasswordAuthenticationToken::getCredentials)
        .containsExactly(loginRequest.getEmail(), loginRequest.getPassword());

    verify(scr, times(1)).saveContext(any(), any(), any());

  }

  @Test
  @DisplayName("Fail login attempt should throw BadCredentialsException and not save session")
  void shouldFailLoginAttemptAndNotSavingSession() {
    LoginRequest loginRequest = LoginRequest.builder()
        .email("test@example.com")
        .password("password")
        .build();

    when(authenticationManager.authenticate(
        any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(
            new BadCredentialsException("Bad Credentials"));

    assertThrows(BadCredentialsException.class, () ->
        authService.loginAttempt(loginRequest, null, null));

    //check no session saved
    verify(scr, never()).saveContext(any(), any(), any());

  }

  @Test
  @DisplayName("Should register user with non-existent email with user role assigned")
  void shouldRegisterUserWithNonExistentEmailWithUserRole() {

    String mockEncodedPassword = "$2a$10%24mockEncodedPassword";

    RegisterRequest registerRequest = RegisterRequest.builder()
        .email("test@test.com")
        .password("password".toCharArray())
        .build();

    ArgumentCaptor<UserPrincipal> userCaptor = ArgumentCaptor.forClass(UserPrincipal.class);

    when(userService.existsByEmail(any(String.class))).thenReturn(false);
    when(passwordEncoder.encode(any(CharSequence.class))).thenReturn(mockEncodedPassword);
    when(userService.saveUser(any(UserPrincipal.class))).thenReturn(any(UserPrincipal.class));

    String result = authService.register(registerRequest);

    verify(userService, times(1)).saveUser(userCaptor.capture());
    verify(passwordEncoder, times(1)).encode(CharBuffer.wrap(registerRequest.getPassword()));

    assertThat(userCaptor.getValue())
        .extracting(UserPrincipal::getRole, UserPrincipal::getPassword)
        .containsExactly(Role.USER, mockEncodedPassword);

    assertThat(result).isEqualTo("User registered successfully");
  }


  @Test
  @DisplayName("Should not register user with existent email")
  void shouldNotRegisterUserWithExistentEmail() {

    RegisterRequest registerRequest = RegisterRequest.builder()
        .email("test@test.com")
        .password("password".toCharArray())
        .build();

    when(userService.existsByEmail(any(String.class))).thenReturn(true);

    assertThatThrownBy(() -> authService.register(registerRequest)).isInstanceOf(
        UserAlreadyExistsException.class);

    verify(userService, times(0)).saveUser(any(UserPrincipal.class));
    verify(passwordEncoder, times(0)).encode(any(CharSequence.class));
  }

}
