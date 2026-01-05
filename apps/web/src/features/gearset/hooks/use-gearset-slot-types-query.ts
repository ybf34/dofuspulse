import {tsr} from "@/services/api/api.ts";

export default function useGearsetSlotTypesQuery() {
  const {data, isPending, isError} = tsr.getGearsetSlotTypes.useQuery({
    queryKey: ["gearset-slot-types"],
    staleTime: Infinity,
  });

  return {
    slotTypes: data?.body,
    isPending,
    isError,
  };
}
