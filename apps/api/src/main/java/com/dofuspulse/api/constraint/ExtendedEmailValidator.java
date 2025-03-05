package com.dofuspulse.api.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Email(message = "Please provide a valid email address")
@Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ExtendedEmailValidator {

  String message() default "Please provide a valid email address";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}