import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import {getUserProfile} from "@/api/api.ts";
import {customInstance} from "@/api/axios.ts";
import {tryCatch} from "@/lib/try-catch.ts";

export type AuthContextType = {
  isAuthenticated: boolean;
  handleSignIn: () => void;
  handleSignOut: () => Promise<void>;
  refreshAuthState: () => Promise<boolean>;
};

const AuthProviderContext = createContext<AuthContextType | undefined>(
    undefined,
);

export function AuthProvider({children}: { children: React.ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const refreshAuthState = useCallback(async (): Promise<boolean> => {
    const [_, userError] = await tryCatch(getUserProfile());
    if (userError) {
      setIsAuthenticated(false);
      return false;
    } else {
      setIsAuthenticated(true);
      return true;
    }
  }, []);

  useEffect(() => {
    const checkAuthState = async () => {
      await refreshAuthState();
    };
    checkAuthState();
  }, [refreshAuthState]);

  const handleSignIn = () => {
    setIsAuthenticated(true);
  };

  const handleSignOut = async (): Promise<void> => {
    const [_, error] = await tryCatch(
        customInstance<string>({
          url: "/api/v1/auth/logout",
          method: "POST",
        }),
    );

    if (!error) {
      setIsAuthenticated(false);
    }
  };

  return (
      <AuthProviderContext.Provider
          value={{
            isAuthenticated,
            handleSignIn,
            handleSignOut,
            refreshAuthState,
          }}
      >
        {children}
      </AuthProviderContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthProviderContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
