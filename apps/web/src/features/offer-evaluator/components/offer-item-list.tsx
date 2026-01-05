import ItemProfitMetricsCard from "@/features/metrics/components/item-profit-metrics-card.tsx";
import type { ItemsProfitMetrics } from "@/services/api/api.types";
import { useOfferEvaluatorStore } from "../hooks/use-offer-evaluator-store";

interface OfferItemsListProps {
	itemsProfitMetrics: ItemsProfitMetrics;
}

export function OfferItemList({ itemsProfitMetrics }: OfferItemsListProps) {
	const selectedItems = useOfferEvaluatorStore((state) => state.selectedItems);

	return (
		<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3 max-w-6xl mx-auto">
			{selectedItems.map((item) => {
				const metrics =
					itemsProfitMetrics.find((e) => e.itemId === item.id)?.profitMetrics ||
					[];
				return (
					<ItemProfitMetricsCard key={item.id} item={item} metrics={metrics} />
				);
			})}
		</div>
	);
}
