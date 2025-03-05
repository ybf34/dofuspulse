package com.dofuspulse.api.auth;

import com.dofuspulse.api.constraint.ExtendedEmailValidator;
import com.dofuspulse.api.constraint.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

  @ExtendedEmailValidator
  @NotBlank
  private String email;

  @ValidPassword
  private char[] password;

}
