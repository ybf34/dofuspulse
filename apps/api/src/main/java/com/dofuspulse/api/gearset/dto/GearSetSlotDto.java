package com.dofuspulse.api.gearset.dto;

import com.dofuspulse.api.items.dto.ItemDetailsDto;
import com.dofuspulse.api.model.GearSetSlot;

public record GearSetSlotDto(Long id, ItemDetailsDto itemDetails, GearSetSlotTypeDto slotType) {

  public GearSetSlotDto(GearSetSlot entity) {
    this(
        entity.getId(),
        new ItemDetailsDto(entity.getItemDetails()),
        new GearSetSlotTypeDto(entity.getGearSetSlotType())
    );
  }
}
