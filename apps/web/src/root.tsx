import { QueryClientProvider } from "@tanstack/react-query";
import type { PropsWithChildren } from "react";
import { Toaster } from "@/components/ui/sonner";
import { ThemeProvider } from "@/contexts/theme-provider";
import { queryClient, tsr } from "@/services/api/api";

export function Root({ children }: PropsWithChildren) {
	return (
		<ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
			<QueryClientProvider client={queryClient}>
				<tsr.ReactQueryProvider>{children}</tsr.ReactQueryProvider>
				<Toaster />
			</QueryClientProvider>
		</ThemeProvider>
	);
}
