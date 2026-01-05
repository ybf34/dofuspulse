import { Plus, Repeat, Trash2 } from "lucide-react";
import type { CSSProperties } from "react";
import { Button } from "@/components/ui/button";
import { MarketSignalTag } from "@/components/ui/market-signal-tag.tsx";
import {
	Popover,
	PopoverContent,
	PopoverTrigger,
} from "@/components/ui/popover";
import { gearsetSlotConfig } from "@/features/gearset/utils/gearset.utils.ts";
import ItemEffectsList from "@/features/items/components/item-effects-list";
import { useIsMobile } from "@/hooks/use-mobile.ts";
import type {
	APIGearsetSlot,
	GearsetSlotTypeIdentifier,
	ItemPerformance,
} from "@/services/api/api.types";
import { itemIconBaseUrl } from "@/utils/constants";

export type SlotSize = {
	width: string;
	height: string;
};

export type GearSetSlotProps = {
	slotType: GearsetSlotTypeIdentifier;
	slot?: APIGearsetSlot;
	performance?: ItemPerformance;
	onUnequip: (slotId: number) => void;
	onSelectSlot: (
		slotType: GearsetSlotTypeIdentifier,
		showItemExplorerSheet?: boolean,
	) => void;
	slotSize: SlotSize;
	isPreview: boolean;
};

export default function GearsetSlot({
	slotType,
	slot,
	performance,
	slotSize,
	onSelectSlot,
	onUnequip,
	isPreview,
}: GearSetSlotProps) {
	const isMobile = useIsMobile();

	const position = gearsetSlotConfig[slotType].gridPosition;
	const equippedItem = slot?.itemDetails;
	const iconUrl = equippedItem
		? `${itemIconBaseUrl}/${equippedItem.iconId}.png`
		: gearsetSlotConfig[slotType].placeholderIcon;

	const slotStyle: CSSProperties = {
		gridColumnStart: position.colStart,
		gridRowStart: position.rowStart,
		width: slotSize.width,
		height: slotSize.height,
		backgroundSize: "78%",
		backgroundPosition: "center",
		backgroundRepeat: "no-repeat",
		backgroundImage: `url('${iconUrl}')`,
		filter: equippedItem ? "none" : "grayscale(100%) brightness(1) opacity(1)",
	};

	return (
		<Popover>
			<PopoverTrigger asChild>
				<Button
					className="relative bg-secondary/60 w-10 border border-primary/10 transition-all duration-200 group p-1 hover:bg-secondary/70 hover:border-primary/30"
					style={slotStyle}
					onClick={() => onSelectSlot(slotType, isMobile && !equippedItem)}
				>
					{!equippedItem && !isPreview && (
						<div className="absolute inset-0 opacity-0 group-hover:opacity-100 rounded-md transition-all duration-200 flex items-center justify-center bg-gradient-to-br from-neutral-700/80 to-neutral-800/80 backdrop-blur-sm">
							<Plus
								className="w-5 h-5 text-white/70 group-hover:text-white transition-all duration-300 group-hover:drop-shadow-[0_0_4px_rgba(255,255,255,0.8)] group-hover:scale-110"
								strokeWidth={2.5}
							/>
						</div>
					)}
				</Button>
			</PopoverTrigger>

			{equippedItem && (
				<PopoverContent className="p-3 w-76 rounded-lg shadow-2xl border border-border bg-card text-card-foreground text-sm dark:border-primary/20">
					<div className="flex justify-between items-start gap-2">
						<div className="flex flex-col flex-grow min-w-0">
							<h4 className="text-base font-bold truncate">
								{equippedItem.name}
							</h4>
						</div>

						{!isPreview && (
							<div className="flex items-center gap-1 flex-shrink-0">
								{isMobile && (
									<Button
										variant="ghost"
										size="icon"
										className="h-6 w-6 text-primary hover:bg-primary/10"
										onClick={() => onSelectSlot(slotType, true)}
										aria-label="Replace item"
									>
										<Repeat className="h-4 w-4" />
									</Button>
								)}
								<Button
									variant="ghost"
									size="icon"
									className="h-6 w-6 text-destructive hover:bg-destructive/10"
									onClick={() => onUnequip(slot?.id)}
									aria-label="Unequip item"
								>
									<Trash2 className="h-4 w-4" />
								</Button>
							</div>
						)}
					</div>

					<div className="flex justify-between items-center mt-1.5 text-xs pb-2 border-b border-border dark:border-primary/20">
						<p className="text-muted-foreground">Level {equippedItem.level}</p>

						{performance && (
							<div className="flex justify-end gap-2">
								<MarketSignalTag
									label="Price"
									value={performance.priceTrend * 100}
									tooltip={`Price ${
										performance.priceTrend >= 0 ? "up" : "down"
									} ${Math.abs(performance.priceTrend * 100).toFixed(
										1,
									)}% (7 days)`}
								/>
								<MarketSignalTag
									label="Craft"
									value={performance.craftCostTrend * 100}
									tooltip={`Craft cost ${
										performance.craftCostTrend >= 0 ? "up" : "down"
									} ${Math.abs(performance.craftCostTrend * 100).toFixed(
										1,
									)}% (7 days)`}
								/>
							</div>
						)}
					</div>

					<div className="pt-2">
						<ItemEffectsList stats={equippedItem.possibleEffects} />
					</div>
				</PopoverContent>
			)}
		</Popover>
	);
}
