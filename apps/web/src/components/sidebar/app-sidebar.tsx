import "@/components/sidebar/menu.css";
import {Link} from "@tanstack/react-router";
import DofusIcon from "@/components/icons/dofus-icon";
import {NavUser} from "@/components/sidebar/nav-user";
import {Sidebar, SidebarContent, SidebarFooter, SidebarHeader,} from "@/components/ui/sidebar";

export function AppSidebar({...props}: React.ComponentProps<typeof Sidebar>) {
  const tabs = [
    {title: "Dashboard", url: "/dashboard"},
    {title: "My Gearsets", url: "/gearsets"},
    {title: "Portfolio", url: "/portfolio"},
    {title: "Offer Evaluator", url: "/offer-evaluator"},
    {title: "Item Browser", url: "/items"},
  ] as const;

  return (
      <Sidebar
          {...props}
          className="z-20 border-border/40 border-r-0 shadow-none border-none"
      >
        <SidebarHeader className="pt-8 pb-10 px-6">
          <Link
              to="/"
              className="flex items-center gap-3 group"
              aria-label="Dofus Pulse home"
          >
            <DofusIcon className="w-8 h-8 fill-zinc-950 dark:fill-white"/>

            <div className="flex items-baseline gap-1.5">
						<span
                className="text-lg font-semibold dark:font-light tracking-tight text-zinc-950 dark:text-white transition-all">
							DOFUS
						</span>
              <span
                  className="text-lg font-medium dark:font-light tracking-widest text-zinc-950 dark:text-white transition-all">
							PULSE
						</span>
            </div>
          </Link>
        </SidebarHeader>
        <SidebarContent className="sidebar-content">
          <ul className="menu-hover-fill text-zinc-100">
            {tabs.map((tab) => (
                <li key={tab.title}>
                  <Link
                      to={tab.url}
                      data-text={tab.title}
                      title={tab.title}
                      activeProps={{
                        className: "text-amber-500 dark:text-amber-400 font-medium",
                      }}
                  >
                    {tab.title}
                  </Link>
                </li>
            ))}
          </ul>
        </SidebarContent>

        <SidebarFooter>
          <NavUser/>
        </SidebarFooter>
      </Sidebar>
  );
}
