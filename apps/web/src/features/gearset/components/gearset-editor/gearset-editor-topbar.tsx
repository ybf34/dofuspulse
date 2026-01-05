import { useNavigate } from "@tanstack/react-router";
import { CheckCircle, Clock } from "lucide-react";
import { useState } from "react";
import { toast } from "sonner";
import { KamasIcon } from "@/components/icons/kamas-icon.tsx";
import { Button } from "@/components/ui/button.tsx";
import type { CharacterSelection } from "@/components/ui/character-class-select";
import { Separator } from "@/components/ui/separator";
import { CharacterSettingsDialog } from "@/features/gearset/components/gearset-editor/character-settings-dialog";
import GearsetEditorActions from "@/features/gearset/components/gearset-editor/gearset-editor-actions";
import { TagsSection } from "@/features/gearset/components/gearset-editor/tag-section";
import { TitleSection } from "@/features/gearset/components/gearset-editor/title-section";
import { GearsetPriceDialog } from "@/features/gearset/components/gearset-price-dialog";
import useDeleteGearsetMutation from "@/features/gearset/hooks/use-delete-gearset-mutation";
import { useUpdateGearsetMutation } from "@/features/gearset/hooks/use-update-gearset-mutation";
import { MAX_GEARSET_TAGS } from "@/features/gearset/utils/gearset.utils";
import { timeAgo } from "@/lib/time-ago.ts";
import { tryCatch } from "@/lib/try-catch.ts";
import type { APIGearset } from "@/services/api/api.types";
import { CharacterSection } from "./character-section";

type GearsetEditorTopbarProps = {
	gearset: APIGearset;
};

export default function GearsetEditorTopbar({
	gearset,
}: GearsetEditorTopbarProps) {
	const navigate = useNavigate();

	const { updateGearset, isPending: isSaving } = useUpdateGearsetMutation();
	const { deleteGearset } = useDeleteGearsetMutation();

	const [isCharacterDialogOpen, setIsCharacterDialogOpen] = useState(false);
	const [isPriceDialogOpen, setIsPriceDialogOpen] = useState(false);
	const [characterSelection, setCharacterSelection] =
		useState<CharacterSelection>({
			characterClass: gearset.characterClass.name,
			gender: gearset.characterGender,
		});

	const lastUpdatedText = timeAgo.format(
		new Date(gearset.updatedAt).getTime(),
		"mini-now",
	);

	const updateGearsetField = (
		body: Parameters<typeof updateGearset>[0]["body"],
	) => {
		updateGearset({ params: { id: gearset.id }, body });
	};

	const handleDeleteGearset = async () => {
		await deleteGearset({ params: { id: gearset.id } });
		toast.success("Gearset deleted successfully");
		await navigate({ to: "/gearsets" });
	};

	const handleAddTag = async (tag: string): Promise<boolean> => {
		const [_, error] = await tryCatch(
			updateGearset({
				params: { id: gearset.id },
				body: { tags: [...(gearset.tags || []), tag] },
			}),
		);
		return !error;
	};

	return (
		<div className="w-full overflow-x-hidden">
			<div className="px-6 sm:px-8 lg:pl-12 lg:pr-8 pt-8 pb-6 w-full">
				<div className="flex items-center justify-between w-full gap-4">
					<TitleSection
						value={gearset.title}
						isSaving={isSaving}
						onSave={(title) => updateGearsetField({ title })}
						className="flex-grow min-w-0"
					/>
					<div className="flex items-center space-x-2 flex-shrink-0">
						<Button
							variant="ghost"
							size="icon"
							onClick={() => setIsPriceDialogOpen(true)}
							className="flex"
						>
							<KamasIcon className="w-6 h-6" />
						</Button>
						<GearsetEditorActions onDelete={handleDeleteGearset} />
					</div>
				</div>
			</div>

			<div className="px-6 sm:px-8 lg:px-12 pb-8">
				<div className="flex flex-wrap items-center gap-3">
					<CharacterSection
						characterClass={gearset.characterClass.name}
						gender={gearset.characterGender}
						onEdit={() => setIsCharacterDialogOpen(true)}
					/>
					<Separator orientation="vertical" className="h-6" />
					<TagsSection
						tags={gearset.tags || []}
						isFull={(gearset.tags?.length || 0) >= MAX_GEARSET_TAGS}
						onAddTag={handleAddTag}
						onRemoveTag={(tag) =>
							updateGearsetField({
								tags: gearset.tags?.filter((t) => t !== tag) || [],
							})
						}
					/>
					<div
						className={`flex items-center gap-1 text-xs whitespace-nowrap ml-auto font-medium ${isSaving ? "text-yellow-500" : "text-muted-foreground"}`}
					>
						{isSaving ? (
							<>
								<Clock className="size-3.5 animate-spin" />
								<span>Saving...</span>
							</>
						) : (
							<>
								<CheckCircle className="size-3.5 text-green-400" />
								<span>Saved {lastUpdatedText}</span>
							</>
						)}
					</div>
				</div>
			</div>

			<CharacterSettingsDialog
				open={isCharacterDialogOpen}
				currentSelection={characterSelection}
				onSelectionChange={setCharacterSelection}
				onOpenChange={setIsCharacterDialogOpen}
				onSave={() => {
					updateGearsetField({
						characterClass: characterSelection.characterClass,
						characterGender: characterSelection.gender,
					});
				}}
			/>

			<GearsetPriceDialog
				open={isPriceDialogOpen}
				onOpenChange={setIsPriceDialogOpen}
				gearset={gearset}
			/>
		</div>
	);
}
