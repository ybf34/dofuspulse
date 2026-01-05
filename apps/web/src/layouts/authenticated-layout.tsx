import { Navigate } from "@tanstack/react-router";
import type { PropsWithChildren } from "react";
import { AuthenticationProvider } from "@/contexts/authentication-provider";

export default function AuthenticatedLayout({ children }: PropsWithChildren) {
	return (
		<AuthenticationProvider
			authenticatedLayout={() => <>{children}</>}
			unauthenticatedLayout={() => <Navigate to="/" />}
		/>
	);
}
