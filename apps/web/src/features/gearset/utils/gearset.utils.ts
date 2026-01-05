import type {
	APIGearset,
	APIGearSetSlotType,
	APIItemDetails,
	GearsetSlotTypeIdentifier,
} from "@/services/api/api.types";
import type { SlotConfig } from "@/types/types";
import {
	characterCardDirectory,
	placeholderIconDirectory,
} from "@/utils/constants";

export const MAX_GEARSET_TAG_LENGTH = 10;
export const MAX_GEARSET_TAGS = 5;
export const MAX_TITLE_LENGTH = 60;

export const SlotSizes = {
	small: { width: "44px", height: "40px", gap: "2px" },
	large: {
		width: "clamp(36px, 6.4vw, 56px)",
		height: "clamp(33px, 5.8vw, 50px)",
		gap: "3px",
	},
} as const;

export function getClassAvatarPath(className: string, gender: string): string {
	const normalizedClass = className.toLowerCase();
	const normalizedGender = gender.toLowerCase();
	return `${characterCardDirectory}${normalizedClass}-${normalizedGender}.png`;
}

export function getSlotIdentifierTypeIds(
	gearSlotTypes: APIGearSetSlotType[],
	slotIdentifier: GearsetSlotTypeIdentifier,
): number[] {
	const matchingSlotType = gearSlotTypes.find(
		(slot) => slot.name === slotIdentifier,
	);

	return matchingSlotType?.itemTypes.map((it) => it.id) ?? [];
}

export function findItemSlotIdentifier(
	gearset: APIGearset,
	item: APIItemDetails,
	slotTypes: APIGearSetSlotType[],
	selectedSlot: GearsetSlotTypeIdentifier | null,
): GearsetSlotTypeIdentifier | null {
	const typeIdToSlotName = slotTypes.flatMap((slot) =>
		slot.itemTypes.map((it) => ({
			itemTypeId: it.id,
			slotIdentifier: slot.name,
		})),
	);

	const possibleSlots = typeIdToSlotName
		.filter((slot) => slot.itemTypeId === item.itemTypeId)
		.map((s) => s.slotIdentifier);

	if (possibleSlots.length === 0) return null;
	if (possibleSlots.length === 1) return possibleSlots[0];

	if (selectedSlot && possibleSlots.includes(selectedSlot)) {
		return selectedSlot;
	}

	const emptySlot = possibleSlots.find(
		(ps) => !gearset.slots.map((s) => s.slotType.name).includes(ps),
	);

	return emptySlot ?? null;
}

export const gearsetSlotConfig: Record<GearsetSlotTypeIdentifier, SlotConfig> =
	{
		AMULET: {
			gridPosition: { colStart: 1, rowStart: 1 },
			placeholderIcon: `${placeholderIconDirectory}amulet.png`,
		},
		RING_1: {
			gridPosition: { colStart: 1, rowStart: 2 },
			placeholderIcon: `${placeholderIconDirectory}ring.png`,
		},
		RING_2: {
			gridPosition: { colStart: 1, rowStart: 3 },
			placeholderIcon: `${placeholderIconDirectory}ring.png`,
		},
		BOOTS: {
			gridPosition: { colStart: 1, rowStart: 4 },
			placeholderIcon: `${placeholderIconDirectory}boots.png`,
		},
		HAT: {
			gridPosition: { colStart: 6, rowStart: 1 },
			placeholderIcon: `${placeholderIconDirectory}hat.png`,
		},
		WEAPON: {
			gridPosition: { colStart: 6, rowStart: 2 },
			placeholderIcon: `${placeholderIconDirectory}weapon.png`,
		},
		CLOAK: {
			gridPosition: { colStart: 6, rowStart: 3 },
			placeholderIcon: `${placeholderIconDirectory}cloack.png`,
		},
		BELT: {
			gridPosition: { colStart: 6, rowStart: 4 },
			placeholderIcon: `${placeholderIconDirectory}belt.png`,
		},
		LEGENDARY_WEAPON: {
			gridPosition: { colStart: 5, rowStart: 2 },
			placeholderIcon: `${placeholderIconDirectory}legendary_weapon.png`,
		},
		PET_MOUNT: {
			gridPosition: { colStart: 3, rowStart: 4 },
			placeholderIcon: `${placeholderIconDirectory}pet_mount.png`,
		},
		SHIELD: {
			gridPosition: { colStart: 4, rowStart: 4 },
			placeholderIcon: `${placeholderIconDirectory}shield.png`,
		},
		DOFUS_TROPHY_1: {
			gridPosition: { colStart: 1, rowStart: 5 },
			placeholderIcon: `${placeholderIconDirectory}dofus.png`,
		},
		DOFUS_TROPHY_2: {
			gridPosition: { colStart: 2, rowStart: 5 },
			placeholderIcon: `${placeholderIconDirectory}dofus.png`,
		},
		DOFUS_TROPHY_3: {
			gridPosition: { colStart: 3, rowStart: 5 },
			placeholderIcon: `${placeholderIconDirectory}dofus.png`,
		},
		DOFUS_TROPHY_4: {
			gridPosition: { colStart: 4, rowStart: 5 },
			placeholderIcon: `${placeholderIconDirectory}dofus.png`,
		},
		DOFUS_TROPHY_5: {
			gridPosition: { colStart: 5, rowStart: 5 },
			placeholderIcon: `${placeholderIconDirectory}dofus.png`,
		},
		DOFUS_TROPHY_6: {
			gridPosition: { colStart: 6, rowStart: 5 },
			placeholderIcon: `${placeholderIconDirectory}dofus.png`,
		},
	};
