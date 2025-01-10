package com.dofuspulse.api.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<String> login(
      @RequestBody @Validated LoginRequest loginRequest,
      HttpServletRequest request,
      HttpServletResponse response) {
    return ResponseEntity.ok(authService.loginAttempt(loginRequest, request, response));
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Validated RegisterRequest registerRequest) {
    return ResponseEntity.ok(authService.register(registerRequest));
  }
}
