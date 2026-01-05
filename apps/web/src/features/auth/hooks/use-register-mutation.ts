import { toast } from "sonner";
import { handleApiError, queryClient, tsr } from "@/services/api/api";

export function useRegisterMutation() {
	const { mutateAsync, isPending, error } = tsr.registerUser.useMutation({
		onSuccess: async (_, variables) => {
			await queryClient.resetQueries();
			toast.success(
				`Welcome ${variables.body.email}! Your account has been created.`,
				{ duration: 3000 },
			);
		},
		onError: (error) => handleApiError(error),
	});

	return {
		register: mutateAsync,
		isPending,
		error,
	};
}
