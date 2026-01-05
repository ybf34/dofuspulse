import {Plus} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Card} from "@/components/ui/card";
import {ScrollArea} from "@/components/ui/scroll-area";
import ItemEffectsList from "@/features/items/components/item-effects-list";
import {cn} from "@/lib/utils";
import type {APIItemDetails} from "@/services/api/api.types";
import {itemIconBaseUrl} from "@/utils/constants";

export type ItemCardProps = {
  itemDetails: APIItemDetails;
  className?: string;
  selectable?: boolean;
  onSelect?: () => void;
};

export default function ItemCard({
                                   itemDetails,
                                   className = "",
                                   selectable = false,
                                   onSelect,
                                 }: ItemCardProps) {
  const {name, level, possibleEffects, iconId} = itemDetails;
  const iconUrl = iconId ? `${itemIconBaseUrl}/${iconId}.png` : undefined;

  return (
      <Card
          className={cn(
              "flex flex-col rounded-lg border border-border bg-card dark:bg-background text-foreground",
              "transition-colors duration-200",
              "p-4 text-sm h-full w-full",
              className,
          )}
      >
        <div className="flex items-start gap-3 mb-3">
          <div
              className="h-12 w-12 shrink-0 rounded-md border border-border bg-muted flex items-center justify-center">
            {iconUrl && (
                <img src={iconUrl} alt={name} className="h-8 w-8 object-contain"/>
            )}
          </div>

          <div className="flex flex-1 items-start gap-2 min-w-0">
            <div className="flex-1 min-w-0">
              <h3
                  className="truncate font-semibold text-sm leading-snug"
                  title={name}
              >
                {name}
              </h3>
              <span className="mt-0.5 text-xs text-muted-foreground">
							Lvl {level}
						</span>
            </div>

            {selectable && (
                <Button
                    size="icon"
                    variant="ghost"
                    className="h-7 w-7 shrink-0 rounded-md border border-border hover:bg-accent"
                    onClick={(e) => {
                      e.stopPropagation();
                      onSelect?.();
                    }}
                >
                  <Plus className="h-4 w-4"/>
                </Button>
            )}
          </div>
        </div>

        <ScrollArea
            className="flex-1 max-h-40 pr-2"
            onClick={(e) => e.stopPropagation()}
        >
          {possibleEffects?.length > 0 ? (
              <ItemEffectsList stats={possibleEffects}/>
          ) : (
              <p className="text-xs italic text-muted-foreground">No effects.</p>
          )}
        </ScrollArea>
      </Card>
  );
}
