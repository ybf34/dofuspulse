package com.dofuspulse.api.exception;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    record ValidationError(String name, String reason) {}

    List<ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
        .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
        .collect(Collectors.toList());

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        "Validation failed");
    problem.setTitle("Validation failed");
    problem.setProperty("invalid-params", errors);

    return problem;
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ProblemDetail handleNoResourceFoundException(NoResourceFoundException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        "Resource not found");
    problem.setTitle("Resource not found");

    return problem;
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ProblemDetail handleNoSuchElementException(NoSuchElementException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    problem.setTitle("No such element found");

    return problem;
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
        "User already exists");
    problem.setTitle("User already exists");

    return problem;
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ProblemDetail handleBadCredentialsException(BadCredentialsException e) {

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
        "Authentication failed: invalid credentials provided.");

    problem.setTitle("Bad credentials");

    return problem;
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ProblemDetail handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e) {

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        "Missing request parameter");
    problem.setTitle("Missing request parameter");

    problem.setProperty("missing-parameter", e.getParameterName());

    return problem;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ProblemDetail handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        "Invalid argument type");
    problem.setTitle("Invalid argument type");

    return problem;
  }

  @ExceptionHandler(NotCraftableItemException.class)
  public ProblemDetail handleNotCraftableItemException(
      NotCraftableItemException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        "Item is not craftable");
    problem.setTitle("Item does not have a recipe");
    problem.setProperty("message", e.getMessage());

    return problem;
  }

  @ExceptionHandler(ItemTypeIncompatibilityException.class)
  public ProblemDetail handleItemTypeIncompatibilityException(
      ItemTypeIncompatibilityException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        "");
    problem.setTitle("Item type incompatible for that slot type");
    problem.setProperty("message", e.getMessage());

    return problem;
  }

  @ExceptionHandler(ItemNotFoundException.class)
  public ProblemDetail handleItemNotFoundException(
      ItemNotFoundException e) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
        "Item not found");
    problem.setTitle("Item not found");
    problem.setProperty("message", e.getMessage());

    return problem;
  }

  @ExceptionHandler(PropertyReferenceException.class)
  public ProblemDetail handlePropertyReferenceException(PropertyReferenceException e) {

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
        "Invalid property provided");
    problem.setTitle("Bad Request");

    return problem;
  }
}
