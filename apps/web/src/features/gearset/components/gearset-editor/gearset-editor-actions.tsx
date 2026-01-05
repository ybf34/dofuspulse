import { Copy, MoreVertical, Share2, Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import ConfirmAlertDialog from "@/components/ui/confirm-alert-dialog.tsx";
import {
	DropdownMenu,
	DropdownMenuContent,
	DropdownMenuGroup,
	DropdownMenuItem,
	DropdownMenuSeparator,
	DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

type GearsetEditorActionsProps = {
	onDelete: () => void;
};

export default function GearsetEditorActions({
	onDelete,
}: GearsetEditorActionsProps) {
	return (
		<DropdownMenu>
			<DropdownMenuTrigger asChild>
				<Button variant="ghost" size="icon">
					<MoreVertical className="h-5 w-5" />
				</Button>
			</DropdownMenuTrigger>

			<DropdownMenuContent align="end" className="w-48">
				<DropdownMenuGroup>
					<DropdownMenuItem>
						<Copy className="h-4 w-4 mr-2" />
						Duplicate
					</DropdownMenuItem>
					<DropdownMenuItem>
						<Share2 className="h-4 w-4 mr-2" />
						Share
					</DropdownMenuItem>
				</DropdownMenuGroup>

				<DropdownMenuSeparator />

				<DropdownMenuItem
					onSelect={(e) => e.preventDefault()}
					variant="destructive"
				>
					<ConfirmAlertDialog
						title="Delete Gearset"
						description="Are you sure you want to delete this gearset? This action cannot be undone."
						confirmText="Delete"
						onConfirm={onDelete}
					>
						<div className="flex w-full items-center">
							<Trash2 color="red" className="mr-4" />
							Delete
						</div>
					</ConfirmAlertDialog>
				</DropdownMenuItem>
			</DropdownMenuContent>
		</DropdownMenu>
	);
}
