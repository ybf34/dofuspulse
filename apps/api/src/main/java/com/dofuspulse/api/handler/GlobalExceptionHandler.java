package com.dofuspulse.api.handler;

import com.dofuspulse.api.constraint.ValidationError;
import com.dofuspulse.api.exception.UserAlreadyExistsException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

    List<ValidationError> errors = e.getBindingResult().getAllErrors().stream()
        .map(error -> new ValidationError(error.getObjectName(), error.getDefaultMessage()))
        .collect(Collectors.toList());

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        "Validation failed");
    problem.setTitle("Validation failed");
    problem.setProperty("invalid-params", errors);

    return problem;
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
        "User already exists");
    problem.setTitle("User already exists");
    problem.setProperty("message", e.getMessage());

    return problem;
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ProblemDetail handleBadCredentialsException(BadCredentialsException e) {

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
        "Authentication failed: invalid credentials provided.");

    problem.setTitle("Bad credentials");
    problem.setProperty("message", e.getMessage());

    return problem;
  }
}
