import { Info, TrendingUp } from "lucide-react";
import { KamasIcon } from "@/components/icons/kamas-icon";
import MultiSeriesChart from "@/components/ui/multi-series-chart.tsx";
import { Skeleton } from "@/components/ui/skeleton";
import {
	Tooltip,
	TooltipContent,
	TooltipProvider,
	TooltipTrigger,
} from "@/components/ui/tooltip";
import {
	calculatePortfolioMetrics,
	calculatePortfolioMetricsHistory,
	type PortfolioDailyMetrics,
} from "@/features/metrics/utils/profit-metrics.ts";
import { cn, formatNumber } from "@/lib/utils";
import type { ItemsProfitMetrics } from "@/services/api/api.types.ts";
import type { ItemWithQuantity } from "@/types/types";

type OfferSummaryProps = {
	selectedItems: ItemWithQuantity[];
	itemsProfitMetrics: ItemsProfitMetrics;
	isPending: boolean;
};

export function OfferSummary({
	isPending,
	itemsProfitMetrics,
	selectedItems,
}: OfferSummaryProps) {
	const summary: PortfolioDailyMetrics =
		!isPending && itemsProfitMetrics?.length
			? calculatePortfolioMetrics(selectedItems, itemsProfitMetrics)
			: {
					snapshotDate: new Date().toDateString(),
					totalCraftCost: 0,
					totalMarketPrice: 0,
					totalProfit: 0,
					roi: 0,
				};

	const offerHistory =
		!isPending && itemsProfitMetrics?.length
			? calculatePortfolioMetricsHistory(selectedItems, itemsProfitMetrics)
			: [];

	return (
		<TooltipProvider>
			<div className="border-b bg-gradient-to-b from-background to-background/50">
				<div className="container max-w-6xl mx-auto px-4 py-8">
					<div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-8 mb-8">
						<div className="flex-shrink-0">
							<div className="flex items-center gap-2 mb-2">
								<TrendingUp className="h-4 w-4 text-muted-foreground" />
								<span className="text-xs uppercase tracking-wider text-muted-foreground">
									Offer Summary
								</span>
							</div>

							{isPending ? (
								<Skeleton className="h-12 w-64 mt-2" />
							) : (
								<div className="flex items-center gap-3 sm:gap-4 py-2">
									<span
										className={cn(
											"text-5xl sm:text-6xl font-hero font-extrabold tracking-wide leading-tight",
											summary.totalProfit >= 0
												? "text-green-600 dark:text-green-500"
												: "text-rose-500 dark:text-rose-600",
										)}
									>
										{summary.totalProfit >= 0 ? "+" : ""}
										{summary.totalProfit
											.toLocaleString("en-US")
											.replace(/,/g, "  ")}
									</span>
									<KamasIcon className="h-10 w-10 sm:h-12 sm:w-12 shrink-0 mb-1" />
								</div>
							)}

							<p className="text-xs sm:text-sm text-muted-foreground mt-1">
								Profit across {selectedItems.length} item
								{selectedItems.length !== 1 ? "s" : ""}
							</p>
						</div>

						{isPending ? (
							<div className="flex flex-col sm:flex-row gap-3">
								{[...Array(2).keys()].map((i) => (
									<Skeleton
										key={i}
										className="h-24 w-full sm:w-40 rounded-lg"
									/>
								))}
							</div>
						) : (
							<div className="flex flex-col sm:flex-row gap-3">
								<SummaryBox
									label="Total Craft Cost"
									amount={summary.totalCraftCost}
									description="Total cost of crafting selected items."
								/>
								<SummaryBox
									label="Total Market Value"
									amount={summary.totalMarketPrice}
									description="Total sale value at current market price."
								/>
							</div>
						)}
					</div>

					{isPending ? (
						<Skeleton className="h-56 w-full mt-2 rounded-xl" />
					) : (
						offerHistory.length > 1 && (
							<div className="w-full mt-8">
								<h4 className="text-sm font-semibold text-foreground mb-4">
									Offer Value History
								</h4>
								<MultiSeriesChart<PortfolioDailyMetrics>
									data={offerHistory}
									xKey="snapshotDate"
									series={[
										{
											key: "totalMarketPrice",
											label: "Market Value",
											color: "#88F4FF",
										},
										{
											key: "totalCraftCost",
											label: "Craft Cost",
											color: "#FFD700",
										},
										{
											key: "totalProfit",
											label: "Total Profit",
											color: "#F080F0",
										},
									]}
									minHeight={220}
									type="line"
								/>
							</div>
						)
					)}
				</div>
			</div>
		</TooltipProvider>
	);
}

type SummaryBoxProps = {
	label: string;
	amount: number;
	description: string;
};

export function SummaryBox({ label, amount, description }: SummaryBoxProps) {
	const formattedAmount = formatNumber(amount);

	return (
		<div className="rounded-xl border border-border bg-background p-5">
			<div className="flex items-start justify-between gap-4 mb-3">
				<p className="text-xs font-medium uppercase tracking-widest text-muted-foreground leading-tight">
					{label}
				</p>

				<div className="shrink-0">
					<Tooltip>
						<TooltipTrigger asChild>
							<Info className="h-4 w-4 cursor-pointer text-muted-foreground/60 transition-colors hover:text-foreground" />
						</TooltipTrigger>
						<TooltipContent
							side="top"
							className="max-w-xs text-xs leading-relaxed"
						>
							{description}
						</TooltipContent>
					</Tooltip>
				</div>
			</div>

			<div className="flex items-baseline gap-2">
				<span className="text-3xl font-bold tabular-nums tracking-tight text-foreground">
					{formattedAmount}
				</span>
				<KamasIcon className="h-5 w-5 opacity-80 translate-y-[1px]" />
			</div>
		</div>
	);
}
