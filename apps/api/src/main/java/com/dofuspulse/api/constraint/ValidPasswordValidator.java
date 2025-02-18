package com.dofuspulse.api.constraint;


import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.CharBuffer;
import java.util.Arrays;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.WhitespaceRule;


public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, char[]> {

  private String message;

  @Override
  public void initialize(final ValidPassword constraintAnnotation) {
    this.message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(char[] password, ConstraintValidatorContext context) {
    PasswordValidator validator = new PasswordValidator(
        Arrays.asList(

            new LengthRule(8, 30),

            // at least one upper-case character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),

            // at least one lower-case character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),

            // at least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1),

            // at least one symbol (special character)
            new CharacterRule(EnglishCharacterData.Special, 1),

            // no whitespace
            new WhitespaceRule()));

    boolean isValid =
        StringUtils.isNotBlank(CharBuffer.wrap(password).toString()) && validator.validate(
            new PasswordData(CharBuffer.wrap(password).toString())).isValid();

    if (!isValid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    return isValid;
  }
}