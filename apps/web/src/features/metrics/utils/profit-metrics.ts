import type { ItemsProfitMetrics } from "@/services/api/api.types";
import type { ItemWithQuantity } from "@/types/types";

export type PortfolioDailyMetrics = {
	snapshotDate: string;
	totalProfit: number;
	totalCraftCost: number;
	totalMarketPrice: number;
	roi: number;
};

export function calculatePortfolioMetricsHistory(
	items: ItemWithQuantity[],
	itemsProfitMetrics: ItemsProfitMetrics,
): PortfolioDailyMetrics[] {
	if (!itemsProfitMetrics.length || !items.length) return [];

	const quantities = new Map(items.map((i) => [i.id, i.quantity || 1]));
	const timeline = new Map<
		string,
		Map<number, { craft: number; market: number }>
	>();
	const allDatesSet = new Set<string>();

	for (const im of itemsProfitMetrics) {
		const qty = quantities.get(im.itemId) || 1;
		for (const p of im.profitMetrics) {
			if (!timeline.has(p.snapshotDate)) {
				timeline.set(p.snapshotDate, new Map());
				allDatesSet.add(p.snapshotDate);
			}
			timeline.get(p.snapshotDate)?.set(im.itemId, {
				craft: p.craftCost * qty,
				market: p.sellPrice * qty,
			});
		}
	}

	const sortedDates = Array.from(allDatesSet).sort(
		(a, b) => new Date(a).getTime() - new Date(b).getTime(),
	);

	const latestValues = new Map<number, { craft: number; market: number }>();

	return sortedDates.map((date) => {
		const updates = timeline.get(date);

		if (updates) {
			for (const [itemId, val] of updates) {
				latestValues.set(itemId, val);
			}
		}

		let totalMarket = 0;
		let totalCraft = 0;

		for (const v of latestValues.values()) {
			totalMarket += v.market;
			totalCraft += v.craft;
		}

		return {
			snapshotDate: date,
			totalMarketPrice: totalMarket,
			totalCraftCost: totalCraft,
			totalProfit: totalMarket - totalCraft,
			roi: totalCraft ? totalMarket / totalCraft : 0,
		};
	});
}

export function calculatePortfolioMetrics(
	items: ItemWithQuantity[],
	itemsProfitMetrics: ItemsProfitMetrics,
): PortfolioDailyMetrics {
	if (!itemsProfitMetrics.length || !items.length) {
		return {
			snapshotDate: "",
			totalProfit: 0,
			totalCraftCost: 0,
			totalMarketPrice: 0,
			roi: 0,
		};
	}

	let totalMarket = 0;
	let totalCraft = 0;
	let latestDate = "";

	for (const item of items) {
		const metrics = itemsProfitMetrics.find(
			(m) => m.itemId === item.id,
		)?.profitMetrics;
		if (!metrics?.length) continue;

		const latest = metrics[metrics.length - 1];
		const qty = item.quantity || 1;

		totalMarket += latest.sellPrice * qty;
		totalCraft += latest.craftCost * qty;

		if (latest.snapshotDate > latestDate) {
			latestDate = latest.snapshotDate;
		}
	}

	return {
		snapshotDate: latestDate,
		totalMarketPrice: totalMarket,
		totalCraftCost: totalCraft,
		totalProfit: totalMarket - totalCraft,
		roi: totalCraft ? totalMarket / totalCraft : 0,
	};
}
