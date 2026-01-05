import {handleApiError, queryClient, tsr} from "@/services/api/api";

export default function useCreateGearsetMutation() {
  const {mutateAsync, isPending, error} = tsr.createGearset.useMutation({
    onSuccess: () => queryClient.invalidateQueries({queryKey: ["gearsets"]}),
    onError: (error) => handleApiError(error),
  });

  return {
    createGearset: mutateAsync,
    isPending,
    error,
  };
}
