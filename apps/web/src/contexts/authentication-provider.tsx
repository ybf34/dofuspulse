import {createContext, useContext} from "react";
import LoadingSpinner from "@/components/ui/loading-spinner";
import {useUserQuery} from "@/features/auth/hooks/use-user-query";
import type {APIUser} from "@/services/api/api.types";

export type AuthenticationProviderProps = {
  authenticatedLayout: (user: APIUser) => React.ReactNode;
  unauthenticatedLayout: () => React.ReactNode;
};

const AuthProviderContext = createContext<APIUser | null>(null);

export function AuthenticationProvider({
                                         authenticatedLayout,
                                         unauthenticatedLayout,
                                       }: AuthenticationProviderProps) {
  const {user, isError} = useUserQuery();

  if (isError) {
    return unauthenticatedLayout();
  }

  if (user) {
    return (
        <AuthProviderContext.Provider value={user}>
          {authenticatedLayout(user)}
        </AuthProviderContext.Provider>
    );
  }
  return <LoadingSpinner className="size-58"/>;
}

export function useAuth() {
  const context = useContext(AuthProviderContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthenticationProvider");
  }
  return context;
}
