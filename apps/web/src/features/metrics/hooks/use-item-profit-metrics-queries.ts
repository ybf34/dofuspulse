import { tsr } from "@/services/api/api";
import type { DateRange, ItemFilters } from "@/services/api/api.types";

export function useItemProfitMetricsQuery(
	itemId: number,
	dateRange: DateRange,
) {
	const { data, isPending, isError } = tsr.getItemProfitMetricsHistory.useQuery(
		{
			queryKey: ["item-profit-metrics", itemId, dateRange],
			queryData: {
				params: { id: itemId },
				query: dateRange,
			},
			enabled: !!itemId && !!dateRange.startDate && !!dateRange.endDate,
		},
	);

	return {
		itemProfitMetrics: data?.body || [],
		isError,
		isPending,
	};
}

export function useItemsProfitMetricsQuery(
	filters: ItemFilters,
	dateRange: DateRange,
	enabled: boolean = true,
) {
	const hasItemIds =
		Array.isArray(filters.itemIds) && filters.itemIds.length > 0;

	const { data, isPending, isError } =
		tsr.getItemsProfitMetricsHistory.useQuery({
			queryKey: ["items-profit-metrics", filters, dateRange],
			queryData: {
				query: {
					...dateRange,
					...filters,
					typesIds: filters.typesIds?.join(","),
					effectsIds: filters.effectsIds?.join(","),
					itemIds: filters.itemIds?.join(","),
				},
			},
			enabled:
				!!dateRange.startDate && !!dateRange.endDate && hasItemIds && enabled,
		});

	return {
		itemsProfitMetrics: data?.body || [],
		isError,
		isPending,
	};
}
