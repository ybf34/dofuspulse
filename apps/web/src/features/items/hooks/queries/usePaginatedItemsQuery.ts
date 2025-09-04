import { useQuery } from "@tanstack/react-query";

import { getGetAllItemDetailsQueryOptions } from "@/api/api.ts";
import type { GetAllItemDetailsParams } from "@/api/model";

export type PaginatedAndFilteredRequestParams =
	Partial<GetAllItemDetailsParams>;

export function usePaginatedItemsQuery(
	paginationParams: PaginatedAndFilteredRequestParams = {},
) {
	const queryParams = {
		params: paginationParams.params || {},
		pageable: paginationParams.pageable || {},
	};

	return useQuery({
		...getGetAllItemDetailsQueryOptions(queryParams),
		placeholderData: (previousData) => previousData,
	});
}
