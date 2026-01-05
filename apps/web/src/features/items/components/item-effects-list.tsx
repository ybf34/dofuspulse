import { memo } from "react";
import { useEffectsQuery } from "@/features/items/hooks/use-effects-query";
import {
	getEffectIconPath,
	itemEffects,
	type ItemStatRow,
	toItemStatRow,
} from "@/features/items/utils/item.utils.ts";
import type { ItemEffect } from "@/services/api/api.types";

type ItemStatsProps = {
	stats?: ItemEffect[];
};

export default memo(function ItemEffectsList({ stats }: ItemStatsProps) {
	const { effects, isPending } = useEffectsQuery();

	if (isPending) {
		return (
			<p className="text-xs text-neutral-500 font-mono">Loading effects…</p>
		);
	}

	const formattedStats = (stats ?? [])
		.map((stat) => toItemStatRow(stat, effects))
		.filter(
			(stat): stat is ItemStatRow =>
				stat !== null && itemEffects.has(stat.effectSlug),
		);

	if (formattedStats.length === 0) {
		return (
			<p className="text-xs text-neutral-500 font-mono">No effects found</p>
		);
	}

	return (
		<div className="flex flex-col gap-1 pr-1 w-full">
			{formattedStats.map((stat) => (
				<div
					key={stat.effectId}
					className={`flex items-center text-xs leading-snug min-w-0 font-mono 
            ${
							stat.isPositive
								? "text-emerald-600 dark:text-emerald-400"
								: "text-rose-600 dark:text-rose-400"
						}`}
				>
					{itemEffects.has(stat.effectSlug) && (
						<img
							src={getEffectIconPath(stat.effectSlug)}
							alt=""
							className="h-4 w-4 mr-2 opacity-80 shrink-0"
							onError={(e) => {
								e.currentTarget.style.display = "none";
							}}
						/>
					)}
					<span className="truncate min-w-0 flex-1">{stat.text}</span>
				</div>
			))}
		</div>
	);
});
