import {SidebarProvider, SidebarTrigger} from "@/components/ui/sidebar";
import {AppSidebar} from "@/components/app-sidebar";

export default function Layout({children}: { children: React.ReactNode }) {
  return (
      <SidebarProvider>
        <div className="flex min-h-screen w-full">
          <AppSidebar/>

          <main className="flex-1">
            <SidebarTrigger/>
            {children}
          </main>
        </div>
      </SidebarProvider>
  );
}