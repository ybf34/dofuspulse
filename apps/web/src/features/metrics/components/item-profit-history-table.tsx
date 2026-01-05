import {KamasIcon} from "@/components/icons/kamas-icon";
import {ScrollArea} from "@/components/ui/scroll-area";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {cn, formatNumber} from "@/lib/utils";
import type {ItemProfitMetrics} from "@/services/api/api.types";

interface ItemProfitHistoryTableProps {
  metrics: ItemProfitMetrics;
  maxRows?: number;
}

export function ItemProfitHistoryTable({
                                         metrics,
                                         maxRows = 10,
                                       }: ItemProfitHistoryTableProps) {
  if (metrics.length === 0) {
    return (
        <div className="text-sm text-muted-foreground text-center py-8">
          No historical data available
        </div>
    );
  }

  const visibleMetrics = [...metrics].reverse().slice(0, maxRows);

  return (
      <ScrollArea className="h-50">
        <Table>
          <TableHeader className="sticky top-0 z-10 bg-background border-b">
            <TableRow className="hover:bg-transparent">
              <TableHead
                  className="text-xs font-semibold text-muted-foreground whitespace-nowrap py-2.5 pl-3 w-[70px]">
                Date
              </TableHead>
              <TableHead
                  className="text-xs font-semibold text-muted-foreground py-2.5 px-2 w-[30%]">
                Craft
              </TableHead>
              <TableHead
                  className="text-xs font-semibold text-muted-foreground py-2.5 px-2 w-[30%]">
                Market
              </TableHead>
              <TableHead
                  className="text-xs font-semibold text-muted-foreground py-2.5 pr-3 pl-2 w-[30%]">
                Profit
              </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {visibleMetrics.map((metric) => {
              const isPositive = metric.profitMargin >= 0;
              const date = new Date(metric.snapshotDate);
              const day = date.getDate().toString().padStart(2, "0");
              const month = date.toLocaleDateString("en-US", {
                month: "short",
              });
              const formattedDate = `${day} ${month}`;
              return (
                  <TableRow
                      key={metric.snapshotDate}
                      className="border-b border-border/30 hover:bg-accent/5 transition-colors"
                  >
                    <TableCell className="py-2.5 pl-3 whitespace-nowrap w-[70px]">
                      <span className="text-xs font-medium">{formattedDate}</span>
                    </TableCell>
                    <TableCell className="py-2.5 px-2">
                      <div className="flex items-center gap-1">
										<span className="text-xs font-medium tabular-nums">
											{formatNumber(metric.craftCost)}
										</span>
                        <KamasIcon className="h-2.5 w-2.5 flex-shrink-0"/>
                      </div>
                    </TableCell>
                    <TableCell className="py-2.5 px-2">
                      <div className="flex items-center gap-1">
										<span className="text-xs font-medium tabular-nums">
											{formatNumber(metric.sellPrice)}
										</span>
                        <KamasIcon className="h-2.5 w-2.5 flex-shrink-0"/>
                      </div>
                    </TableCell>
                    <TableCell className="py-2.5 pr-3 pl-2">
                      <div className="flex items-center gap-1">
										<span
                        className={cn(
                            "font-semibold text-xs tabular-nums",
                            isPositive
                                ? "text-green-600 dark:text-green-500"
                                : "text-rose-600 dark:text-rose-400",
                        )}
                    >
											{isPositive ? "+" : ""}
                      {formatNumber(metric.profitMargin)}
										</span>
                        <KamasIcon className="h-2.5 w-2.5 flex-shrink-0"/>
                      </div>
                    </TableCell>
                  </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </ScrollArea>
  );
}
