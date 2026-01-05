import {Navigate} from "@tanstack/react-router";
import type {PropsWithChildren} from "react";
import {AuthenticationProvider} from "@/contexts/authentication-provider";

export default function UnauthenticatedLayout({children}: PropsWithChildren) {
  return (
      <AuthenticationProvider
          authenticatedLayout={() => <Navigate to="/gearsets" replace/>}
          unauthenticatedLayout={() => <>{children}</>}
      />
  );
}
