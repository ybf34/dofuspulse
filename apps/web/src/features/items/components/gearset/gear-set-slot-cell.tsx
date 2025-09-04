import { TooltipArrow } from "@radix-ui/react-tooltip";
import { Plus, Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Label } from "@/components/ui/label.tsx";
import {
	Popover,
	PopoverContent,
	PopoverTrigger,
} from "@/components/ui/popover.tsx";
import {
	Tooltip,
	TooltipContent,
	TooltipTrigger,
} from "@/components/ui/tooltip.tsx";
import { ItemEffectsList } from "@/features/items/components/item/item-effects-list.tsx";
import type { GearSetSlotCellProps, GridPosition } from "@/types/types.ts";
import { itemIconBaseUrl } from "@/utils/constants.ts";
import { GearsetSlotGridPosition } from "@/utils/gear-set.ts";

export default function GearSetSlotCell({
	itemDetails,
	slotIdentifier,
}: GearSetSlotCellProps) {
	const position: GridPosition = GearsetSlotGridPosition[slotIdentifier];

	const equipmentIcon = itemDetails?.iconId
		? {
				backgroundImage: `url('${itemIconBaseUrl}/${itemDetails.iconId}.png')`,
			}
		: {};

	const baseSlotStyle = {
		gridColumnStart: position.colStart,
		gridRowStart: position.rowStart,
		backgroundSize: "80%",
		backgroundPosition: "center",
		backgroundRepeat: "no-repeat",
	};

	const equipmentSlotStyle = {
		...baseSlotStyle,
		...equipmentIcon,
	};

	return (
		<Popover>
			<Tooltip>
				<PopoverTrigger asChild>
					<TooltipTrigger asChild>
						{itemDetails ? (
							<Button
								className="bg-secondary w-10"
								style={equipmentSlotStyle}
							></Button>
						) : (
							<Button
								className="relative bg-secondary w-10 transition-all duration-200 group p-1 hover:bg-secondary"
								style={baseSlotStyle}
							>
								<div className="w-full h-full opacity-0 group-hover:opacity-100 rounded-md transition-all duration-200 flex items-center justify-center bg-neutral-700">
									<Plus
										className="w-5 h-5 text-white/70 group-hover:text-white transition-transform duration-300 group-hover:drop-shadow-[0_0_2px_rgba(255,255,255,0.7)] group-hover:animate-float"
										strokeWidth={2}
									/>
								</div>
							</Button>
						)}
					</TooltipTrigger>
				</PopoverTrigger>

				{itemDetails && (
					<TooltipContent
						className="p-2 rounded-lg shadow-2xl border border-primary/20 bg-card text-card-foreground max-w-xs
                                 font-mono text-sm leading-tight tracking-tight"
					>
						<div className="flex justify-between items-start gap-1 pb-1 border-b border-primary/20 mb-1">
							<div className="flex flex-col flex-grow">
								<h4 className={`text-base font-extrabold text-foreground`}>
									{itemDetails.name}
								</h4>
								<p className="text-muted-foreground text-sm">
									Level {itemDetails.level} | {itemDetails.itemTypeId}
								</p>
							</div>

							<Button
								variant="ghost"
								size="icon"
								className="text-destructive hover:bg-destructive/10 h-6 w-6"
							>
								<Trash2 className="h-4 w-4" />
							</Button>
						</div>

						{itemDetails.possibleEffects && (
							<ItemEffectsList stats={itemDetails.possibleEffects} />
						)}
						<TooltipArrow className="fill-card" />
					</TooltipContent>
				)}
			</Tooltip>
			<PopoverContent className="w-80">
				<div className="grid gap-4">
					<div className="space-y-2">
						<h4 className="leading-none font-medium">
							{itemDetails
								? `Edit ${itemDetails.name}`
								: `Select Item for ${slotIdentifier}`}
						</h4>
						<p className="text-muted-foreground text-sm">
							{itemDetails
								? "Adjust item details or replace."
								: "Choose an item for this slot."}
						</p>
					</div>
					<div className="grid gap-2">
						<div className="grid grid-cols-3 items-center gap-4">
							<Label htmlFor="width">Width</Label>
							<Input defaultValue="100%" className="col-span-2 h-8" />
						</div>
						<div className="grid grid-cols-3 items-center gap-4">
							<Label htmlFor="maxWidth">Max. width</Label>
							<Input defaultValue="300px" className="col-span-2 h-8" />
						</div>
						<div className="grid grid-cols-3 items-center gap-4">
							<Label htmlFor="height">Height</Label>
							<Input defaultValue="25px" className="col-span-2 h-8" />
						</div>
						<div className="grid grid-cols-3 items-center gap-4">
							<Label htmlFor="maxHeight">Max. height</Label>
							<Input defaultValue="none" className="col-span-2 h-8" />
						</div>
					</div>
				</div>
			</PopoverContent>
		</Popover>
	);
}
