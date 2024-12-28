package com.dofuspulse.api.model;


import com.dofuspulse.api.auth.Role;
import com.dofuspulse.api.constraint.ValidPassword;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RegisterRequest {

    @Email
    private String email;

    @ValidPassword
    private char[] password;

    private Role role;
}
