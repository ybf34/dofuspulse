package com.dofuspulse.api.items.dto;

import com.dofuspulse.api.model.ItemEffect;

public record ItemEffectDto(Long effectId, Integer minValue,
                            Integer maxValue) {

  public ItemEffectDto(ItemEffect itemEffect) {
    this(itemEffect.getEffect().getId(), itemEffect.getMinValue(), itemEffect.getMaxValue());
  }
}
