import {tsr} from "@/services/api/api";

export const userGearsetsQueryKey = ["gearsets", "user"] as const;

export function useGearsetsQuery() {
  const {data, isPending} = tsr.getUserGearsets.useQuery({
    queryKey: userGearsetsQueryKey,
  });

  return {
    gearsets: data?.body,
    isPending,
  };
}
