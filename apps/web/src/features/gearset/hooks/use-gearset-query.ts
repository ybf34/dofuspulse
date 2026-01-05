import { tsr } from "@/services/api/api.ts";

export default function useGearsetQuery(gearsetId: number) {
	const { data, isPending, isError } = tsr.getGearsetById.useQuery({
		queryKey: ["gearsets", gearsetId],
		queryData: {
			params: {
				id: gearsetId,
			},
		},
		retry: 0,
	});

	return {
		gearset: data?.body,
		isPending,
		isError,
	};
}
