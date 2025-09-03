import {createFileRoute, Outlet, redirect} from "@tanstack/react-router";

export const Route = createFileRoute("/_auth")({
  beforeLoad: async ({context}) => {
    const isAuthenticated = await context.auth.refreshAuthState();
    if (!isAuthenticated) {
      throw redirect({
        to: "/login",
      });
    }
  },
  component: Outlet,
});
