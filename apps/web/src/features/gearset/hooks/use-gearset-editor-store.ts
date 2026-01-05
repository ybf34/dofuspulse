import { create } from "zustand";
import type { GearsetSlotTypeIdentifier } from "@/services/api/api.types";

interface GearsetEditorStore {
	selectedSlot: GearsetSlotTypeIdentifier | null;
	setSelectedSlot: (slot: GearsetSlotTypeIdentifier | null) => void;
	isItemExplorerOpen: boolean;
	setItemExplorerOpen: (open: boolean) => void;
}

export const useGearsetEditorStore = create<GearsetEditorStore>((set) => ({
	selectedSlot: null,
	setSelectedSlot: (slot) => set({ selectedSlot: slot }),
	isItemExplorerOpen: false,
	setItemExplorerOpen: (open: boolean) => set({ isItemExplorerOpen: open }),
}));
