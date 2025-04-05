package com.dofuspulse.api.items.dto;

import com.dofuspulse.api.model.ItemDetails;
import java.util.List;

public record ItemDetailsDto(Long id, String name, Long level, Long itemTypeId,
                             List<Long> ingredientIds, List<Integer> possibleEffects) {


  public ItemDetailsDto(ItemDetails itemDetails) {
    this(itemDetails.getId(),
        itemDetails.getName(),
        itemDetails.getLevel(),
        itemDetails.getItemTypeId(),
        itemDetails.getIngredientIds(),
        itemDetails.getPossibleEffects());
  }
}
