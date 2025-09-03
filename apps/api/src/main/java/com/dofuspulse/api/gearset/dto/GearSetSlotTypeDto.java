package com.dofuspulse.api.gearset.dto;

import com.dofuspulse.api.items.dto.ItemTypeDto;
import com.dofuspulse.api.model.GearSetSlotType;
import java.util.List;

public record GearSetSlotTypeDto(Long id, String name, List<ItemTypeDto> itemTypes) {

  public GearSetSlotTypeDto(GearSetSlotType entity) {
    this(entity.getId(),
        entity.getName(),
        entity.getItemTypes()
            .stream()
            .map(ItemTypeDto::new)
            .toList());
  }
}