import { tsr } from "@/services/api/api";

export function useEffectsQuery() {
	const { data, isPending, isError } = tsr.getEffects.useQuery({
		queryKey: ["effects"],
		staleTime: Infinity,
		refetchOnMount: false,
	});

	return {
		effects: data?.body ?? [],
		isPending,
		isError,
	};
}
