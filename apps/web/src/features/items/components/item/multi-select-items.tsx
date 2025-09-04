import { useMemo, useState } from "react";
import MultiSelect, { type ItemOption } from "@/components/ui/multi-select.tsx";
import { usePaginatedItemsQuery } from "@/features/items/hooks/queries/usePaginatedItemsQuery.ts";
import useDebounce from "@/hooks/use-debounce.ts";
import { itemIconBaseUrl } from "@/utils/constants.ts";

export function MultiSelectItems() {
	const [name, setName] = useState("");
	const [selected, setSelected] = useState<ItemOption[]>([]);
	const debouncedName = useDebounce(name, 250);

	const { data: items, isFetching } = usePaginatedItemsQuery({
		params: { name: debouncedName },
		pageable: { size: 100 },
	});

	const isSearching = name !== debouncedName || isFetching;

	const options = useMemo(() => {
		return (
			items?.content?.map((item) => ({
				label: item.name ?? "",
				value: String(item.id) ?? "",
				iconUrl: `${itemIconBaseUrl}/${item.iconId}.png`,
			})) || []
		);
	}, [items]);

	return (
		<MultiSelect
			options={options}
			selectedOptions={selected}
			onSelect={setSelected}
			onSearch={setName}
			onClearAll={() => {
				setSelected([]);
			}}
			searchValue={name}
			isLoading={isSearching}
		/>
	);
}
