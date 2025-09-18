package com.dofuspulse.api.constraint;

import com.dofuspulse.api.gearset.dto.CharacterClassName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CharacterClassNameValidator implements
    ConstraintValidator<ValidCharacterClassName, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    try {
      CharacterClassName.valueOf(value.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}