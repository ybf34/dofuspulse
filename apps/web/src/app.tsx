import {QueryClientProvider} from "@tanstack/react-query";
import {RouterProvider} from "@tanstack/react-router";
import {getGetAllEffectsQueryOptions} from "@/api/api.ts";
import {AuthProvider, useAuth} from "@/contexts/auth-provider.tsx";
import {ThemeProvider} from "@/contexts/theme-provider.tsx";
import {queryClient} from "@/query-client.ts";
import {router} from "@/router.tsx";

function InnerApp() {
	const auth = useAuth();
	return <RouterProvider router={router} context={{auth}}/>;
}

queryClient
.prefetchQuery(
		getGetAllEffectsQueryOptions({
			query: {
				staleTime: Infinity,
				gcTime: Infinity,
			},
		}),
)
.catch((error) => {
	console.error(error);
});

function App() {
	return (
			<QueryClientProvider client={queryClient}>
				<AuthProvider>
					<ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
						<InnerApp/>
					</ThemeProvider>
				</AuthProvider>
			</QueryClientProvider>
	);
}

export default App;
