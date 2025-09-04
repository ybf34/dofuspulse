import { createRouter } from "@tanstack/react-router";
import { routeTree } from "@/routeTree.gen.ts";

// const _GlobalLoadingSpinner = () => (
// 	<div className="fixed inset-0 z-50 bg-[oklch(0.145_0_0)] flex items-center justify-center">
// 		<LoadingSpinner className="size-48" />
// 	</div>
// );

export const router = createRouter({
	routeTree,
	context: {
		// biome-ignore lint/style/noNonNullAssertion: router
		auth: undefined!,
	},
});

declare module "@tanstack/react-router" {
	interface Register {
		router: typeof router;
	}
}
