package com.dofuspulse.api.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter

public class LoginRequest {

    @Email
    private String email;
    private char[] password;
}
