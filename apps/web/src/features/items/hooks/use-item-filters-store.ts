import {create} from "zustand";
import type {ItemFilters} from "@/services/api/api.types.ts";

type ItemFiltersState = {
  filters: ItemFilters;
  setFilters: (filters: ItemFilters) => void;
  toggleId: (key: "typesIds" | "effectsIds", id: number) => void;
  resetFilters: () => void;
};

const initialFilters: ItemFilters = {
  size: 25,
  page: 0,
};

export const useItemFiltersStore = create<ItemFiltersState>((set) => ({
  filters: initialFilters,

  setFilters: (filters) =>
      set((state) => {
        const isPageChange = "page" in filters;
        return {
          filters: {
            ...state.filters,
            ...filters,
            page: isPageChange ? filters.page : 0,
          },
        };
      }),

  toggleId: (key, id) =>
      set((state) => {
        const current = state.filters[key] ?? [];
        return {
          filters: {
            ...state.filters,
            [key]: current.includes(id)
                ? current.filter((x) => x !== id)
                : [...current, id],
            page: 0,
          },
        };
      }),

  resetFilters: () => set({filters: initialFilters}),
}));
