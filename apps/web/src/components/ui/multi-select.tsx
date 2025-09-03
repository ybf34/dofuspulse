import {ChevronsUpDown, ChevronUp, X} from "lucide-react";
import {useState} from "react";
import {Badge} from "@/components/ui/badge";
import {Checkbox} from "@/components/ui/checkbox.tsx";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
} from "@/components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import {Skeleton} from "@/components/ui/skeleton";
import {cn} from "@/lib/utils";
import {ScrollArea} from "./scroll-area";

export type ItemOption = {
  label: string;
  value: string;
  iconUrl?: string;
};

interface MultiSelectProps {
  options?: ItemOption[];
  selectedOptions: ItemOption[];
  onSelect: (selected: ItemOption[]) => void;
  onSearch?: (search: string) => void;
  onClearAll?: () => void;
  searchValue: string;
  placeholder?: string;
  className?: string;
  isLoading?: boolean;
  disabled?: boolean;
}

const BADGE_LIMIT = 10;

export default function MultiSelect({
                                      options,
                                      selectedOptions,
                                      onSelect,
                                      onSearch,
                                      onClearAll,
                                      placeholder = "Select items...",
                                      searchValue,
                                      className,
                                      isLoading = false,
                                      disabled = false,
                                    }: MultiSelectProps) {
  const [open, setOpen] = useState(false);
  const [expanded, setExpanded] = useState(false);

  const handleUnselect = (item: ItemOption) => {
    onSelect(selectedOptions.filter((i) => i.value !== item.value));
  };

  const handleSelect = (item: ItemOption) => {
    if (
        selectedOptions.some((selectedItem) => selectedItem.value === item.value)
    ) {
      onSelect(selectedOptions.filter((i) => i.value !== item.value));
    } else {
      onSelect([...selectedOptions, item]);
    }
  };

  return (
      <div className={cn("w-full", className)}>
        <Popover open={open} onOpenChange={setOpen}>
          <PopoverTrigger
              className={cn(
                  "flex h-auto w-full min-h-12 transition-all items-start justify-between rounded-md border border-input bg-background text-sm",
                  "focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2",
                  "disabled:cursor-not-allowed disabled:opacity-50",
              )}
              disabled={disabled}
              aria-expanded={open}
          >
            <div className="flex justify-between flex-1 overflow-hidden">
              <div className="flex flex-wrap gap-1 flex-1 py-2 px-3">
                {selectedOptions.length === 0 ? (
                    <span className="text-muted-foreground truncate my-auto">
									{placeholder}
								</span>
                ) : expanded ? (
                    <>
                      {selectedOptions.map((item) => (
                          <Badge
                              key={item.value}
                              variant="outline"
                              className="text-xs flex items-center gap-1.5 hover:bg-accent"
                          >
                            {item.iconUrl && (
                                <img
                                    src={item.iconUrl}
                                    alt={item.label}
                                    className="h-5 w-5"
                                />
                            )}
                            <span>{item.label}</span>
                            <button
                                type="button"
                                className="ml-1 hover:bg-destructive transition-all hover:text-destructive-foreground rounded-full p-0.5"
                                onClick={(e) => {
                                  e.stopPropagation();
                                  handleUnselect(item);
                                }}
                            >
                              <X className="h-3 w-3"/>
                            </button>
                          </Badge>
                      ))}
                      {selectedOptions.length > BADGE_LIMIT && (
                          <button
                              type="button"
                              onClick={(e) => {
                                e.stopPropagation();
                                setExpanded(false);
                              }}
                              className="cursor-pointer border border-border rounded-full px-2.5 py-0.5 text-xs font-semibold"
                          >
                            <ChevronUp className="h-3 w-3"/>
                          </button>
                      )}
                    </>
                ) : (
                    <>
                      {selectedOptions.slice(0, BADGE_LIMIT).map((item) => (
                          <Badge
                              key={item.value}
                              variant="outline"
                              className="text-xs flex items-center gap-1.5 hover:bg-accent"
                          >
                            {item.iconUrl && (
                                <img
                                    src={item.iconUrl}
                                    alt={item.label}
                                    className="h-5 w-5"
                                />
                            )}
                            <span>{item.label}</span>
                            <button
                                type="button"
                                className="ml-1 hover:bg-destructive transition-all hover:text-destructive-foreground rounded-full p-0.5"
                                onClick={(e) => {
                                  e.stopPropagation();
                                  handleUnselect(item);
                                }}
                            >
                              <X className="h-3 w-3"/>
                            </button>
                          </Badge>
                      ))}
                      {selectedOptions.length > BADGE_LIMIT && (
                          <button
                              type="button"
                              onClick={(e) => {
                                e.stopPropagation();
                                setExpanded(true);
                              }}
                              className="border border-outline rounded-md text-xs font-semibold px-2.5 py-0.5"
                          >
                            +{selectedOptions.length - BADGE_LIMIT} more
                          </button>
                      )}
                    </>
                )}
              </div>
              <div className="flex flex-col items-center justify-center p-1 mx-1.5 gap-1">
                <button
                    type="button"
                    onClick={(e) => {
                      e.stopPropagation();
                      setOpen((prev) => !prev);
                    }}
                    tabIndex={0}
                    className={cn(
                        "p-1 outline-none transition-all",
                        "focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2",
                        "hover:bg-accent/50 rounded-sm cursor-pointer",
                    )}
                >
                  <ChevronsUpDown className="h-4 w-4 shrink-0 opacity-50"/>
                </button>
                {selectedOptions.length > 0 && onClearAll && (
                    <button
                        type="button"
                        onClick={(e) => {
                          e.stopPropagation();
                          onClearAll();
                        }}
                        tabIndex={0}
                        className={cn(
                            "p-1 outline-none transition-all",
                            "focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2",
                            "hover:bg-accent/50 rounded-sm cursor-pointer",
                        )}
                    >
                      <X className="h-4 w-4 shrink-0 opacity-50"/>
                    </button>
                )}
              </div>
            </div>
          </PopoverTrigger>
          <PopoverContent
              className="w-[var(--radix-popover-trigger-width)] p-1 bg-popover rounded-md"
              align="start"
          >
            <Command filter={() => 1} className="bg-popover">
              <CommandInput
                  autoFocus={false}
                  placeholder="Search items..."
                  onValueChange={onSearch}
                  value={searchValue}
                  className="border-border/50"
              />
              <ScrollArea className="h-64 max-h-[65vh]">
                {isLoading ? (
                    <div className="p-2">
                      {[...Array(13).keys()].map((i) => (
                          <Skeleton key={i} className="h-4 w-full mb-1 last:mb-0"/>
                      ))}
                    </div>
                ) : options && options.length > 0 ? (
                    <CommandGroup>
                      {options.map((option) => {
                        const isSelected = selectedOptions.some(
                            (selectedItem) => selectedItem.value === option.value,
                        );
                        return (
                            <CommandItem
                                key={option.value}
                                value={option.value}
                                onSelect={() => handleSelect(option)}
                                className="px-2 py-1 flex items-center gap-2 cursor-pointer bg-popover hover:bg-accent/50 rounded-md"
                            >
                              <div className="flex items-center gap-2">
                                <Checkbox
                                    checked={isSelected}
                                    onCheckedChange={() => handleSelect(option)}
                                    className="mr-2"
                                />
                                {option.iconUrl && (
                                    <img
                                        src={option.iconUrl}
                                        alt={option.label}
                                        className="h-5 w-5"
                                    />
                                )}
                                <span className="truncate">{option.label}</span>
                              </div>
                            </CommandItem>
                        );
                      })}
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
