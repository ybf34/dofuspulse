import { handleApiError, queryClient, tsr } from "@/services/api/api.ts";

export function useUpdateGearsetMutation() {
	const { mutateAsync, isPending, error } = tsr.updateGearset.useMutation({
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["gearsets"] });
		},
		onError: (error) => handleApiError(error),
	});

	return {
		updateGearset: mutateAsync,
		isPending,
		error,
	};
}
