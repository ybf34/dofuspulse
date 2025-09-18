package com.dofuspulse.api.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CharacterClassNameValidator.class)
public @interface ValidCharacterClassName {

  String message() default "Invalid character class name.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}