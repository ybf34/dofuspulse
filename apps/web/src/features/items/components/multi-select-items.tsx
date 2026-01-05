import { useMemo, useState } from "react";
import MultiSelect from "@/components/ui/multi-select.tsx";
import { useItemsQuery } from "@/features/items/hooks/use-items-query";
import useDebounce from "@/hooks/use-debounce";
import type { ItemWithQuantity } from "@/types/types";

export type MultiSelectItemsProps = {
	selectedOptions: ItemWithQuantity[];
	onSelect: (items: ItemWithQuantity[]) => void;
	onQuantityChange?: (id: number, quantity: number) => void;
	showQuantities?: boolean;
	maxVisibleBadges: number;
};

export default function MultiSelectItems({
	selectedOptions,
	onSelect,
	onQuantityChange,
	showQuantities = true,
	maxVisibleBadges = 5,
}: MultiSelectItemsProps) {
	const [name, setName] = useState("");
	const debouncedName = useDebounce(name, 250);

	const { items, isFetching } = useItemsQuery({
		name: debouncedName,
		size: 150,
	});

	const isSearching = name !== debouncedName || isFetching;

	const options = useMemo(() => {
		return (
			items.map(
				(item) =>
					({
						id: item.id,
						name: item.name,
						iconId: item.iconId,
					}) as ItemWithQuantity,
			) || []
		);
	}, [items]);

	return (
		<MultiSelect
			options={options}
			selectedOptions={selectedOptions}
			onSelect={onSelect}
			onClearAll={() => onSelect([])}
			onSearch={setName}
			searchValue={name}
			isLoading={isSearching}
			onQuantityChange={onQuantityChange}
			maxVisibleBadges={maxVisibleBadges}
			showQuantities={showQuantities}
		/>
	);
}
