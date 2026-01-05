import {
  createRootRouteWithContext,
  createRoute,
  createRouter,
  Outlet,
} from "@tanstack/react-router";
import {lazy, Suspense} from "react";
import Navbar from "@/components/navbar/navbar.tsx";
import {AppSidebar} from "@/components/sidebar/app-sidebar";
import {SidebarProvider} from "@/components/ui/sidebar";
import GearsetEditorPage from "@/features/gearset/gearset-editor-page.tsx";
import GearsetsPage from "@/features/gearset/gearsets-page";
import ItemPage from "@/features/items/items-page.tsx";
import {LandingPage} from "@/features/landing/landing-page.tsx";
import {OfferEvaluatorPage} from "@/features/offer-evaluator/offer-evaluator-page.tsx";
import AuthenticatedLayout from "@/layouts/authenticated-layout";
import UnauthenticatedLayout from "@/layouts/unauthenticated-layout";
import {Root} from "@/root";

declare module "@tanstack/react-router" {
	interface Register {
		router: typeof router;
	}
}

const TanStackRouterDevtools = import.meta.env.prod
    ? lazy(() =>
        import("@tanstack/react-router-devtools").then((res) => ({
          default: res.TanStackRouterDevtools,
        })),
    )
    : () => null;

const rootRoute = createRootRouteWithContext()({
  component: () => (
      <Root>
        <Outlet/>
        <Suspense>
          <TanStackRouterDevtools position="bottom-left"/>
        </Suspense>
      </Root>
  ),
  head: () => {
    return {
      meta: [{title: "Dofus Pulse"}],
    };
  },
});

const indexRoute = createRoute({
  getParentRoute: () => rootRoute,
  path: "/",
  component: () => (
      <UnauthenticatedLayout>
        <LandingPage/>
      </UnauthenticatedLayout>
  ),
  head: () => {
    return {
      meta: [{title: "Dofus Pulse - Market Intelligence for Dofus Touch"}],
    };
  },
});

const authRoute = createRoute({
  getParentRoute: () => rootRoute,
  id: "auth",
  component: () => (
      <AuthenticatedLayout>
        <SidebarProvider className="h-screen w-full overflow-hidden">
          <AppSidebar/>
          <div
              className="flex-1 flex flex-col min-w-0 h-screen pl-0 sm:pr-2 sm:pt-2 bg-slate-50 dark:bg-background">
            <div
                className="flex flex-col flex-1 bg-background overflow-hidden border-zinc-200 sm:border-t sm:border-x sm:dark:border-x-2 sm:dark:border-t-2 dark:border-zinc-900 sm:rounded-t-xl">
              {" "}
              <Navbar/>
              <main className="flex-1 min-h-0 relative overflow-y-auto">
                <Outlet/>
              </main>
            </div>
          </div>
        </SidebarProvider>
      </AuthenticatedLayout>
  ),
});

const gearsetsRoute = createRoute({
  getParentRoute: () => authRoute,
  path: "/gearsets",
  component: () => <GearsetsPage/>,
  head: () => {
    return {
      meta: [{title: "My Gearsets"}],
    };
  },
});

const gearsetByIdRoute = createRoute({
  getParentRoute: () => authRoute,
  path: "/gearsets/$id",
  component: () => <GearsetEditorPage/>,
  head: () => {
    return {
      meta: [{title: "Gearset Editor"}],
    };
  },
});

const itemsRoute = createRoute({
  getParentRoute: () => authRoute,
  path: "/items",
  component: () => <ItemPage/>,
  head: () => {
    return {
      meta: [{title: "Item Browser"}],
    };
  },
});

const offerEvaluatorRoute = createRoute({
  getParentRoute: () => authRoute,
  path: "/offer-evaluator",
  component: () => <OfferEvaluatorPage/>,
  head: () => {
    return {
      meta: [{title: "Offer Evaluator"}],
    };
  },
});

const dashboardRoute = createRoute({
  getParentRoute: () => authRoute,
  path: "/dashboard",
  component: () => <h1> Dashboard </h1>,
  head: () => {
    return {
      meta: [{title: "Dashboard"}],
    };
  },
});

const portfolioRoute = createRoute({
  getParentRoute: () => authRoute,
  path: "/portfolio",
  component: () => <h1> Portfolio </h1>,
  head: () => {
    return {
      meta: [{title: "My Portfolio"}],
    };
  },
});

const routeTree = rootRoute.addChildren([
  indexRoute,
  authRoute.addChildren([
    dashboardRoute,
    portfolioRoute,
    gearsetsRoute,
    gearsetByIdRoute,
    itemsRoute,
    offerEvaluatorRoute,
  ]),
]);

export const router = createRouter({
  routeTree,
});
