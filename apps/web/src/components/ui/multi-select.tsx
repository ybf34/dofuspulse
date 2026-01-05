import { ChevronsUpDown, ChevronUp, Minus, Plus, X } from "lucide-react";
import { useState } from "react";
import { Badge } from "@/components/ui/badge";
import {
	Command,
	CommandEmpty,
	CommandGroup,
	CommandInput,
	CommandItem,
} from "@/components/ui/command";
import {
	Popover,
	PopoverContent,
	PopoverTrigger,
} from "@/components/ui/popover";
import { Skeleton } from "@/components/ui/skeleton";
import { cn } from "@/lib/utils";
import type { ItemWithQuantity } from "@/types/types";
import { itemIconBaseUrl } from "@/utils/constants.ts";
import { ScrollArea } from "./scroll-area";

interface MultiSelectProps {
	options?: ItemWithQuantity[];
	selectedOptions: ItemWithQuantity[];
	onSelect: (selected: ItemWithQuantity[]) => void;
	onSearch?: (search: string) => void;
	onClearAll?: () => void;
	searchValue: string;
	placeholder?: string;
	className?: string;
	isLoading?: boolean;
	disabled?: boolean;
	onQuantityChange?: (id: number, quantity: number) => void;
	showQuantities?: boolean;
	maxVisibleBadges?: number;
}

function SelectedBadge({
	item,
	onUnselect,
	onQuantityChange,
	showQuantity,
}: {
	item: ItemWithQuantity;
	onUnselect: (item: ItemWithQuantity) => void;
	onQuantityChange?: (id: number, quantity: number) => void;
	showQuantity?: boolean;
}) {
	const quantity = item.quantity || 1;

	return (
		<Badge
			key={item.id}
			variant="outline"
			className="flex items-center gap-1.5 text-xs"
		>
			{item.iconId && (
				<img
					src={`${itemIconBaseUrl}/${item.iconId}.png`}
					alt={item.name}
					className="h-5 w-5"
				/>
			)}
			<span>{item.name}</span>
			{showQuantity && quantity !== undefined && onQuantityChange && (
				<div className="flex items-center gap-0.5 ml-1 border-l pl-1.5">
					<button
						type="button"
						onClick={(e) => {
							e.stopPropagation();
							onQuantityChange(item.id, Math.max(1, quantity - 1));
						}}
						className="rounded p-0.5 hover:bg-accent"
					>
						<Minus className="h-2.5 w-2.5" />
					</button>
					<span className="min-w-[1.5rem] text-center font-medium">
						{quantity}
					</span>
					<button
						type="button"
						onClick={(e) => {
							e.stopPropagation();
							onQuantityChange(item.id, quantity + 1);
						}}
						className="rounded p-0.5 hover:bg-accent"
					>
						<Plus className="h-2.5 w-2.5" />
					</button>
				</div>
			)}
			<button
				type="button"
				aria-label={`Remove ${item.name}`}
				className="ml-1 rounded-full p-0.5 hover:bg-destructive hover:text-destructive-foreground"
				onClick={(e) => {
					e.stopPropagation();
					onUnselect(item);
				}}
			>
				<X className="h-3 w-3" />
			</button>
		</Badge>
	);
}

