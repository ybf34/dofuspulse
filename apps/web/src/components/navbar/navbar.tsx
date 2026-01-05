import {useMatches} from "@tanstack/react-router";
import {Search} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {ModeToggle} from "@/components/ui/mode-toggle.tsx";
import {Separator} from "@/components/ui/separator";
import {SidebarTrigger} from "@/components/ui/sidebar.tsx";

export default function Navbar() {
  const matches = useMatches();
  const title = matches[matches.length - 1]?.meta?.[0]?.title || "";

  return (
      <header
          className="sticky top-0 z-50 flex h-14 w-full items-center border-b bg-background px-4 ">
        <div className="flex items-center gap-2">
          <SidebarTrigger className="h-9 w-9"/>
          <Separator orientation="vertical" className="hidden h-6 sm:block"/>
          <h1 className="truncate text-sm font-medium tracking-tight sm:text-base">
            {title}
          </h1>
        </div>

        <div className="hidden sm:flex flex-1 items-center justify-center px-4 md:px-8">
          <div className="relative w-full max-w-[400px]">
            <Search
                className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground"/>
            <Input
                placeholder="Search..."
                className="h-9 w-full border-none bg-muted/60 pl-10 shadow-none transition-colors focus-visible:bg-muted focus-visible:ring-1 focus-visible:ring-ring"
            />
          </div>
        </div>

        <div className="flex flex-1 sm:flex-none items-center justify-end gap-1">
          <Button
              variant="ghost"
              size="icon"
              className="h-9 w-9 sm:hidden"
              aria-label="Search"
          >
            <Search className="h-4 w-4"/>
          </Button>

          <ModeToggle/>
        </div>
      </header>
  );
}
