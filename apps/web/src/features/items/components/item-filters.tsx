import { Search } from "lucide-react";
import { memo, useCallback } from "react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import { Slider } from "@/components/ui/slider";
import { useEffectsQuery } from "@/features/items/hooks/use-effects-query.ts";
import { useItemFiltersStore } from "@/features/items/hooks/use-item-filters-store";
import { useItemTypesQuery } from "@/features/items/hooks/use-item-types-query.ts";
import {
	getEffectIconPath,
	itemEffects,
	toEffectRow,
} from "@/features/items/utils/item.utils.ts";
import type { Effect, ItemType } from "@/services/api/api.types";

export type ItemFilterHide = {
	itemTypes?: boolean;
	effects?: boolean;
	levelRange?: boolean;
	searchBar?: boolean;
};

export type ItemFilterConfig = {
	hide?: ItemFilterHide;
};

export type ItemFilterProps = {
	filterConfig?: ItemFilterConfig;
};

type ToggleIdFunction = (key: "typesIds" | "effectsIds", id: number) => void;

const ItemTypeCheckbox = memo(
	({ type, toggleId }: { type: ItemType; toggleId: ToggleIdFunction }) => {
		const checked = useItemFiltersStore(
			(state) => state.filters.typesIds?.includes(type.id) ?? false,
		);
		const handleChange = useCallback(
			() => toggleId("typesIds", type.id),
			[toggleId, type.id],
		);

		return (
			<div className="flex items-center gap-2 rounded-md px-2 py-1 hover:bg-accent hover:text-accent-foreground transition-colors">
				<Checkbox
					checked={checked}
					onCheckedChange={handleChange}
					id={`type-${type.id}`}
				/>
				<span className="text-sm dark:text-gray-200 cursor-pointer">
					{type.name}
				</span>
			</div>
		);
	},
);

const EffectCheckbox = memo(
	({ effect, toggleId }: { effect: Effect; toggleId: ToggleIdFunction }) => {
		const formatted = toEffectRow(effect);

		const effectString = formatted?.text;
		const checked = useItemFiltersStore(
			(state) => state.filters.effectsIds?.includes(effect.id) ?? false,
		);
		const handleChange = useCallback(
			() => toggleId("effectsIds", effect.id),
			[toggleId, effect.id],
		);

		if (
			!effectString ||
			!itemEffects.has(formatted?.effectSlug) ||
			effectString.startsWith("-") ||
			effectString.startsWith("+")
		) {
			return null;
		}

		return (
			<div className="flex items-center gap-2 rounded-md px-2 py-1 hover:bg-accent hover:text-accent-foreground transition-colors">
				<Checkbox
					checked={checked}
					onCheckedChange={handleChange}
					id={`effect-${effect.id}`}
				/>
				<img
					src={getEffectIconPath(formatted?.effectSlug)}
					alt={formatted?.effectSlug}
					className="w-4 h-4"
				/>
				<span className="text-sm dark:text-gray-200 cursor-pointer">
					{formatted.text}
				</span>
			</div>
		);
	},
);

export default function ItemFilters({
	filterConfig: { hide = {} } = {},
}: ItemFilterProps) {
	const { itemTypes } = useItemTypesQuery({ size: 204 });
	const { effects } = useEffectsQuery();
	const { filters, setFilters, toggleId } = useItemFiltersStore();

	const sliderKey = `${filters.minLevel}-${filters.maxLevel}`;

	return (
		<div className="flex flex-col h-full bg-transparent">
			<div className="p-5 flex flex-col gap-6">
				{!hide?.searchBar && (
					<div className="relative">
						<Search className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground w-4 h-4" />
						<Input
							placeholder="Search items..."
							value={filters.name ?? ""}
							onChange={(e) => setFilters({ name: e.target.value })}
							className="pl-9 h-9 text-sm rounded-md"
						/>
					</div>
				)}

				{!hide?.levelRange && (
					<div className="space-y-3">
						<h2 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
							Level Range
						</h2>
						<div className="space-y-4">
							<Slider
								key={sliderKey}
								defaultValue={[filters.minLevel ?? 1, filters.maxLevel ?? 200]}
								onValueCommit={([min, max]) => {
									setFilters({ minLevel: min, maxLevel: max });
								}}
								min={1}
								max={200}
								step={1}
								className="w-full"
							/>
							<div className="flex items-center justify-between text-sm text-muted-foreground">
								<div className="flex items-center gap-1.5">
									<span>Min:</span>
									<Badge variant="outline">{filters.minLevel ?? 1}</Badge>
								</div>
								<div className="flex items-center gap-1.5">
									<span>Max:</span>
									<Badge variant="outline">{filters.maxLevel ?? 200}</Badge>
								</div>
							</div>
						</div>
					</div>
				)}

				{!hide?.itemTypes && (
					<>
						<Separator />
						<div className="space-y-3">
							<div className="flex items-center justify-between">
								<h2 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
									Item Types
								</h2>
								{(filters.typesIds?.length ?? 0) > 0 && (
									<Button
										variant="ghost"
										size="sm"
										onClick={() => setFilters({ typesIds: [] })}
										className="h-5 px-2 text-xs text-muted-foreground hover:text-foreground"
									>
										Clear
									</Button>
								)}
							</div>
							<ScrollArea className="h-48 pr-2">
								<div className="space-y-1.5">
									{itemTypes?.map((type) => (
										<ItemTypeCheckbox
											key={type.id}
											type={type}
											toggleId={toggleId}
										/>
									))}
								</div>
							</ScrollArea>
						</div>
					</>
				)}

				<Separator />

				{!hide?.effects && (
					<div className="space-y-3">
						<div className="flex items-center justify-between">
							<h2 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
								Effects
							</h2>
							{(filters.effectsIds?.length ?? 0) > 0 && (
								<Button
									variant="ghost"
									size="sm"
									onClick={() => setFilters({ effectsIds: [] })}
									className="h-5 px-2 text-xs text-muted-foreground hover:text-foreground"
								>
									Clear
								</Button>
							)}
						</div>
						<ScrollArea className="h-68 pr-2">
							<div className="space-y-1.5">
								{effects?.map((effect) => (
									<EffectCheckbox
										key={effect.id}
										effect={effect}
										toggleId={toggleId}
									/>
								))}
							</div>
						</ScrollArea>
					</div>
				)}
			</div>
		</div>
	);
}
