import {
	createRootRouteWithContext,
	Outlet,
	useMatchRoute,
} from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { AppSidebar } from "@/components/navbar/app-sidebar.tsx";
import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar.tsx";
import type { AuthContextType } from "@/contexts/auth-provider";

interface MyRouterContext {
	auth: AuthContextType;
}

export const Route = createRootRouteWithContext<MyRouterContext>()({
	component: RootComponent,
});

function RootComponent() {
	const matchRoute = useMatchRoute();
	const matchedLoginRoute = matchRoute({ to: "/login" });

	const routeLayout = matchedLoginRoute ? "w-full" : "flex min-h-screen w-full";

	return (
		<SidebarProvider>
			<div className={routeLayout}>
				{!matchedLoginRoute && <AppSidebar />}
				<main className="flex flex-col w-full">
					{!matchedLoginRoute && <SidebarTrigger />}
					<Outlet />
				</main>
			</div>
			<TanStackRouterDevtools position="bottom-right" />
		</SidebarProvider>
	);
}
