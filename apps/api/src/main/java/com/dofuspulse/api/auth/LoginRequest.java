package com.dofuspulse.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginRequest {

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String password;
}
