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
import ItemFilters from "@/features/items/components/item-filters";
import { useItemFiltersStore } from "@/features/items/hooks/use-item-filters-store";
import { useItemsQuery } from "@/features/items/hooks/use-items-query";

export default function ItemPage() {
	const { filters, setFilters } = useItemFiltersStore();
	const { items, pageInfo, isPending, isError } = useItemsQuery(filters);

	const page = filters.page ?? 0;
	const currentPage = page + 1;
	const totalPages = pageInfo?.totalPages ?? 1;

	return (
		<div className="flex bg-background h-full">
			<aside className="hidden md:block w-72 shrink-0 border-r border-border bg-background">
				<ItemFilters />
			</aside>

			<div className="flex-1 flex flex-col h-full">
				<div className="flex items-center justify-between px-6 py-4 border-b border-border shrink-0">
					<div>
						<h1 className="text-lg font-semibold text-foreground">Items</h1>
						{items && (
							<p className="text-sm text-muted-foreground">
								{pageInfo?.totalElements} results
							</p>
						)}
					</div>
					<div className="md:hidden">
						<Sheet>
							<SheetTrigger asChild>
								<Button size="sm" variant="outline">
									Filters
								</Button>
							</SheetTrigger>
							<SheetContent side="left" className="w-72 p-0">
								<SheetHeader className="p-4 border-b">
									<SheetTitle>Filters</SheetTitle>
								</SheetHeader>
								<ItemFilters />
							</SheetContent>
						</Sheet>
					</div>
				</div>

				<div className="flex-grow overflow-auto">
					<div className="p-6">
						{isPending && (
							<div className="flex items-center justify-center h-full">
								<p className="text-muted-foreground">Loading...</p>
							</div>
						)}

						{isError && (
							<div className="flex items-center justify-center h-full">
								<p className="text-red-400">Error fetching items!</p>
							</div>
						)}

						{items && items.length > 0 ? (
							<div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
								{items.map((item) => (
									<ItemCard key={item.id} itemDetails={item} />
								))}
							</div>
						) : (
							!isPending && (
								<div className="flex items-center justify-center h-full">
									<p className="text-muted-foreground">No items found.</p>
								</div>
							)
						)}
					</div>
				</div>

				<Separator className="shrink-0" />

				<div className="px-6 py-3 flex items-center justify-center shrink-0 border-t border-border bg-background">
					<Pagination>
						<PaginationContent>
							<PaginationItem>
								<PaginationPrevious
									href="#"
									onClick={(e) => {
										e.preventDefault();
										if (page > 0) setFilters({ page: page - 1 });
									}}
								/>
							</PaginationItem>
							<PaginationItem>
								<PaginationLink isActive>{currentPage}</PaginationLink>
							</PaginationItem>
							{currentPage < totalPages && (
								<>
									<PaginationItem>
										<PaginationEllipsis />
									</PaginationItem>
									<PaginationItem>
										<PaginationLink
											href="#"
											onClick={(e) => {
												e.preventDefault();
												setFilters({ page: totalPages - 1 });
											}}
										>
											{totalPages}
										</PaginationLink>
									</PaginationItem>
								</>
							)}

							<PaginationItem>
								<PaginationNext
									href="#"
									onClick={(e) => {
										e.preventDefault();
										if (page < totalPages - 1) setFilters({ page: page + 1 });
									}}
								/>
							</PaginationItem>
						</PaginationContent>
					</Pagination>
				</div>
			</div>
		</div>
	);
}
