import {Ban, BarChart3, Package} from "lucide-react";
import {KamasIcon} from "@/components/icons/kamas-icon";
import {Badge} from "@/components/ui/badge";
import {Dialog, DialogContent, DialogHeader, DialogTitle,} from "@/components/ui/dialog";
import {EmptyDescription, EmptyHeader, EmptyTitle,} from "@/components/ui/empty";
import MultiSeriesChart from "@/components/ui/multi-series-chart";
import {ScrollArea} from "@/components/ui/scroll-area";
import {Separator} from "@/components/ui/separator";
import {Skeleton} from "@/components/ui/skeleton";
import ItemProfitMetricsCard from "@/features/metrics/components/item-profit-metrics-card";
import {useItemsProfitMetricsQuery} from "@/features/metrics/hooks/use-item-profit-metrics-queries";
import {
  calculatePortfolioMetrics,
  calculatePortfolioMetricsHistory,
} from "@/features/metrics/utils/profit-metrics";
import {getRangeStartDate} from "@/lib/utils";
import {formatNumber} from "@/lib/utils.ts";
import type {APIGearset} from "@/services/api/api.types";
import type {ItemWithQuantity} from "@/types/types";
import {itemIconBaseUrl} from "@/utils/constants";

const MetricRow = ({label, value}: { label: string; value: number }) => (
    <div
        className="flex items-center justify-between py-4 border-b border-zinc-100 dark:border-zinc-800/60 last:border-0">
		<span
        className="text-xs font-mono uppercase tracking-wider text-zinc-500 dark:text-zinc-400 font-medium">
			{label}
		</span>
      <div className="flex items-center gap-4">
			<span
          className="text-2xl font-bold tabular-nums text-zinc-900 dark:text-white tracking-tight">
				{formatNumber(value)}
			</span>
        <KamasIcon className="w-5 h-5 flex-shrink-0"/>
      </div>
    </div>
);

const NoDataCard = ({item}: { item: ItemWithQuantity }) => (
    <div
        className="flex flex-col items-center justify-center h-full min-h-[140px] p-4 rounded-lg border border-dashed border-zinc-200 dark:border-zinc-800 bg-zinc-50/50 dark:bg-zinc-900/10">
      <div className="relative mb-3">
        <img
            src={`${itemIconBaseUrl}/${item.iconId}.png`}
            alt={item.name}
            className="w-10 h-10 opacity-40 grayscale"
        />
        <div
            className="absolute -right-1 -bottom-1 bg-zinc-100 dark:bg-zinc-800 rounded-full p-0.5 border border-zinc-200 dark:border-zinc-700">
          <Ban className="w-3 h-3 text-zinc-400"/>
        </div>
      </div>
      <h4 className="text-xs font-medium text-zinc-500 dark:text-zinc-400 text-center line-clamp-1 max-w-[90%]">
        {item.name}
      </h4>
      <span className="text-[10px] text-zinc-400 mt-1 font-mono uppercase tracking-wide">
			No Metrics Data
		</span>
    </div>
);

const DashboardEmptyState = () => (
    <div
        className="flex flex-col items-center justify-center h-[400px] border border-dashed border-zinc-200 dark:border-zinc-800 rounded-lg bg-zinc-50/50 dark:bg-zinc-900/20">
      <EmptyHeader>
        <div className="flex items-center justify-center mb-4">
          <div
              className="p-4 rounded-full bg-white dark:bg-zinc-900 shadow-sm ring-1 ring-zinc-200 dark:ring-zinc-800">
            <Package className="w-8 h-8 text-zinc-400 dark:text-zinc-500"/>
          </div>
        </div>
        <EmptyTitle className="text-lg font-medium text-zinc-900 dark:text-zinc-100">
          No Equipped Items
        </EmptyTitle>
        <EmptyDescription
            className="text-zinc-500 dark:text-zinc-400 max-w-xs text-center mx-auto mt-2 text-sm">
          Equip items to see market value analysis and crafting costs.
        </EmptyDescription>
      </EmptyHeader>
    </div>
);

