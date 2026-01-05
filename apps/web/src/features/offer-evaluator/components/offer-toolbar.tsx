import { TimeRangeTabs } from "@/components/ui/time-range-tabs.tsx";
import MultiSelectItems from "@/features/items/components/multi-select-items";
import { useOfferEvaluatorStore } from "../hooks/use-offer-evaluator-store";

export function OfferToolbar() {
	const {
		selectedItems,
		setSelectedItems,
		setItemQuantity,
		timeRange,
		setTimeRange,
	} = useOfferEvaluatorStore();

	return (
		<div className="order-2 lg:order-1 shrink-0 border-t lg:border-t-0 lg:border-b bg-background fixed lg:sticky bottom-0 lg:top-0 left-0 right-0 z-20">
			<div className="container max-w-6xl mx-auto px-4 py-3">
				<div className="flex flex-col sm:flex-row gap-3 items-start sm:items-center">
					<div className="w-full sm:flex-1">
						<MultiSelectItems
							selectedOptions={selectedItems}
							onSelect={setSelectedItems}
							onQuantityChange={setItemQuantity}
							maxVisibleBadges={3}
							showQuantities
						/>
					</div>

					<div className="w-full sm:w-auto">
						<TimeRangeTabs range={timeRange} onChange={setTimeRange} />
					</div>
				</div>
			</div>
		</div>
	);
}
