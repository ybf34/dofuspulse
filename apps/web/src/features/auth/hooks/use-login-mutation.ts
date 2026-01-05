import { toast } from "sonner";
import { handleApiError, queryClient, tsr } from "@/services/api/api";

export function useLoginMutation() {
	const { mutateAsync, isPending, error } = tsr.loginUser.useMutation({
		onSuccess: async (_, variables) => {
			await queryClient.resetQueries();
			toast.success(`Welcome ${variables.body.email}`, { duration: 3000 });
		},
		onError: (error) => handleApiError(error),
	});

	return {
		login: mutateAsync,
		isPending,
		error,
	};
}
