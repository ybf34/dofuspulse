package com.dofuspulse.api.gearset.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum GearSetSlotTypeIdentifier {
  HAT,
  CLOAK,
  AMULET,
  WEAPON,
  LEGENDARY_WEAPON,
  SHIELD,
  RING_1,
  RING_2,
  BELT,
  BOOTS,
  PET_MOUNT,
  DOFUS_TROPHY_1,
  DOFUS_TROPHY_2,
  DOFUS_TROPHY_3,
  DOFUS_TROPHY_4,
  DOFUS_TROPHY_5,
  DOFUS_TROPHY_6
}
