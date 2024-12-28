package com.dofuspulse.api.auth;

import com.dofuspulse.api.model.LoginRequest;
import com.dofuspulse.api.model.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated LoginRequest loginRequest, HttpServletRequest request,
                                               HttpServletResponse response){
        return ResponseEntity.ok(authService.loginAttempt(loginRequest,request,response));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Validated RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }
}
