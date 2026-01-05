import {tsr} from "@/services/api/api";
import type {DateRange, ItemFilters} from "@/services/api/api.types";

export function useItemPerformanceQuery(itemId: number, dateRange: DateRange) {
  return tsr.getItemPerformance.useQuery({
    queryKey: ["item-performance", itemId, dateRange],
    queryData: {
      params: {id: itemId},
      query: dateRange,
    },
    enabled: !!itemId && !!dateRange.startDate && !!dateRange.endDate,
  });
}

export function useItemsPerformanceQuery(
    filters: ItemFilters,
    dateRange: DateRange,
) {
  const hasItemIds =
      Array.isArray(filters.itemIds) && filters.itemIds.length > 0;

  const {data, isPending} = tsr.getItemsPerformanceMetrics.useQuery({
    queryKey: ["items-performance", filters, dateRange],
    queryData: {
      query: {
        ...filters,
        ...dateRange,
        typesIds: filters.typesIds?.join(","),
        effectsIds: filters.effectsIds?.join(","),
        itemIds: filters.itemIds?.join(","),
      },
    },
    enabled: !!dateRange.startDate && !!dateRange.endDate && hasItemIds,
  });

  return {
    itemsPerformance: data?.body ?? [],
    isPending,
  };
}
