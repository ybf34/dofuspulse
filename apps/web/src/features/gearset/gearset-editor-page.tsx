import { useParams } from "@tanstack/react-router";
import { toast } from "sonner";
import LoadingSpinner from "@/components/ui/loading-spinner.tsx";
import {
	ResizableHandle,
	ResizablePanel,
	ResizablePanelGroup,
} from "@/components/ui/resizable";
import { SheetWrapper } from "@/components/ui/sheet-wrapper.tsx";
import GearsetEditor from "@/features/gearset/components/gearset-editor.tsx";
import useEquipItemMutation from "@/features/gearset/hooks/use-equip-item-mutation.ts";
import { useGearsetEditorStore } from "@/features/gearset/hooks/use-gearset-editor-store.ts";
import useGearsetQuery from "@/features/gearset/hooks/use-gearset-query";
import useGearsetSlotTypesQuery from "@/features/gearset/hooks/use-gearset-slot-types-query.ts";
import { findItemSlotIdentifier } from "@/features/gearset/utils/gearset.utils.ts";
import { ItemExplorer } from "@/features/items/components/item-explorer.tsx";
import { useIsMobile } from "@/hooks/use-mobile.ts";
import type { APIItemDetails } from "@/services/api/api.types.ts";

export default function GearsetEditorPage() {
	const isMobile = useIsMobile();

	const gearsetId = useParams({
		from: "/auth/gearsets/$id",
		select: (params) => params.id,
	});

	const { gearset, isError, isPending } = useGearsetQuery(Number(gearsetId));
	const { slotTypes } = useGearsetSlotTypesQuery();

	const { equipItem } = useEquipItemMutation();

	const setItemExplorerOpen = useGearsetEditorStore(
		(s) => s.setItemExplorerOpen,
	);
	const isItemExplorerOpen = useGearsetEditorStore((s) => s.isItemExplorerOpen);

	const handleEquipItem = (item: APIItemDetails) => {
		if (!gearset || !slotTypes) return;

		const { selectedSlot } = useGearsetEditorStore.getState();

		const matchingSlot = findItemSlotIdentifier(
			gearset,
			item,
			slotTypes,
			selectedSlot,
		);

		if (matchingSlot) {
			equipItem({
				params: { gearSetId: gearset.id },
				body: { slotIdentifier: matchingSlot, itemId: item.id },
			});
		} else {
			toast.error("This item cannot be equipped.");
		}
	};

	if (isPending) {
		return <LoadingSpinner className="size-58" />;
	}

	if (isError) {
		return <h1 className="text-destructive">Gearset not found</h1>;
	}

	if (isMobile) {
		return (
			<div className="flex h-full">
				<div className="flex-1 min-w-0">
					{gearset && <GearsetEditor gearset={gearset} />}
				</div>
				<SheetWrapper
					side="right"
					className="sm:max-w-lg md:max-w-xl"
					open={isItemExplorerOpen}
					onOpenChange={setItemExplorerOpen}
					ariaLabel="item-explorer"
				>
					<ItemExplorer onSelect={handleEquipItem} />
				</SheetWrapper>
			</div>
		);
	}

	return (
		<ResizablePanelGroup direction="horizontal">
			<ResizablePanel defaultSize={50} minSize={30}>
				<div className="h-full">
					{gearset && <GearsetEditor gearset={gearset} />}
				</div>
			</ResizablePanel>
			<ResizableHandle withHandle />
			<ResizablePanel defaultSize={50} minSize={35}>
				<div className="h-full">
					<ItemExplorer onSelect={handleEquipItem} />
				</div>
			</ResizablePanel>
		</ResizablePanelGroup>
	);
}
