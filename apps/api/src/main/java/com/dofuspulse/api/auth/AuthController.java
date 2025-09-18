package com.dofuspulse.api.auth;

import com.dofuspulse.api.user.dto.UserProfileDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/auth/login")
  public ResponseEntity<UserProfileDto> login(
      @RequestBody @Valid LoginRequest loginRequest,
      HttpServletRequest request,
      HttpServletResponse response) {
    UserProfileDto user = authService.loginAttempt(loginRequest, request, response);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/auth/register")
  public ResponseEntity<UserProfileDto> register(@RequestBody @Validated RegisterRequest req) {
    return ResponseEntity.ok(authService.register(req));
  }

  @GetMapping("/admin")
  public String admin() {
    return "admin protected resource";
  }

  @GetMapping("/userprotected")
  public String user() {
    return "user role protected resource";
  }
}
