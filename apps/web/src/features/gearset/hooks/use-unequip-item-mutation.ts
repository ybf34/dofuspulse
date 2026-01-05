import { handleApiError, queryClient, tsr } from "@/services/api/api.ts";

export default function useUnequipItemMutation() {
	const { mutate, isPending } = tsr.unequipItem.useMutation({
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["gearsets"] });
		},
		onError: (error) => handleApiError(error),
	});

	return {
		unequipItem: mutate,
		isPending,
	};
}
