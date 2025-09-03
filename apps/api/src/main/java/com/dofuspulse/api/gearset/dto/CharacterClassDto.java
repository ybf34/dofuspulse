package com.dofuspulse.api.gearset.dto;

import com.dofuspulse.api.model.CharacterClass;

public record CharacterClassDto(String name) {

  public CharacterClassDto(CharacterClass characterClass) {
    this(characterClass.getName());
  }
}