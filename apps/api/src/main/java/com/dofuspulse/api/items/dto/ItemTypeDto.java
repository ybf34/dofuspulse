package com.dofuspulse.api.items.dto;

import com.dofuspulse.api.model.ItemType;

public record ItemTypeDto(Long id, String name) {

  public ItemTypeDto(ItemType itemType) {
    this(itemType.getId(), itemType.getName());
  }
}
