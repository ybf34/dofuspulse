import {handleApiError, queryClient, tsr} from "@/services/api/api.ts";

export default function useEquipItemMutation() {
  const {mutate, isPending} = tsr.equipItem.useMutation({
    onSuccess: () => {
      queryClient.invalidateQueries({queryKey: ["gearsets"]});
    },
    onError: (error) => handleApiError(error),
  });

  return {
    equipItem: mutate,
    isPending,
  };
}
