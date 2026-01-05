import { TrendingDown, TrendingUp } from "lucide-react";
import { KamasIcon } from "@/components/icons/kamas-icon";
import { ItemProfitMetricsTabs } from "@/features/metrics/components/item-profit-metrics-tabs";
import { cn } from "@/lib/utils";
import type { ItemProfitMetrics } from "@/services/api/api.types";
import type { ItemWithQuantity } from "@/types/types";
import { itemIconBaseUrl } from "@/utils/constants";

type ItemProfitMetricsCardProps = {
	item: ItemWithQuantity;
	metrics: ItemProfitMetrics;
};

export default function ItemProfitMetricsCard({
	item,
	metrics,
}: ItemProfitMetricsCardProps) {
	const latest = metrics[metrics.length - 1];
	const quantity = item.quantity || 1;

	if (!latest) return null;

	const totalCraftCost = latest.craftCost * quantity;
	const totalSellPrice = latest.sellPrice * quantity;
	const totalProfit = totalSellPrice - totalCraftCost;
	const isProfit = totalProfit >= 0;

	return (
		<div className=" border bg-background p-4 space-y-4">
			<div className="flex items-start gap-3">
				<div className="relative shrink-0">
					<img
						src={`${itemIconBaseUrl}/${item.iconId}.png`}
						alt={item.name}
						className="h-10 w-10 rounded"
					/>
					{quantity > 1 && (
						<span className="absolute -top-1.5 -right-1.5 flex h-5 w-5 items-center justify-center rounded-full bg-primary text-[10px] font-bold text-primary-foreground">
							{quantity}
						</span>
					)}
				</div>

				<div className="flex flex-1 min-w-0 flex-col gap-0.5">
					<div className="flex items-center justify-between">
						<h3 className="text-sm font-semibold truncate pr-4">{item.name}</h3>
						<div className="flex items-center gap-1.5 shrink-0">
							<span className="text-sm text-muted-foreground font-medium">
								Profit:
							</span>
							{isProfit ? (
								<TrendingUp className="h-3.5 w-3.5 text-green-600 dark:text-green-400" />
							) : (
								<TrendingDown className="h-3.5 w-3.5 text-red-600 dark:text-red-400" />
							)}
							<span
								className={cn(
									"text-sm font-bold tabular-nums",
									isProfit
										? "text-green-600 dark:text-green-400"
										: "text-red-600 dark:text-red-400",
								)}
							>
								{isProfit ? "+" : ""}
								{totalProfit.toLocaleString()}
							</span>
							<KamasIcon className="h-3.5 w-3.5" />
						</div>
					</div>

					<div className="flex items-center justify-between text-xs">
						<div className="flex items-center gap-1">
							<span className="text-muted-foreground">Craft:</span>
							<span className="font-medium text-foreground">
								{totalCraftCost.toLocaleString()}
							</span>
							<KamasIcon className="h-3 w-3" />
						</div>
						<div className="flex items-center gap-1">
							<span className="text-muted-foreground">Market:</span>
							<span className="font-medium text-foreground">
								{totalSellPrice.toLocaleString()}
							</span>
							<KamasIcon className="h-3 w-3" />
						</div>
					</div>
				</div>
			</div>
			<ItemProfitMetricsTabs metrics={metrics} />
		</div>
	);
}
