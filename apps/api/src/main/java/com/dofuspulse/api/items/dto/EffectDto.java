package com.dofuspulse.api.items.dto;

import com.dofuspulse.api.model.Effect;

public record EffectDto(Long id, String descriptionTemplate) {

  public EffectDto(Effect effect) {
    this(effect.getId(), effect.getDescriptionTemplate());
  }
}
