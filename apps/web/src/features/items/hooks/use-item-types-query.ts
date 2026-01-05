import {tsr} from "@/services/api/api";
import type {PageableParams} from "@/services/api/api.types";

export const itemTypesQueryKey = ["itemsTypes"] as const;

export function useItemTypesQuery(params: PageableParams) {
  const {data, error, isPending} = tsr.getItemTypes.useQuery({
    queryKey: itemTypesQueryKey,
    queryData: {
      query: params,
    },
    staleTime: Infinity,
  });

  return {
    itemTypes: data?.body?.content,
    pageInfo: data?.body?.page,
    isPending,
    error,
  };
}
