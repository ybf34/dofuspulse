package com.dofuspulse.api.auth.unit;

import com.dofuspulse.api.auth.RegisterRequest;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class InvalidRegistrationScenarios {

  public static Stream<Arguments> scenarios() {
    return Stream.of(
        Arguments.of(
            "Empty email",
            RegisterRequest.builder()
                .email("")
                .password("StrongPassword123@".toCharArray())
                .build()
        ),
        Arguments.of(
            "Empty password",
            RegisterRequest.builder()
                .email("validmail@mail.com")
                .password("".toCharArray())
                .build()
        ),
        Arguments.of(
            "Email doesn't contain @",
            RegisterRequest.builder()
                .email("invalidEmail")
                .password("StrongPassword123@".toCharArray())
                .build()
        ),
        Arguments.of(
            "Email doesn't contain domain",
            RegisterRequest.builder()
                .email("invalidEmail@")
                .password("StrongPassword123@".toCharArray())
                .build()
        ),
        Arguments.of(
            "Email doesn't contain domain extension",
            RegisterRequest.builder()
                .email("invalidEmail@domain")
                .password("StrongPassword123@".toCharArray())
                .build()
        ),
        Arguments.of(
            "Short password",
            RegisterRequest.builder()
                .email("validmail@mail.com")
                .password("a".toCharArray())
                .build()),
        Arguments.of(
            "Long password",
            RegisterRequest.builder()
                .email("validmail@mail.com")
                .password("a".repeat(100).toCharArray())
                .build()
        ),
        Arguments.of(
            "Password doesn't contain special character",
            RegisterRequest.builder()
                .email("validmail@mail.com")
                .password("NoSpecialChars123".toCharArray())
                .build()
        ),
        Arguments.of(
            "Password doesn't contain digits",
            RegisterRequest.builder()
                .email("validmail@mail.com")
                .password("NoDigitsProvided".toCharArray())
                .build()
        ),
        Arguments.of(
            "Password doesn't contain Uppercase character",
            RegisterRequest.builder()
                .email("validmail@mail.com")
                .password("nouppercasepassword".toCharArray())
                .build()
        ),
        Arguments.of(
            "Password doesn't contain lowercase character",
            RegisterRequest.builder()
                .email("validmail@mail.com")
                .password("NOLOWERCASECHAR".toCharArray())
                .build()
        )
    );
  }
}
