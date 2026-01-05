import { Settings } from "lucide-react";
import { Button } from "@/components/ui/button";
import type { CharacterSelection } from "@/components/ui/character-class-select";
import CharacterClassSelect from "@/components/ui/character-class-select";
import {
	Dialog,
	DialogContent,
	DialogFooter,
	DialogHeader,
	DialogTitle,
} from "@/components/ui/dialog";

interface CharacterSettingsDialogProps {
	open: boolean;
	currentSelection: CharacterSelection;
	onOpenChange: (open: boolean) => void;
	onSelectionChange: (character: CharacterSelection) => void;
	onSave: () => void;
}

export function CharacterSettingsDialog({
	open,
	currentSelection,
	onOpenChange,
	onSelectionChange,
	onSave,
}: CharacterSettingsDialogProps) {
	const handleSave = () => {
		onSave();
		onOpenChange(false);
	};

	const handleCancel = () => {
		onOpenChange(false);
	};

	return (
		<Dialog open={open} onOpenChange={onOpenChange}>
			<DialogContent className="sm:max-w-md p-8">
				<DialogHeader>
					<DialogTitle className="flex items-center gap-2">
						<Settings className="h-5 w-5" />
						Character Settings
					</DialogTitle>
				</DialogHeader>

				<CharacterClassSelect
					value={currentSelection}
					onChange={onSelectionChange}
					className="py-4"
				/>

				<DialogFooter>
					<Button variant="outline" onClick={handleCancel}>
						Cancel
					</Button>

					<Button onClick={handleSave}>Save</Button>
				</DialogFooter>
			</DialogContent>
		</Dialog>
	);
}
