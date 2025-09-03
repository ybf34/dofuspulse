import {useRouter} from "@tanstack/react-router";
import {
  AudioWaveform,
  BellIcon,
  Bot,
  GalleryVerticalEnd,
  Settings2,
  ShoppingBagIcon,
  SquareTerminal,
} from "lucide-react";
import {useGetUserProfile} from "@/api/api.ts";
import {NavUser} from "@/components/navbar/nav-user.tsx";
import {DofusLogo} from "@/components/ui/dofus-logo.tsx";
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarRail,
} from "@/components/ui/sidebar";
import {useAuth} from "@/contexts/auth-provider.tsx";
import {Route} from "@/routes/_auth.index.tsx";
import type {UserDisplayProfile, UserProfile} from "@/types/types.ts";
import {normalizeUserProfile} from "@/utils/user-profile.ts";
import {NavMain} from "./nav-main";

const nav_data = {
  teams: [
    {
      name: "Dofus Pulse Team",
      logo: GalleryVerticalEnd,
    },
    {
      name: "Support Team",
      logo: AudioWaveform,
      plan: "Free",
    },
  ],
  navMain: [
    {
      title: "Dashboard",
      url: "#",
      icon: SquareTerminal,
      isActive: true,
      items: [
        {
          title: "Overview",
          url: "/overview",
        },
        {
          title: "Market Trends",
          url: "/market-trends",
        },
        {
          title: "Top Gainers",
          url: "/top-gainers",
        },
      ],
    },
    {
      title: "Market Insights",
      url: "#",
      icon: Bot,
      items: [
        {
          title: "Price History",
          url: "/market/price-history",
        },
        {
          title: "Sales Analytics",
          url: "/market/sales-analytics",
        },
        {
          title: "Item Performance",
          url: "/market/item-performance",
        },
      ],
    },
    {
      title: "Investment Tools",
      url: "#",
      icon: ShoppingBagIcon,
      items: [
        {
          title: "Portfolio",
          url: "/investment/portfolio",
        },
        {
          title: "Set Planner",
          url: "/investment/set-planner",
        },
        {
          title: "Offer Analysis",
          url: "/investment/offer-analysis",
        },
      ],
    },
    {
      title: "Watchlist & Alerts",
      url: "#",
      icon: BellIcon,
      items: [
        {
          title: "Watchlist",
          url: "/watchlist",
        },
        {
          title: "Alerts",
          url: "/alerts",
        },
      ],
    },
    {
      title: "Settings",
      url: "#",
      icon: Settings2,
      items: [
        {
          title: "Profile",
          url: "/settings/profile",
        },
        {
          title: "Preferences",
          url: "/settings/preferences",
        },
        {
          title: "Billing",
          url: "/settings/billing",
        },
        {
          title: "Limits",
          url: "/settings/limits",
        },
        {
          title: "Sign Out",
          url: "/login",
        },
      ],
    },
  ],
};

export function AppSidebar({...props}: React.ComponentProps<typeof Sidebar>) {
  const {handleSignOut} = useAuth();
  const navigate = Route.useNavigate();
  const router = useRouter();
  const {data} = useGetUserProfile();

  const userDisplayProfile: UserDisplayProfile | null | undefined =
      data && normalizeUserProfile(data as UserProfile);

  const signOut = async () => {
    await handleSignOut().then(() => {
      router.invalidate().finally(() => {
        navigate({to: "/login"});
      });
    });
  };

  return (
      <Sidebar collapsible="icon" {...props}>
        <SidebarHeader>
          <SidebarMenu>
            <SidebarMenuItem>
              <SidebarMenuButton size="lg" asChild>
                <a href="/">
                  <div
                      className="text-sidebar-primary-foreground flex aspect-square size-8 items-center justify-center rounded-lg">
                    <DofusLogo className={"fill-white"}/>
                  </div>
                  <div className="grid flex-1 text-left text-sm leading-tight">
                    <span className="truncate">Dofus Pulse</span>
                  </div>
                </a>
              </SidebarMenuButton>
            </SidebarMenuItem>
          </SidebarMenu>
        </SidebarHeader>
        <SidebarContent>
          <NavMain items={nav_data.navMain}/>
        </SidebarContent>
        <SidebarFooter>
          {userDisplayProfile && (
              <NavUser user={userDisplayProfile} onSignOut={signOut}/>
          )}
        </SidebarFooter>
        <SidebarRail/>
      </Sidebar>
  );
}
