import { BadgeCheck, ChevronsUpDown, LogOut } from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
	DropdownMenu,
	DropdownMenuContent,
	DropdownMenuItem,
	DropdownMenuSeparator,
	DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
	SidebarMenu,
	SidebarMenuButton,
	SidebarMenuItem,
	useSidebar,
} from "@/components/ui/sidebar";
import { useUserQuery } from "@/features/auth/hooks/use-user-query";
import { normalizeUserProfile } from "@/features/auth/utils/user-profile-utils";
import { getApiUrl } from "@/services/api/api";
import type { UserProfile } from "@/types/types";

export function NavUser() {
	const { isMobile } = useSidebar();
	const { user: user_ } = useUserQuery();

	const user = user_ && normalizeUserProfile(user_ as UserProfile);

	const primaryIdentifier = user?.name || user?.email;
	const secondaryIdentifier = user?.name ? user?.email : null;

	return (
		<SidebarMenu>
			<SidebarMenuItem>
				<DropdownMenu>
					<DropdownMenuTrigger asChild>
						<SidebarMenuButton
							size="lg"
							className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
						>
							<Avatar className="h-8 w-8 rounded-lg">
								<AvatarImage src={user?.avatar || ""} alt={primaryIdentifier} />
								<AvatarFallback className="rounded-lg">
									{primaryIdentifier?.slice(0, 2).toUpperCase()}
								</AvatarFallback>
							</Avatar>
							<div className="grid flex-1 text-left text-sm leading-tight">
								<span className="truncate font-semibold">
									{primaryIdentifier}
								</span>
								{secondaryIdentifier && (
									<span className="truncate text-xs text-muted-foreground">
										{secondaryIdentifier}
									</span>
								)}
							</div>
							<ChevronsUpDown className="ml-auto size-4" />
						</SidebarMenuButton>
					</DropdownMenuTrigger>
					<DropdownMenuContent
						className="w-(--radix-dropdown-menu-trigger-width) min-w-56 rounded-lg"
						side={isMobile ? "bottom" : "right"}
						align="end"
						sideOffset={4}
					>
						<DropdownMenuItem>
							<BadgeCheck className="mr-2 h-4 w-4" />
							Account
						</DropdownMenuItem>

						<DropdownMenuSeparator />

						<a href={getApiUrl("/api/v1/auth/logout")}>
							<DropdownMenuItem className="text-destructive focus: text-destructive cursor-pointer">
								<LogOut className="mr-2 h-4 w-4" />
								Disconnect
							</DropdownMenuItem>
						</a>
					</DropdownMenuContent>
				</DropdownMenu>
			</SidebarMenuItem>
		</SidebarMenu>
	);
}
