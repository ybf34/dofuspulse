import {ChevronsUpDown, X} from "lucide-react";
import {useMemo, useState} from "react";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
} from "@/components/ui/command";
import {Popover, PopoverContent, PopoverTrigger,} from "@/components/ui/popover";
import {ScrollArea} from "@/components/ui/scroll-area";
import {Skeleton} from "@/components/ui/skeleton";
import {useItemsQuery} from "@/features/items/hooks/use-items-query";
import useDebounce from "@/hooks/use-debounce";
import {cn} from "@/lib/utils";
import type {APIItemDetails} from "@/services/api/api.types";
import type {ItemWithQuantity} from "@/types/types";
import {itemIconBaseUrl} from "@/utils/constants";

interface ItemSelectProps {
  value: ItemWithQuantity | null;
  onChange: (item: ItemWithQuantity | null) => void;
  placeholder?: string;
  className?: string;
  disabled?: boolean;
}

export default function ItemSelect({
                                     value,
                                     onChange,
                                     placeholder = "Select an item...",
                                     className,
                                     disabled = false,
                                   }: ItemSelectProps) {
  const [open, setOpen] = useState(false);
  const [search, setSearch] = useState("");
  const debouncedSearch = useDebounce(search, 250);

  const {items, isFetching} = useItemsQuery({
    name: debouncedSearch,
    size: 100,
  });

  const isSearching = search !== debouncedSearch || isFetching;

  const options = useMemo<ItemWithQuantity[]>(() => {
    return (
        items?.map(
            (item: APIItemDetails) =>
                ({
                  name: item.name,
                  id: item.id,
                  iconId: item.iconId,
                }) as ItemWithQuantity,
        ) || []
    );
  }, [items]);

  return (
      <div className={cn("w-full", className)}>
        <Popover open={open} onOpenChange={setOpen}>
          <div
              className={cn(
                  "flex h-12 w-full items-center justify-between rounded-md border border-input bg-background text-sm",
                  "focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2",
                  "disabled:cursor-not-allowed disabled:opacity-50",
              )}
          >
            <PopoverTrigger asChild>
              <button
                  type="button"
                  disabled={disabled}
                  className="flex justify-between flex-1 items-center px-3"
              >
                {value ? (
                    <div className="flex items-center gap-2 truncate">
                      {value.iconId && (
                          <img
                              src={`${itemIconBaseUrl}/${value.iconId}.png`}
                              alt={value.name}
                              className="h-5 w-5 shrink-0"
                          />
                      )}
                      <span className="truncate">{value.name}</span>
                    </div>
                ) : (
                    <span className="text-muted-foreground truncate">
									{placeholder}
								</span>
                )}
              </button>
            </PopoverTrigger>

            <div className="flex items-center gap-1 pr-2">
              {value && (
                  <button
                      type="button"
                      onClick={() => onChange(null)}
                      className="hover:bg-accent/50 rounded-sm p-1"
                  >
                    <X className="h-4 w-4 opacity-50"/>
                  </button>
              )}
              <button
                  type="button"
                  onClick={() => setOpen((prev) => !prev)}
                  className="hover:bg-accent/50 rounded-sm p-1"
              >
                <ChevronsUpDown className="h-4 w-4 opacity-50"/>
              </button>
            </div>
          </div>

          <PopoverContent
              className="w-[var(--radix-popover-trigger-width)] p-1 bg-popover rounded-md"
              align="start"
          >
            <Command filter={() => 1} className="bg-popover">
              <CommandInput
                  autoFocus={false}
                  placeholder="Search items..."
                  onValueChange={setSearch}
                  value={search}
                  className="border-border/50"
              />
              <ScrollArea className="h-64 max-h-[65vh]">
                {isSearching ? (
                    <div className="p-2">
                      {[...Array(13).keys()].map((i) => (
                          <Skeleton key={i} className="h-4 w-full mb-1 last:mb-0"/>
                      ))}
                    </div>
                ) : options.length > 0 ? (
                    <CommandGroup>
                      {options.map((option) => (
                          <CommandItem
                              key={option.id}
                              value={String(option.id)}
                              onSelect={() => {
                                onChange(option);
                                setOpen(false);
                              }}
                              className="px-2 py-1 flex items-center gap-2 cursor-pointer bg-popover hover:bg-accent/50 rounded-md"
                          >
                            {option.iconId && (
                                <img
                                    src={`${itemIconBaseUrl}/${option.iconId}.png`}
                                    alt={option.name}
                                    className="h-5 w-5"
                                />
                            )}
                            <span className="truncate">{option.name}</span>
                          </CommandItem>
                      ))}
                    </CommandGroup>
                ) : (
                    <CommandEmpty className="text-center text-sm py-4 text-muted-foreground">
                      No items found.
                    </CommandEmpty>
                )}
              </ScrollArea>
            </Command>
          </PopoverContent>
        </Popover>
      </div>
  );
}
