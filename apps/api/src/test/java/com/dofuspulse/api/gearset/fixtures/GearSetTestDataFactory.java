package com.dofuspulse.api.gearset.fixtures;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.gearset.dto.CharacterClassName;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.model.CharacterClass;
import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.model.GearSetSlot;
import com.dofuspulse.api.model.GearSetSlotType;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemType;
import java.util.List;

public class GearSetTestDataFactory {

  public static GearSetSlotType createMockSlotType(
      GearSetSlotTypeIdentifier slotName,
      List<ItemType> slotItemTypes) {
    GearSetSlotType slotType = new GearSetSlotType();

    slotType.setName(slotName);
    slotType.setItemTypes(slotItemTypes);
    return slotType;
  }

  public static GearSetSlot createMockGearSetSlot(
      GearSet gearset,
      GearSetSlotType slotType,
      ItemDetails itemDetails) {
    GearSetSlot slot = new GearSetSlot();

    slot.setGearSet(gearset);
    slot.setItemDetails(itemDetails);
    slot.setGearSetSlotType(slotType);

    return slot;
  }

  public static CharacterClass createMockCharacterClass(CharacterClassName className) {
    return new CharacterClass(1L, className);
  }

  public static GearSet createMockGearSet(UserPrincipal userPrincipal) {
    GearSet gearSet = new GearSet();

    gearSet.setCharacterGender("m");
    gearSet.setCharacterClass(createMockCharacterClass(CharacterClassName.CRA));
    gearSet.setTags(List.of("tag1", "tag2"));
    gearSet.setTitle("mock gearset");
    gearSet.setUserPrincipal(userPrincipal);

    return gearSet;
  }


}
