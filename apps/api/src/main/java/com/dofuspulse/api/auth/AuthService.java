package com.dofuspulse.api.auth;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.CharBuffer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final SecurityContextRepository scr = new HttpSessionSecurityContextRepository();

  public String loginAttempt(
      LoginRequest loginRequest,
      HttpServletRequest request,
      HttpServletResponse response) {

    var authenticationToken = new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(),
        CharBuffer.wrap(loginRequest.getPassword())
    );

    var authentication = authenticationManager.authenticate(authenticationToken);

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);

    scr.saveContext(context, request, response);

    return "Successfully logged in";
  }

  public String register(RegisterRequest request) {

    var encodedPassword = passwordEncoder.encode(CharBuffer.wrap(request.getPassword()))
        .toCharArray();

    UserPrincipal newUser = new UserPrincipal();

    newUser.setEmail(request.getEmail());
    newUser.setPassword(encodedPassword);
    newUser.setRole(Role.USER);

    userService.saveUser(newUser);
    return "User registered successfully";
  }
}
