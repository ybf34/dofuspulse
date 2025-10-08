package com.dofuspulse.api.auth.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.auth.AuthController;
import com.dofuspulse.api.auth.AuthService;
import com.dofuspulse.api.auth.LoginRequest;
import com.dofuspulse.api.auth.RegisterRequest;
import com.dofuspulse.api.exception.UserAlreadyExistsException;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import com.dofuspulse.api.user.dto.UserProfileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(AuthController.class)
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
class AuthControllerUnitTest {

  @Autowired
  MockMvcTester mvcTester;

  @MockitoBean
  AuthService authService;

  @Test
  @DisplayName("Check user can register with valid data")
  @WithAnonymousUser
  public void shouldSucceedRegistrationWithValidData() throws Exception {

    when(authService.register(any()))
        .thenReturn(any(UserProfileDto.class));

    RegisterRequest registerRequest = RegisterRequest.builder()
        .email("newacc@test.com")
        .password("ValidPassword123@".toCharArray())
        .build();

    mvcTester.post().uri("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(registerRequest))
        .assertThat()
        .hasStatus(HttpStatus.OK);

    verify(authService, times(1)).register(registerRequest);

  }

  @Test
  @DisplayName("Check user can't register with existing email address")
  @WithAnonymousUser
  public void shouldReturn409WhenRegisteringWithExistingEmail() throws Exception {

    when(authService.register(any()))
        .thenThrow(new UserAlreadyExistsException("User already exists"));

    RegisterRequest registerRequest = RegisterRequest.builder()
        .email("newacc@gmail.com")
        .password("ValidPassword123@".toCharArray())
        .build();

    mvcTester.post().uri("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(registerRequest))
        .assertThat()
        .hasStatus(HttpStatus.CONFLICT);

    verify(authService, times(1)).register(registerRequest);
  }

  @ParameterizedTest(name = "{index} => Register Scenario: {0}")
  @MethodSource("com.dofuspulse.api.auth.unit.InvalidRegistrationScenarios#scenarios")
  @DisplayName("Should return 400 when invalid registration data is provided")
  @WithAnonymousUser
  void shouldReturn400WhenInvalidRegistrationDataProvided(
      String displayName,
      RegisterRequest registerRequest) throws Exception {

    mvcTester.post().uri("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(registerRequest))
        .assertThat()
        .hasStatus(HttpStatus.BAD_REQUEST);

    verify(authService, times(0)).register(any(RegisterRequest.class));
  }

  @Test
  @DisplayName("Check user can login with valid credentials")
  @WithAnonymousUser
  void shouldReturn200WhenUserLogsInWithValidCredentials() throws Exception {

    LoginRequest loginRequest = LoginRequest.builder()
        .email("user@test.com")
        .password("ValidPassword1@")
        .build();

    mvcTester.post().uri("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(loginRequest))
        .assertThat()
        .hasStatus(HttpStatus.OK);

    verify(authService, times(1)).loginAttempt(eq(loginRequest), any(), any());

  }

  @Test
  @DisplayName("Check user can't login in with invalid credentials")
  @WithAnonymousUser
  void shouldReturn401WhenLoginFailsWithInvalidCredentials() throws Exception {

    doThrow(new BadCredentialsException("Invalid credentials"))
        .when(authService).loginAttempt(any(), any(), any());

    mvcTester.post().uri("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(LoginRequest.builder()
            .email("user@test.com")
            .password("WrongPassword&1").build()))
        .assertThat()
        .hasStatus(HttpStatus.UNAUTHORIZED);

    verify(authService, times(1)).loginAttempt(any(), any(), any());
  }

  @Test
  @DisplayName("Check normal user can access user protected resource")
  @WithMockUser(username = "normalUser", roles = "USER")
  void shouldReturn200WhenAuthenticatedUserAccessesProtectedResource() {

    mvcTester.get().uri("/api/v1/userprotected")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.OK);
  }

  @Test
  @DisplayName("Check normal user can't access admin protected resource")
  @WithMockUser(username = "normalUser", roles = "USER")
  void shouldReturn403WhenNormalUserAccessesAdminProtectedResource() {

    mvcTester
        .get()
        .uri("/api/v1/admin")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.FORBIDDEN);
  }

  @Test
  @DisplayName("Check admin user can access admin protected resource")
  @WithMockUser(username = "adminUser", roles = "ADMIN")
  void shouldReturn200WhenAdminAccessesAdminProtectedResource() {

    mvcTester.get()
        .uri("/api/v1/admin")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.OK);
  }

  @Test
  @DisplayName("Check anonymous user can't access protected resource")
  @WithAnonymousUser
  void shouldReturn401WhenAnonymousUserAccessProtectedResources() {

    mvcTester.get()
        .uri("/api/v1/admin")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.UNAUTHORIZED);

    mvcTester.get()
        .uri("/")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(HttpStatus.UNAUTHORIZED);
  }
}
