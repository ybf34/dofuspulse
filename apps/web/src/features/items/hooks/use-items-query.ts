import { keepPreviousData } from "@tanstack/react-query";
import { tsr } from "@/services/api/api";
import type { ItemFilters } from "@/services/api/api.types";

export function useItemsQuery(filters: ItemFilters) {
	const { data, isPending, isError, isFetching } = tsr.getItems.useQuery({
		queryKey: ["items", filters],
		queryData: {
			query: {
				...filters,
				typesIds: filters.typesIds?.join(","),
				effectsIds: filters.effectsIds?.join(","),
				itemIds: filters.itemIds?.join(","),
			},
		},
		placeholderData: keepPreviousData,
	});

	return {
		items: data?.body?.content ?? [],
		pageInfo: data?.body?.page,
		isPending,
		isError,
		isFetching,
	};
}
