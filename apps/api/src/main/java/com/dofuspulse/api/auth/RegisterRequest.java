package com.dofuspulse.api.auth;


import com.dofuspulse.api.constraint.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RegisterRequest {

    @Email
    @NotNull
    private String email;

    @ValidPassword
    @NotNull
    private char[] password;

    private Role role;
}
