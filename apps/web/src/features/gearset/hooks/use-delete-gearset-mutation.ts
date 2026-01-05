import {handleApiError, queryClient, tsr} from "@/services/api/api";

export default function useDeleteGearsetMutation() {
  const {mutateAsync, isPending} = tsr.deleteGearsetById.useMutation({
    onSuccess: () => {
      queryClient.invalidateQueries({queryKey: ["gearsets"]});
    },
    onError: (error) => handleApiError(error),
  });

  return {
    deleteGearset: mutateAsync,
    isPending,
  };
}
