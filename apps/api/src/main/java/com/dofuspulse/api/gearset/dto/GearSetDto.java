package com.dofuspulse.api.gearset.dto;

import com.dofuspulse.api.model.GearSet;
import java.time.Instant;
import java.util.List;

public record GearSetDto(Long id,
                         String title,
                         CharacterClassDto characterClass,
                         String characterGender,
                         List<String> tags,
                         List<GearSetSlotDto> slots,
                         Instant createdAt,
                         Instant updatedAt) {

  public GearSetDto(GearSet entity) {
    this(entity.getId(),
        entity.getTitle(),
        new CharacterClassDto(entity.getCharacterClass()),
        entity.getCharacterGender(),
        entity.getTags(),
        entity.getSlots()
            .stream()
            .map(GearSetSlotDto::new)
            .toList(),
        entity.getCreatedAt(),
        entity.getUpdatedAt()
    );
  }
}