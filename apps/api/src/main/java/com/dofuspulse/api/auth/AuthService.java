package com.dofuspulse.api.auth;


import com.dofuspulse.api.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.CharBuffer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final SecurityContextRepository scr;

  public void loginAttempt(
      LoginRequest loginRequest,
      HttpServletRequest request,
      HttpServletResponse response) {

    var authenticationToken = new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(),
        loginRequest.getPassword()
    );

    var authentication = authenticationManager.authenticate(authenticationToken);

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);

    scr.saveContext(context, request, response);
  }

  @Transactional
  public String register(RegisterRequest request) {

    if (userService.existsByEmail(request.getEmail())) {
      throw new UserAlreadyExistsException(
          "User already exists");
    }

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
