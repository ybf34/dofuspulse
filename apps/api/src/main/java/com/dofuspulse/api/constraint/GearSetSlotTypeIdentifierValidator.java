package com.dofuspulse.api.constraint;

import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GearSetSlotTypeIdentifierValidator implements
    ConstraintValidator<ValidGearSetSlotTypeIdentifier, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    try {
      GearSetSlotTypeIdentifier.valueOf(value.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}