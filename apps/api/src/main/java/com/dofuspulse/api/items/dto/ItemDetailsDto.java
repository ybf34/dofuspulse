package com.dofuspulse.api.items.dto;

import com.dofuspulse.api.model.ItemDetails;
import java.util.List;

public record ItemDetailsDto(Long id, String name, Long level, Long itemTypeId, Long iconId,
                             List<Long> ingredientIds, List<ItemEffectDto> possibleEffects) {


  public ItemDetailsDto(ItemDetails itemDetails) {
    this(itemDetails.getId(),
        itemDetails.getName(),
        itemDetails.getLevel(),
        itemDetails.getItemTypeId(),
        itemDetails.getIconId(),
        itemDetails.getIngredientIds(),
        itemDetails.getPossibleEffects()
            .stream()
            .map(ItemEffectDto::new)
            .toList());
  }
}