export default function MultiSelect({
	options,
	selectedOptions,
	onSelect,
	onSearch,
	onClearAll,
	placeholder = "Select items...",
	searchValue,
	className,
	isLoading = false,
	onQuantityChange,
	showQuantities = false,
	maxVisibleBadges = 5,
}: MultiSelectProps) {
	const [open, setOpen] = useState(false);
	const [expanded, setExpanded] = useState(false);

	const handleUnselect = (item: ItemWithQuantity) => {
		onSelect(selectedOptions.filter((i) => i.id !== item.id));
	};

	const handleSelect = (item: ItemWithQuantity) => {
		const alreadySelected = selectedOptions.some((i) => i.id === item.id);
		onSelect(
			alreadySelected
				? selectedOptions.filter((i) => i.id !== item.id)
				: [...selectedOptions, item],
		);
	};

	const visibleItems = expanded
		? selectedOptions
		: selectedOptions.slice(0, maxVisibleBadges);

	return (
		<div className={cn("w-full", className)}>
			<Popover open={open} onOpenChange={setOpen}>
				<PopoverTrigger asChild>
					<div
						className={cn(
							"flex w-full min-h-12 items-start justify-between rounded-md border border-input bg-background text-sm",
							"focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2",
							"disabled:cursor-not-allowed disabled:opacity-50",
						)}
					>
						<div className="flex flex-wrap gap-1 flex-1 py-2 px-3">
							{selectedOptions.length === 0 ? (
								<span className="my-auto truncate text-muted-foreground">
									{placeholder}
								</span>
							) : (
								<>
									{visibleItems.map((item) => (
										<SelectedBadge
											key={item.id}
											item={item}
											onUnselect={handleUnselect}
											onQuantityChange={onQuantityChange}
											showQuantity={showQuantities}
										/>
									))}
									{selectedOptions.length > maxVisibleBadges && (
										<button
											type="button"
											onClick={(e) => {
												e.stopPropagation();
												setExpanded(!expanded);
											}}
											className="flex items-center rounded-full border border-border px-2.5 py-0.5 text-xs font-semibold hover:bg-accent/50 hover:text-primary"
										>
											{expanded ? (
												<ChevronUp className="h-3 w-3" />
											) : (
												`+${selectedOptions.length - maxVisibleBadges} more`
											)}
										</button>
									)}
								</>
							)}
						</div>

						<div className="flex h-full flex-col items-center justify-center gap-1 p-1">
							<ChevronsUpDown className="h-6 w-6 shrink-0 opacity-50 p-1 rounded-sm cursor-pointer hover:bg-accent/50" />
							{selectedOptions.length > 0 && onClearAll && (
								<button
									type="button"
									onClick={(e) => {
										e.stopPropagation();
										onClearAll();
									}}
									className="rounded-sm p-1 focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 hover:bg-accent/50"
									aria-label="Clear all selected items"
								>
									<X className="h-4 w-4 shrink-0 opacity-50" />
								</button>
							)}
						</div>
					</div>
				</PopoverTrigger>

				<PopoverContent
					className="w-[var(--radix-popover-trigger-width)] rounded-md bg-popover p-1"
					align="start"
				>
					<Command filter={() => 1} className="bg-popover">
						<CommandInput
							autoFocus={false}
							placeholder="Search items..."
							onValueChange={onSearch}
							value={searchValue}
							className="border-border/50"
						/>
						<ScrollArea className="h-64 max-h-[65vh]">
							{isLoading ? (
								<div className="p-2">
									{Array.from({ length: 13 }, (_, i) => `skeleton-${i}`).map(
										(key) => (
											<Skeleton
												key={key}
												className="mb-1 h-4 w-full last:mb-0"
											/>
										),
									)}
								</div>
							) : options && options.length > 0 ? (
								<CommandGroup>
									{options.map((option) => {
										const isSelected = selectedOptions.some(
											(i) => i.id === option.id,
										);
										return (
											<CommandItem
												key={option.id}
												value={String(option.id)}
												onSelect={() => handleSelect(option)}
												className="flex items-center gap-2 rounded-md px-2 py-1 hover:bg-accent/50"
											>
												<input
													type="checkbox"
													checked={isSelected}
													onChange={() => handleSelect(option)}
													className="appearance-none h-4 w-4 rounded border border-primary bg-background checked:bg-primary checked:text-primary-foreground relative checked:before:content-['✓'] checked:before:absolute checked:before:top-1/2 checked:before:left-1/2 checked:before:transform checked:before:-translate-x-1/2 checked:before:-translate-y-1/2 checked:before:text-xs checked:before:font-bold"
												/>
												{option.iconId && (
													<img
														src={`${itemIconBaseUrl}/${option.iconId}.png`}
														alt={option.name}
														className="h-5 w-5"
													/>
												)}
												<span className="truncate">{option.name}</span>
											</CommandItem>
										);
									})}
								</CommandGroup>
							) : (
								<CommandEmpty className="py-4 text-center text-sm text-muted-foreground">
									No items found.
								</CommandEmpty>
							)}
						</ScrollArea>
					</Command>
				</PopoverContent>
			</Popover>
		</div>
	);
}
