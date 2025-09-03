import {PenLine, Trash2} from "lucide-react";
import {Badge} from "@/components/ui/badge";
import ConfirmAlertDialog from "@/components/ui/confirm-alert-dialog";
import {Separator} from "@/components/ui/separator.tsx";
import GearSetGrid from "@/features/items/components/gearset/gear-set-grid.tsx";
import type {GearSetCardProps} from "@/types/types.ts";

function GearSetCard({gearSet, onDelete}: GearSetCardProps) {
  const {title, tags, id, slots, characterClass, characterGender} = gearSet;

  const normalizedTitle =
      title && title.at(0)?.toUpperCase() + title.slice(1).toLowerCase();

  const handleGearSetDeletionConfirmation = () => {
    if (id) {
      onDelete(id);
    } else {
      console.error("Error: Cannot delete gearset. The ID is missing.");
    }
  };

  return (
      <div
          className="mx-auto max-w-fit rounded-md border border-border/70 bg-background shadow-sm hover:shadow-md transition-shadow">
        <header className="flex flex-col gap-2 p-4 pb-2">
          <div className="flex items-center justify-between gap-2">
            <h2 className="truncate text-base font-semibold text-foreground">
              {normalizedTitle}
            </h2>
            <div className="flex items-center gap-1">
              <button
                  type="button"
                  className="p-1 hover:bg-secondary transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-border rounded-md"
              >
                <PenLine className="size-4 stroke-muted-foreground"/>
              </button>
              <ConfirmAlertDialog
                  title="Delete Gearset"
                  description={`Are you sure you want to delete the gearset "${normalizedTitle}"? This action cannot be undone.`}
                  confirmText="Delete"
                  onConfirm={handleGearSetDeletionConfirmation}
              >
                <button
                    type="button"
                    className="p-1 hover:bg-red-500/10 transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-red-500 rounded-md"
                >
                  <Trash2 className="size-4 stroke-red-500"/>
                </button>
              </ConfirmAlertDialog>
            </div>
          </div>
          {tags && tags.length > 0 && (
              <ul className="flex flex-wrap gap-1.5 mt-1">
                {tags.map((tag) => (
                    <li key={tag}>
                      <Badge
                          variant="secondary"
                          className="rounded-sm border border-white/10 bg-white/5 text-secondary-foreground px-2 py-0.5 text-xs font-medium"
                      >
                        {tag}
                      </Badge>
                    </li>
                ))}
              </ul>
          )}
        </header>
        <div className="px-4">
          <Separator className="opacity-40"/>
        </div>
        <div className="flex items-center justify-center p-4 overflow-x-auto overflow-y-hidden">
          <GearSetGrid
              id={String(id)}
              gearsetSlots={slots || []}
              characterClass={{
                name: characterClass?.name ?? "",
                gender: characterGender ?? "m",
              }}
          />
        </div>
      </div>
  );
}

export default GearSetCard;
