import {ChartColumnIncreasing} from "lucide-react";
import {Empty, EmptyDescription, EmptyHeader, EmptyMedia, EmptyTitle,} from "@/components/ui/empty";
import {ScrollArea} from "@/components/ui/scroll-area.tsx";
import {Skeleton} from "@/components/ui/skeleton";
import {useItemsProfitMetricsQuery} from "@/features/metrics/hooks/use-item-profit-metrics-queries";
import {getRangeStartDate} from "@/lib/utils.ts";
import {OfferItemList} from "./components/offer-item-list.tsx";
import {OfferSummary} from "./components/offer-summary";
import {OfferToolbar} from "./components/offer-toolbar";
import {useOfferEvaluatorStore} from "./hooks/use-offer-evaluator-store";

export function OfferEvaluatorPage() {
  const timeRange = useOfferEvaluatorStore((state) => state.timeRange);
  const selectedItems = useOfferEvaluatorStore((state) => state.selectedItems);

  const {itemsProfitMetrics, isPending} = useItemsProfitMetricsQuery(
      {itemIds: selectedItems.map((item) => item.id)},
      {
        startDate: getRangeStartDate(timeRange).toISOString().split("T")[0],
        endDate: new Date().toISOString().split("T")[0],
      },
  );

  const hasItems = selectedItems.length > 0;

  return (
      <div className="flex flex-col h-full w-full overflow-hidden bg-background">
        <ScrollArea className="flex-1 min-h-0 w-full order-1 lg:order-2">
          <div className="flex flex-col min-h-full">
            {hasItems ? (
                <div className="flex-1">
                  <OfferSummary
                      isPending={isPending}
                      itemsProfitMetrics={itemsProfitMetrics || []}
                      selectedItems={selectedItems}
                  />

                  <div className="container max-w-6xl mx-auto px-4 py-6 pb-60 lg:pb-10">
                    {isPending ? (
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
                          {[...Array(6).keys()].map((i) => (
                              <Skeleton key={i} className="h-40 rounded-xl"/>
                          ))}
                        </div>
                    ) : (
                        <OfferItemList
                            itemsProfitMetrics={itemsProfitMetrics || []}
                        />
                    )}
                  </div>
                </div>
            ) : (
                <div className="flex-1 flex items-center justify-center p-12">
                  <Empty>
                    <EmptyHeader>
                      <EmptyMedia variant="icon">
                        <ChartColumnIncreasing/>
                      </EmptyMedia>
                      <EmptyTitle>No items selected</EmptyTitle>
                      <EmptyDescription>
                        Select items above to start analyzing.
                      </EmptyDescription>
                    </EmptyHeader>
                  </Empty>
                </div>
            )}
          </div>
        </ScrollArea>
        <div className="shrink-0 z-20 order-2 lg:order-1 border-t lg:border-t-0">
          <OfferToolbar/>
        </div>
      </div>
  );
}
