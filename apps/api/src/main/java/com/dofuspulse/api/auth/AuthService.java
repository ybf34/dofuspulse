package com.dofuspulse.api.auth;


import com.dofuspulse.api.model.LoginRequest;
import com.dofuspulse.api.model.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public String loginAttempt(LoginRequest loginRequest, HttpServletRequest request,
                                             HttpServletResponse response){
        var authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), CharBuffer.wrap(loginRequest.getPassword()))
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        SecurityContext context = SecurityContextHolder.getContext();
        securityContextRepository.saveContext(context, request, response);

        return "Successfully logged in";
    }

    public String register(RegisterRequest request) {

        UserPrincipal newUser = UserPrincipal.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(CharBuffer.wrap(request.getPassword())).toCharArray())
                .role(Role.valueOf("USER"))
                .build();

        userService.saveUser(newUser);
        return "User registered successfully";
    }
}
