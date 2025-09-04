import { createFileRoute, redirect } from "@tanstack/react-router";
import LoginPage from "@/features/auth/page/login-page.tsx";

export const Route = createFileRoute("/login")({
	component: LoginPage,
	beforeLoad: async ({ context }) => {
		const isAuthenticated = await context.auth.refreshAuthState();
		if (isAuthenticated) {
			throw redirect({
				to: "/",
			});
		}
	},
});