export function GearsetPriceDialog({
                                     open,
                                     onOpenChange,
                                     gearset,
                                   }: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  gearset: APIGearset;
}) {
  const items = gearset.slots.map((s) => s.itemDetails);
  const hasItems = items.length > 0;

  const {itemsProfitMetrics = [], isPending} = useItemsProfitMetricsQuery(
      {itemIds: items.map((i) => i.id)},
      {
        startDate: getRangeStartDate("max").toISOString().split("T")[0],
        endDate: new Date().toISOString().split("T")[0],
      },
      open,
  );

  const portfolio = calculatePortfolioMetrics(items, itemsProfitMetrics);

  const history =
      !isPending && hasItems
          ? calculatePortfolioMetricsHistory(items, itemsProfitMetrics)
          : [];

  const sortedItems = gearset.slots
  .map((slot) => {
    const metrics =
        itemsProfitMetrics.find((m) => m.itemId === slot.itemDetails.id)
            ?.profitMetrics || [];
    return {
      id: slot.id,
      item: slot.itemDetails as ItemWithQuantity,
      metrics,
      hasData: metrics.length > 0,
    };
  })
  .sort((a, b) => {
    if (a.hasData !== b.hasData) return Number(b.hasData) - Number(a.hasData);
    return a.item.name.localeCompare(b.item.name);
  });

  return (
      <Dialog open={open} onOpenChange={onOpenChange}>
        <DialogContent
            className="max-w-[95vw] w-full sm:max-w-6xl h-[90vh] flex flex-col p-0 gap-0 bg-background border-zinc-200 dark:border-zinc-800 shadow-2xl overflow-hidden">
          <DialogHeader
              className="px-6 py-5 border-b border-zinc-200 dark:border-zinc-800 bg-zinc-50/50 dark:bg-zinc-900/20 flex-shrink-0">
            <div className="flex flex-col gap-1.5">
              <div
                  className="flex items-center gap-2 text-xs font-mono text-zinc-500 dark:text-zinc-400 uppercase tracking-widest">
                <BarChart3 className="w-3.5 h-3.5"/>
                Gearset Price Analysis
              </div>
              <div className="flex items-center justify-between">
                <DialogTitle
                    className="text-xl font-bold tracking-tight text-zinc-900 dark:text-white flex items-center gap-3">
                  {gearset.title}
                  <Badge
                      variant="outline"
                      className="font-mono text-xs font-normal text-zinc-500 dark:text-zinc-400 border-zinc-200 dark:border-zinc-700"
                  >
                    {items.length} Items
                  </Badge>
                </DialogTitle>
              </div>
            </div>
          </DialogHeader>

          {hasItems ? (
              <ScrollArea className="flex-1 min-h-0 bg-background ">
                <div className="p-6 md:p-8 space-y-10">
                  <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
                    <div className="lg:col-span-4 xl:col-span-3 space-y-6">
                      <div>
                        <h3 className="text-sm font-mono text-zinc-500 dark:text-zinc-400 uppercase tracking-widest mb-4">
                          Total Valuation
                        </h3>
                        <div className="px-1">
                          <MetricRow
                              label="Market Price"
                              value={portfolio.totalMarketPrice}
                          />
                          <MetricRow
                              label="Craft Cost"
                              value={portfolio.totalCraftCost}
                          />
                        </div>
                      </div>
                    </div>

                    <div className="lg:col-span-8 xl:col-span-9">
                      <div
                          className="h-full border border-zinc-200 dark:border-zinc-800 rounded-lg bg-zinc-50/50 dark:bg-zinc-900/10 p-6">
                        <div className="flex items-center justify-between mb-6">
                          <h3 className="text-sm font-mono text-zinc-500 dark:text-zinc-400 uppercase tracking-widest">
                            Gearset Total Price & Craft Evolution
                          </h3>
                        </div>

                        {isPending ? (
                            <Skeleton className="w-full h-[260px] bg-zinc-100 dark:bg-zinc-800/50"/>
                        ) : (
                            <MultiSeriesChart
                                data={history}
                                xKey="snapshotDate"
                                series={[
                                  {
                                    key: "totalMarketPrice",
                                    label: "Market",
                                    color: "hsl(var(--primary))",
                                  },
                                  {
                                    key: "totalCraftCost",
                                    label: "Craft",
                                    color: "hsl(var(--muted-foreground))",
                                  },
                                ]}
                                minHeight={260}
                                type="line"
                            />
                        )}
                      </div>
                    </div>
                  </div>

                  <Separator className="bg-zinc-200 dark:bg-zinc-800"/>

                  <div className="space-y-6">
                    <div className="flex items-center justify-between">
                      <h3 className="text-sm font-mono text-zinc-500 dark:text-zinc-400 uppercase tracking-widest">
                        Item Breakdown
                      </h3>
                    </div>

                    {isPending ? (
                        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
                          {[...Array(6)].map((_, i) => (
                              <Skeleton
                                  key={`skeleton-${i}-${Date.now()}`}
                                  className="h-36 rounded border border-zinc-200 dark:border-zinc-800 bg-zinc-100 dark:bg-zinc-900/50"
                              />
                          ))}
                        </div>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-5">
                          {sortedItems.map(({id, item, metrics, hasData}) =>
                              hasData ? (
                                  <ItemProfitMetricsCard
                                      key={id}
                                      item={item}
                                      metrics={metrics}
                                  />
                              ) : (
                                  <NoDataCard key={id} item={item}/>
                              ),
                          )}
                        </div>
                    )}
                  </div>
                </div>
              </ScrollArea>
          ) : (
              <div className="flex-1 flex bg-white dark:bg-[#09090b]">
                <div className="m-auto px-6">
                  <DashboardEmptyState/>
                </div>
              </div>
          )}
        </DialogContent>
      </Dialog>
  );
}
