import MultiSeriesChart from "@/components/ui/multi-series-chart.tsx";
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs";
import {ItemProfitHistoryTable} from "@/features/metrics/components/item-profit-history-table";
import type {ItemProfitMetrics, ProfitMetricsEntry,} from "@/services/api/api.types";

interface ItemProfitMetricsTabsProps {
  metrics: ItemProfitMetrics;
}

export function ItemProfitMetricsTabs({metrics}: ItemProfitMetricsTabsProps) {
  if (metrics.length === 0) {
    return (
        <div className="text-sm text-muted-foreground text-center py-4">
          No historical data available
        </div>
    );
  }

  return (
      <Tabs defaultValue="profit" className="w-full">
        <TabsList className="w-full h-8">
          <TabsTrigger value="cost" className="text-xs">
            Craft
          </TabsTrigger>
          <TabsTrigger value="price" className="text-xs">
            Price
          </TabsTrigger>
          <TabsTrigger value="profit" className="text-xs">
            Profit
          </TabsTrigger>
          <TabsTrigger value="history" className="text-xs">
            History
          </TabsTrigger>
        </TabsList>

        <TabsContent value="profit" className="mt-3">
          <MultiSeriesChart<ProfitMetricsEntry>
              data={metrics}
              xKey="snapshotDate"
              series={[{key: "profitMargin", label: "Profit"}]}
              type="line"
              minHeight={180}
          />
        </TabsContent>

        <TabsContent value="cost" className="mt-3">
          <MultiSeriesChart<ProfitMetricsEntry>
              data={metrics}
              xKey="snapshotDate"
              series={[{key: "craftCost", label: "Craft Cost"}]}
              type="line"
              minHeight={180}
          />
        </TabsContent>

        <TabsContent value="price" className="mt-3">
          <MultiSeriesChart<ProfitMetricsEntry>
              data={metrics}
              xKey="snapshotDate"
              series={[{key: "sellPrice", label: "Sell Price"}]}
              type="line"
              minHeight={180}
          />
        </TabsContent>

        <TabsContent value="history" className="mt-3">
          <ItemProfitHistoryTable metrics={metrics} maxRows={50}/>
        </TabsContent>
      </Tabs>
  );
}
