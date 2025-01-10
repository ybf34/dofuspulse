package com.dofuspulse.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter

public class LoginRequest {

  @Email
  @NotNull
  private String email;

  @NotNull
  private char[] password;
}
