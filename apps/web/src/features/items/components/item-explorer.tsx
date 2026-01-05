import { Button } from "@/components/ui/button";
import {
	Pagination,
	PaginationContent,
	PaginationEllipsis,
	PaginationItem,
	PaginationLink,
	PaginationNext,
	PaginationPrevious,
} from "@/components/ui/pagination";
import { Separator } from "@/components/ui/separator";
import {
	Sheet,
	SheetContent,
	SheetHeader,
	SheetTitle,
	SheetTrigger,
} from "@/components/ui/sheet";
import ItemCard from "@/features/items/components/item-card";
import ItemFilters, {
	type ItemFilterConfig,
} from "@/features/items/components/item-filters";
import { useItemFiltersStore } from "@/features/items/hooks/use-item-filters-store";
import { useItemsQuery } from "@/features/items/hooks/use-items-query";
import { useIsMobile } from "@/hooks/use-mobile.ts";
import type { APIItemDetails } from "@/services/api/api.types.ts";

type ItemExplorerProps = {
	onSelect: (itemId: APIItemDetails) => void;
	filterConfig?: ItemFilterConfig;
};

export function ItemExplorer({ onSelect, filterConfig }: ItemExplorerProps) {
	const { filters, setFilters } = useItemFiltersStore();
	const isMobile = useIsMobile();

	const { items, pageInfo, isPending } = useItemsQuery({
		...filters,
		size: 50,
	});

	const page = filters.page ?? 0;
	const currentPage = page + 1;
	const totalPages = pageInfo?.totalPages ?? 1;

	return (
		<div className="flex flex-1 h-full">
			{!isMobile && (
				<aside className="w-72 shrink-0 border-r border-border bg-background overflow-y-auto">
					<ItemFilters filterConfig={filterConfig} />
				</aside>
			)}

			<div className="flex flex-col flex-1 min-w-0">
				<div className="flex items-center justify-between p-6 shrink-0 border-b border-border">
					{isMobile && (
						<Sheet>
							<SheetTrigger asChild>
								<Button size="sm" variant="outline">
									Filters
								</Button>
							</SheetTrigger>
							<SheetContent side="left" className="w-80 p-0">
								<SheetHeader className="p-4 border-b border-border">
									<SheetTitle>Filters</SheetTitle>
								</SheetHeader>
								<ItemFilters />
							</SheetContent>
						</Sheet>
					)}

					<div className="text-sm text-muted-foreground">
						{pageInfo?.totalElements || 0} items found
					</div>
				</div>

				<div className="flex-1 overflow-y-auto min-h-0">
					<div className="p-5">
						{isPending && (
							<div className="flex items-center justify-center h-32">
								<p className="text-muted-foreground">Loading...</p>
							</div>
						)}
						{items && items.length > 0 ? (
							<div
								className="grid gap-4 w-full"
								style={{
									gridTemplateColumns:
										"repeat(auto-fill, minmax(min(200px, 100%), 1fr))",
								}}
							>
								{items.map((item) => (
									<ItemCard
										key={item.id}
										itemDetails={item}
										selectable={true}
										onSelect={() => onSelect(item)}
									/>
								))}
							</div>
						) : (
							!isPending && (
								<div className="flex items-center justify-center h-32">
									<p className="text-muted-foreground">No items found.</p>
								</div>
							)
						)}
					</div>
				</div>

				<Separator />
				<div className="flex px-6 py-3 items-center justify-center border-t border-border bg-background">
					<Pagination>
						<PaginationContent className="justify-center">
							<PaginationItem>
								<PaginationPrevious
									href="#"
									onClick={(e) => {
										e.preventDefault();
										if (page > 0) setFilters({ page: page - 1 });
									}}
									aria-disabled={page === 0}
									className={page === 0 ? "pointer-events-none opacity-50" : ""}
								/>
							</PaginationItem>
							<PaginationItem>
								<PaginationLink isActive>{currentPage}</PaginationLink>
							</PaginationItem>
							<PaginationItem>
								<PaginationEllipsis />
							</PaginationItem>
							<PaginationItem>
								<PaginationLink
									href="#"
									onClick={(e) => {
										e.preventDefault();
										if (currentPage !== totalPages) {
											setFilters({ page: totalPages - 1 });
										}
									}}
									aria-disabled={currentPage === totalPages}
									className={
										currentPage === totalPages
											? "pointer-events-none opacity-50"
											: ""
									}
								>
									{totalPages}
								</PaginationLink>
							</PaginationItem>
							<PaginationItem>
								<PaginationNext
									href="#"
									onClick={(e) => {
										e.preventDefault();
										if (page < totalPages - 1) setFilters({ page: page + 1 });
									}}
									aria-disabled={page >= totalPages - 1}
									className={
										page >= totalPages - 1
											? "pointer-events-none opacity-50"
											: ""
									}
								/>
							</PaginationItem>
						</PaginationContent>
					</Pagination>
				</div>
			</div>
		</div>
	);
}
