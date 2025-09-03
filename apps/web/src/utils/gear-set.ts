import {
  type GearsetSlotIdentifier,
  GearsetSlotIdentifierValues,
  type GridPosition,
} from "@/types/types.ts";

export const GearsetSlotGridPosition: Record<
    GearsetSlotIdentifier,
    GridPosition
> = {
  [GearsetSlotIdentifierValues.AMULET]: {colStart: 1, rowStart: 1},
  [GearsetSlotIdentifierValues.RING_1]: {colStart: 1, rowStart: 2},
  [GearsetSlotIdentifierValues.RING_2]: {colStart: 1, rowStart: 3},
  [GearsetSlotIdentifierValues.BOOTS]: {colStart: 1, rowStart: 4},

  [GearsetSlotIdentifierValues.HAT]: {colStart: 6, rowStart: 1},
  [GearsetSlotIdentifierValues.WEAPON]: {colStart: 6, rowStart: 2},
  [GearsetSlotIdentifierValues.CLOAK]: {colStart: 6, rowStart: 3},
  [GearsetSlotIdentifierValues.BELT]: {colStart: 6, rowStart: 4},
  [GearsetSlotIdentifierValues.LEGENDARY_WEAPON]: {colStart: 5, rowStart: 2},

  [GearsetSlotIdentifierValues.PET_MOUNT]: {colStart: 3, rowStart: 4},
  [GearsetSlotIdentifierValues.SHIELD]: {colStart: 4, rowStart: 4},

  [GearsetSlotIdentifierValues.DOFUS_TROPHY_1]: {colStart: 1, rowStart: 5},
  [GearsetSlotIdentifierValues.DOFUS_TROPHY_2]: {colStart: 2, rowStart: 5},
  [GearsetSlotIdentifierValues.DOFUS_TROPHY_3]: {colStart: 3, rowStart: 5},
  [GearsetSlotIdentifierValues.DOFUS_TROPHY_4]: {colStart: 4, rowStart: 5},
  [GearsetSlotIdentifierValues.DOFUS_TROPHY_5]: {colStart: 5, rowStart: 5},
  [GearsetSlotIdentifierValues.DOFUS_TROPHY_6]: {colStart: 6, rowStart: 5},
};
