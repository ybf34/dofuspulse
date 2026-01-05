import {create} from "zustand";
import type {ItemWithQuantity, TimeRange} from "@/types/types";

interface OfferEvaluatorState {
  timeRange: TimeRange;
  selectedItems: ItemWithQuantity[];

  setTimeRange: (range: TimeRange) => void;
  setSelectedItems: (items: ItemWithQuantity[]) => void;
  setItemQuantity: (itemId: number, quantity: number) => void;
}

export const useOfferEvaluatorStore = create<OfferEvaluatorState>((set) => ({
  selectedItems: [],
  timeRange: "7d",

  setTimeRange: (range) => set({timeRange: range}),
  setSelectedItems: (items) =>
      set((state) => {
        const updatedItems = items.map((item) => {
          const existingItem = state.selectedItems.find((i) => i.id === item.id);
          return {
            ...item,
            quantity: existingItem?.quantity ?? item.quantity ?? 1,
          };
        });
        return {selectedItems: updatedItems};
      }),
  setItemQuantity: (itemId, quantity) =>
      set((state) => ({
        selectedItems: state.selectedItems.map((item) =>
            item.id === itemId ? {...item, quantity} : item,
        ),
      })),
}));
