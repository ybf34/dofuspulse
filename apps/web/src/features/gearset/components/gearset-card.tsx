import {useNavigate} from "@tanstack/react-router";
import {Clock, SquarePen, Trash2} from "lucide-react";
import {useState} from "react";
import {Badge} from "@/components/ui/badge";
import {Button} from "@/components/ui/button";
import ConfirmAlertDialog from "@/components/ui/confirm-alert-dialog.tsx";
import {Separator} from "@/components/ui/separator";
import GearsetGrid from "@/features/gearset/components/gearset-grid";
import {GearsetPriceDialog} from "@/features/gearset/components/gearset-price-dialog.tsx";
import {timeAgo} from "@/lib/time-ago.ts";
import type {APIGearset} from "@/services/api/api.types";

export type GearSetCardProps = {
  onDelete: () => void;
  gearset: APIGearset;
};

function GearsetCard({gearset, onDelete}: GearSetCardProps) {
  const {id, title, tags, updatedAt} = gearset;

  const navigate = useNavigate();

  const [isPriceDialogOpen, setIsPriceDialogOpen] = useState(false);

  const normalizedTitle =
      (title[0] ?? "").toUpperCase() + title.slice(1).toLowerCase();

  const lastUpdatedText = timeAgo.format(
      new Date(updatedAt).getTime(),
      "mini-now",
  );

  return (
      <div
          className="mx-auto max-w-fit rounded-md border border-border/70 bg-background shadow-sm hover:shadow-lg transition-shadow">
        <header className="flex flex-col gap-2 p-4 pb-2">
          <div className="flex items-center justify-between gap-2">
            <h2 className="text-base font-semibold text-foreground truncate">
              {normalizedTitle}
            </h2>
            <div className="flex items-center gap-x-0 flex-shrink-0">
              <Button
                  onClick={() =>
                      navigate({
                        to: "/gearsets/$id",
                        params: {id: String(id)},
                      })
                  }
                  variant="ghost"
                  size="icon"
              >
                <SquarePen/>
              </Button>
              <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => setIsPriceDialogOpen(true)}
              >
                <img src="/icons/kamas-coin.png" alt="Kamas" className="size-5"/>
              </Button>

              <ConfirmAlertDialog
                  title="Delete Gearset"
                  description="Are you sure you want to delete this gearset? This action cannot be undone."
                  confirmText="Delete"
                  onConfirm={onDelete}
              >
                <Button size="icon" variant="ghost">
                  <Trash2 color="red"/>
                </Button>
              </ConfirmAlertDialog>
            </div>
          </div>

          <div className="flex items-center justify-between mt-1">
            <div className="flex flex-wrap gap-1.5 min-w-0">
              {tags.length > 0 && (
                  <ul className="flex flex-wrap gap-1.5">
                    {tags.map((tag) => (
                        <li key={tag}>
                          <Badge
                              variant="secondary"
                              className="rounded-sm bg-secondary dark:bg-white/5 border border-border dark:border-white/10 text-secondary-foreground px-2 py-0.5 text-xs font-medium"
                          >
                            {tag}
                          </Badge>
                        </li>
                    ))}
                  </ul>
              )}
            </div>

            <div className="flex items-center gap-1 text-xs text-muted-foreground flex-shrink-0">
              <Clock className="size-3.5"/>
              <span>{lastUpdatedText}</span>
            </div>
          </div>
        </header>

        <div className="px-4">
          <Separator className="opacity-40"/>
        </div>
        <div className="flex items-center justify-center p-4 overflow-x-auto overflow-y-hidden">
          <GearsetGrid isPreview={true} gearset={gearset}/>
        </div>
        <GearsetPriceDialog
            open={isPriceDialogOpen}
            onOpenChange={setIsPriceDialogOpen}
            gearset={gearset}
        />
      </div>
  );
}

export default GearsetCard;
