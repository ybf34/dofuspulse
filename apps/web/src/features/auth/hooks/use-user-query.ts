import { tsr } from "@/services/api/api";

export const userQueryKey = ["user", "profile"] as const;

export function useUserQuery() {
	const { data, isLoading, isError, refetch, error } = tsr.getUser.useQuery({
		queryKey: userQueryKey,
		retry: 0,
		refetchOnMount: false,
	});

	return {
		user: data?.body,
		isError,
		isLoading,
		refetch,
		error,
	};
}
